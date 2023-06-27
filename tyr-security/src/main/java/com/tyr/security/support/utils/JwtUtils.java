package com.tyr.security.support.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tyr.security.support.core.SysUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtUtils {

    private static final String jwtSecret = "tyr-@123456";

    /**
     * jwt的token超时时间，单位分钟
     */
    private final Long jwtTokenTimeout = 120L;

    public static String createToken(String username, String password) {
        return JWT.create()
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(4).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("username", username)
                .withClaim("password", password)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    /**
     * 秘钥8
     * @param username 用户名
     * @return
     */
    public static String createToken(String username, String uuid,Integer type,String tenantCode) {
        String token = JWT.create()
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(4).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("username", username)
                .withClaim("uuid", uuid)
                .withClaim("type", type)
                .withClaim("tenant", tenantCode)
                .sign(Algorithm.HMAC256(jwtSecret));
        return token;
    }

    /**
     * 根据用户名、角色、权限、菜单等信息生成token
     * @param username 用户名
     * @param roles 角色
     * @param authorities 权限
     * @param menus 菜单
     * @return
     */
    public static String createToken(String username, List<String> roles, List<String> authorities, List<String> menus) {

        JWTCreator.Builder builder = JWT.create();
        //角色
        if (!CollectionUtils.isEmpty(roles)) {
            builder.withClaim("role", roles);
        }

        //权限
        if (!CollectionUtils.isEmpty(authorities)) {
            builder.withClaim("authorities", authorities);
        }

        //菜单
        if (!CollectionUtils.isEmpty(authorities)) {
            builder.withClaim("menus", menus);
        }

        String token = builder.withClaim("username", username)
                .sign(Algorithm.HMAC256(jwtSecret));

        return token;
    }

    /**
     * 获取jwt的负载
     * @param token
     * @return
     */
    public static Map<String, Claim> getClaim(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaims();
    }

    // 判断JWT是否过期
    public static boolean tokenExpired(String token) {
        Claim claim = getClaim(token).get("exp");
        if (claim.isNull()) {
            return true;
        }
        return claim.asDate().before(new Date());
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        Claim claim = getClaim(token).get("username");
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }


    /**
     * 获取租户
     * @param token
     * @return
     */
    public static String getTenant(String token) {
        Claim claim = getClaim(token).get("tenant");
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }

    /**
     * 获取用户类型
     * @param token
     * @return
     */
    public static Integer getUserType(String token) {
        Claim claim = getClaim(token).get("type");
        if(claim == null) {
            return null;
        }
        return claim.asInt();
    }

    public String getPassword(String token) {
        Claim claim = getClaim(token).get("password");
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }

    /**
     * 获取客户端唯一标识
     * @param token
     * @return
     */
    public static String getUUID(String token) {
        Claim claim = getClaim(token).get("uuid");
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        SysUser user = (SysUser) userDetails;
        String username = getUsername(token);
        return (username.equals(user.getUsername()) && !tokenExpired(token));
    }
}