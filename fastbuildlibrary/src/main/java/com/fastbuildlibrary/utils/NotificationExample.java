//package com.fastbuildlibrary.utils;
//
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//
///**
// * 通知操作工具
// */
//public class NotificationExample {
//    private static final int NOTIFICATION_ID = 0x111;
//    private static NotificationManager notificationManager = null;
//
//    private NotificationExample() {
//        throw new UnsupportedOperationException("cannot be instantiated");
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static void sendNotification(Context context) {
//        if(notificationManager == null){
//            // 获取系统的NotificationManager服务
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        // 创建一个启动其他Activity的Intent
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
//
//        Notification notify = null;
//
//        // 低于 API 11 写法
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
////            notify = new Notification(R.mipmap.ic_launcher, "有通知到来", System.currentTimeMillis());
////            notify.setLatestEventInfo(context, "这是通知的标题", "这是通知的内容", pi);
//        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
//            // 高于 API 11 低于 API 16
//            Notification.Builder builder = new Notification.Builder(context)
//                    .setAutoCancel(true)
//                    .setContentTitle("有通知到来")
//                    .setContentText("describe")
//                    .setContentIntent(pi)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setWhen(System.currentTimeMillis())
//                    .setOngoing(true)
//                    // 设置使用系统默认的声音、默认LED灯
//                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
//                    // 设置通知的自定义声音
////                    .setSound(Uri.parse("android.resource://org.crazyit.ui/" + R.raw.msg));
//
//            notify = builder.getNotification();
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            // 高于 API 16
//            notify = new Notification.Builder(context)
//                    .setAutoCancel(true)
//                    .setContentTitle("有通知到来")
//                    .setContentText("describe")
//                    .setContentIntent(pi)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setWhen(System.currentTimeMillis())
//                    .setOngoing(true)
//                    .build();
//        }
//
//        // 发送通知
//        notificationManager.notify(NOTIFICATION_ID, notify);
//    }
//
//    /**
//     * 取消通知
//     */
//    public void cancelNotification(){
//        if(notificationManager != null){
//            // 取消通知
//            notificationManager.cancel(NOTIFICATION_ID);
//        }
//    }
//}
