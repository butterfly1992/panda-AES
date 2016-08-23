package com.mbl.base64.test;

import com.mbl.base64.BackAES;

public class Test {

	public static void main(String[] args) {

		String content = "197;0;10.1";
		String skey = null;
		String appid = "6d83e445a8d24cf6990ef99dd0dd8e3e";
		String imei = "613717300230065";

		try {
			// 加密
			skey = "s_" + appid.substring(0, 8) + imei.substring(imei.length() - 3, imei.length());
			System.out.println("s_key:"+skey);
//			byte[] encryptResultStr = BackAES.encrypt(content, skey, 1);
//			System.out.println("方法-加密后：" + new String(encryptResultStr));
			String decryptString = BackAES.decrypt(new String("Newb28h65vVJvBz1g/6hRQ=="), skey, 1);
			System.out.println("方法-解密后：" + decryptString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
