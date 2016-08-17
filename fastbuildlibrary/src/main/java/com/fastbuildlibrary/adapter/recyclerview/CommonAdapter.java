package com.fastbuildlibrary.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.fastbuildlibrary.adapter.recyclerview.base.ItemViewDelegate;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    /**
     * 刷新方法
     * @param mDatas
     */
    public void updateData(List<T> mDatas) {
        this.mDatas = mDatas;
        this.notifyItemRangeChanged(0,mDatas.size());
    }

    public void addData(List<T> dailys) {

        if (this.mDatas == null) {
            updateData(dailys);
        } else {
            this.mDatas.addAll(dailys);
            notifyDataSetChanged();
        }
    }
}
