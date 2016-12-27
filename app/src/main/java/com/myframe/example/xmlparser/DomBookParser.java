package com.myframe.example.xmlparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DomBookParser implements BookParser {

	/**
	 * 1 首先利用DocumentBuilderFactory创建一个DocumentBuilderFactory实例 
	 * 2 然后利用DocumentBuilderFactory创建DocumentBuilder 
	 * 3 然后加载XML文档（Document） 
	 * 4 然后获取文档的根结点(Element) 
	 * 5 然后获取根结点中所有子节点的列表（NodeList） 
	 * 6 然后使用再获取子节点列表中的需要读取的结点
	 */
	@Override
	public List<Book> parse(InputStream in) throws Exception {
		List<Book> booksList = new ArrayList<Book>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(in); // 解析输入流 得到Document实例
		Element rootElement = doc.getDocumentElement();
		NodeList items = rootElement.getElementsByTagName("book");
		for (int i = 0; i < items.getLength(); i++) {
			Book book = new Book();
			Node item = items.item(i);
			NodeList properties = item.getChildNodes();
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				String nodeName = property.getNodeName();
				if (nodeName.equals("id")) {
					book.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
				} else if (nodeName.equals("name")) {
					book.setName(property.getFirstChild().getNodeValue());
				} else if (nodeName.equals("price")) {
					book.setPrice(Float.parseFloat(property.getFirstChild().getNodeValue()));
				}
			}
			booksList.add(book);
		}
		return booksList;
	}

	@Override
	public String serialize(List<Book> booksList) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument(); // 由builder创建新文档

		Element rootElement = doc.createElement("books");

		for (Book book : booksList) {
			Element bookElement = doc.createElement("book");
			bookElement.setAttribute("id", book.getId() + "");

			Element nameElement = doc.createElement("name");
			nameElement.setTextContent(book.getName());
			bookElement.appendChild(nameElement);

			Element priceElement = doc.createElement("price");
			priceElement.setTextContent(book.getPrice() + "");
			bookElement.appendChild(priceElement);

			rootElement.appendChild(bookElement);
		}

		doc.appendChild(rootElement);

		TransformerFactory transFactory = TransformerFactory.newInstance();// 取得TransformerFactory实例
		Transformer transformer = null;
		transformer = transFactory.newTransformer();// 从transFactory获取Transformer实例
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // 设置输出采用的编码方式
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 是否自动添加额外的空白
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // 是否忽略XML声明

		StringWriter writer = new StringWriter();

		Source source = new DOMSource(doc); // 表明文档来源是doc
		Result result = new StreamResult(writer);// 表明目标结果为writer
		transformer.transform(source, result);// 开始转换

		return writer.toString();
	}
}
