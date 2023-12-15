package com.movie.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public final class JwtTokenUtil {

    //在http header中的名字
    public final static String TOKEN_HEADER = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";  // 令牌前缀

    // 一周过期
    public final static long REMEMBER_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    // 一天过期
    public final static long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 应用密钥
    private static final String APP_SECRET = "movie.api";

    // 角色权限声明
    private static final String ROLE_CLAIMS = "roles";

    /**
     * 生成Token
     */
    public static String createToken(String username, List<String> roles, long expiration) {
        Map<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, roles);
        String token = Jwts.builder()
                .setSubject(username)
                .setClaims(map)
                .setIssuer("movie.api")  // 设置Issuer
                .setAudience("movie.client")  // 设置Audience
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        // 将 Token 存储到 Redis 中
        try (Jedis jedis = new Jedis("127.0.0.1")) {  // Redis 主机地址
            jedis.set(token, username);  // 以 Token 作为 Key，用户名作为 Value 进行存储
            jedis.expire(token, (int) (expiration / 1000));  // 设置 Token 的过期时间（单位为秒）
        }

        return token;
    }

    /**
     * 从请求中获取Token
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 获取token body
     */
    private static Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(APP_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * 从Token中获取username
     */
    public static String getUsername(String token) {
        return getTokenClaims(token).getSubject();
    }

    /**
     * 从Token中获取用户角色
     */
    public static List<String> getTokenRoles(String token) {
        List<String> roles = new ArrayList<>();
        Object object = getTokenClaims(token).get(ROLE_CLAIMS);
        if (object instanceof ArrayList<?>) {
            for (Object o : (List<?>) object) {
                roles.add((String) o);
            }
        }
        return roles;
    }

    /**
     * 校验Token是否过期
     */
    public static boolean isExpiration(String token) {
        return getTokenClaims(token).getExpiration().before(new Date());
    }
}
