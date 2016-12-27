package com.fastbuildlibrary.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 用来生成Android中 findViewById代码块，以及BaseAdapter 中的getView 方法中的ViewHolder模块代码。<br/>
 * 看不懂没关系，跑一遍这个代码，你就知道干嘛用了。
 * 
 * @author tangxiaocheng<br/>
 *         使用规范:<br/>
 *         1:layout 布局xml文件中的id必须是"@+id/..."这种形式，不得是引用id.<br/>
 *         2:在生成代码之前，必须格式化layout 布局xml文件，这个很重要，因为这个工具类的原理仅仅是简单的逐行读取数据而已。<br/>
 *         3:为避免出错，不要有什么注释。<br/>
 *         注意事项<br/>
 *         1:如果不想对某个id生成对应的成员变量，请将这个id命名成以"_fake"为结尾的后缀<br/>
 */
public class MakeFindViewByIdUtils {
	private static final String CLASS_NAME = "MainActivity2"; //生成的文件名
	private static final String FAKE_TAG = "_fake";//不生成标志

	/**
	 * layout布局xml文件名称（你要生成findViewById的布局文件）
	 */
	private static String XML_NAME = "activity_main2";
	private static String CONVERT_VIEW = " convertView ";

	/**
	 * 项目路径 （请填写你项目所在路径）
	 */
	private static final String PROJECT_PATH = "./app/src/main";
	private static String xmlPath = PROJECT_PATH + "/res/layout/" + XML_NAME + ".xml";

	public static void main(String[] args) {
        XML_NAME = "activity_main2";
        CONVERT_VIEW = " convertView ";
		readByOnCreate();
		readByGetView();
	}

	/**
	 * 根据ID在Activity onCreate 方法中生成findViewById的方法以及对应的成员变量
	 */
	public static void readByOnCreate() {
	        Map<String, String> map = new HashMap<String, String>();
	        BufferedReader bufferedReader = null;
	        String lastLine = "";
	        try {
	            bufferedReader = new BufferedReader((new FileReader(xmlPath)));
	            while (bufferedReader.read() != -1) {
	                final String readLine =
	                    bufferedReader.readLine().toString().trim();
	                if (readLine.contains("android:id=\"@+id/")
	                    && !readLine.contains(FAKE_TAG)) {
	                    String key =
	                        readLine.replace("android:id=\"@+id/", "")
	                            .replace("\"", "");
	                    String value = lastLine.replace("<", "");
	                    map.put(key, value);
	                }
	                lastLine = readLine;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
	            String id = (String)iterator.next();
	            System.out.println("private " + map.get(id) + " " + id + ";");
	        }
	        System.out.println();
	        System.out.println("private void initView(){");
	        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
	            String id = (String)iterator.next();
	            System.out.println(id + " = (" + map.get(id)
	                + ")findViewById(R.id." + id + ");");
	        }
	        System.out.println("}");
	    }

	/**
	 * 在BaseAdapter类中，根据指定的xml布局文件生成对应的findViewById方法以及对应的ViewHolder
	 */
	public static void readByGetView() {
	        Map<String, String> map = new HashMap<String, String>();
	        BufferedReader bufferedReader = null;
	        String lastLine = "";
	        try {
	            bufferedReader = new BufferedReader((new FileReader(xmlPath)));
	            while (bufferedReader.read() != -1) {
	                final String readLine = bufferedReader.readLine().toString().trim();
	                if (readLine.contains("android:id=\"@+id/")&& !readLine.contains(FAKE_TAG)) {
	                    String key =readLine.replace("android:id=\"@+id/", "").replace("\"", "");
	                    String value = lastLine.replace("<", "");
	                    map.put(key, value);
	                }
	                lastLine = readLine;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        System.out.println("    private final class " + CLASS_NAME + " { ");
	        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
	            String id = (String)iterator.next();
	            System.out.println("private " + map.get(id) + " " + id + ";");
	        }
	        System.out.println("}");
	        System.out.println();

	        final String firstString = new String(new char[]{CLASS_NAME.charAt(0)});
	        final String classObject = firstString.toLowerCase()  +CLASS_NAME.substring(1,CLASS_NAME.length());
	        System.out.println("" + CLASS_NAME + " " + classObject + "  = null ;");
	        System.out.println(" if (" + CONVERT_VIEW + " == null) { ");
	        System.out.println(" " + CONVERT_VIEW
	            + " = LayoutInflater.from(context) .inflate(R.layout." + XML_NAME
	            + ", null);");
	        System.out.println(classObject + " = new " + CLASS_NAME + "  ();");

	        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
	            String id = (String)iterator.next();
	            System.out.println(classObject + "." + id + " = (" + map.get(id)
	                + ")" + CONVERT_VIEW + "." + "findViewById(R.id." + id + ");");
	        }
	        System.out.println(CONVERT_VIEW + ".setTag(" + classObject + ");");
	        System.out.println("} else { " + classObject + " = (" + CLASS_NAME
	            + ")" + CONVERT_VIEW + ".getTag(); }");

	    }
}
