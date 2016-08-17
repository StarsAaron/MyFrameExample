package com.myframe.net;

import com.myframe.model.DailyComment;
import com.myframe.model.DailyDetail;
import com.myframe.model.DailyExtraMessage;
import com.myframe.model.DailyListBean;
import com.myframe.model.DailyRecommend;
import com.myframe.model.DailySections;
import com.myframe.model.DailyTypeBean;
import com.myframe.model.HotNews;
import com.myframe.model.LuanchImageBean;
import com.myframe.model.SectionsDetails;
import com.myframe.model.ThemesDetails;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 网络请求接口
 */
public interface ZhiHuDailyAPI {

    /**
     * 获取最新的日报数据
     *
     * @return
     */
    @GET("stories/latest")
    Observable<DailyListBean> getlatestNews();


    /**
     * 根据时间获取对应的日报数据
     *
     * @param date
     * @return
     */
    @GET("stories/before/{date}")
    Observable<DailyListBean> getBeforeNews(@Path("date") String date);

    /**
     * 获取日报详情数据
     *
     * @param id
     * @return
     */
    @GET("story/{id}")
    Observable<DailyDetail> getNewsDetails(@Path("id") int id);

    /**
     * 根据分辨率获取启动界面图片
     *
     * @param res
     * @return
     */
    @GET("start-image/{res}")
    Observable<LuanchImageBean> getLuanchImage(@Path("res") String res);

    /**
     * 获取专题日报
     *
     * @return
     */
    @GET("themes")
    Observable<DailyTypeBean> getDailyType();

    /**
     * 根据id查询主题日报内容
     *
     * @param id
     * @return
     */
    @GET("theme/{id}")
    Observable<ThemesDetails> getThemesDetailsById(@Path("id") int id);

    /**
     * 根据id查询日报的额外信息
     *
     * @param id
     * @return
     */
    @GET("story-extra/{id}")
    Observable<DailyExtraMessage> getDailyExtraMessageById(@Path("id") int id);

    /**
     * 根据id查询日报的长评论
     *
     * @param id
     * @return
     */
    @GET("story/{id}/long-comments")
    Observable<DailyComment> getDailyLongComment(@Path("id") int id);

    /**
     * 根据id查询日报的短评论
     *
     * @param id
     * @return
     */
    @GET("story/{id}/short-comments")
    Observable<DailyComment> getDailyShortComment(@Path("id") int id);

    /**
     * 获取最新的热门新闻
     *
     * @return
     */
    @GET("news/hot")
    Observable<HotNews> getHotNews();


    /**
     * 获取知乎专栏数据
     *
     * @return
     */
    @GET("sections")
    Observable<DailySections> getZhiHuSections();

    /**
     * 获取专栏详情数据
     *
     * @param id
     * @return
     */
    @GET("section/{id}")
    Observable<SectionsDetails> getSectionsDetails(@Path("id") int id);

    /**
     * 获取专栏的之前消息
     *
     * @param id
     * @param timestamp
     * @return
     */
    @GET("section/{id}/before/{timestamp}")
    Observable<SectionsDetails> getBeforeSectionsDetails(@Path("id") int id, @Path("timestamp") long timestamp);

    /**
     * 根据日报id查询该日报的推荐者信息
     *
     * @param id
     * @return
     */
    @GET("story/{id}/recommenders")
    Observable<DailyRecommend> getDailyRecommendEditors(@Path("id") int id);
}
