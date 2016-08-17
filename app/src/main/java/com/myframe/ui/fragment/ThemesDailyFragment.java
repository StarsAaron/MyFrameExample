package com.myframe.ui.fragment;


import android.os.Bundle;
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
import com.myframe.model.DailyTypeBean;
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
 * 主题分类页面
 */
public class ThemesDailyFragment extends Fragment {

    @BindView(R.id.themes_recycle)
    RecyclerView themesRecycle;
    @BindView(R.id.theme_swipe_refresh)
    SwipeRefreshLayout themeSwipeRefresh;

    private List<DailyTypeBean.SubjectDaily> subjectDailies = new ArrayList<>();
    private CommonAdapter<DailyTypeBean.SubjectDaily> commonAdapter;

    public ThemesDailyFragment() {
    }

    public static ThemesDailyFragment newInstance() {
        ThemesDailyFragment fragment = new ThemesDailyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themes_daily, container, false);
        ButterKnife.bind(this, view);

        themeSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        themeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDailyTypeData();
            }
        });

        themesRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        themesRecycle.setItemAnimator(new DefaultItemAnimator());
        commonAdapter = new CommonAdapter<DailyTypeBean.SubjectDaily>(getActivity(),R.layout.item_themes_daily,subjectDailies) {
            @Override
            protected void convert(final ViewHolder holder, final DailyTypeBean.SubjectDaily item, int position) {
                holder.setText(R.id.item_type_name,item.getName());
                holder.setText(R.id.item_type_des,item.getDescription());
                Picasso.with(getContext()).load(item.getThumbnail()).into((ImageView) holder.getView(R.id.item_type_img));
                holder.getView(R.id.themes_daily_recycle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情界面
                        UIHelper.showThemesList(getActivity(),item.getId());
                    }
                });
            }
        };
        themesRecycle.setAdapter(commonAdapter);

        getDailyTypeData();
        return view;
    }

    /**
     * 获取数据
     */
    public void getDailyTypeData() {
        new RetrofitHelper(getActivity()).getDailyType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DailyTypeBean>() {

                    @Override
                    public void call(DailyTypeBean dailyTypeBean) {

                        if (dailyTypeBean != null) {
                            List<DailyTypeBean.SubjectDaily> others = dailyTypeBean.getOthers();
                            if(others != null && others.size()>0){
//                                subjectDailies.removeAll(null);
                                subjectDailies.addAll(others);
                                commonAdapter.notifyItemRangeChanged(0,others.size());
                            }
                        }
                        themeSwipeRefresh.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        themeSwipeRefresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        Unbinder.EMPTY.unbind();
        super.onDestroyView();
    }
}
