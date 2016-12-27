package com.myframe.example.json;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json 使用
 */
public class JsonTestActivity extends AppCompatActivity {
    private String jsonData = "{\"pp\":[{\"name\":\"pp\"},{\"name\":\"ppi\"}],\"persion\":[{\"name\":\"bill\",\"sex\":\"boy\",\"addr\":\"ss\"},{\"name\":\"tom\",\"sex\":\"girl\",\"addr\":\"dd\"}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildJsonData();
//        parseJsonData();
    }

    /**
     * 创建Json数据
     */
    private void buildJsonData(){
        Map<String,String> map1 = new ArrayMap<>();
        Map<String,String> map2 = new ArrayMap<>();
        List<Map> list = new ArrayList<>();

        map1.put("name","john");
        map1.put("sex","girl");
        map1.put("addr","ddd");

        map2.put("name","tom");
        map2.put("sex","boy");
        map2.put("addr","ccc");

        list.add(map1);
        list.add(map2);

        List<PersionBean> persons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PersionBean p = new PersionBean();
            p.name = "john";
            p.sex = "girl";
            p.addr = "ccc";
            persons.add(p);
        }

        Gson gson = new Gson();

        String str = gson.toJson(persons);
        Log.i("buildJsonData----",str);
        /*
        [{"addr":"ccc","name":"john","sex":"girl"},{"addr":"ccc","name":"john","sex":"girl"},...]
         */

        String str2 = gson.toJson(map1,new TypeToken<Map<String,String>>(){}.getType());
        Log.i("buildJsonData2----", str2);
        /*
        {"sex":"girl","addr":"ddd","name":"john"}
         */
        PersionBean p = gson.fromJson(str2,PersionBean.class);
        Log.i("parseJsonData2----",p.toString());
        /*
        name:john,sexgirl,addrddd
         */

        String str3 = gson.toJson(list,new TypeToken<List<Map>>(){}.getType());
        Log.i("buildJsonData3----", str3);
        /*
        [{"sex":"girl","addr":"ddd","name":"john"},{"sex":"boy","addr":"ccc","name":"tom"}]
         */
    }

    private void parseJsonData(){
//---------------不用Gson
//        try {
//            JSONObject jsonObject = new JSONObject(jsonData);
//            JSONArray jsonArray = jsonObject.getJSONArray("persion");
//            for(int i = 0;i<jsonArray.length();i++){
//                PersionBean persionBean = new PersionBean();
//                persionBean.name = jsonArray.getJSONObject(i).getString("name");
//                persionBean.sex = jsonArray.getJSONObject(i).getString("sex");
//                persionBean.addr = jsonArray.getJSONObject(i).getString("addr");
//                Log.i("parseJsonData","第"+i+"个："+persionBean.toString()+"\n");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//---------------用Gson
        Gson gson = new Gson();
        List<PersionBean> pp = gson.fromJson(jsonData, new TypeToken<List<PersionBean>>(){}.getType());
        for(PersionBean p : pp){
            Log.i("parseJsonData",p.toString()+"\n");
        }
    }
}
