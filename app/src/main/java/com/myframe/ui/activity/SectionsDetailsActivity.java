package com.myframe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fastbuildlibrary.adapter.recyclerview.CommonAdapter;
import com.fastbuildlibrary.adapter.recyclerview.LoadMoreOnScrollListener;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;
import com.fastbuildlibrary.base.FBLSwipeBackBaseActivity;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.SectionsDetails;
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
 * 专栏详情界面
 */
public class SectionsDetailsActivity extends FBLSwipeBackBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    private int id;
    private List<SectionsDetails.SectionsDetailsInfo> sectionsDetailsInfos = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private long timetemp;
    private CommonAdapter<SectionsDetails.SectionsDetailsInfo> commonAdapter;
    private LoadMoreOnScrollListener loadMoreOnScrollListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sections_details;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("section_id", -1);
        }
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSectionsDetails();
            }
        });

        commonAdapter = new CommonAdapter<SectionsDetails.SectionsDetailsInfo>(getApplicationContext()
                ,R.layout.item_sections_details,sectionsDetailsInfos) {
            @Override
            protected void convert(ViewHolder holder, final SectionsDetails.SectionsDetailsInfo item, int position) {
                holder.setText(R.id.item_title,item.getTitle());
                holder.setText(R.id.item_time,item.getDisplayDate());
                if(item.getImages() != null && item.getImages().size()>0){
                    String imgPath = item.getImages().get(0);
                    if(!TextUtils.isEmpty(imgPath)){
                        Picasso.with(getApplicationContext())
                                .load(imgPath)
                                .error(R.drawable.account_avatar)
                                .into((ImageView) holder.getView(R.id.item_image));
                    }
                }
                holder.getView(R.id.cv_section).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情界面
                        UIHelper.showDetail(SectionsDetailsActivity.this,item.getId());
                    }
                });
            }
        };


        LinearLayoutManager llm = new LinearLayoutManager(SectionsDetailsActivity.this);
        loadMoreOnScrollListener = new LoadMoreOnScrollListener(llm) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMore(timetemp);
//                ToastUtils.showShort(getActivity(),"正在加载更多");
            }
        };
        mRecyclerView.addOnScrollListener(loadMoreOnScrollListener);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(commonAdapter);
        getSectionsDetails();
    }

    private void getSectionsDetails() {

        RetrofitHelper.getLastZhiHuApi().getSectionsDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SectionsDetails>() {

                    @Override
                    public void call(SectionsDetails sectionsDetails) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (sectionsDetails != null) {
                            mToolbar.setTitle(sectionsDetails.name);
                            timetemp = sectionsDetails.timestamp;
                            List<SectionsDetails.SectionsDetailsInfo> stories = sectionsDetails.stories;
                            if (stories != null && stories.size() > 0) {
                                sectionsDetailsInfos.clear();
                                sectionsDetailsInfos.addAll(stories);
                                commonAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void loadMore(long timestamp) {

        RetrofitHelper.getLastZhiHuApi().getBeforeSectionsDetails(id, timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SectionsDetails>() {

                    @Override
                    public void call(SectionsDetails sectionsDetails) {
                        if (sectionsDetails != null) {
                            List<SectionsDetails.SectionsDetailsInfo> stories = sectionsDetails.stories;
                            timetemp = sectionsDetails.timestamp;
                            if (stories != null && stories.size() > 0) {
                                sectionsDetailsInfos.addAll(stories);
                                commonAdapter.notifyDataSetChanged();
                                loadMoreOnScrollListener.setLoading(false);
                            }
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        loadMoreOnScrollListener.setLoading(false);
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Unbinder.EMPTY.unbind();
        super.onDestroy();
    }
}
