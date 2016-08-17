package com.fastbuildlibrary.config;

/**
 * 实现或继承此接口的类，其共有属性和方法将不参与混淆
 *
 -keep public interface com.yuzhi.fine.common.NotObfuscateInterface{public *;}
 -keep class * implements com.yuzhi.fine.common.NotObfuscateInterface{
 <methods>;
 <fields>;
 }

 */
public interface FBLNotObfuscateInterface {
}
