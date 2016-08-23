package com.mbl.base64;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * 
 * @author Administrator
 *
 */
public class BackAES {

	/**
	 * 偏移量，可自行修改
	 */
	private static String ivParameter = "6543210987654321";
	/**
	 * 加密算法
	 */
	private static String WAYS = "AES";
	/**
	 * 分组加密的四种模式
	 */
	private static String MODE = "";
	/**
	 * 是否使用密钥
	 */
	private static boolean isPwd = true;
	/**
	 * 分组密码填充方式：填充字符串由一个值为 5 的字节序列组成，每个字节填充该字节序列的长度。
	 */
	private static String ModeCode = "PKCS5Padding";

	/**
	 * 密钥长度
	 */
	private static int pwdLenght = 16;
	private static String val = "3";//用于密钥长度不足时补足用

	/**
	 * 选择分组密码工作模式
	 * 
	 * @param type
	 * @return
	 */
	public static String selectMod(int type) {
		// ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
		/*
		 * 1.ECB电子密码本模：把明文进行分组，然后分别加密，最后串在一起的过程 2.CBC加密块链模式：初始化向量 IV
		 * ，第一组明文与初始化向量进行异或运算后再加密，以后的每组明文都与前一组的密文进行异或运算后再加密。 IV
		 * 不需要保密，它可以明文形式与密文一起传送。 3.CFB密文反馈模式：初始化向量IV
		 * ，加密后与第一个分组明文进行异或运算产生第一组密文，然后对第一组密文加密后再与第二组明文进行异或运算缠身第二组密文，一次类推，直到加密完毕
		 * 4.OFB输出反馈模式：初始化向量IV
		 * ，加密后得到第一次加密数据，此加密数据与第一个分组明文进行异或运算产生第一组密文，然后对第一次加密数据进行第二次加密，得到第二次加密数据，
		 * 第二次加密数据再与第二组明文进行异或运算产生第二组密文，一次类推，直到加密完毕。
		 */
		switch (type) {
		case 0:
			isPwd = false;
			MODE = WAYS + "/" + AESType.ECB.key() + "/" + ModeCode;
			break;
		case 1:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CBC.key() + "/" + ModeCode;
			break;
		case 2:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CFB.key() + "/" + ModeCode;
			break;
		case 3:
			isPwd = true;
			MODE = WAYS + "/" + AESType.OFB.key() + "/" + ModeCode;
			break;

		}

		return MODE;

	}

	/**
	 * 加密方法
	 * 
	 * @param 加密内容
	 * @param 密钥
	 * @param 加密模式
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String sSrc, String sKey, int type) throws Exception {
		sKey = toMakekey(sKey, pwdLenght, val);
		Cipher cipher = Cipher.getInstance(selectMod(type));
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);

		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		if (isPwd == false) {// ECB 不用密码
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		}

		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return Base64.encode(encrypted);// 此处使用BASE64做转码。
	}

	/**
	 * 解密
	 * @param 加密后的内容
	 * @param 密钥
	 * @param 加密模式
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String sSrc, String sKey, int type) throws Exception {
		sKey = toMakekey(sKey, pwdLenght, val);
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);
			Cipher cipher = Cipher.getInstance(selectMod(type));
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			if (isPwd == false) {// ECB 不用密码
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			}
			byte[] encrypted1 = Base64.decode(sSrc.getBytes());// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据密码，生成密钥
	 * 
	 * @param 密码
	 * @param 密钥长度
	 * @param 长度不足用0补齐
	 * @return
	 */
	public static String toMakekey(String str, int strLength, String val) {

		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(str).append(val);
				str = buffer.toString();
				strLen = str.length();
			}
		}
		return str;
	}


	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * java将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
