package com.myframe.example.filechoose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fastbuildlibrary.utils.ToastUtils;
import com.myframe.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 文件浏览器
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */
public class FileChooserActivity extends Activity {

    private ListView mListView;
    private TextView mBackfolder, tv_path;
    private Button mClose;

    private String mSdcardRootPath; // sdcard 根路径
    private String mLastFilePath; // 当前显示的路径

    private ArrayList<FileInfo> mFileLists;
    private FileChooserAdapter mAdatper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filechooser);

        mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mBackfolder = (TextView) findViewById(R.id.tv_file_back);
        tv_path = (TextView) findViewById(R.id.tv_path);
        mListView = (ListView) findViewById(R.id.lv_file_list);
        mClose = (Button) findViewById(R.id.btn_file_exit);

        mBackfolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backProcess();
            }
        });

        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mFileLists = new ArrayList<>();
        mAdatper = new FileChooserAdapter(FileChooserActivity.this, mFileLists);
        mListView.setAdapter(mAdatper);
        updateFileItems(mSdcardRootPath);
        mListView.setOnItemClickListener(mItemClickListener);
    }


    // 根据路径更新数据，并且通知Adatper数据改变
    private void updateFileItems(String filePath) {
        mLastFilePath = filePath;
        tv_path.setText(mLastFilePath);
        if (mFileLists == null)
            mFileLists = new ArrayList<>();
        if (!mFileLists.isEmpty())
            mFileLists.clear();

        File[] files = folderScan(filePath);
        if (files == null)
            return;

        for (int i = 0; i < files.length; i++) {
            if (files[i].isHidden()) // 不显示隐藏文件
                continue;
            String fileAbsolutePath = files[i].getAbsolutePath();
            String fileName = files[i].getName();
            boolean isDirectory = false;
            if (files[i].isDirectory()) {
                isDirectory = true;
            }
            FileInfo fileInfo = new FileInfo(fileAbsolutePath, fileName, isDirectory);
            mFileLists.add(fileInfo);
        }
        if (mAdatper != null)
            mAdatper.notifyDataSetChanged(); // 重新刷新
    }

    // 获得当前路径的所有文件
    private File[] folderScan(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FileInfo fileInfo = (FileInfo) (((FileChooserAdapter) adapterView.getAdapter()).getItem(position));
            if (fileInfo.isDirectory()) { // 点击项为文件夹, 显示该文件夹下所有文件
                updateFileItems(fileInfo.getFilePath());
            } else if (fileInfo.isTextFile()) { // 是text文件 ， 则将该路径通知给调用者
                Intent intent = new Intent();
                intent.putExtra("filepath", fileInfo.getFilePath());
                setResult(RESULT_OK, intent);
                finish();
            } else { // 其他文件.....
                ToastUtils.showShort(FileChooserActivity.this, "不符合格式！");
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backProcess();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 返回上一层目录的操作
    public void backProcess() {
        // 判断当前路径是不是sdcard根路径 ， 如果不是，则返回到上一层。
        if (!mLastFilePath.equals(mSdcardRootPath)) {
            File thisFile = new File(mLastFilePath);
            String parentFilePath = thisFile.getParent();
            tv_path.setText(parentFilePath);
            updateFileItems(parentFilePath);
        } else { // 是sdcard路径 ，直接结束
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}