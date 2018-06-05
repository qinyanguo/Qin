package test;

import com.ycmm.common.utils.HttpClientUtils;
import net.sf.json.JSONObject;

/**
 *
 */
public class LimitTest {

    private static String host = "http://127.0.0.1:8080/smile/test";

    public static JSONObject demoTest(String module, String method, JSONObject biz_param) throws Exception {

        JSONObject params = new JSONObject();
        params.put("id", "admin");
        params.put("uid", "123456");
        JSONObject postJson = HttpClientUtils.httpPostJson(host + "/limit.do", params);
        return  postJson;
    }

    public static void main(String[] args) throws Exception {

        long time = System.currentTimeMillis();
        System.err.println(time);
        JSONObject biz_param = new JSONObject();
//		biz_param.put("mobile", "admin");
        biz_param.put("status", 1);
        biz_param.put("createTime", 1518316500125l);
        biz_param.put("pn", 1);
        biz_param.put("pSize", 20);
        String module = "equipment";
        String method = "deleteEquipment.do";

        for (int i=0; i< 1; i++) {
            if (i == 4) {
                Thread.sleep(5000);
            }
            JSONObject result = demoTest(module, method, biz_param);
            System.err.println(result);
            System.out.println((System.currentTimeMillis() - time));
        }

    }
}
