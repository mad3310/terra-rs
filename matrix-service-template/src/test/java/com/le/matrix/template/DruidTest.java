package com.le.matrix.template;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 生成druid加密密码
 */
public class DruidTest {
    public static void main(String[] args) throws Exception {
        //生成druid加密密码
        //密码明文
        String password = "root";
        System.out.println("password:" + password);
        password = ConfigTools.encrypt("root");
        System.out.println("encrypt password:" + password);
        password = ConfigTools.decrypt(password);
        System.out.println("decrypt password:" + password);
    }
}
