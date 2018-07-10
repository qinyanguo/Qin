package com.ycmm.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.ycmm.model.wx.ImageMessage;
import com.ycmm.model.wx.OutPutMessage;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.print.Book;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * xml解析
 */
public class XmlPraser {

    private final static List<Book> bookList = new ArrayList<Book>();

    public static void dome4j() {
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File("C:/Users/jishubu/Downloads/books.xml"));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();

            // 将解析结果存储在HashMap中
            HashMap<String, String> map = new HashMap<String, String>();
            recursiveParseXML(bookStore,map);


            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历某一本书=====");
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    System.out.println("属性名：" + attr.getName() + "--属性值："
                            + attr.getValue());
                }
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                }
                System.out.println("=====结束遍历某一本书=====");
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 解析微信服务器返回xml
     * @param root
     * @param map
     */
    private static void recursiveParseXML(Element root,HashMap<String, String> map){
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        //判断有没有子元素列表
        if(elementList.size() == 0){
            map.put(root.getName(), root.getText());
            System.out.println(root.getName());
            System.out.println(root.getText());
        }else{
            //遍历
            for (Element e : elementList){
                recursiveParseXML(e,map);
            }
        }
    }

    /**
     * 扩展xstream使其支持CDATA
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                //@SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String messageToXML(OutPutMessage textMessage) {
        xstream.alias("xml", OutPutMessage.class);
        return xstream.toXML(textMessage);
    }

    /**
     * 图片消息对象转换成xml
     *
     * @param imageMessage 图片消息对象
     * @return xml
     */
    public static String messageToXML(ImageMessage imageMessage) {
        xstream.alias("xml", ImageMessage.class);
        return xstream.toXML(imageMessage);
    }
    /**
     * 语音消息对象转换成xml
     *
     * @param voiceMessage 语音消息对象
     * @return xml
     */
//    public static String messageToXML(VoiceMessage voiceMessage) {
//        xstream.alias("xml", VoiceMessage.class);
//        return xstream.toXML(voiceMessage);
//    }

    /**
     * 视频消息对象转换成xml
     *
     * @param
     * @return xml
     */
//    public static String messageToXML(VideoMessage videoMessage) {
//        xstream.alias("xml", VideoMessage.class);
//        return xstream.toXML(videoMessage);
//    }

    public static void main(String[] args) {
        dome4j();
    }
}
