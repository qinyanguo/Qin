package com.ycmm.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 *
 */
public class Transcoder {
	/**
	 * 在解压图片的时候发现ByteArrayOutputStream不需要关闭，为啥呢？
	 * ByteArrayOutputStream或ByteArrayInputStream是内存读写流，不同于指向硬盘的流，
	 * 它内部是使用字节数组读内存的，这个字节数组是它的成员变量，当这个数组不再使用变成垃圾的时候，Java的垃圾回收机制会将它回收。所以不需要关流。
	 */

	/**
	 * 序列化
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] serialize(Object value) {
		if(value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			/**
			 * ObjectOutputStream代表对象输出流， 它的writeObject(Object obj)方法可对参数指定的obj对象进行序列化把得到的字节序列
			 * 写到一个目标（现在是ByteArrayOutputStream）输出流中
			 * ObjectInputStream代表对象输出流，它的readObject()方法从一个源输入流中读取字节序列，再把它们反序列化为一个对象，并将其返回
			 * 
			 * ByteArrayOutputStream类实现了一个输出流，其中的数据被写入一个 byte 数组。缓冲区会随着数据的不断写入而自动增长。
			 * 可使用 toByteArray() 和 toString() 获取数据，它的大小是该输出流中已存入的有效数据的当前大小
			 * 
			 *  只有实现了Serializable和Externalizable接口的类的对象才能被序列化。Externalizable接口继承自 Serializable接口，
			 *  实现Externalizable接口的类完全由自身来控制序列化的行为，而仅实现Serializable接口的类可以 采用默认的序列化方式 。
			 *  对象序列化包括如下步骤：
			 *  1） 创建一个对象输出流，它可以包装一个其他类型的目标输出流，如文件输出流；
			 *  2） 通过对象输出流的writeObject()方法写对象。
			 *  对象反序列化的步骤如下：
			 *  1） 创建一个对象输入流，它可以包装一个其他类型的源输入流，如文件输入流；
			 *  2） 通过对象输入流的readObject()方法读取对象。
			 */
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(value);
			rv = bos.toByteArray();
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rv;
	}
	
	public static Object deserialize(byte[] in) throws Exception {
		
		Object rv = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if(in != null) {
				/**
				 * 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组。该缓冲区数组不是复制得到的。pos 的初始值是 0，count 的初始值是 buf 的长度。
				 * 把XXX转变为字节数组in，并作为流输入的来源
				 */
				bis = new ByteArrayInputStream(in);
				/**
				 * 读取对象的，那么自然输入也应该是一个对象的字节数组
				 */
				is = new ObjectInputStream(bis);
				if(is != null) {
					rv = is.readObject();
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					is.close();
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rv;
	}
	
}
