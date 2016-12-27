package com.myframe.example.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;


/**
 * SAX解析
 * 
 * @author zhrjian
 *
 */
public class SaxBookParser implements BookParser {

	/**
	 * 采用SAX解析时具体处理步骤是： 1 创建SAXParserFactory对象 2
	 * 根据SAXParserFactory.newSAXParser()方法返回一个SAXParser解析器 3
	 * 根据SAXParser解析器获取事件源对象XMLReader 4 实例化一个DefaultHandler对象 5
	 * 连接事件源对象XMLReader到事件处理类DefaultHandler中 6 调用XMLReader的parse方法从输入源中获取到的xml数据
	 * 7 通过DefaultHandler返回我们需要的数据集合。
	 */
	@Override
	public List<Book> parse(InputStream in) throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		MyHandler myHandler = new MyHandler(); // 实例化自定义Handler
		saxParser.parse(in, myHandler);
		return myHandler.getBookList();
	}

	@Override
	public String serialize(List<Book> booksList) throws Exception {
		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();// 取得SAXTransformerFactory实例
		TransformerHandler handler = factory.newTransformerHandler(); // 从factory获取TransformerHandler实例
		Transformer transformer = handler.getTransformer(); // 从handler获取Transformer实例
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // 设置输出采用的编码方式
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 是否自动添加额外的空白
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // 是否忽略XML声明

		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		handler.setResult(result);

		String uri = ""; // 代表命名空间的URI 当URI无值时 须置为空字符串
		String localName = ""; // 命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串

		handler.startDocument();
		handler.startElement(uri, localName, "books", null);
		AttributesImpl attrs = new AttributesImpl(); // 负责存放元素的属性信息
		char[] ch = null;
		for (Book book : booksList) {
			attrs.clear(); // 清空属性列表
			attrs.addAttribute(uri, localName, "id", "string", String.valueOf(book.getId()));// 添加一个名为id的属性(type影响不大,这里设为string)
			handler.startElement(uri, localName, "book", attrs); // 开始一个book元素关联上面设定的id属性

			handler.startElement(uri, localName, "name", null); // 开始一个name元素
			ch = String.valueOf(book.getName()).toCharArray();
			handler.characters(ch, 0, ch.length); // 设置name元素的文本节点
			handler.endElement(uri, localName, "name");

			handler.startElement(uri, localName, "price", null);// 开始一个price元素
			ch = String.valueOf(book.getPrice()).toCharArray();
			handler.characters(ch, 0, ch.length); // 设置price元素的文本节点
			handler.endElement(uri, localName, "price");

			handler.endElement(uri, localName, "book");
		}
		handler.endElement(uri, localName, "books");
		handler.endDocument();
		return writer.toString();
	}

	// 需要重写DefaultHandler的方法
	private class MyHandler extends DefaultHandler {
		private List<Book> booksList;
		private Book book;
		private StringBuilder builder;

		public List<Book> getBookList() {
			return booksList;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			booksList = new ArrayList<Book>();
			builder = new StringBuilder();
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("book")) {
				book = new Book();
			}
			builder.setLength(0);// 将字符长度设置为0 以便重新开始读取元素内的字符节点
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("id")) {
				book.setId(Integer.parseInt(builder.toString()));
			} else if (localName.equals("name")) {
				book.setName(builder.toString());
			} else if (localName.equals("price")) {
				book.setPrice(Float.parseFloat(builder.toString()));
			} else if (localName.equals("book")) {
				booksList.add(book);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);
		}

	}
}
