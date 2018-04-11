package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.codehaus.jackson.map.ObjectMapper;
import org.folio.cataloging.F;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.dao.persistence.Model;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.RecordTemplate;
import org.folio.rest.jaxrs.model.RecordTemplateCollection;
import org.folio.rest.jaxrs.resource.CatalogingRecordTemplatesResource;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.*;

/**
 * BIB / AUT Record templates API.
 *
 * @since 1.0
 * @author agazzarini
 * @author carment
 */
public class RecordTemplatesAPI implements CatalogingRecordTemplatesResource {
    protected final Log logger = new Log(RecordTemplate.class);

    private Function<Avp<Integer>, RecordTemplate> toRecordTemplate = avp -> {
        final RecordTemplate template = new RecordTemplate();
        template.setId(avp.getValue());
        template.setName(avp.getLabel());
        return template;

    };

    @Override
    public void getCatalogingRecordTemplates(
            final CatalogingRecordTemplatesResource.Type type,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final List<Avp<Integer>> templates =
                        type == Type.A
                            ? storageService.getAuthorityRecordTemplates()
                            : storageService.getBibliographicRecordTemplates();

                final RecordTemplateCollection collection = new RecordTemplateCollection();
                collection.setRecordTemplates(templates.stream().map(toRecordTemplate).collect(toList()));
                return collection;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void getCatalogingRecordTemplatesById(
            final String id,
            final Type type,
            final String lang,final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final Model template =
                        type == Type.A
                                ? storageService.getAuthorityRecordRecordTemplatesById(id)
                                : storageService.getBibliographicRecordRecordTemplatesById(id);
                final ObjectMapper objectMapper = new ObjectMapper();
                final RecordTemplate recordTemplate = objectMapper.readValue(template.getRecordFields(), RecordTemplate.class);
                return recordTemplate;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }


    @Override
    public void postCatalogingRecordTemplates(
            final String lang,
            final RecordTemplate entity,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doPost((storageService, configuration, future) -> {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                final String jsonInString = mapper.writeValueAsString(entity);
                if("A".equals(entity.getType()))
                    storageService.saveAuthorityRecordTemplate(entity.getName(), entity.getGroup(), jsonInString);
                else
                    storageService.saveBibliographicRecordTemplate(entity.getName(), entity.getGroup(), jsonInString);
                return entity;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext,
                () -> {return F.isNotNullOrEmpty(entity.getName());},
                () -> {return String.valueOf(entity.getId());});
    }

    @Override
    public void putCatalogingRecordTemplatesById(
            final String id,final String lang,
            final RecordTemplate entity, Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        {
            doPut((storageService, configuration, future) -> {
                try {
                    final ObjectMapper mapper = new ObjectMapper();
                    final String jsonInString = mapper.writeValueAsString(entity);
                    if("A".equals(entity.getType()))
                        storageService.updateAuthorityRecordTemplate(id, entity.getName(), entity.getGroup(), jsonInString);
                    else
                        storageService.updateBibliographicRecordTemplate(id, entity.getName(), entity.getGroup(), jsonInString);
                    return entity;
                } catch (final Exception exception) {
                    logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                    return null;
                }
            },  asyncResultHandler, okapiHeaders, vertxContext, () -> {
                return (id != null &&  F.isNotNullOrEmpty(entity.getName()));
            });
        }
    }
    @Override
    public void deleteCatalogingRecordTemplatesById(
            final String id,
            final Type type,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doDelete((storageService, configuration, future) -> {
            try {
                if("A".equals(type))
                    storageService.deleteAuthorityRecordTemplate(id);
                else
                    storageService.deleteBibliographicRecordTemplate(id);
                return id;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }


}
