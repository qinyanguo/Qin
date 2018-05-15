package com.ycmm;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	
	public static ClassPathXmlApplicationContext context;


	public static void main(String[] args) throws InterruptedException {
		context = new ClassPathXmlApplicationContext(new String[]{"/applicationContext.xml"});

//        Object stockMapper = context.getBean("");
//        System.err.println(stockMapper);
//        context.start();
//		while(true) {
//			Thread.sleep(100000);
//		}
	}
}
