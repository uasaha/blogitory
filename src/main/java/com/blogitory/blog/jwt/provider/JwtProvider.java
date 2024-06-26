package com.blogitory.blog.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * Provider of JWT.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
public class JwtProvider {

  /**
   * Create new JWT.
   *
   * @param secret secret
   * @param value  subject
   * @param expire expire time
   * @return new JWT
   */
  public String createToken(String secret, String value, Long expire) {
    Claims claims = Jwts.claims();
    claims.setSubject(value);

    return createToken(secret, claims, expire);
  }

  /**
   * Create new JWT.
   *
   * @param secret secret
   * @param claims claims
   * @param expire expire time
   * @return new JWT
   */
  public String createToken(String secret, Claims claims, Long expire) {
    Date now = new Date();
    claims.setIssuedAt(now);

    Date expireDate = new Date(now.getTime() + expire);
    claims.setExpiration(expireDate);

    return Jwts.builder()
            .setClaims(claims)
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS512)
            .compact();
  }

  /**
   * Get Claims from JWT.
   *
   * @param token  token
   * @param secret secret
   * @return Claims
   */
  public Claims getClaims(String token, String secret) {
    return Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secret)).build()
            .parseClaimsJws(token).getBody();
  }

  /**
   * Check for JWT is expired.
   *
   * @param token  JWT
   * @param secret secret
   * @return is expired
   */
  public boolean isExpired(String token, String secret) {
    try {
      Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secret))
              .build().parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      return true;
    }
    return false;
  }
}
