package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class RestTemplateConfigTest {

  @Test
  void clientHttpRequestFactory() {
    RestTemplateConfig config = new RestTemplateConfig();
    ClientHttpRequestFactory factory = config.clientHttpRequestFactory();

    assertInstanceOf(SimpleClientHttpRequestFactory.class, factory);
  }

  @Test
  void restTemplate() {
    RestTemplateConfig config = new RestTemplateConfig();

    RestTemplate restTemplate = config.restTemplate();

    assertInstanceOf(RestTemplate.class, restTemplate);
  }
}