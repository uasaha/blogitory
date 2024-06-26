package com.blogitory.blog.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * GitHub Oauth properties.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.github")
public class GithubOauthProperties {
  private String clientId;
  private String clientSecret;
}
