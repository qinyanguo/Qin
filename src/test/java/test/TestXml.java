package test;


import com.ycmm.common.utils.HttpClientUtils;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestXml {

//    private static ClassPathXmlApplicationContext context;
//    public static void main(String[] args) {
//        context = new ClassPathXmlApplicationContext(new String[] { "/META-INF/applicationContext.xml" });
//        context.start();
//
//        File file = new File("C:/Users/jishubu/Downloads/books.xml");
//        System.out.println(file.getAbsoluteFile());
//
//        XmlPraser.dome4j();
//    }


    private static String host = "http://127.0.0.1:8080/smile/weixin";

    public static JSONObject demoTest() throws Exception {

        long time = System.currentTimeMillis();
        JSONObject biz_paramLogin = new JSONObject();
        biz_paramLogin.put("id", "admin");
        biz_paramLogin.put("uid", "123456");
        InputStream i = new FileInputStream(new File("C:/Users/jishubu/Downloads/books.xml"));
        JSONObject httpPostJson = HttpClientUtils.httpPostJson(host + "/chat.do", biz_paramLogin);
        if (!httpPostJson.getJSONObject("result").getString("code").equals("1c01")) {
            System.out.println(httpPostJson.toString());
        }
        return httpPostJson;
    }

    public static void main(String[] args) throws Exception {

        demoTest();

    }
}
