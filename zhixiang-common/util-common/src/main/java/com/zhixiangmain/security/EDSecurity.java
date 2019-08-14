package com.zhixiangmain.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.security
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-22 16:46
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class EDSecurity {

    /**
     *  EDS的加密解密代码
     */
    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };

    /**   
     * @author hhp
     * @Description: 加密
     * @date:  2018-11-21 16:51
     * @param: @param data 需要加密的字符串
     * @return: encryptedData 加完密之后的字符串
     */
    @SuppressWarnings("restriction")
    public static String encryptBasedDes(String data) {

        String encryptedData = null;

        try {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            DESKeySpec deskey = new DESKeySpec(DES_KEY);

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);

            // 加密，并把字节数组编码成字符串
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            // log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**   
     * @author hhp
     * @Description: 解密
     * @date: 2018-11-21 16:51
     * @param: cryptData 需要解密字符串
     * @return: decryptedData 解密完的字符串
     */
    @SuppressWarnings("restriction")
    public static String decryptBasedDes(String cryptData) {

        String decryptedData = null;

        try {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);

            // 把字符串进行解码，解码为为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));

        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

}
