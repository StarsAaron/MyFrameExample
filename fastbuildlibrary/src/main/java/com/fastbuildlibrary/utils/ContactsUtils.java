package com.fastbuildlibrary.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * 联系人管理工具(演示，不可调用)
 */
public class ContactsUtils {
    private static final String TAG = "ContactsTest";

    /**
     * 获取所有联系人
     *
     * @throws Exception
     */
    public static void getAllContacts(Context context) throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);//查询
        while (cursor.moveToNext()) {
            int contactid = cursor.getInt(0);
            StringBuilder sb = new StringBuilder("contactid=");
            sb.append(contactid);
            uri = Uri.parse("content://com.android.contacts/contacts/" + contactid + "/data");//该路径可以查询该联系人的所有数据
            //因为联系人的表采用关联查询，我们需要取得"mimetype"内容类型,"data1"内容,"data2"内容项数字段的值
            Cursor datacursor = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
            while (datacursor.moveToNext()) {
                String data = datacursor.getString(datacursor.getColumnIndex("data1"));
                String type = datacursor.getString(datacursor.getColumnIndex("mimetype"));
                //判断内容类型
                if ("vnd.android.cursor.item/name".equals(type)) {//姓名
                    sb.append(",name=" + data);
                } else if ("vnd.android.cursor.item/email_v2".equals(type)) {//email
                    sb.append(",email=" + data);
                } else if ("vnd.android.cursor.item/phone_v2".equals(type)) {//phone
                    sb.append(",phone=" + data);
                }
            }
            Log.i(TAG, sb.toString());
        }
    }

    /**
     * 根据号码获取联系人的姓名
     *
     * @throws Exception
     */
    public static void getContactNameByNumber(Context context, String num) throws Exception {
        String number = "18601025011";
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            Log.i(TAG, name);
        }
        cursor.close();
    }

    /**
     * 添加联系人
     * 先往raw_contacts表添加联系人的id，再data表添加联系人的信息
     *
     * @throws Exception
     */
    public static void addContact(Context context) throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        //根据resolver.insert(uri, values)返回的URI取得id
        long contactid = ContentUris.parseId(resolver.insert(uri, values));
        //添加姓名
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", contactid);
        values.put("mimetype", "vnd.android.cursor.item/name");//注意mimetype项是mimetypes表中的项，因为使用insert方法，该表与data表关联
        values.put("data2", "张小小");
        resolver.insert(uri, values);
        //添加电话
        values.clear();
        values.put("raw_contact_id", contactid);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", "13671323507");
        resolver.insert(uri, values);
        //添加Email
        values.clear();
        values.put("raw_contact_id", contactid);
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        values.put("data2", "2");
        values.put("data1", "zhangxx@csdn.net");
        resolver.insert(uri, values);
    }

    //在同一个事务中完成联系人各项数据的添加，避免数据添加分步进行
    public static void testAddContact2(Context context) throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();//操作值
        ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
                .withValue("account_name", null)//赋值
                .build();
        operations.add(op1);//添加进操作集
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)//该方法的作用是 使用0字段的操作对象op1插入操作后对应的id填入"raw_contact_id"字段
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", "李小龙")
                .build();
        operations.add(op2);
        ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data1", "13560650505")
                .withValue("data2", "2")
                .build();
        operations.add(op3);
        ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data1", "liming@sohu.com")
                .withValue("data2", "2")
                .build();
        operations.add(op4);
        resolver.applyBatch("com.android.contacts", operations);//批量操作
    }
}
