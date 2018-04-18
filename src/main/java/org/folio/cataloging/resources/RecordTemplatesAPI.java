package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.codehaus.jackson.map.ObjectMapper;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.dao.persistence.Model;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.RecordTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.isNotNullOrEmpty;

/**
 * BIB / AUT Record templates API.
 *
 * @since 1.0
 * @author agazzarini
 * @author carment
 */
@RestController
@Api(value = "modcat-api", description = "Record template resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class RecordTemplatesAPI extends BaseResource {


    private Function<Avp<Integer>, RecordTemplate> toRecordTemplate = avp -> {
        final RecordTemplate template = new RecordTemplate();
        template.setId(avp.getValue());
        template.setName(avp.getLabel());
        return template;

    };

    @ApiOperation(value = "Returns all templates associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested templates."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/record-templates")
    public void getRecordTemplates(
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
                return objectMapper.readValue(template.getRecordFields(), RecordTemplate.class);
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
                if("A".equals(entity.getType())) {
                    storageService.saveAuthorityRecordTemplate(entity.getName(), entity.getGroup(), jsonInString);
                } else {
                    storageService.saveBibliographicRecordTemplate(entity.getName(), entity.getGroup(), jsonInString);
                }
                return entity;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext,
                () -> isNotNullOrEmpty(entity.getName()),
                () -> String.valueOf(entity.getId()));
    }

    @Override
    public void putCatalogingRecordTemplatesById(
            final String id,final String lang,
            final RecordTemplate entity, Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
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
            }, asyncResultHandler,
               okapiHeaders,
               vertxContext,
               () -> isNotNullOrEmpty(id) &&  isNotNullOrEmpty(entity.getName()));
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
                if("A".equals(type.name()))
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