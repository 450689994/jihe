package com.tao.back_project.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtils {


    public static String createToken(String uid) {

        Date expiresDate = new Date(System.currentTimeMillis() + 1000 * 30);     //30s

        return JWT.create().withAudience(uid)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .sign(Algorithm.HMAC256(uid + "TaoChenSecret"));   //加密
    }

    public static boolean verifyToken(String token, String uid) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(uid + "TaoChenSecret")).build();
        try {
            verifier.verify(token);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static String getAudience(String token) {
        return JWT.decode(token).getAudience().get(0);
    }

    public static boolean isEffective(String token){
        Date date = JWT.decode(token).getExpiresAt();
        //有效期比先在时间小
        if (date.compareTo(new Date()) < 0){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        verifyToken(createToken("2"), "1");
    }
}

