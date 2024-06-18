package com.blogitory.blog.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configurations for RestTemplate.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
public class RestTemplateConfig {

  /**
   * ClientHttpRequestFactory bean.
   *
   * @return ClientHttpRequestFactory
   */
  @Bean
  public ClientHttpRequestFactory clientHttpRequestFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

    factory.setConnectTimeout(30000);
    factory.setReadTimeout(100000);

    return factory;
  }

  /**
   * RestTemplate bean.
   *
   * @return RestTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(clientHttpRequestFactory());
  }
}
