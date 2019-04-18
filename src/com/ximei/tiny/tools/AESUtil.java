package com.ximei.tiny.tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AESUtil {
	
	public static final String CHARSET = "UTF-8";
    private static byte[] encryptOrDecrypt(int mode,byte[] byteContent, String key,byte[] iv, AESType type, String modeAndPadding) throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //此处解决mac，linux报错
       /* SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        kgen.init(type.value, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();*/
        
        
        SecretKeySpec keySpec = new SecretKeySpec(TypeConvert.hexStringToBytes(key), "AES");
        Cipher cipher = Cipher.getInstance(modeAndPadding);// 创建密码器
       
        
       /* SecretKeySpec skeySpec = new SecretKeySpec(TypeConvert.hexStringToBytes(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(byteContent);*/
        
        
        
        if ( null !=iv ) {
            //指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位
            cipher.init(mode, keySpec, new IvParameterSpec(iv));
        } else {
            cipher.init(mode, keySpec);
        }
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        //        /**
        //         * 1.2 AES/ECB
    	//         * AES/ECB/NoPadding
        //         * AES/ECB/PKCS5Padding
        //         * AES/ECB/ISO10126Padding
        //         */
                System.out.println("【1.3】AES_ECB模式");
                String content = "00112233445566778899aabb80000000";
                String key = "01020304050607080000000000000000";
                
                System.out.println("原文："+content);
                System.out.println("密文："+encrypt(content,key));
                System.out.println("解密后原文："+decrypt(encrypt(content,key),key));
            }
    /**
    *
    * @param isEncrypt 加密true  解密false  
    * @param source  数据
    * @param key	密钥
    * @param type	
    * @param encodeType 补码方式
    */
   public static String encryptOrdecrypt(boolean isEncrypt,byte[] source,String key,byte[] iv,AESType type,String encodeType) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
       if (isEncrypt){
           byte[] encodeByte = encryptOrDecrypt(Cipher.ENCRYPT_MODE,source,key,iv,type,encodeType);
           String encodeStr = TypeConvert.bytesToHexString(encodeByte);
           return encodeStr;
       }else{
           byte[] decodeByte = encryptOrDecrypt(Cipher.DECRYPT_MODE,source, key,iv,type, encodeType);
           String decodeStr = TypeConvert.bytesToHexString(decodeByte);
           return decodeStr;
       }
   }
   
   /**
    * 加密
    * content=明文
    * key=密钥
    * return=密文
    */
   public static String encrypt(String content,String key) throws Exception {
	   String encrypt  = encryptOrdecrypt(true,TypeConvert.hexStringToBytes(content),key,null,AESType.AES_128,EncodeType.AES_ECB_NoPadding);
       return encrypt;
   }
   /**
    * 解密
    * content=密文
    * key=密钥
    * return=明文
    */
   public static String decrypt(String content,String key) throws Exception {
	   String decrypt = encryptOrdecrypt(false,TypeConvert.hexStringToBytes(content),key,null,AESType.AES_128,EncodeType.AES_ECB_NoPadding);
       return decrypt;
   }
}
