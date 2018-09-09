package test;

import org.junit.Test;

/**
 * @Author Oliver.qin
 * @Date 2018/8/14 10:14
 * @Description:
 */

public class MethodTest {

    @Test
    public void t() {
        String goodsIds = "[\"5b568bd3d95e911491540ef7\", \"sss\"]";
        Object parse = com.alibaba.fastjson.JSONArray.parse(goodsIds);

    }
}
