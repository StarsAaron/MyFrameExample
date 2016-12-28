package com.myframe.example.camera;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.myframe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 调用系统照相截图+相册截图
 * 需要添加相机权限
 */
public class CameraTestActivity extends AppCompatActivity {
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    ImageView imageView = null;
    Button button0 = null;
    Button button1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameratest);
        imageView = (ImageView) findViewById(R.id.show_image);
        button0 = (Button) findViewById(R.id.choose_camera);
        button1 = (Button) findViewById(R.id.choose_gallery);
// 相册
        button0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PHOTOZOOM);
            }
        });
// 拍照
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), "temp.jpg")));
                System.out.println("============="
                        + Environment.getExternalStorageDirectory());
                startActivityForResult(intent, PHOTOHRAPH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == NONE || data == null)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            System.out.println("------------------------" + picture.getPath());
            startPhotoZoom(Uri.fromFile(picture));
        }
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            try {
                // 获得图片的uri
                Uri originalUri = data.getData();
                // 将图片内容解析成字节数组
                byte[] mContent = readStream(resolver.openInputStream(Uri
                        .parse(originalUri.toString())));
                // 将字节数组转换为BitmapDrawable对象
                Bitmap myBitmap = getPicFromBytes(mContent, null);
                BitmapDrawable bd = new BitmapDrawable(myBitmap);
                //按比例缩放图片
//				Drawable d = zoomDrawable(bd, 150, 10, true);
                //不按比例缩放图片，指定大小
                Drawable d = zoomDrawable(bd, 150, 100, false);
                // //把得到的图片绑定在控件上显示
                imageView.setImageDrawable(d);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -
                // 100)压缩文件
                imageView.setImageBitmap(photo);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开启一个系统页面来裁剪传进来的照片
     *
     * @param uri 需要裁剪的照片的URI值
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 将图片缩小/放大到指定宽高度
     *
     * @param  drawable 图片的drawable
     * @param       w 指定缩小到的宽度
     * @param       h 指定缩小到的高度
     * @param   scale 是否保持宽高比，TRUE:将忽略h的值，根据指定宽度自动计算高度 FALSE：根据指定宽度，高度生成图像
     * @return Drawable 返回新生成图片的Drawable
     */
    private Drawable zoomDrawable(Drawable drawable, int w, int h, Boolean scale) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth;
        float scaleHeight;
        if (scale == true) {
            // 如果要保持宽高比，那说明高度跟宽度的缩放比例都是相同的
            scaleWidth = ((float) w / width);
            scaleHeight = ((float) w / width);
        } else {
            // 如果不保持缩放比，那就根据指定的宽高度进行缩放
            scaleWidth = ((float) w / width);
            scaleHeight = ((float) h / height);
        }
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    /**
     * 根据图片Drawable返回图像
     *
     * @param drawable
     * @return Bitmap bitmap or null ;如果出错，返回NULL
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), "error:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
}