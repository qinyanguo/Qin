package com.ycmm.common.utils.sms;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

/**
 * 
 * <pre>
 * <h1>Debugging</h1>
 * The JavaMail API supports a debugging option that will can be very
 * useful if you run into problems. You can activate debugging on any of the
 * mail classes by calling setDebug(true). The debugging output will be written
 * to System.out.
 * 
 * Sometimes you want to experiment with various security setting or features of
 * commons-email. A good starting point is the test class EmailLiveTest and
 * EmailConfiguration which are used for testing commons-email with real SMTP
 * servers.
 * 
 * </pre>
 * 
 * @describe Apache Commons Email 使用示例
 * @author liwen
 * 
 */
public class SendEmailDemo {


    /**
     * 发送者邮箱
     */
    private static final String FROM = "notification@yaocaimaimai.com";
    /**
     * 发送者邮箱密码
     */
    private static final String SECRET = "Mianguan0707";

    /**
     * 接收者
     */
    private static final String receiver = "2155483971@qq.com";

    /**
	 * @describe 发送内容为简单文本的邮件
	 * @throws EmailException
	 */
	public static void sendSimpleTextEmail() throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		// 用户名和密码为邮箱的账号和密码（不需要进行base64编码）
		email.setAuthenticator(new DefaultAuthenticator("username", "password"));
		email.setSSLOnConnect(true);
		email.setFrom("user@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("foo@bar.com");
		email.send();
	}

