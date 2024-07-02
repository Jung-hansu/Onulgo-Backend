package com.ssafy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Component
public class EncryptUtil {

    @Value("${encrypt.algorithm}")
    private String algorithm;

    public String getEncrypt(String source, String salt){
        return getEncrypt(source, salt.getBytes());
    }
    public String getEncrypt(String source, byte[] salt){

        String result = "";

        byte[] a = source.getBytes();
        byte[] bytes = new byte[a.length + salt.length];

        System.arraycopy(a,0,bytes, 0,a.length);
        System.arraycopy(salt,0,bytes, a.length,salt.length);

        log.debug("algorithm {}", algorithm );
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(bytes);

            byte[] byteData = messageDigest.digest();

            StringBuffer sb = new StringBuffer();
            for(int i=0;i<byteData.length;i++){
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    //TODO: 회원 가입 시 SALT 값 저장
    public String generateSalt(){
        Random random = new Random();

        byte[] salt = new byte[8];
        random.nextBytes(salt);

        StringBuffer sb = new StringBuffer();
        for(int i=0;i<salt.length;i++){
            sb.append(String.format("%02x",salt[i]));
        }

        return sb.toString();
    }
}
