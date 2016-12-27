package com.myframe.example.json;

import java.io.Serializable;

/**
 * Created by Aaron on 2016/6/2.
 */
public class PersionBean implements Serializable {
    public String name;
    public String sex;
    public String addr;

    @Override
    public String toString() {
        return "name:"+name+",sex:"+sex+",addr:"+addr;
    }
}