	/**
	 * <pre>
	 * 
	 * To add attachments to an email, you will need to use the MultiPartEmail
	 * class. This class works just like SimpleEmail except that it adds several
	 * overloaded attach() methods to add attachments to the email. You can add
	 * an unlimited number of attachments either inline or attached. The
	 * attachments will be MIME encoded.
	 * 
	 * The simplest way to add the attachments is by using the EmailAttachment
	 * class to reference your attachments.
	 * 
	 * In the following example, we will create an attachment for a picture. We
	 * will then attach the picture to the email and send it.
	 * 
	 * </pre>
	 * 
	 * @describe 发送包含附件的邮件（附件为本地资源）
	 * @throws EmailException
	 */
	public static void sendEmailsWithAttachments() throws EmailException {
		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath("mypictures/john.jpg");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Picture of John");
		attachment.setName("John");

		
		
		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
//		email.setHostName("mail.myserver.com");
//		email.addTo("jdoe@somewhere.org", "John Doe");
//		email.setFrom("me@apache.org", "Me");
//		email.setSubject("The picture");
//		email.setMsg("Here is the picture you wanted");
		
		email.setHostName("smtp.163.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("qinyg24@163.com", "qin6719568"));
		email.setSSLOnConnect(true);
		email.setFrom("qinyg24@163.com", "秦");
		email.setSubject("好消息");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("2155483971@qq.com");

		// add the attachment
		email.attach(attachment);

		// send the email
		email.send();
	}

	/**
	 * <pre>
	 * 
	 * You can also use EmailAttachment to reference any valid URL for files
	 * that you do not have locally. When the message is sent, the file will be
	 * downloaded and attached to the message automatically.
	 * 
	 * The next example shows how we could have sent the apache logo to John
	 * instead.
	 * 
	 * </pre>
	 * 
	 * @describe 发送包含附件的邮件（附件为在线资源）
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	public static void sendEmailsWithOnlineAttachments() throws EmailException, MalformedURLException {
		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setURL(new URL("http://test.image.yaocaimaimai.com/031001600411_59682341_15221177026321045.pdf"));
//		attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Apache logo");
		attachment.setName("Apache.pdf");

//		FileUtils.copyURLToFile(source, destination, connectionTimeout, readTimeout);
		
		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
//		email.setHostName("mail.myserver.com");
//		email.addTo("jdoe@somewhere.org", "John Doe");
//		email.setFrom("me@apache.org", "Me");
//		email.setSubject("The logo");
//		email.setMsg("Here is Apache's logo");
		
		email.setHostName("smtp.163.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("qinyg24@163.com", "xxxxxx"));
		email.setSSLOnConnect(true);
		email.setFrom("qinyg24@163.com", "秦");
		email.setSubject("好消息");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("2155483971@qq.com");

		// add the attachment
		email.attach(attachment);

		// send the email
		email.send();
	}

	/**
	 * <pre>
	 * 
	 * Sending HTML formatted email is accomplished by using the HtmlEmail
	 * class. This class works exactly like the MultiPartEmail class with
	 * additional methods to set the html content, alternative text content if
	 * the recipient does not support HTML email, and add inline images.
	 * 
	 * In this example, we will send an email message with formatted HTML
	 * content with an inline image.
	 * 
	 * First, notice that the call to embed() returns a String. This String is a
	 * randomly generated identifier that must be used to reference the image in
	 * the image tag.
	 * 
	 * Next, there was no call to setMsg() in this example. The method is still
	 * available in HtmlEmail but it should not be used if you will be using
	 * inline images. Instead, the setHtmlMsg() and setTextMsg() methods were
	 * used.
	 * 
	 * <pre>
	 * 
	 * @describe 发送内容为HTML格式的邮件
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	public static void sendHTMLFormattedEmail() throws EmailException, MalformedURLException {
		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName("mail.myserver.com");
		email.addTo("jdoe@somewhere.org", "John Doe");
		email.setFrom("me@apache.org", "Me");
		email.setSubject("Test email with inline image");

		// embed the image and get the content id
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid = email.embed(url, "Apache logo");

		// set the html message
		email.setHtmlMsg("<html>The apache logo - <img src=\"cid:" + cid + "\"></html>");

		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
	}

	/**
	 * <pre>
	 * 
	 * The previous example showed how to create a HTML email with embedded
	 * images but you need to know all images upfront which is inconvenient when
	 * using a HTML email template. The ImageHtmlEmail helps you solving this
	 * problem by converting all external images to inline images.
	 * 
	 * First we create a HTML email template referencing some images. All
	 * referenced images are automatically transformed to inline images by the
	 * specified DataSourceResolver.
	 * 
	 * </pre>
	 * 
	 * @describe 发送内容为HTML格式的邮件（嵌入图片更方便）
	 * @throws MalformedURLException
	 * @throws EmailException
	 */
	public static void sendHTMLFormattedEmailWithEmbeddedImages() throws MalformedURLException, EmailException {
		// load your HTML email template
		String htmlEmailTemplate = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";

		// define you base URL to resolve relative resource locations
		URL url = new URL("http://www.apache.org");

		// create the email message
		ImageHtmlEmail email = new ImageHtmlEmail();
		email.setDataSourceResolver(new DataSourceUrlResolver(url));
		email.setHostName("mail.myserver.com");
		email.addTo("jdoe@somewhere.org", "John Doe");
		email.setFrom("me@apache.org", "Me");
		email.setSubject("Test email with inline image");

		// set the html message
		email.setHtmlMsg(htmlEmailTemplate);

		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
	}

	public static void main(String[] args) throws Exception {
//		Email email = new SimpleEmail();
//		email.setHostName("smtp.163.com");
//		email.setSmtpPort(465);
//		email.setAuthenticator(new DefaultAuthenticator("qinyg24@163.com", "密码"));
//		email.setSSLOnConnect(true);
//		email.setFrom("qinyg24@163.com", "秦");
//		email.setSubject("好消息");
//		email.setMsg("This is a test mail ... :-)");
//		email.addTo("2155483971@qq.com");
//		email.send();
		sendEmailsWithOnlineAttachments();
		
//		downloadHttpUrl("http://www.apache.org/images/asf_logo_wide.gif", "D:/poi", "pppp");
//		downloadHttpUrl("http://test.image.yaocaimaimai.com/%E8%8D%AF%E6%9D%90%E5%93%81%E7%A7%8D%E8%A7%84%E6%A0%BC%E5%BD%95%E5%85%A5%20.xlsx", "D:/poi",
//                "报价比价表.xlsx");
//		downloadHttpUrl("http://test.image.yaocaimaimai.com/031001600411_59682341_15221177026321045.pdf", "D:/poi",
//				"pppp.xlsx");
		
	}
	
	 /**
	 * 下载文件---返回下载后的文件存储路径
	 * 
	 * @param url 文件地址
	 * @param dir 存储目录
	 * @param fileName 存储文件名
	 * @return
	 */
	public static void downloadHttpUrl(String url, String dir, String fileName) {
		try {
			URL httpurl = new URL(url);
			File dirfile = new File(dir);  
	        if (!dirfile.exists()) {  
	        	dirfile.mkdirs();
	        }
			FileUtils.copyURLToFile(httpurl, new File(dir+fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
