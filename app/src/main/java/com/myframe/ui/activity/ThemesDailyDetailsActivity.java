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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fastbuildlibrary.adapter.recyclerview.CommonAdapter;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;
import com.fastbuildlibrary.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.fastbuildlibrary.base.FBLSwipeBackBaseActivity;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.Editors;
import com.myframe.model.Stories;
import com.myframe.model.ThemesDetails;
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
 * 主题分栏详细类表
 */
public class ThemesDailyDetailsActivity extends FBLSwipeBackBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    //主题日报故事列表
    private List<Stories> stories = new ArrayList<>();
    //主题日报主编列表
    private List<Editors> editors = new ArrayList<>();
    private CommonAdapter<Stories> commonAdapter;
    private int id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_theme_daily;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("theme_id", -1);
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
                getThemesDetails();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commonAdapter = new CommonAdapter<Stories>(getApplicationContext(),R.layout.item_themes_stories,stories) {
            @Override
            protected void convert(ViewHolder holder, final Stories item, int position) {
                holder.setText(R.id.item_title,item.getTitle());
                if(item.getImages() != null && item.getImages().size()>0){
                    String imgPath = item.getImages().get(0);
                    if(!TextUtils.isEmpty(imgPath)){
                        Picasso.with(getApplicationContext())
                                .load(imgPath)
                                .error(R.drawable.account_avatar)
                                .into((ImageView) holder.getView(R.id.item_image));
                    }
                }
                holder.getView(R.id.cv_theme_detail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情界面
                        UIHelper.showDetail(ThemesDailyDetailsActivity.this,item.getId());
                    }
                });
            }
        };

        getThemesDetails();
    }

    /**
     * 添加头视图
     */
    private HeaderAndFooterWrapper addHeadView(ThemesDetails themesDetails) {
        View headView = getLayoutInflater().inflate(R.layout.layout_themes_details_head, mRecyclerView, false);
        ImageView mThemesBg = (ImageView) headView.findViewById(R.id.type_image);
        TextView mThemesTitle = (TextView) headView.findViewById(R.id.type_title);
        LinearLayout meditor = (LinearLayout) headView.findViewById(R.id.ll_editor);

        Picasso.with(getApplicationContext()).load(themesDetails.getBackground()).error(R.drawable.account_avatar).into(mThemesBg);
        mThemesTitle.setText(themesDetails.getDescription());
        for(Editors ee:editors){
            ImageView img = new ImageView(ThemesDailyDetailsActivity.this);
            img.setScaleType(ImageView.ScaleType.CENTER);
            ViewGroup.LayoutParams l = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(l);
            img.setPadding(5,5,5,5);
            Picasso.with(getApplicationContext()).load(ee.getAvatar()).into(img);
            meditor.addView(img);
        }
        HeaderAndFooterWrapper hafw = new HeaderAndFooterWrapper(commonAdapter);
        hafw.addHeaderView(headView);
        return hafw;
    }

    /**
     * 获取数据
     */
    private void getThemesDetails() {
        new RetrofitHelper(getApplicationContext()).getThemesDetailsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThemesDetails>() {

                    @Override
                    public void call(ThemesDetails themesDetails) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (themesDetails != null) {
                            stories.addAll(themesDetails.getStories());
                            editors.addAll(themesDetails.getEditors());
                            mToolbar.setTitle(themesDetails.getName());
                            mRecyclerView.setAdapter(addHeadView(themesDetails));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSwipeRefreshLayout.setRefreshing(false);
//                        LogUtil.all("加载数据失败");
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
