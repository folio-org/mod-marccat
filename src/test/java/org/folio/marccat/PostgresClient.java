package org.folio.marccat;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;
import org.postgresql.ds.PGPoolingDataSource;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class PostgresClient {

  private static EmbeddedPostgres embeddedPostgres;

  @Bean(destroyMethod = "stop")
  public PostgresProcess postgresProcess() throws IOException {
    EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres(Version.Main.V10);
    //TODO leggere dal .test yml
    embeddedPostgres.start("localhost", 5432, "database_for_tests", "folio_admin", "folio_admin"/*,
       Arrays.asList("-E", "UTF-8", "--locale", "en_US.UTF-8")*/);
    Runtime.getRuntime().addShutdownHook(new Thread(PostgresClient::stopEmbeddedPostgres));
    return embeddedPostgres.getProcess().get();

  }

  @Bean(destroyMethod = "close")
  @DependsOn("postgresProcess")
  DataSource dataSource(PostgresProcess postgresProcess) {
    PGPoolingDataSource dataSource = new PGPoolingDataSource();
    PostgresConfig postgresConfig = postgresProcess.getConfig();
    dataSource.setUser(postgresConfig.credentials().username());
    dataSource.setPassword(postgresConfig.credentials().password());
    dataSource.setPortNumber(postgresConfig.net().port());
    dataSource.setServerName(postgresConfig.net().host());
    dataSource.setDatabaseName(postgresConfig.storage().dbName());
     return dataSource;
  }

  public static void stopEmbeddedPostgres() {
    if (embeddedPostgres != null) {
     embeddedPostgres.stop();
      embeddedPostgres = null;

    }
  }

}
