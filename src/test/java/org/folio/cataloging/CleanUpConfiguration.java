package org.folio.cataloging;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.tools.utils.TenantTool;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.datasourceConfiguration;

public class CleanUpConfiguration {
    public static void main(final String args []) throws Exception {
        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        System.getProperty("config.server.listen.address", "192.168.0.158"),
                        Integer.parseInt(System.getProperty("config.server.listen.port", "8085")),
                        "tnx");

        configuration.getEntries("module==CATALOGING", 0, 100, "en", response ->
                response.bodyHandler(body -> {
                    ids(body).forEach(id -> {
                        try {
                            configuration.deleteEntryId(id, "en", delResponse -> System.out.println(id + " -> " + delResponse.statusMessage()));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    });
                }));
    }

    public static List<String> ids(final Buffer value) {
        return new JsonObject(value.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .map(obj -> obj.getString("id"))
                .collect(toList());
    }
}
