package org.folio.marccat;

import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class PostgresClient {
  @Bean(destroyMethod = "stop")
  public PostgresProcess postgresProcess() throws IOException {
    //log.info("Starting embedded Postgres");

    String tempDir = System.getProperty("java.io.tmpdir");
    String dataDir = tempDir + "/database_for_tests";
    String binariesDir = System.getProperty("java.io.tmpdir") + "/postgres_binaries";

    PostgresConfig postgresConfig = new PostgresConfig(
      Version.V10_3,
      new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
      new AbstractPostgresConfig.Storage("database_for_tests", dataDir),
      new AbstractPostgresConfig.Timeout(60_000),
      new AbstractPostgresConfig.Credentials("bob", "ninja")
    );

    PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getInstance(EmbeddedPostgres.cachedRuntimeConfig(Paths.get(binariesDir)));
    PostgresExecutable exec = runtime.prepare(postgresConfig);
    PostgresProcess process = exec.start();
    return process;
  }


}
