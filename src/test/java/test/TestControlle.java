package test;

import com.ycmm.base.spring.ContextHolderUtils;
import com.ycmm.common.utils.HttpClientUtils;
import net.sf.json.JSONObject;

/**
 * Created by jishubu on 2018/4/25.
 */
public class TestControlle {

    private static String host = "http://127.0.0.1:8080/smile/yan";

    public static JSONObject demoTest(String module, String method, JSONObject biz_param) throws Exception {
        ContextHolderUtils.getSession();
        long time = System.currentTimeMillis();
        JSONObject biz_paramLogin = new JSONObject();
        biz_paramLogin.put("id", "admin");
        biz_paramLogin.put("uid", "123456");
        JSONObject httpPostJson = HttpClientUtils.httpPostJson(host + "/exportExcel222.do", biz_paramLogin);
        if (!httpPostJson.getJSONObject("result").getString("code").equals("1c01")) {
            System.out.println(httpPostJson.toString());
        }
//        String sign = SecretUtils
//                .HmacSHA1Encrypt("module=" + module + "&method=" + method + "&time=" + time, httpPostJson.optString("KEY"));
//        String sid = httpPostJson.getString("SID");
//        JSONObject result = HttpClientUtils.httpPostJson(host + "/" + module + "/" + method, biz_param);
        return httpPostJson;
    }

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        System.err.println(time);
        JSONObject biz_param = new JSONObject();
//		biz_param.put("mobile", "admin");
//		biz_param.put("code", "1234");

        biz_param.put("name", "驾驶");
        biz_param.put("store", "驾驶");
        biz_param.put("supplierId", 1);
        biz_param.put("id", 3);
        biz_param.put("operatorId", -1);
        biz_param.put("operatorName", "");
        biz_param.put("status", 1);
        biz_param.put("createTime", 1518316500125l);
        biz_param.put("pn", 1);
        biz_param.put("pSize", 20);
        String module = "equipment";
        String method = "deleteEquipment.do";
        JSONObject result = demoTest(module, method, biz_param);
        System.err.println(result);
        System.out.println((System.currentTimeMillis() - time));
    }

}
