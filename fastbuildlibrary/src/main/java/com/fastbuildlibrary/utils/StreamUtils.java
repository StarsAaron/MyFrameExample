package com.fastbuildlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * InputStream,int,shot,long与byte数组之间的互相转换
 * Created by aaron on 16-6-12.
 */
public class StreamUtils {
    /**
     * InputStream 转为 byte[]
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] inputStream2ByteArray(InputStream inputStream) throws Exception {
//        byte[] data = null;
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            byte[] result = new byte[1024];
//            int len = 0;
//            while ((len = inputStream.read(result)) != -1) {
//                outputStream.write(result, 0, len);
//            }
//            data = outputStream.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//            }
//        }
//        return data;

        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] b = new byte[count];
        inputStream.read(b);
        inputStream.close();
        return b;
    }

    /**
     * byte[] 转 InputStream
     * @param b
     * @return
     * @throws Exception
     */
    public static InputStream byteArray2InputStream(byte[] b) throws Exception {
        InputStream is = new ByteArrayInputStream(b);
        return is;
    }

    /**
     * 短整型与字节的转换
     * @param number
     * @return
     */
    public static byte[] short2ByteArray(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 字节的转换与短整型
     * @param b
     * @return
     */
    public static short byteArray2Short(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 整型与字节数组的转换
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     * @param i
     * @return
     */
    public static byte[] int2ByteArray(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) (0xff & i);
        bt[1] = (byte) ((0xff00 & i) >> 8);
        bt[2] = (byte) ((0xff0000 & i) >> 16);
        bt[3] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     * @param value
     * @return
     */
    public static byte[] int2ByteArray2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     * @param ary    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int byteArray2Int(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
     * @param src
     * @param offset
     * @return
     */
    public static int byteArray2Int2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    /**
     * 字节数组和长整型的转换
     * @param number
     * @return
     */
    public static byte[] long2ByteArray(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();
            // 将最低位保存在最低位
            temp = temp >> 8;
            // 向右移8位
        }
        return b;
    }

    /**
     * 字节数组和长整型的转换
     * @param b
     * @return
     */
    public static long byteArray2Long(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff; // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * Simple wrapper around {@link InputStream#read()} that throws EOFException
     * instead of returning -1.
     *
     * @param is the is
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static int read(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new EOFException();
        }
        return b;
    }

    /**
     * Write int.
     *
     * @param os the os
     * @param n  the n
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 0xff);
        os.write((n >> 8) & 0xff);
        os.write((n >> 16) & 0xff);
        os.write((n >> 24) & 0xff);
    }

    /**
     * Read int.
     *
     * @param is the is
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static int readInt(InputStream is) throws IOException {
        int n = 0;
        n |= (read(is) << 0);
        n |= (read(is) << 8);
        n |= (read(is) << 16);
        n |= (read(is) << 24);
        return n;
    }

    /**
     * Write long.
     *
     * @param os the os
     * @param n  the n
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte) (n >>> 0));
        os.write((byte) (n >>> 8));
        os.write((byte) (n >>> 16));
        os.write((byte) (n >>> 24));
        os.write((byte) (n >>> 32));
        os.write((byte) (n >>> 40));
        os.write((byte) (n >>> 48));
        os.write((byte) (n >>> 56));
    }

    /**
     * Read long.
     *
     * @param is the is
     * @return the long
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static long readLong(InputStream is) throws IOException {
        long n = 0;
        n |= ((read(is) & 0xFFL) << 0);
        n |= ((read(is) & 0xFFL) << 8);
        n |= ((read(is) & 0xFFL) << 16);
        n |= ((read(is) & 0xFFL) << 24);
        n |= ((read(is) & 0xFFL) << 32);
        n |= ((read(is) & 0xFFL) << 40);
        n |= ((read(is) & 0xFFL) << 48);
        n |= ((read(is) & 0xFFL) << 56);
        return n;
    }

    /**
     * Write string.
     *
     * @param os the os
     * @param s  the s
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeString(OutputStream os, String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        writeLong(os, b.length);
        os.write(b, 0, b.length);
    }

    /**
     * Write string string map.
     *
     * @param map the map
     * @param os  the os
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
        if (map != null) {
            writeInt(os, map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeString(os, entry.getKey());
                writeString(os, entry.getValue());
            }
        } else {
            writeInt(os, 0);
        }
    }
}
