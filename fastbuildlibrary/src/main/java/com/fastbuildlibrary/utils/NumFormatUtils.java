package com.fastbuildlibrary.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字格式化工具类
 */
public class NumFormatUtils {
    private String str = "###,##";//格式化的形式

    private NumFormatUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * DecimalFormat
     * @param price
     * @return
     */
    public static String format1(double price){
        return new DecimalFormat("###.##").format(price);
    }

    /**
     * BigDecimal
     * @param price
     * @return
     */
    public static String format2(double price){
        /*
        ROUND_HALF_UP  : 遇到 .5 的情况时往上近似,例: 1.5 ->;2
        ROUND_HALF_DOWN : 遇到 .5 的情况时往下近似,例: 1.5 ->;1
         */
        BigDecimal b = new BigDecimal(price);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//四舍五入，保留两位小数
        return String.valueOf(f1);
    }

    /**
     * NumberFormat
     * @param price
     * @return
     */
    public static String format3(double price){
        NumberFormat numberFormat = NumberFormat.getInstance( );
        numberFormat.setMinimumIntegerDigits(3);  //整数位最少位数
        numberFormat.setMinimumFractionDigits(2); // 小数位最少位数
        numberFormat.setMaximumFractionDigits(4); // 小数位最多位数
        return numberFormat.format(price);
    }

    /**
     * 数字转中文单位
     * 例如：300.25
     * 转成中文金钱字符串：叁佰元贰角伍分
     *
     * @param value
     * @return
     */
    public static String numToRMB(double value) {
        char[] hunit = {'拾', '佰', '仟'}; // 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        BigDecimal midVal = new BigDecimal(Math.round(value * 100)); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串
        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分
        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else if ((rail.charAt(0) - '0') > 0 && (rail.charAt(1) - '0') == 0) {
            suffix = digit[rail.charAt(0) - '0'] + "角";// 把角转化出来
        } else if ((rail.charAt(0) - '0') == 0
                && (rail.charAt(1) - '0') > 0) {
            suffix = digit[rail.charAt(1) - '0'] + "分";// 把角转化出来
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角"
                    + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        boolean preZero = false; // 标志当前位的上一位是否为有效0位（如万位的0对千位无效）
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                preZero = true;
                zeroSerNum++; // 连续0次数递增
                if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    preZero = false; // 不管上一位是否为0，置为无效0位
                }
            } else {
                zeroSerNum = 0; // 连续0次数清零
                if (preZero) { // 上一位为有效0位
                    prefix += digit[0]; // 只有在这地方用到'零'
                    preZero = false;
                }
                prefix += digit[chDig[i] - '0']; // 转化该数字表示
                if (idx > 0)
                    prefix += hunit[idx - 1];
                if (idx == 0 && vidx > 0) {
                    prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
                }
            }
        }

        if (prefix.length() > 0)
            prefix += '元'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }
}
