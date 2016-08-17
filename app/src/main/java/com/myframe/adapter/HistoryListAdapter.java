package com.myframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fastbuildlibrary.utils.DateUtils;
import com.myframe.R;
import com.myframe.db.entity.TomatoTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView适配器例子
 * Created by Aaron on 2016/7/14.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryHolder> {
    private Context context;
    private List<TomatoTask> tomatoTasks;

    public HistoryListAdapter(Context context,List<TomatoTask> tomatoTasks){
        this.context = context;
        this.tomatoTasks = tomatoTasks;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
//        view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
//        TomatoTask tomatoTask = tomatoTasks.get(position);
//        holder.tv_item_taskname.setText(tomatoTask.getTaskName());
//        holder.tv_item_starttime.setText("起始时间："+ DateUtils.formatDate(tomatoTask.getBegin(),"yyyy-MM-dd HH:mm:ss"));
//        holder.tv_item_endtime.setText("结束时间："+ DateUtils.formatDate(tomatoTask.getEnd(),"yyyy-MM-dd HH:mm:ss"));
//        holder.tv_item_tomato.setText("消耗："+tomatoTask.getNeedTomato()+"(个)");
//        holder.tv_item_bad_tomato.setText(tomatoTask.getBadTomato()+"(个)");
    }

    @Override
    public int getItemCount() {
        return tomatoTasks.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_item_taskname)
//        TextView tv_item_taskname;
//        @BindView(R.id.tv_item_starttime)
//        TextView tv_item_starttime;
//        @BindView(R.id.tv_item_endtime)
//        TextView tv_item_endtime;
//        @BindView(R.id.tv_item_tomato)
//        TextView tv_item_tomato;
//        @BindView(R.id.tv_item_bad_tomato)
//        TextView tv_item_bad_tomato;

        public HistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
