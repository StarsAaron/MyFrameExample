package com.myframe.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fastbuildlibrary.adapter.recyclerview.CommonAdapter;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.HotNews;
import com.myframe.net.RetrofitHelper;
import com.myframe.ui.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 热门文章推荐 每日20条
 */
public class HotNewsFragment extends Fragment {

    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private List<HotNews.HotNewsInfo> hotNewsInfos = new ArrayList<>();
    private CommonAdapter<HotNews.HotNewsInfo> commonAdapter;

    public HotNewsFragment() {
    }

    public static HotNewsFragment newInstance() {
        HotNewsFragment fragment = new HotNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_news, container, false);
        ButterKnife.bind(this, view);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                getHotNews();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setItemAnimator(new DefaultItemAnimator());
        commonAdapter = new CommonAdapter<HotNews.HotNewsInfo>(getActivity(),R.layout.item_hot_news,hotNewsInfos) {
            @Override
            protected void convert(final ViewHolder holder, final HotNews.HotNewsInfo item, int position) {
                holder.setText(R.id.item_des,item.getTitle());
                Picasso.with(getContext()).load(item.getThumbnail()).into((ImageView) holder.getView(R.id.item_img));
                holder.getView(R.id.cv_hotnews).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情界面
                        UIHelper.showDetail(getActivity(),item.getNewsId());
                    }
                });
            }
        };
        recycle.setAdapter(commonAdapter);

        getHotNews();
        return view;
    }

    private void getHotNews() {

        RetrofitHelper.getLastZhiHuApi().getHotNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HotNews>() {

                    @Override
                    public void call(HotNews hotNews) {

                        if (hotNews != null) {
                            List<HotNews.HotNewsInfo> recent = hotNews.recent;
                            if (recent != null && recent.size() > 0) {
                                hotNewsInfos.clear();
                                hotNewsInfos.addAll(recent);
                                commonAdapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefresh.setRefreshing(false);
                        Snackbar.make(recycle, "加载失败,请重新下拉刷新数据.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        Unbinder.EMPTY.unbind();
        super.onDestroyView();
    }
}
