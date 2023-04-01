package club.doctorxiong.api.uitls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * jwt 工具类
 *
 * @author zhongshenghua
 * @date 2021/5/28
 */
@Slf4j
public class JwtUtil {
    /**
     * 普通密钥
     */
    private static final String TOKEN_SECRET = "W3DNZAeon1BQB9DetkisoOoZYRuBsIEu+HZ/A/t3qgY=";

    /**
     * 功能：构建token
     *
     * @param jwtId   Id
     * @param account 账号
     * @param userId  用户id
     * @return jwt
     */
    public static String createJwt(String jwtId, String account, int userId) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET));
        return createJwt(jwtId, account, userId, key);
    }

    /**
     * 功能：构建token
     *
     * @param jwtId   Id
     * @param account 账号
     * @param userId  用户id
     * @return jwt
     */
    public static String createJwt(String jwtId, String account, int userId, Key key) {
        return Jwts.builder()
                .setId(jwtId)
                .setSubject(account)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key)
                .compact();
    }

    /**
     * 功能：从token 中获取 Claims 内容
     *
     * @param jwt token
     * @return Claims
     */
    public static Claims parseJwt(String jwt) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET));
        return parseJwt(jwt, key);
    }

    /**
     * 功能：从token 中获取 Claims 内容
     *
     * @param jwt token
     * @return Claims
     */
    public static Claims parseJwt(String jwt, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 解析PKCS#8格式私钥字符串
     *
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) {
        byte[] privateKeyByte = Base64.getMimeDecoder().decode(privateKeyStr);
        try {
            KeyFactory factory = KeyFactory.getInstance(SignatureAlgorithm.RS256.getFamilyName());
            return factory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
        } catch (Exception e) {
            log.error("私钥解密失败", e);
            throw new RuntimeException("密钥不合法，请检查");
        }
    }

    /**
     * 解析PKCS#8格式公钥字符串
     *
     * @return 私钥对象
     */
    public static PublicKey loadPublicKey(String publicKeyStr) {
        byte[] publicKeyStrByte = Base64.getMimeDecoder().decode(publicKeyStr);
        try {
            KeyFactory factory = KeyFactory.getInstance(SignatureAlgorithm.RS256.getFamilyName());
            return factory.generatePublic(new X509EncodedKeySpec(publicKeyStrByte));
        } catch (Exception e) {
            log.error("公钥解密失败", e);
            throw new RuntimeException("密钥不合法，请检查");
        }
    }

    /**
     * 功能：从token 中获取用户id
     *
     * @param jwt token
     * @return userId
     */
    public static Integer getUserId(String jwt) {
        Claims claims = parseJwt(jwt);
        return claims.get("userId", Integer.class);
    }

    /**
     * 解密token并封装JwtUser对象返回
     * @param jwt token
     * @param publicKeyStr PKCS#8格式公钥字符串
     * @return JwtUser
     */
    public static JwtUser getJwtUser(String jwt, String publicKeyStr){
        Claims claims;
        try {
            PublicKey publicKey = loadPublicKey(publicKeyStr);
            claims = parseJwt(jwt, publicKey);
        } catch (Exception e){
            log.info("token不合法，解密失败");
            return null;
        }
        return JwtUser.builder()
                .userId(claims.get("userId", Integer.class))
                .jwtId(claims.getId())
                .account(claims.getSubject())
                .build();
    }

    @Data
    @Builder
    public static class JwtUser{
        /**
         * jwt id
         */
        private String jwtId;
        /**
         * 用户id
         */
        private Integer userId;
        /**
         * 账号
         */
        private String account;
    }


    public static void main(String[] args) {
        // 测试生成公钥、私钥
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        String generatePrivateKeyStr = Base64.getMimeEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String generatePublicKeyStr = Base64.getMimeEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println(generatePrivateKeyStr);
        System.out.println();
        System.out.println(generatePublicKeyStr);

        // 测试普通密钥加密的JWT
        String tokenId = UUID.randomUUID().toString().replace("-", "");
        String generalJwt = createJwt(tokenId, "15013051024", 1);
        System.out.println("普通密钥加密的JWT：" + generalJwt);
        Claims generalClaims = parseJwt(generalJwt);
        System.out.println("普通密钥加密的JWT解密：" + generalClaims);

        // 测试非对称加密的JWT
        PrivateKey privateKey = loadPrivateKey(generatePrivateKeyStr);
        PublicKey publicKey = loadPublicKey(generatePublicKeyStr);
        String asymmetricJwt = createJwt(tokenId, "15013051024", 1, privateKey);
        System.out.println("非对称加密的JWT：" + asymmetricJwt);
        Claims asymmetricClaims = parseJwt(asymmetricJwt, publicKey);
        System.out.println("非对称加密的JWT解密：" + asymmetricClaims);
    }
}
