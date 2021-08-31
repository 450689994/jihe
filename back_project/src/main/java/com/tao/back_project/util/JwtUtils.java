package com.tao.back_project.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.Serializable;
import java.util.Date;

public class JwtUtils {


    public static String createToken(String uid) {

        Date expiresDate = new Date(System.currentTimeMillis()+1000*60*30);     //30分钟

        return JWT.create().withAudience(uid)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .sign(Algorithm.HMAC256(uid+"TaoChenSecret"));   //加密
    }

    public static void verifyToken(String token, String uid) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(uid+"TaoChenSecret")).build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new RuntimeException();
        }
        return audience;
    }

    public static void main(String[] args) {
        verifyToken(createToken("2"),"1");
    }
}

