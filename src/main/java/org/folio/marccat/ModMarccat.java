package org.folio.marccat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.folio.marccat.config.log.Log;
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

  /**
   * Module entry point.
   *
   * @param args the command line arguments.
   */
  public static void main(final String[] args) {
    InetAddress inetAddress = null;
	try {
		inetAddress = InetAddress.getLocalHost();
		logger.info("OKAPI_URL: "+ System.getProperty("okapiurl"));
		logger.info("IP Address-localhost:- " + inetAddress);
		logger.info("IP Address-host:- " + inetAddress.getHostAddress());
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}
    SpringApplication.run(ModMarccat.class, args);
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }
}
