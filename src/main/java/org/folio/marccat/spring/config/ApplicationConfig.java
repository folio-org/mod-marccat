package org.folio.marccat.spring.config;


import javax.ws.rs.core.Response;

import com.rits.cloning.Cloner;
import org.folio.common.pf.PartialFunction;
import org.folio.config.ModConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import org.folio.rest.tools.messages.Messages;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.folio.rest.exc.RestExceptionHandlers.*;

@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = {
  "org.folio.marccat.search",
  "org.folio.marccat.resources",
  "org.folio.marccat.spring.config",
  "org.folio.marccat.annotation"
})
public class ApplicationConfig {

  @Bean
  public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setLocation(new ClassPathResource("application.properties"));
    return configurer;
  }

  @Bean
  public Messages messages() {
    return Messages.getInstance();
  }

  @Bean
  public PartialFunction<Throwable, Response> defaultExcHandler() {
    return logged(badRequestHandler()
      .orElse(notFoundHandler())
      .orElse(generalHandler())
      .compose(completionCause())); // extract the cause before applying any handler
  }

  @Bean
  public org.folio.config.Configuration configuration(@Value("${note.configuration.module}") String module) {
    return new ModConfiguration(module);
  }

  @Bean
  public Cloner restModelCloner() {
    return new Cloner();
  }
}
