package org.folio.cataloging.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.cataloging.integration.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestConfiguration implements Configuration {
    @Override
    public ObjectNode attributes(String tenant, boolean withDatasource, String... configurationSets) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode cfg = mapper.createObjectNode();

        final ArrayNode configs = cfg.putArray("configs");
        configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","user").put("value", "amicus"));
        configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","password").put("value","oracle"));
        configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","url").put("value","jdbc:postgresql://192.168.0.158:5433/olidb_sv4"));

        return cfg;
    }
}
