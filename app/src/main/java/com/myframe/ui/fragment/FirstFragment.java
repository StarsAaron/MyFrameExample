package com.myframe.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastbuildlibrary.adapter.recyclerview.CommonAdapter;
import com.fastbuildlibrary.adapter.recyclerview.LoadMoreOnScrollListener;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;
import com.fastbuildlibrary.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.fastbuildlibrary.widget.CirclePageIndicator;
import com.fastbuildlibrary.widget.loopviewpager.AutoLoopViewPager;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.DailyBean;
import com.myframe.model.DailyListBean;
import com.myframe.model.TopDailys;
import com.myframe.net.RetrofitHelper;
import com.myframe.ui.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FirstFragment extends Fragment {
    @BindView(R.id.daily_recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CommonAdapter dailylistAdapter;
    private List<DailyBean> dailys = new ArrayList<>();
    private AutoLoopViewPager autoLoopViewPager;
    private CirclePageIndicator circlePageIndicator;
    private LayoutInflater inflater;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private String currentTime;//当前时间
    private LoadMoreOnScrollListener loadMoreOnScrollListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
            }
        }
    };

    public FirstFragment() {
    }

    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //刷新加载更多
                getLatesDailys(false);
            }
        });

        dailylistAdapter = new CommonAdapter<DailyBean>(getActivity(), R.layout.item_daily_list, dailys) {

            @Override
            protected void convert(final ViewHolder viewHolder, final DailyBean item, int position) {
                viewHolder.setText(R.id.item_title,item.getTitle());
                List<String> images = item.getImages();
                if (images != null && images.size() > 0) {
                    Picasso.with(getContext()).load(images.get(0)).into((ImageView) viewHolder.getView(R.id.item_image));
                }
                boolean multipic = item.isMultipic();
                if (multipic) {
                    viewHolder.getView(R.id.item_more_pic).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.item_more_pic).setVisibility(View.GONE);
                }
                if (!item.isRead()) {
                    viewHolder.setTextColorRes(R.id.item_title,R.color.color_unread);
                } else {
                    viewHolder.setTextColorRes(R.id.item_title,R.color.color_read);
                }
                viewHolder.getView(R.id.card_view).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (!item.isRead()) {
                            item.setRead(true);
                            viewHolder.setTextColorRes(R.id.item_title,R.color.color_read);
//                            new Thread(new Runnable() {
//
//                                @Override
//                                public void run() {
//
//                                    mDailyDao.insertReadNew(dailyBean.getId() + "");
//                                }
//                            }).start();
                        }
                        //跳转到详情界面
                        UIHelper.showDetail(getActivity(),item.getId());
                    }
                });
            }
        };

        headerAndFooterWrapper = new HeaderAndFooterWrapper(dailylistAdapter);
        View headView = inflater.inflate(R.layout.headview_daily, null, false);
        autoLoopViewPager = (AutoLoopViewPager) headView.findViewById(R.id.daily_head_autolooppager);
        circlePageIndicator = (CirclePageIndicator) headView.findViewById(R.id.daily_head_indicator);

        headerAndFooterWrapper.addHeaderView(headView); // 添加头

        mRecyclerView.setAdapter(headerAndFooterWrapper);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);

        loadMoreOnScrollListener = new LoadMoreOnScrollListener(llm) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMore();
//                ToastUtils.showShort(getActivity(),"正在加载更多");
            }
        };
        mRecyclerView.addOnScrollListener(loadMoreOnScrollListener);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
        getLatesDailys(false);

        return view;
    }

    /**
     * 获取最新内容
     * @param isDownRefresh
     */
    public void getLatesDailys(final boolean isDownRefresh) {

        new RetrofitHelper(getContext()).getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {

                    @Override
                    public void call() {

                        if (!isDownRefresh) {
//                            showProgress();
                        }
                    }
                })
                .map(new Func1<DailyListBean, DailyListBean>() {

                    @Override
                    public DailyListBean call(DailyListBean dailyListBean) {

                        return changeReadState(dailyListBean);
                    }
                })
                .subscribe(new Action1<DailyListBean>() {

                    @Override
                    public void call(DailyListBean dailyListBean) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (dailyListBean.getStories() == null) {

                        } else {
                            currentTime = dailyListBean.getDate();
                            dailylistAdapter.addData(dailyListBean.getStories());
                            List<TopDailys> tops = dailyListBean.getTop_stories();
                            MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getContext(),tops);
                            autoLoopViewPager.setAdapter(myPagerAdapter);
                            circlePageIndicator.setViewPager(autoLoopViewPager);
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        mSwipeRefreshLayout.setRefreshing(false);
//                        hideProgress();
                    }
                });
    }

    /**
     * 加载更多
     */
    public void loadMore(){
        new RetrofitHelper(getContext()).getBeforeNews(currentTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DailyListBean, DailyListBean>() {

                    @Override
                    public DailyListBean call(DailyListBean dailyListBean) {
//                        cacheAllDetail(dailyListBean.getStories());
                        return changeReadState(dailyListBean);
                    }
                })
                .subscribe(new Action1<DailyListBean>() {

                    @Override
                    public void call(DailyListBean dailyListBean) {
                        loadMoreOnScrollListener.setLoading(false);
                        dailylistAdapter.addData(dailyListBean.getStories());
                        currentTime = dailyListBean.getDate();
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        loadMoreOnScrollListener.setLoading(false);
//                        LogUtil.all("加载更多数据失败");
                    }
                });
    }

    /**
     * 改变点击已阅读状态
     *
     * @param dailyList
     * @return
     */
    public DailyListBean changeReadState(DailyListBean dailyList) {

//        List<String> allReadId = new DailyDao(getActivity()).getAllReadNew();
//        for (DailyBean daily : dailyList.getStories()) {
//            daily.setDate(dailyList.getDate());
//            for (String readId : allReadId) {
//                if (readId.equals(daily.getId() + "")) {
//                    daily.setRead(true);
//                }
//            }
//        }
        return dailyList;
    }

    @Override
    public void onDestroyView() {
        Unbinder.EMPTY.unbind();
        super.onDestroyView();
    }

    //轮播图适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<TopDailys> tops;
        private Context context;

        public MyPagerAdapter(Context context, List<TopDailys> tops) {
            this.context = context;
            this.tops = tops;
        }

        @Override
        public int getCount() {
            return tops.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = inflater.inflate(R.layout.item_autopager, container, false);
            ImageView mImg = (ImageView) view.findViewById(R.id.pager_img);
            TextView mTitle = (TextView) view.findViewById(R.id.pager_title);
            TopDailys mTopDaily = tops.get(position);

            Picasso.with(context).load(mTopDaily.getImage()).into(mImg);
            mTitle.setText(mTopDaily.getTitle());

            final int id = mTopDaily.getId();
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //跳转到详细页面
//                    DailyDetailActivity.lanuch(mContext, id);
                    //跳转到详情界面
                    UIHelper.showDetail(getActivity(),tops.get(position).getId());
                }
            });
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
