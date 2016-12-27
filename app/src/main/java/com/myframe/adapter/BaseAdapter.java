package com.myframe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView适配器基类BaseAdapter
 * @param <T>
 */
public abstract class BaseAdapter<T extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<T> {

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.setData();
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setData();
    }

}
