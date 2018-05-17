package test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jishubu on 2018/5/17.
 */
public class encryptionTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        String str  = "abcd"; //待加密字符串

        //1、MD5加密
        String md5Str = DigestUtils.md5Hex(str);
        System.out.println("MD5-->" + md5Str);
        //SHA1加密
        String sha1Str = DigestUtils.sha1Hex(str);
        System.out.println("SHA1-->" + sha1Str);
        //JDK自带加密算法 SHA1   APP端开发为了省空间，不打算导入apache-commons的jar包，可以使用JDK自带的加密算法
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(str.getBytes());
        String  outStr = byteToString(digest);
        System.err.println("SHA1 JDK自带加密算法-->" + outStr);
        //MD5 + SHA1 加密
        String s = DigestUtils.md5Hex(DigestUtils.sha1Hex(str));
        System.err.println("MD5 + SHA1-->" + s);
        //Base64加密
        String base64Str = Base64.encodeBase64String(str.getBytes());
        System.out.println("base64加密-->" + base64Str);
        //Base64解密
        String base64DecodeStr = new String(Base64.decodeBase64(base64Str));
        System.out.println("base64解密-->" + base64DecodeStr);

    }

    private static String byteToString(byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String tempStr = Integer.toHexString(digest[i] & 0xff);
            if (tempStr.length() == 1) {
                buf.append("0").append(tempStr);
            } else {
                buf.append(tempStr);
            }
        }
        return buf.toString().toLowerCase();
    }
}
