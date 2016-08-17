package com.myframe.model;

import java.util.List;

/**
 * Created by hcc on 16/4/23 14:06
 * 100332338@qq.com
 * <p/>
 * 知乎专栏列表
 */
public class DailySections {

    public List<DailySectionsInfo> data;

    public class DailySectionsInfo {

        public String description;

        public int id;

        public String name;

        public String thumbnail;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
