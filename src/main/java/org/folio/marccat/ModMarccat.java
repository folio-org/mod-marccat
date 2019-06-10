package org.folio.marccat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.client.RestTemplate;

/**
 * Main application entry point.
 *
 */
@SpringBootApplication
public class ModMarccat {

  public static final String BASE_URI = "marccat";


  /**
   * Module entry point.
   *
   * @param args the command line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(ModMarccat.class, args);
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }
}
