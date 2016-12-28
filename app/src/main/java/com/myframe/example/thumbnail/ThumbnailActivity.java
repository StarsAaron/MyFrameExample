package com.myframe.example.thumbnail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.myframe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片，文字合成 使用Canvas
 *
 * 1、 saveMyBitmap(Bitmap bitmap)、changeBase64ToImage(String
 * strImg)和compressBitmapAndGetBase64String(String filePath)这几个方法，在本例中并没有用到，只是
 * 分享而已,如果不需要将图片做Base64转换，可以将Base64.java文件删掉 。
 * 2、本人有点懒，在设置控件id时，并不规范，望见谅！
 * 
 * @author ljw
 * 
 * @date 2014-4-26
 */
public class ThumbnailActivity extends Activity implements OnClickListener {
	private Button btn01;
	private Button btn02;
	private Button btn03;
	private ImageView imv01;
	private ImageView imv02;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thumbnail);
		setupViews();
		setListener();
	}

	/**
	 * 初始化
	 */
	private void setupViews() {
		btn01 = (Button) findViewById(R.id.btn01);
		btn02 = (Button) findViewById(R.id.btn02);
		btn03 = (Button) findViewById(R.id.btn03);
		imv01 = (ImageView) findViewById(R.id.imv01);
		imv02 = (ImageView) findViewById(R.id.imv02);
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		btn01.setOnClickListener(this);
		btn02.setOnClickListener(this);
		btn03.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn01:// 图片合成水印
			imv01.setVisibility(View.VISIBLE);
			imv01.setImageDrawable(getResources().getDrawable(R.drawable.v));
			addWaterMark(1);
			break;
		case R.id.btn02:// 绘制文字的的方式
			imv01.setVisibility(View.VISIBLE);
			imv01.setImageDrawable(getResources().getDrawable(R.drawable.v));
			addWaterMark(0);
			break;
		case R.id.btn03:// 压缩图片
			String path = Environment.getExternalStorageDirectory() + "/test.jpg";
			imv01.setVisibility(View.GONE);
			Bitmap bitmap = getSmallBitmap(path);
			imv02.setImageBitmap(bitmap);
			break;

		default:
			break;
		}
	}

	/**
	 * 添加水印
	 */
	private void addWaterMark(int addType) {
		Bitmap a;
		if (addType == 1) {// 图片合成水印
			Bitmap mark = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
			Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.v);
			a = myCreateBitmap(photo, mark);
		} else {// 绘制文字的的方式
			a = myCreateBitmap(getViewBitmap(imv01), "截图时间：");
		}
		imv02.setImageBitmap(a);
	}

	/**
	 * 使用两张图片合成水印的方法
	 * 
	 * @param src
	 *            主图片
	 * @param watermark
	 *            水印图片
	 * @return
	 */
	private Bitmap myCreateBitmap(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 截取特定大小的图片
		// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 将文字直接绘制到图片上
	 * 
	 * @param src
	 * @param str
	 * @return
	 */
	private Bitmap myCreateBitmap(Bitmap src, String str) {
		Time t = new Time();
		t.setToNow();// 当前时间
		int w = src.getWidth();
		int h = src.getHeight();
		String mstrTitle = t.hour + ":" + t.minute + ":" + t.second;
		str = str + mstrTitle;
		Bitmap bmpTemp = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 截取特定大小的图片
		Canvas canvas = new Canvas(bmpTemp);
		Paint p = new Paint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		p.setColor(Color.BLUE);
		p.setTypeface(font);
		p.setTextSize(22);
		canvas.drawBitmap(src, 0, 0, p);// 绘制在src的左上角
		canvas.drawText(str, 0, 40, p);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bmpTemp;
	}

	/**
	 * 获取要添加水印的位图
	 * 
	 * @param v
	 * @return
	 */
	private Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		// 保存原设置
		boolean willNotCache = v.willNotCacheDrawing();
		// 不使用缓存绘制
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		// 保存原背景
		int color = v.getDrawingCacheBackgroundColor();

		// 去View背景
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		// 重建缓存
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			Log.i("info", "cacheBitmap is null");
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view 恢复视图
		v.destroyDrawingCache();// 清理视图缓存
		v.setWillNotCacheDrawing(willNotCache);// 恢复原设置
		v.setDrawingCacheBackgroundColor(color);// 设置View背景

		return bitmap;
	}

	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * 
	 * @param filePath
	 * @return
	 */
	public Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		/* 设置为true,可以不把图片读到内存中,但依然可以计算出图片的大小，这正是我们需要的 */
		options.inJustDecodeBounds = true;
		File file = new File(filePath);
		InputStream is = null;
		try {
			if (file.exists()) {

				is = new FileInputStream(file);
				// BitmapFactory.decodeFile(filePath, options);
				BitmapFactory.decodeStream(is, null, options);

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, 400, 200);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;

				Log.i("info", "options.inSampleSize=" + options.inSampleSize);
				// 这样重新获取一个新的is输入流,就可以解决decodeStream(is,null, options)返回null的问题
				is = new FileInputStream(file);

				Bitmap bm = BitmapFactory.decodeStream(is, null, options);
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		/* 压缩一张图片。我们需要知道这张图片的原始大小，然后根据我们设定的压缩比例进行压缩。 */
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		/*
		 * 1、如果图片的原始高度或者宽带大约我们期望的宽带和高度，我们需要计算出缩放比例的数值。否则就不缩放
		 * 2、如果使用大的值作位压缩倍数，则压缩出来的图片大小会小于我们设定的值
		 * 3、如果使用小的值作位压缩倍数，则压缩出来的图片大小会大于我们设定的值
		 */
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 将图片按比例进行压缩,返回由图片转换成Base64编码的字符串，以便将图片上传到服务器
	 * 
	 * @param filePath
	 * @return
	 */
	private String compressBitmapAndGetBase64String(String filePath) {
		/* 得到了一个缩放的bitmap对象，如果你在应用中显示图片，就可以使用这个bitmap对象了 */
		Bitmap bm = getSmallBitmap(filePath);
		/* 调整位图图像颜色值,以降低对内存的消耗 */
		bm = bm.copy(Config.ARGB_4444, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;// 设置为100，则说明不进行压缩
		bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {// 循环判断如果压缩后图片字节流大于100kb，继续压缩
			baos.reset();
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;
		}
		// saveMyBitmap(bm);
		return changeImageToBase64(baos);// 101539b =99kb sd卡中368KB
	}

	/**
	 * 将Base64转换为图片
	 * 
	 * @param strImg
	 * @return
	 */
	private Bitmap changeBase64ToImage(String strImg) {
		Bitmap bp = null;
		if (strImg == null || "".equals(strImg))
			return null;
		try {
			byte[] data;
			/**
			 * 由于在2.2之前的SDK没有Base64,所以直接将Base64.java引入了包中的，不直接调用系统的
			 * 
			 * @date 2013-11-29
			 */
			data = Base64.decode(strImg, Base64.DEFAULT);
			for (int i = 0; i < data.length; i++) {
				if (data[i] < 0) {
					data[i] += 256;
				}
			}
			bp = BitmapFactory.decodeByteArray(data, 0, data.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bp;
	}

	/**
	 * 将图片转换为Base64
	 * 
	 * @param stream
	 * @return
	 */
	private String changeImageToBase64(ByteArrayOutputStream stream) {

		byte[] bytes = null;
		try {
			bytes = new byte[stream.size()];
			bytes = stream.toByteArray();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 由于在2.2之前的SDK没有Base64,所以直接将Base64.java引入了包中的，不直接调用系统的
		 * 
		 * @date 2013-11-29
		 */
		String str = new String(Base64.encode(bytes, Base64.DEFAULT));
		return str;
	}

	/**
	 * 将压缩后的图片保存到sd卡
	 * 
	 * @param bitmap
	 * @param bitName
	 */
	private void saveMyBitmap(Bitmap bitmap) {
		File dir = new File(Environment.getExternalStorageDirectory() + "/bptemp");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File imageFile = new File(dir, "r_n_verify.jpg");
		if (imageFile.exists()) {
			if (imageFile.isFile()) {
				imageFile.delete();// 先删掉原来的，再另外创建一张图片（如果不先将已存在的图片删除，部分手机在保存时会出问题）
				imageFile = new File(dir, "r_n_verify.jpg");
			}
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(imageFile);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)) {
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				try {
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}