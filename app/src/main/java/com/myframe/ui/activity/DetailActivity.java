package com.myframe.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fastbuildlibrary.base.FBLSwipeBackBaseActivity;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.DailyBean;
import com.myframe.model.DailyDetail;
import com.myframe.model.DailyExtraMessage;
import com.myframe.net.RetrofitHelper;
import com.myframe.utils.HtmlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DetailActivity extends FBLSwipeBackBaseActivity {
    @BindView(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.detail_image)
    ImageView mDetailImage;

    @BindView(R.id.detail_title)
    TextView mDetailTitle;

    @BindView(R.id.detail_source)
    TextView mDetailSource;

    @BindView(R.id.detail_web_view)
    WebView mWebView;

    @BindView(R.id.pb_loading_detail)
    ProgressBar loading_detail;

    private MenuItem itemCommentNum;
    private MenuItem itemPariseNum;
    private MenuItem itemParise;
    private DailyBean mDaily;
    private int id;
    private DailyExtraMessage mDailyExtraMessage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("detail_id", -1);
        }

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitleEnabled(true);
        supportActionBar.setTitle("");

//        //返回按钮也可以这样写
//        toolbar.setNavigationIcon(R.drawable.back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });

        getData(id);

    }

    /**
     * 获取数据
     */
    private void getData(int newsId) {
        new RetrofitHelper(getApplicationContext()).getNewsDetails(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<DailyDetail>() {
                    @Override
                    public void call(DailyDetail dailyDetail) {
                        loading_detail.setVisibility(View.GONE);
                        if (dailyDetail != null) {
                            //设置标题
                            mDetailTitle.setText(dailyDetail.getTitle());
                            //设置图片来源
                            mDetailSource.setText(dailyDetail.getImage_source());
                            //设置web内容加载
                            String htmlData = HtmlUtil.createHtmlData(dailyDetail);
                            mWebView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                            //设置图片
                            Picasso.with(getApplicationContext()).load(dailyDetail.getImage()).error(R.drawable.account_avatar).into(mDetailImage);
                            getDailyMessage(dailyDetail.getId());
                        }
                    }
                },new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loading_detail.setVisibility(View.GONE);
                        //"数据加载失败"
                        toastShort(R.string.load_failed);
                    }
                });
    }

    /**
     * 设置日报的评论数跟点赞数
     *
     * @param id
     */
    private void getDailyMessage(int id) {

        new RetrofitHelper(getApplicationContext()).getDailyExtraMessageById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DailyExtraMessage>() {

                    @Override
                    public void call(DailyExtraMessage dailyExtraMessage) {

                        if (dailyExtraMessage != null) {
                            mDailyExtraMessage = dailyExtraMessage;

                            itemCommentNum.setTitle(dailyExtraMessage.comments + "");
                            itemPariseNum.setTitle(dailyExtraMessage.popularity + "");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                DetailActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                            }
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);
        itemCommentNum = menu.findItem(R.id.menu_action_comment_num);
        itemPariseNum = menu.findItem(R.id.menu_action_parise_num);
        itemParise = menu.findItem(R.id.menu_action_parise);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_action_share:
                //分享新闻
                share();
                return true;
            case R.id.menu_action_comment:
                // 查看新闻评论
                CommentActivity.luancher(DetailActivity.this, mDaily == null ? id : mDaily.getId(), mDailyExtraMessage.comments, mDailyExtraMessage.longComments, mDailyExtraMessage.shortComments);
                return true;
            case R.id.menu_action_parise:
                //执行点赞动画
                AnimationUtils.loadAnimation(DetailActivity.this, R.anim.anim_small);
                itemParise.setIcon(R.mipmap.ic_detail_praise);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_from) + mDaily.getTitle() + "，http://daily.zhihu.com/story/" + mDaily.getId());
        startActivity(Intent.createChooser(intent, mDaily.getTitle()));
    }

    @Override
    protected void onDestroy() {
        Unbinder.EMPTY.unbind();
        super.onDestroy();
    }
}
