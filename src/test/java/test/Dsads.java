package test;

import java.security.AlgorithmParameters;
import java.security.NoSuchProviderException; 
import java.security.Security; 
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ycmm.common.utils.Base64;
import net.sf.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;



public class Dsads {

	public static void main(String[] args) throws Exception {
		   String appId = "wx11948e15ebecf897";
//		   String appId = "wx4f4bc4dec97d474b";
		   String sessionKey = "mIQ4aaSEUY2Rr/pKOOR4Cw==";
//	       String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
//	       String encryptedData =
//	                "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZMQmRzooG2xrDcvSnxIMXFufNstNGTyaGS9uT" +
//	                        "5geRa0W4oTOb1WT7fJlAC+oNPdbB+3hVbJSRgv+4lGOETKUQz6OYStslQ142dNCuabNPGBzloo" +
//	                        "OmB231qMM85d2/fV6ChevvXvQP8Hkue1poOFtnEtpyxVLW1zAo6/1Xx1COxFvrc2d7UL/lmHI" +
//	                        "nNlxuacJXwu0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn/Hz7saL8xz+W//FRAUid1OksQaQx" +
//	                        "4CMs8LOddcQhULW4ucetDf96JcR3g0gfRK4PC7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB6Y5a" +
//	                        "naHqZ9J6nKEBvB4DnNLIVWSgARns/8wR2SiRS7MNACwTyrGvt9ts8p12PKFdlqYTopNHR1Vf" +
//	                        "7XjfhQlVsAJdNiKdYmYVoKlaRv85IfVunYzO0IKXsyl7JCUjCpoG20f0a04COwfneQAGGwd5o" +
//	                        "a+T8yO5hzuyDb/XcxxmK01EpqOyuxINew==";
        String encryptedData ="qek89AG9rg7CoV0ma" +
                "/wnWoadKsoIb8wM6IFtgHF4nNBkBrTp1t9fAstliRSH2Cp8HYYGfok5LtZzBy" +
                "WHAQZjFvz3ybPFPSyrbAo2BBcRpIOfAdPvxxOvAAx/eXBf9uXqYfRaj+D8UZOD6ZC7qTQo" +
                "cM4vSELLJyaS1KcKeMlg0GCDFmkx6jrhijPFSM0KUEgEGqibWEYicvqAgFjcx+xxL2h8H9s8q9TE" +
                "LUXgGZvS/wA7UxIjF73F/skDSqVE8Dq0RQUb9KWP+xA+WJlgd9iewOokuKX0wKm8IAY8wHcuHNE8Xf0" +
                "nxomYTldc7WeXpJ9ygq4cnsuqiLIriXio4AdGOh+bKfGB5Gho6Nlq+LWGeUewt5tlEA2Z0h8e7F2BV9sBn7B" +
                "EUC50MLt66YNwMDJyeQUkumEWSrc6hc7fix+cnZKn5jULqbdx2FzjMV77yuJLzYsJlKh1fn9BKjCR+KKmxw==";
                String iv = "bcY5t3OXaBibYVcOQSIoWA==";
//	        String iv = "r7BXXKkLb8qrSNn05n0qiA==";

//	        String pc = WXBizDataCrypt(appId, sessionKey)

//	        print pc.decrypt(encryptedData, iv)
	        		
	        		
	        		// 被加密的数据 
	        		byte[] dataByte = Base64.decode(encryptedData);
	        // 加密秘钥 
	        byte[] keyByte = Base64.decode(sessionKey); 
	        // 偏移量
	        byte[] ivByte = Base64.decode(iv);
	        try { 
	        	// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要 
	        	int base = 16; 
	        	if (keyByte.length % base != 0) {
	        		int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
	        		byte[] temp = new byte[groups * base]; Arrays.fill(temp, (byte) 0);
	        		System.arraycopy(keyByte, 0, temp, 0, keyByte.length); keyByte = temp; 
	        		}
	        	// 初始化 
	        	Security.addProvider(new BouncyCastleProvider()); 
	        	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC"); 
	        	SecretKeySpec spec = new SecretKeySpec(keyByte, "AES"); 
	        	AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES"); parameters.init(new IvParameterSpec(ivByte)); 
	        	cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
	        	// 初始化 
	        	byte[] resultByte = cipher.doFinal(dataByte); 
	        	if (null != resultByte && resultByte.length > 0) { 
	        		String result = new String(resultByte, "UTF-8");
                    System.err.println(JSONObject.fromObject(result));
//	        		return JSONObject.fromObject(result);
	        		} 
	        	}catch (NoSuchProviderException e){ 
	        		e.printStackTrace(); 
	        }

	}
}
