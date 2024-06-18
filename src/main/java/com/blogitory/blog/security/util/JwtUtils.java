package com.blogitory.blog.security.util;

import com.blogitory.blog.security.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.Cookie;
import java.util.Base64;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Util class for JWT.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
  public static final String ACCESS_TOKEN_COOKIE_NAME = "uids";
  public static final Integer ACCESS_COOKIE_EXPIRE = 90000;
  public static final String BLACK_LIST_KEY = "Black-List";

  /**
   * make secure cookie.
   *
   * @param key    key
   * @param value  value
   * @param expire expire time
   * @return new cookie
   */
  public static Cookie makeSecureCookie(String key, String value, Integer expire) {
    Cookie cookie = new Cookie(key, value);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(expire);
    cookie.setPath("/");

    return cookie;
  }

  /**
   * Get user's uuid.
   *
   * @param accessToken JWT
   * @return uuid
   */
  public static String getUuid(String accessToken) {
    String payload = getPayload(accessToken);
    JSONParser parser = new JSONParser();
    JSONObject jsonObject;

    try {
      jsonObject = (JSONObject) parser.parse(payload);
    } catch (ParseException e) {
      return "";
    }

    return (String) jsonObject.get("sub");
  }

  /**
   * Get payload from JWT.
   *
   * @param accessToken JWT
   * @return payload
   */
  public static String getPayload(String accessToken) {
    String[] chunks = accessToken.split("\\.");

    Base64.Decoder decoder = Base64.getDecoder();
    return new String(decoder.decode(chunks[1]));
  }

  /**
   * Parsing JWT.
   *
   * @param secret secret key
   * @param token  JWT
   * @return Claims
   */
  public static Claims parseToken(String secret, String token) {
    return Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secret)).build()
            .parseClaimsJws(token).getBody();
  }

  /**
   * Checking expired JWT.
   *
   * @param secret secret
   * @param token  JWT
   * @return is expired
   */
  public static boolean isExpiredToken(String secret, String token) {
    try {
      Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secret)).build()
              .parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      throw new AuthenticationException("Expired or invalid JWT");
    }
    return false;
  }

  /**
   * Get roles from JWT.
   *
   * @param secret      secret
   * @param accessToken JWT
   * @return roles
   */
  public static List<String> getRoles(String secret, String accessToken) {
    Claims claims = parseToken(secret, accessToken);

    return (List<String>) claims.get("roles");
  }
}
