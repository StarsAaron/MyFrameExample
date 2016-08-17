package com.fastbuildlibrary.http.nohttp;

import android.graphics.Bitmap;

import com.yolanda.nohttp.BasicBinary;
import com.yolanda.nohttp.Binary;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.InputStreamBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnUploadListener;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.tools.ImageLocalLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NohttpManager {

    private static NohttpManager nohttpManager;

    /**
     * 请求队列.
     */
    private static RequestQueue requestQueue;

    /**
     * 下载队列.
     */
    private static DownloadQueue downloadQueue;

    private NohttpManager() {
        // 创建请求队列, 默认并发3个请求, 传入数字改变并发数量: NoHttp.newRequestQueue(1);
        requestQueue = NoHttp.newRequestQueue();
        //创建下载队列
        downloadQueue = NoHttp.newDownloadQueue();
    }

    /**
     * 获取NohttpManager实例
     */
    public synchronized static NohttpManager getInstance() {
        if (nohttpManager == null)
            nohttpManager = new NohttpManager();
        return nohttpManager;
    }

    /**
     * 添加请求
     * @param what
     * @param request
     * @param onResponseListener
     */
    public void addRequest(int what, Request request, OnResponseListener onResponseListener){
        requestQueue.add(what, request,onResponseListener);
    }

    /**
     * 请求String 对象
     * GET方式
     * @param tag 标识
     * @param url
     * @param onResponseListener
     */
    public Request stringRequest(int tag,String url,OnResponseListener onResponseListener){
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 请求JsonObject对象
     * GET方式
     * @param tag 标识
     * @param url
     * @param onResponseListener
     */
    public Request jsonObjectRequest(int tag,String url,OnResponseListener onResponseListener){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url, RequestMethod.GET);

//        Request<JSONObject> request = ...
//        request.add("name", "yoldada");// String类型
//        request.add("age", 18);// int类型
//        request.add("sex", '0')// char类型
//        request.add("time", 16346468473154); // long类型
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 请求JsonArray 对象
     * GET方式
     * @param tag 标识
     * @param url
     * @param onResponseListener
     */
    public Request jsonArrayRequest(int tag,String url,OnResponseListener onResponseListener){
        Request<JSONArray> request = NoHttp.createJsonArrayRequest(url, RequestMethod.GET);
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 请求Bitmap 对象
     * GET方式
     * @param tag 标识
     * @param url
     * @param onResponseListener
     */
    public Request bitmapRequest(int tag,String url,OnResponseListener onResponseListener){
        Request<Bitmap> request = NoHttp.createImageRequest(url, RequestMethod.GET);

        //优先网络获取，获取不了再取缓存
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
//        //优先使用缓存，如果没有缓存才去请求服务器
//        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
//        //仅仅请求网络
//        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
//        //仅仅读取缓存，不会请求网络和其它操作
//        request.setCacheMode(CacheMode.ONLY_READ_CACHE);
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 请求fastJson 对象
     * @param tag
     * @param url
     * @param onResponseListener
     * @return
     */
    public Request fastJsonRequest(int tag,String url,OnResponseListener onResponseListener){
        Request<com.alibaba.fastjson.JSONObject> request = new FastJsonRequest(url,RequestMethod.GET);
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 下载请求
     * @param tag
     * @param url
     * @param downloadListener
     * @return
     */
    public DownloadRequest downloadSignleFileRequest(int tag,String url, DownloadListener downloadListener){
        // 这里不传文件名称、不断点续传，则会从响应头中读取文件名自动命名，如果响应头中没有则会从url中截取。
        // url 下载地址。
        // fileFolder 文件保存的文件夹。
        // isDeleteOld 发现文件已经存在是否要删除重新下载。
//        DownloadRequest request = NoHttp.createDownloadRequest(url,"/NoHttpDownload",true);//删除旧文件

        // 如果使用断点续传的话，一定要指定文件名喔。
        // url 下载地址。
        // fileFolder 保存的文件夹。
        // fileName 文件名。
        // isRange 是否断点续传下载。
        // isDeleteOld 如果发现存在同名文件，是否删除后重新下载，如果不删除，则直接下载成功。
        DownloadRequest request = NoHttp.createDownloadRequest(url, "/NoHttpDownload", "nohttp.apk", true, true);
        // what 区分下载。
        // downloadRequest 下载请求对象。
        // downloadListener 下载监听。
        downloadQueue.add(tag,request,downloadListener);
        return request;
    }

    /**
     * 上传单一文件
     * @param tag
     * @param url
     * @param filePath
     * @param onResponseListener
     * @param onUploadListener
     */
    public void uploadSingleFileRequest(int tag, String url, String filePath, OnResponseListener onResponseListener, OnUploadListener onUploadListener){
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        // 添加普通参数。
        request.add("user", "yolanda");
        // 上传文件需要实现NoHttp的Binary接口，NoHttp默认实现了FileBinary、InputStreamBinary、ByteArrayBitnary、BitmapBinary。
        // FileBinary用法
        BasicBinary binary = new FileBinary(new File(filePath));
//        监听上传过程，如果不需要监听就不用设置。
//        第一个参数：what，what和handler的what一样，会在回调被调用的回调你开发者，作用是一个Listener可以监听多个文件的上传状态。
//        第二个参数： 监听器。
        binary.setUploadListener(tag, onUploadListener);
        request.add("file", binary);
        this.addRequest(tag,request,onResponseListener);
    }

    /**
     * 上传多种类型文件，使用不同Key
     * @param tag
     * @param url
     * @param filePath
     * @param onResponseListener
     * @param onUploadListener
     */
    public void uploadMultiFileRequest(int tag, String url, String filePath, OnResponseListener onResponseListener, OnUploadListener onUploadListener) {
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        // 添加普通参数。
        request.add("user", "yolanda");

        try {
            // 上传文件需要实现NoHttp的Binary接口，NoHttp默认实现了FileBinary、InputStreamBinary、ByteArrayBitnary、BitmapBinary。
            File file1 = new File("/image1.jpg");
            Bitmap file2 = ImageLocalLoader.getInstance().readImage("/image2.jpg", 720, 1280);
            File file3 = new File("/image3.png");

            // 1. FileBinary用法。
            BasicBinary binary1 = new FileBinary(file1);
            /**
             * 监听上传过程，如果不需要监听就不用设置。
             * 第一个参数：what，what和handler的what一样，会在回调被调用的回调你开发者，作用是一个Listener可以监听多个文件的上传状态。
             * 第二个参数： 监听器。
             */
            binary1.setUploadListener(0, onUploadListener);
            request.add("image1", binary1);


            // 2. BitmapBinary用法。
            /**
             * 第一个参数是bitmap。
             * 第二个参数是文件名，因为bitmap无法获取文件名，所以需要传，如果你的服务器不关心这个参数，你可以不传。
             */
            BasicBinary binary2 = new BitmapBinary(file2, "userHead.jpg");// 或者：BasicBinary binary2 = new BitmapBinary(file2, null);
            binary2.setUploadListener(1, onUploadListener);
            request.add("image2", binary2);


            // 3. InputStreamBinary用法。
            /**
             * 第一个参数是inputStream。
             * 第二个参数是文件名，因为bitmap无法获取文件名，所以需要传fileName，如果你的服务器不关心这个参数，你可以不传。
             */
            BasicBinary binary3 = new InputStreamBinary(new FileInputStream(file3), file3.getName());
            binary3.setUploadListener(2, onUploadListener);
            request.add("image3", binary3);
            this.addRequest(tag,request,onResponseListener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传多文件，用同一Key
     * @param tag
     * @param url
     * @param filePaths
     * @param onResponseListener
     * @param onUploadListener
     * @return
     */
    public Request uploadFileListRequest(int tag, String url, List<String> filePaths, OnResponseListener onResponseListener, OnUploadListener onUploadListener){
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        // 添加普通参数。
        request.add("user", "yolanda");
        List<Binary> binaries = new ArrayList<>();
        for(int i=0;i<filePaths.size();i++){
            BasicBinary binary = new FileBinary(new File(filePaths.get(i)));
            binary.setUploadListener(0, onUploadListener);
            binaries.add(binary);
        }
        // 添加FileList到请求
        request.add("image1", binaries);
        // 这里要提醒一点喔：在POST, PUT, DELETE, PATCH请求方法下，相同的key重复添加是不会被覆盖的。所以你也可以这么添加文件，用一个key上传多个文件。
//        request.add("image1", binary1);
//        request.add("image1", binary2);
//        request.add("image1", binary3);
        this.addRequest(tag,request,onResponseListener);
        return request;
    }

    /**
     * 同步请求
     * @param tag
     * @param url
     * @param onResponseListener
     */
    public void SyncRequest(int tag, final String url, OnResponseListener onResponseListener) {
        // 在子线程中可以使用同步请求
        new Thread() {
            public void run() {
                // 在子线程中可以使用同步请求
                Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
                Response<String> response = NoHttp.startRequestSync(request);
                if (response.isSucceed()) {
                    //成功
                    response.get();
                } else {
                    //失败
                    response.getException().getMessage();
                }
            }
        }.start();
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        requestQueue.stop();
    }

}
