/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2012
 * 文件：com.topdot.framework.message.security.SecurityUtils.java
 * 所含类: SecurityUtils.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月21日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>类名</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2012</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName SecurityUtils
 * @author zhangyongxin
 * @version 1.0
 */

public class SecurityCipher {
	private Cipher c1;
	private SecretKey deskey;

	private SecurityCipher() {

	}

	@SuppressWarnings("restriction")
	public static SecurityCipher getInstace(String keyBytesStr, String algorithm) {
		SecurityCipher securityCipher = new SecurityCipher();
		// 添加新安全算法,如果用JCE就要把它添加进去
		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		final byte[] keyBytes = new byte[keyBytesStr.length() / 2];
		for (int i = 0; i < keyBytesStr.length(); i = i + 2) {
			Integer b = Integer.parseInt(keyBytesStr.substring(i, i + 2), 16);
			keyBytes[i / 2] = b.byteValue();
		}
		try {
			securityCipher.deskey = new SecretKeySpec(keyBytes, algorithm);
			securityCipher.c1 = Cipher.getInstance(algorithm);
			securityCipher.c1.init(Cipher.ENCRYPT_MODE, securityCipher.deskey);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return securityCipher;
	}

	public String encrypt(String src) {
		byte[] bytes = null;
		try {
			byte[] srcB = new String(src.getBytes(), "ISO-8859-1").getBytes("ISO-8859-1");
			bytes = encrypt(srcB);
			return new String(bytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public String decrypt(String src) {
		byte[] bs = null;
		try {
			bs = src.getBytes("ISO-8859-1");
			byte[] bytes = decrypt(bs);
			return new String(bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] encrypt(byte[] src) {
		return execute(Cipher.ENCRYPT_MODE, src);
	}

	public byte[] decrypt(byte[] src) {
		return execute(Cipher.DECRYPT_MODE, src);
	}

	private byte[] execute(int mode, byte[] src) {
		try {
			c1.init(mode, deskey);
			return c1.doFinal(src);
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String aa = "31224E688810403828257951CBDD556677297498304036E2";
		SecurityCipher su = SecurityCipher.getInstace(aa, "TripleDES");

		String szSrc = "This is a 3DES test. 测试-Good 中国";

		System.out.println("加密前的字符串:" + szSrc + System.currentTimeMillis());
		String encoded = su.encrypt(szSrc);
		System.out.println("加密后的字符串:" + encoded + System.currentTimeMillis());
		String src = su.decrypt(encoded);
		System.out.println("解密后的字符串:" + src + System.currentTimeMillis());

		String t = "解密后的字符串:HHA";
		try {
			byte[] bs = new String(t.getBytes(), "ISO-8859-1").getBytes("ISO-8859-1");
			System.out.println(">>>>:" + bs.length);
			System.out.println(">>>>:" + new String(bs, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
