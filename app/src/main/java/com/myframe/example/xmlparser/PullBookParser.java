package com.myframe.example.xmlparser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class PullBookParser implements BookParser {

	/**
	 * 1：当导航到XmlPullParser.START_DOCUMENT，可以不做处理，当然你可以实例化集合对象等等。
	 * 2：当导航到XmlPullParser.START_TAG，则判断是否是xx标签，如果是，则实例化xx对象，并调用getAttributeValue方法获取标签中属性值。
	 * 3：当导航到其他标签，比如Introduction时候，则判断xx对象是否为空，如不为空，则取出Introduction中的内容，nextText方法来获取文本节点内容
	 * 4：当导航到XmlPullParser.END_TAG在这里我们就需要判读是否是xx结束标签，如果是，则把xx对象存进list集合中了，并设置xx对象为null.
	 */
	@Override
	public List<Book> parse(InputStream in) throws Exception {
		List<Book> books = null;
		Book book = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(in, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				books = new ArrayList<Book>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("book")) {
					book = new Book();
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
					book.setId(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("name")) {
					eventType = parser.next();
					book.setName(parser.getText());
				} else if (parser.getName().equals("price")) {
					eventType = parser.next();
					book.setPrice(Float.parseFloat(parser.getText()));
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("book")) {
					books.add(book);
					book = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return books;
	}

	@Override
	public String serialize(List<Book> booksList) throws Exception {
//      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
//      XmlSerializer serializer = factory.newSerializer();  
          
        XmlSerializer serializer = Xml.newSerializer(); //由android.util.Xml创建一个XmlSerializer实例  
        StringWriter writer = new StringWriter();  
        serializer.setOutput(writer);   //设置输出方向为writer  
        serializer.startDocument("UTF-8", true);  
        serializer.startTag("", "books");  
        for (Book book : booksList) {  
            serializer.startTag("", "book");  
            serializer.attribute("", "id", book.getId() + "");  
              
            serializer.startTag("", "name");  
            serializer.text(book.getName());  
            serializer.endTag("", "name");  
              
            serializer.startTag("", "price");  
            serializer.text(book.getPrice() + "");  
            serializer.endTag("", "price");  
              
            serializer.endTag("", "book");  
        }  
        serializer.endTag("", "books");  
        serializer.endDocument();  
          
        return writer.toString();  
    } 
}
