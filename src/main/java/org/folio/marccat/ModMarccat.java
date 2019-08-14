package org.folio.marccat;

import org.folio.marccat.config.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main application entry point.
 *
 */
@SpringBootApplication

public class ModMarccat {
  private static final Log logger = new Log(ModMarccat.class);
  public static final String BASE_URI = "marccat";

  @Value("${server.port}")
  private static String port;


  /**
   * Module entry point.
   *
   * @param args the command line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(ModMarccat.class, args);
    logger.info("PRINT_INFO_SERVER_PORT: ", port);
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }
}
