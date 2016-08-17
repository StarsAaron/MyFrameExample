package com.myframe.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 基于values/dimens.xml文件自动生成不同dpi下的dimen文件。单位为dp
 * 基础参照smallestScreenWidth为800
 * 注意：取数值31sp  >< 之间不能有空格，<括号前面紧贴的是两个字符dp或sp  <dimen name="text_size_31">31sp</dimen>
 */
public class DimenTool {
    public static final int[] sizes = {300,320,340,360,370,380,400,480,500,520,600,720,800,820};
    public static boolean isBefore13 = true;//是否兼容V13之前的版本，因为V13之前的版本不认识sw符号的资源
    public static String pathBegin = "./app/src/main/res/values-sw";
    public static String pathEnd = "dp/dimens.xml";
    public static String basePath = "./app/src/main/res/values/dimens.xml";//基准文件路径
    public static int baseSize = 380;//基准为sw380dp
    public static ResBean[] resBeens;

    static class ResBean{
        public int size;//尺寸
        public String path;//文件路径
        public StringBuffer stringBuffer;//字符内容构建
        public double bei;//与基准的比

        ResBean(int size){
            this.stringBuffer = new StringBuffer();
            this.size = size;
            this.path = pathBegin+size+pathEnd;
            this.bei = (double)size/baseSize;
        }
    }

    public static void gen() {
        File file = new File(basePath);
        BufferedReader reader = null;

        //创建ResBean数组
        resBeens = new ResBean[sizes.length];
        for(int i=0;i<sizes.length;i++){
            resBeens[i] = new ResBean(sizes[i]);
        }

        //开始转换文件
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    //取数值31sp  >< 之间不能有空格，<括号前面紧贴的是两个字符dp或sp  <dimen name="text_size_31">31sp</dimen>
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<"));
                    //去空格
                    String valueNoSpace = tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>")).replaceAll(" ","");
                    //去单位，单位dp或sp占2个字符
                    String valueNoDp = valueNoSpace.substring(0,valueNoSpace.length()-2);
                    //获取单位
                    String d = valueNoSpace.substring(valueNoSpace.length()-2,valueNoSpace.length());
                    end = d + end;
                    //去掉小数点，取整
                    int index = valueNoDp.lastIndexOf(".");
                    if(index != -1){
                        valueNoDp = valueNoDp.substring(0,index);
                    }
                    int num = Integer.valueOf(valueNoDp);

                    //分别生成不同尺寸
                    for(int i=0;i<resBeens.length;i++){
                        resBeens[i].stringBuffer.append(start).append((int) Math.round(num * resBeens[i].bei)).append(end).append("\n");
                    }
                } else {
                    //分别生成不同尺寸，不改变值
                    for(int i=0;i<resBeens.length;i++){
                        resBeens[i].stringBuffer.append(tempString).append("\n");
                    }
                }
            }
            reader.close();
            //打印输出
            for(int i=0;i<resBeens.length;i++){
                System.out.println("<!-- "+ resBeens[i].size +" -->");
                System.out.println(resBeens[i].stringBuffer.toString());
            }

            // 写出到文件
            for(int i=0;i<resBeens.length;i++){
                if(isBefore13){
                    if(sizes[i] == 300){
                        //兼容13以下版本
                        //可以当做sw300计算
                        String small = "./app/src/main/res/values-small/dimens.xml";
                        writeFile(small, resBeens[i].stringBuffer.toString());
                    }
                    if(sizes[i] == 320){
                        //可以当做sw320计算
                        String normal = "./app/src/main/res/values-normal/dimens.xml";
                        writeFile(normal, resBeens[i].stringBuffer.toString());
                    }
                    if(sizes[i] == 480){
                        //可以当做sw480计算
                        String large = "./app/src/main/res/values-large/dimens.xml";
                        writeFile(large, resBeens[i].stringBuffer.toString());
                    }
                    if(sizes[i] == 720){
                        //可以当做sw720计算
                        String xlarge = "./app/src/main/res/values-xlarge/dimens.xml";
                        writeFile(xlarge, resBeens[i].stringBuffer.toString());
                    }
                }
                writeFile(resBeens[i].path, resBeens[i].stringBuffer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入文件
     *
     * @param file
     * @param text
     */
    public static void writeFile(String file, String text) {
        //没有这个文件夹就先创建，不然后面的写入文件就会报错，文件找不到
        File dis = new File(file.substring(0, file.lastIndexOf("/")));
        if (!dis.exists()) {
            dis.mkdir();
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}