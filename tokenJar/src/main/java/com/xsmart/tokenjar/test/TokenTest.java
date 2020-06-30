package com.xsmart.tokenjar.test;

import com.xsmart.tokenjar.utils.EncryptUtils2;

/*
 * Created by Administrator on 2020/6/30.
 */
public class TokenTest {

    public static void main(String[] args) {
        String refreshToken = EncryptUtils2.getRefreshToken("123456789");
        System.out.println("token = " + refreshToken);
    }

}
