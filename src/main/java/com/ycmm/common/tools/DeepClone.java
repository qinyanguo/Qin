package com.ycmm.common.tools;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Author Oliver.qin
 * @Date 2018/8/1 10:48
 * @Description:
 */

public class DeepClone {

    /**
     * 对象深克隆
     * @param  obj需要克隆的对象
     * @return 克隆完成的对象
     * @author Oliver.qin
     * @date 2018/8/1 10:53
     */
    public static Object deepCopy(Object obj) {
        try {
            //创建一个新的 byte 数组输出流。  此类实现了一个输出流，其中的数据被写入一个 byte 数组。缓冲区会随着数据的不断写入而自动增长。
            // 可f使用 toByteArray() 和 toString() 获取数据。关闭 ByteArrayOutputStream 无效。此类中的方法在关闭此流后仍可被调用，
            // 而不会产生任何 IOException。
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            //将指定的对象写入 ObjectOutputStream
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(obj);
            // 包含一个内部缓冲区，该缓冲区包含从流中读取的字节。创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组。
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream objIn = new ObjectInputStream(byteIn);
            return objIn.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 类型转换，将普通对像解析为其他已知类型实例，字段相同匹配
     * @param  o普通对象，已知对象类型clazz
     * @return
     * @author Oliver.qin
     * @date 2018/8/1 11:06
     */
    public static <T> T convert(Object o, Class<T> clazz) {
        String obj = JSON.toJSONString(o);
        return JSON.parseObject(obj, clazz);
    }
}
