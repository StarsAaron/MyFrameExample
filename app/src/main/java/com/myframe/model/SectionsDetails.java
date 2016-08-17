package com.myframe.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hcc on 16/4/23 15:25
 * 100332338@qq.com
 * <p/>
 * 专栏详情数据查看
 */
public class SectionsDetails {

    public String name;

    public long timestamp;

    public List<SectionsDetailsInfo> stories;

    public class SectionsDetailsInfo {

        public String date;

        @SerializedName("display_date")
        public String displayDate;

        public int id;

        public List<String> images;

        public String title;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDisplayDate() {
            return displayDate;
        }

        public void setDisplayDate(String displayDate) {
            this.displayDate = displayDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
