package com.ycmm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.util.Scanner;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtils {
	
	
//	public static void main(String[] args) throws Exception{
//		
//		System.out.println(read(2,10));
////		
//		String readLineFile = readLineFile("‪C:\\bean id.docx", 2);
//		System.out.println(readLineFile);
//		
////		String strContent = readTxtFile("E:\\bean id.docx");
////		System.out.println(strContent);
//		
////		String readFile = readFile("E:\\bean id.docx");
////		System.out.println(readFile);
//		
////		uploadFile("<bean>", "E:\\bean id.docx");
//		
//		
////		String htmlContent = getHtmlContent("http://www.zyctd.com/zixun/204-3997.html");
////		System.out.println(htmlContent);
//	}

	public static void main(String[] args) throws Exception {
		readfile("F:/verifies");
		
		 System.out.println("input your file.");
	        Scanner from = new Scanner(System.in);
	         
	        //System.getProperty("user.dir")获取当前目录，也就是当前项目的根目录       
	        FileInputStream in=new FileInputStream(System.getProperty("user.dir")+"\\"+from.next());
	        System.out.println("input your new file name.");
	        Scanner to = new Scanner(System.in);
	        FileOutputStream out = new FileOutputStream(to.next());
	        int a;
	        while ((a = in.read()) != -1) {
	            out.write(a);
	            System.out.println(a);
	        }
	        in.close();
	        out.close();
	        from.close();
	        to.close();
	        System.out.println("Succeed!");
	}

	/**
	 * 读取某个文件夹下的所有文件
	 */
	@SuppressWarnings({ "unused", "resource" })
	public static boolean readfile(String filepath) throws FileNotFoundException, IOException {

		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("文件");
				System.out.println("path=" + file.getPath());
				System.out.println("absolutepath=" + file.getAbsolutePath());
				System.out.println("name=" + file.getName());

			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						System.out.println("path=" + readfile.getPath());
						System.out.println("absolutepath=" + readfile.getAbsolutePath());
						System.out.println("name=" + readfile.getName());

						FileInputStream inputStream = new FileInputStream(readfile.getPath());
						GetImageStr(readfile.getPath());
					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i]);
					}
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return true;
	}

	// 图片转化成base64字符串
	public static String GetImageStr(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// System.out.println(encoder.encode(data));
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	// base64字符串转化成图片
	public static boolean GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = "D:\\new.jpg";// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 向服务器上传Base64加密的string文件
	 * @param fileContent   文件内容
	 * @param fileName      文件名
	 */
	public static void uploadFile(String fileContent, String fileName) {
		
		//看是否有上传的文件夹，没有则新建
		File file = new File(fileName);
		File dir = file.getParentFile();
		
		if((!dir.exists()) || !(dir.isDirectory())) {
			dir.mkdirs();
		}
		
		InputStream in = null;
		OutputStream out = null;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			in = new ByteArrayInputStream(base64Decoder.decodeBuffer(fileContent));
			int read = 0;
			byte[] buffer = new byte[1024];
			out = new FileOutputStream(file);
			while ((read = in.read(buffer, 0, 1024)) != -1) {
				out.write(buffer, 0, read);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 功能：从指定文件中的指定位置开始读取指定长度的内容
	 * @param from
	 * @param to
	 * @return
	 */
	 @SuppressWarnings({ "resource", "unused" })
	public static String read(int from ,int to){

		String result="";
	        byte[] result2=new byte[to-from+1];
	        try{
	            FileInputStream fis=new FileInputStream("‪‪F:\\上海总部 规章制度_v1.0.doc");
				BufferedInputStream bis=new BufferedInputStream(fis);
	            bis.skip(from-1);
	            bis.read(result2, 0, to-from+1);
	        }catch(FileNotFoundException e){
	            e.printStackTrace();
	        }catch(IOException e){
	            e.printStackTrace();
	        }
	        return new String(result2);

	}
	
	/**
	 * 功能：读取文本具体某行内容 
	 * @param filePath
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public static String readLineFile(String filePath, int number) throws Exception{
		long timeStart = System.currentTimeMillis();
		String txt = "";
		File file = new File(filePath);// 文件路径
		FileReader fileReader = new FileReader(file);
		LineNumberReader reader = new LineNumberReader(fileReader);
		try {
			int lines = 0;
			boolean flag = true;
			while (txt != null && flag) {
				lines++;
				txt = reader.readLine();
				if (lines == number) {
					System.out.println("第" + reader.getLineNumber() + "行的内容是：" + txt + "\n");
					long timeEnd = System.currentTimeMillis();
					System.out.println("总共花费：" + (timeEnd - timeStart) + "ms");
					flag = false;
//					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return txt;
	}
	
	 /**
     * 功能：Java读取某文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
	public static String readTxtFile(String filePath) {
		StringBuffer strContent = new StringBuffer();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					strContent.append(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		return strContent.toString();
	}
}
