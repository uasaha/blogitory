package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.properties.MailProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * MailConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class MailConfigTest {

  @Test
  void javaMailSender() {
    MailProperties mailProperties = mock(MailProperties.class);
    MailConfig mailConfig = new MailConfig(mailProperties);

    when(mailProperties.getHost()).thenReturn("test");
    when(mailProperties.getUsername()).thenReturn("test");
    when(mailProperties.getPort()).thenReturn("443");
    when(mailProperties.getPassword()).thenReturn("test");

    JavaMailSender mailSender = mailConfig.javaMailSender();

    assertInstanceOf(JavaMailSenderImpl.class, mailSender);
  }
}