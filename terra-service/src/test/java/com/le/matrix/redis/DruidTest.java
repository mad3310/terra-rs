package com.le.matrix.redis;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 生成druid加密密码
 */
public class DruidTest {
    public static void main(String[] args) throws Exception {
        //生成druid加密密码
        //密码明文
        String password = "snlPiwCu";
        System.out.println("password:" + password);
        String encryptResult = ConfigTools.encrypt(password);
        System.out.println("encrypt password:" + encryptResult);
        String decryptPassword = ConfigTools.decrypt(encryptResult);
        System.out.println("decrypt password:" + decryptPassword);
    }
}
