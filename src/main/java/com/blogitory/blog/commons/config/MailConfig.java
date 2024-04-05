package com.blogitory.blog.commons.config;

import com.blogitory.blog.commons.properties.MailProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration for using JavaMailSender.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class MailConfig {
  private final MailProperties mailProperties;

  /**
   * Setting for JavaMailSender Bean.
   *
   * @return JavaMailSender
   */
  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setHost(mailProperties.getHost());
    javaMailSender.setUsername(mailProperties.getUsername());
    javaMailSender.setPort(Integer.parseInt(mailProperties.getPort()));
    javaMailSender.setPassword(mailProperties.getPassword());

    javaMailSender.setJavaMailProperties(getMailProperties());

    return javaMailSender;
  }

  /**
   * Setting for JavaMailProperties.
   *
   * @return MailProperties
   */
  private Properties getMailProperties() {
    Properties properties = new Properties();
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.connectiontimeout", "5000");
    properties.setProperty("mail.smtp.starttls.enable", "true");

    return properties;
  }
}
