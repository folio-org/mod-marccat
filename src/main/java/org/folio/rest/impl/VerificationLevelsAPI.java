package org.folio.rest.impl;

import com.thoughtworks.xstream.core.util.Fields;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.F;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.dao.persistence.CodeTable;
import org.folio.cataloging.dao.persistence.T_VRFTN_LVL;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.VerificationLevel;
import org.folio.rest.jaxrs.model.VerificationLevelCollection;
import org.folio.rest.jaxrs.resource.CatalogingVerificationLevelsResource;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.integration.CatalogingHelper.doPut;

/**
 * Verification Levels RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class VerificationLevelsAPI implements CatalogingVerificationLevelsResource {

    protected final Log logger = new Log(VerificationLevelsAPI.class);

    private Function<Avp<String>, VerificationLevel> toVerificationLevel = source -> {
        final VerificationLevel verificationLevel = new VerificationLevel();
        verificationLevel.setCode(source.getValue());
        verificationLevel.setDescription(source.getLabel());
        return verificationLevel;
    };

    @Override
    public void getCatalogingVerificationLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
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
    public void postCatalogingVerificationLevels(String lang, VerificationLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void getCatalogingVerificationLevelsByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void deleteCatalogingVerificationLevelsByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void putCatalogingVerificationLevelsByCode(String code, String lang, VerificationLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doPut((storageService, future) -> {
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
            return (F.isNotNullOrEmpty(code) &&  F.isNotNullOrEmpty(lang)  &&  F.isNotNullOrEmpty(entity.getDescription()));
        });
    }
}
