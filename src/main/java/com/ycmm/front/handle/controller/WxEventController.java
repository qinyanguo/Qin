package com.ycmm.front.handle.controller;

import com.thoughtworks.xstream.XStream;
import com.ycmm.model.wx.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * 微博地址：https://blog.csdn.net/u014703502/article/details/54911554
 * 第一步：
 *    用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，或在进入会话后每5秒上报一次地理位置，
 *    公众号可以在公众平台网站中修改以上设置。上报地理位置时，微信会将上报地理位置事件推送到开发者填写的URL。
 *    这里填写的url，如图
 *    url填写为自己服务器的域名 如：http://www.xxxx.com/weixin/chat
 *    /weixin/chat  是下面wxEventController ,推送的链接。
 *    Token(令牌) ： 随意
 *    EncodingAESKey :随机生成就可以
 * 第二步：创建自己的controller,并解析 微信返回xml数据包
 */

@Controller
@RequestMapping("/weixin")
public class WxEventController {

    private static Logger logger = Logger.getLogger(WxEventController.class);


    private String Token = "sdapp080808";

    @RequestMapping(value = "/chat", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public void liaotian(Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入chat");
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            System.out.println(signature);
            System.out.println(timestamp);
            System.out.println(nonce);
            System.out.println(echostr);
            //access(request, response);
        } else {
            // 进入POST聊天处理
            System.out.println("enter post");
            try {
                // 接收消息并返回消息
                acceptMessage(request, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证URL真实性
     */
//    private String access(HttpServletRequest request, HttpServletResponse response) {
//        // 验证URL真实性
//        System.out.println("进入验证access");
//        String signature = request.getParameter("signature");// 微信加密签名
//        String timestamp = request.getParameter("timestamp");// 时间戳
//        String nonce = request.getParameter("nonce");// 随机数
//        String echostr = request.getParameter("echostr");// 随机字符串
//        List<String> params = new ArrayList<String>();
//        params.add(Token);
//        params.add(timestamp);
//        params.add(nonce);
//        // 1. 将token、timestamp、nonce三个参数进行字典序排序
//        Collections.sort(params, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
//        String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
//        if (temp.equals(signature)) {
//            try {
//                response.getWriter().write(echostr);
//                System.out.println("成功返回 echostr：" + echostr);
//                return echostr;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("失败 认证");
//        return null;
//    }

    private void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理接收消息
//        ServletInputStream in = request.getInputStream();
        /**
         * 这里测试用下边，现获取的为本地文件流，正式可直接用上边，直接获取文件输入流
         */
        InputStream in = new FileInputStream(new File("C:/Users/jishubu/Downloads/books.xml"));
        // 将POST流转换为XStream对象
        XStream xs = SerializeXmlUtil.createXstream();
        xs.processAnnotations(InputMessage.class);
        xs.processAnnotations(OutPutMessage.class);
        // 将指定节点下的xml节点数据映射为对象
        xs.alias("xml", InputMessage.class);
        // 将流转换为字符串
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            xmlMsg.append(new String(b, 0, n, "UTF-8"));
        }
        // 将xml内容转换为InputMessage对象
        InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());

        String servername = inputMsg.getToUserName();// 服务端
        String custermname = inputMsg.getFromUserName();// 客户端
        long createTime = inputMsg.getCreateTime();// 接收时间
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间

        // 取得消息类型
        String msgType = inputMsg.getMsgType();
        /**
         * 根据消息类型获取对应的消息内容，现只举个别类型实例
         */
        if (msgType.equals(MsgType.Text.toString())) {
            // 文本消息
            System.out.println("开发者微信号：" + inputMsg.getToUserName());
            System.out.println("发送方帐号：" + inputMsg.getFromUserName());
            System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
            System.out.println("消息内容：" + inputMsg.getContent());
            System.out.println("消息Id：" + inputMsg.getMsgId());

            StringBuffer str = new StringBuffer();
            str.append("<xml>");
            str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");
            str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");
            str.append("<CreateTime>" + returnTime + "</CreateTime>");
            str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");
            str.append("<Content><![CDATA[你说的是：" + inputMsg.getContent() + "，吗？]]></Content>");
            str.append("</xml>");
            System.out.println(str.toString());
            response.getWriter().write(str.toString());
        }
        // 获取并返回多图片消息
        if (msgType.equals(MsgType.Image.toString())) {
            System.out.println("获取多媒体信息");
            System.out.println("多媒体文件id：" + inputMsg.getMediaId());
            System.out.println("图片链接：" + inputMsg.getPicUrl());
            System.out.println("消息id，64位整型：" + inputMsg.getMsgId());

            OutPutMessage outputMsg = new OutPutMessage();
            outputMsg.setFromUserName(servername);
            outputMsg.setToUserName(custermname);
            outputMsg.setCreateTime(returnTime);
            outputMsg.setMsgType(msgType);
            ImageMessage images = new ImageMessage();
            images.setMediaId(inputMsg.getMediaId());
            outputMsg.setImage(images);
            System.out.println("xml转换：/n" + xs.toXML(outputMsg));
            response.getWriter().write(xs.toXML(outputMsg));

        }
    }

}