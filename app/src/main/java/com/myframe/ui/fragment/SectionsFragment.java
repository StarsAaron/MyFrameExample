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
import com.myframe.model.DailySections;
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
 * 分栏列表
 */
public class SectionsFragment extends Fragment {

    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private List<DailySections.DailySectionsInfo> sectionsInfos = new ArrayList<>();
    private CommonAdapter<DailySections.DailySectionsInfo> commonAdapter;

    public SectionsFragment() {
    }

    public static SectionsFragment newInstance() {
        SectionsFragment fragment = new SectionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        ButterKnife.bind(this, view);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getSections();
            }
        });

        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setItemAnimator(new DefaultItemAnimator());
        commonAdapter = new CommonAdapter<DailySections.DailySectionsInfo>(getActivity(),R.layout.item_sections,sectionsInfos) {
            @Override
            protected void convert(final ViewHolder holder, final DailySections.DailySectionsInfo item, int position) {
                holder.setText(R.id.item_name,item.getName());
                holder.setText(R.id.item_des,item.getDescription());
                Picasso.with(getContext()).load(item.getThumbnail()).into((ImageView) holder.getView(R.id.item_img));
                holder.getView(R.id.cv_section).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情界面
                        UIHelper.showSectionsList(getActivity(),item.getId());
                    }
                });
            }
        };
        recycle.setAdapter(commonAdapter);
        getSections();
        return view;
    }

    private void getSections() {
        RetrofitHelper.getLastZhiHuApi().getZhiHuSections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DailySections>() {

                    @Override
                    public void call(DailySections dailySections) {

                        if (dailySections != null) {
                            List<DailySections.DailySectionsInfo> data = dailySections.data;
                            if (data != null && data.size() > 0) {
                                sectionsInfos.clear();
                                sectionsInfos.addAll(data);
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
