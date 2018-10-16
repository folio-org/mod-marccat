package org.folio.cataloging;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

/**
 * Main application entry point.
 *
 * @author Andrea Gazzarini
 * @author Carmen Trazza
 * @since 1.0
 */
@SpringBootApplication
public class ModCataloging {

  public static final String BASE_URI = "cataloging";

  /**
   * Module entry point.
   *
   * @param args the command line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run (ModCataloging.class, args);
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build ( );
  }
}
