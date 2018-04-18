package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.F;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.VerificationLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Verification Levels RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Verification level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class VerificationLevelsAPI extends BaseResource {

    private Function<Avp<String>, VerificationLevel> toVerificationLevel = source -> {
        final VerificationLevel verificationLevel = new VerificationLevel();
        verificationLevel.setCode(source.getValue());
        verificationLevel.setDescription(source.getLabel());
        return verificationLevel;
    };

    @ApiOperation(value = "Returns all levels associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested levels."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/verification-levels")
    public void getVerificationLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final VerificationLevelCollection container = new VerificationLevelCollection();
                container.setVerificationLevels(
                        storageService.getVerificationLevels(lang)
                                .stream()
                                .map(toVerificationLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void putCatalogingVerificationLevelsByCode(String code, String lang, VerificationLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doPut((storageService, configuration, future) -> {
            try {
                // Here we have to manage the update of the entity associated with the incoming id, using the given state
                // (the entity attribute).
                T_VRFTN_LVL codeTable = new T_VRFTN_LVL();
                codeTable.setLanguage(lang);
                codeTable.setCode(code.charAt(0));
                codeTable = (T_VRFTN_LVL) storageService.getCodeTableByCode(T_VRFTN_LVL.class, codeTable);

                codeTable.setLongText(entity.getDescription());
                codeTable.setShortText(entity.getDescription());

                // Note that the validation predicate (the last parameter) will be invoked before entering here so at
                // this time the incoming state (the entity parameter) is supposed to be valid.

                // So: here there's the update logic...
                storageService.updateCodeTable(codeTable);
                // And here, since the interface require something to be returned, in case everything goes well
                // we will return the same entity passed in input.
                return entity;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, () -> {
            // Here each service should provide a body where the incoming entity is validated.
            // The function return true or false depending on the "congruency" of the entity.
            // Note that if you return false here, no other method will be called, a 400 BAD request will be returned.
            //I can find empty code valid
            return (code != null &&  F.isNotNullOrEmpty(lang)  &&  F.isNotNullOrEmpty(entity.getDescription()));
        });
    }
}
