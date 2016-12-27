package com.myframe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Adapter 普通适配器模板
 */
public class MyAdapter extends BaseAdapter {
	private Context context;
	private List<? extends Map<String, ?>> data;

	public MyAdapter(Context context, List<? extends Map<String, ?>> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder=new ViewHolder();
			//获取组件
//			convertView = LayoutInflater.inflate(R.layout.shoucang_liebiao_content_view,null);
//			viewHolder.img_one = (ImageView) convertView.findViewById(R.id.image_kj);
//			viewHolder.img = (ImageView) convertView.findViewById(R.id.y_souchang1);
//			viewHolder.time = (TextView) convertView.findViewById(R.id.shou_news_time);
//			viewHolder.title = (TextView) convertView.findViewById(R.id.shou_news_title);
//			viewHolder.r_buju_r = (RelativeLayout) convertView.findViewById(R.id.r_buju_r);
//			viewHolder.shoucang_list = (ListView) ((Activity) context).findViewById(R.id.shoucang_list);
//			viewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.r_buju_r);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		//设置具体的值
		viewHolder.title.setText("xxxxxx");
		viewHolder.time.setText("xxxxxx");
		//...
		return convertView;
	}
	static class ViewHolder {
		public ImageView img_one;
		public ImageView img;
		public TextView time;
		public TextView title;
		public RelativeLayout r_buju_r;
		public ListView shoucang_list;
		public RelativeLayout rl;
	}
}
