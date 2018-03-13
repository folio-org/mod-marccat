package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.domain.GeneralInformation;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.model.FixedField;
import org.folio.rest.jaxrs.model.VariableField;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;


/**
 * FieldTemplate Restful API
 * @author natasciab
 *
 */

public class FieldTemplateAPI implements CatalogingFieldTemplateResource {

    //protected static AbstractMapBackedFactory TAG_FACTORY = new MapBackedFactory();
    //protected static AbstractMapBackedFactory FIXED_FIELDS_FACTORY = new MapBackedFactory();

    /*static {
        final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
                TAG_FACTORY);
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
                FIXED_FIELDS_FACTORY);
    }*/

    protected final Log logger = new Log(FieldTemplateAPI.class);


    /*public void getCatalogingFieldTemplate(
            final int categoryCode,
            final String headingTypeCode,
            final String itemType,
            final String functionCode,
            final String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

          doGet((storageService, future) -> {
            try {
                Tag tag = (Tag) TAG_FACTORY.create(categoryCode);
                return null;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }*/


    @Override
    public void getCatalogingFieldTemplate(final int categoryCode,
                                           final String ind1, final String ind2,
                                           final String code, final Integer headerType,
                                           final String lang,
                                           final Map<String, String> okapiHeaders,
                                           final Handler<AsyncResult<Response>> asyncResultHandler,
                                           final Context vertxContext) throws Exception {

        doGet((storageService, future) -> {
            try {
                final FieldTemplate fieldTemplate =
                !isFixedField(code)
                ? ofNullable(storageService.getCorrelationVariableField(categoryCode, ind1, ind2, code))
                        .map(correlationValues -> {
                            final short code1 = correlationValues.getValue(1);
                            final Class clazz = Global.firstCorrelationHeadingClassMap.get(Integer.toString(categoryCode));
                            final String description = storageService.getHeadingTypeDescription(code1, lang, clazz);
                            final Validation validation = storageService.getSubfieldsByCorrelations(Integer.toString(categoryCode), correlationValues.getValue(1),
                                    correlationValues.getValue(2), correlationValues.getValue(3));

                            final FieldTemplate fieldT = new FieldTemplate();
                            fieldT.setVariableField(getVariableField(categoryCode, ind1, ind2, code, correlationValues, description, validation));
                            fieldT.setCode(code);
                            return fieldT;

                        }).orElse(null)
                :   ofNullable(getFixedField(storageService, headerType, code))
                        .map(fixedField -> {
                            final FieldTemplate fieldT = new FieldTemplate();
                            fieldT.setFixedField(fixedField);
                            fieldT.setCode(code);
                            return fieldT;

                        }).orElse(null) ;

                return fieldTemplate;

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    private FixedField getFixedField(final StorageService storageService, final int headerType, String code) {

        //for update leader tag 008 have to change
        //Map<String, String> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByLeaderValues(leader.charAt(5), leader.charAt(6), code);

        FixedField fixedField = new FixedField();
        if (isFixedField(code)){

            final GeneralInformation generalInformation = new GeneralInformation();
            generalInformation.setHeaderType((short) headerType);
            generalInformation.setFormOfMaterial(storageService.getFormOfMaterialByHeaderCode((short) headerType, code));
            //generalInformation.setDefaultValuesForBook(configuration);
            generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedDateYYMMDD());

            //fixedField.getTemplate().setAdditionalProperty();
            if (code.equals(Global.MATERIAL_TAG_CODE)){
                generalInformation.setMaterialDescription008Indicator("1");
            } else if (code.equals(Global.OTHER_MATERIAL_TAG_CODE)){
                generalInformation.setMaterialDescription008Indicator("0");

            }



        }

        return fixedField;
    }

    private VariableField getVariableField(final int categoryCode,
                             final String ind1,
                             final String ind2,
                             final String code,
                             final CorrelationValues correlations,
                             final String description, final Validation validation) {

        VariableField variableField = new VariableField();
        if (!isFixedField(code))
        {
            variableField.setHeadingTypeCode(Short.toString(correlations.getValue(1)));
            variableField.setItemTypeCode(Short.toString(correlations.getValue(2)));
            variableField.setFunctionCode(Short.toString(correlations.getValue(3)));
            variableField.setCategoryCode(categoryCode);
            variableField.setCode(code);
            variableField.setInd1(ind1);
            variableField.setInd2(ind2);
            variableField.setDescription(description);

            variableField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split(""))
                                .collect(Collectors.toList()));
            variableField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

        }
        return variableField;
    }

    private boolean isFixedField(final String tagNumber) {
        return Global.FIXED_FIELDS.contains(tagNumber);
    }

    /*private void setValuesGeneralInformation(MaterialDescription md)
    {
        if (md.getMaterialDescription008Indicator() == '1') {
            //format date entered on file
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date = formatter.parse(getEnteredOnFileDate(),	new ParsePosition(0));
            md.setEnteredOnFileDate(date);
            md.setItemDateTypeCode(getItemDateTypeCode());
            md.setItemDateFirstPublication(getItemDateFirstPublication());
            md.setItemDateLastPublication(getItemDateLastPublication());

            if(getLanguageCodeCustom() == null) //proviene da ModelForm
                md.setLanguageCode(getLanguageCode());
            else
                md.setLanguageCode(getLanguageCodeCustom());

            //-- inizio modifiche Natascia bug 2711/2701
            String marcCountry = getMarcCountryCodeCustom();
            if(marcCountry == null) //proviene da ModelForm
                md.setMarcCountryCode(getMarcCountryCode());
            else
            {
                if (getMarcCountryCodeCustom().length() == 2)
                {
                    marcCountry = getMarcCountryCodeCustom() + " ";
                }

                md.setMarcCountryCode(marcCountry);
            }
            //-- fine

            md.setRecordModifiedCode(getRecordModifiedCode());
            md.setRecordCataloguingSourceCode(getRecordCataloguingSourceCode());
        } else {
            md.setMaterialTypeCode(getMaterialTypeCode());
        }

        if (md.isBook()) {
            md.setBookIllustrationCode(getBook_illustrationOne().toString()
                    + getBook_illustrationTwo().toString()
                    + getBook_illustrationThree().toString()
                    + getBook_illustrationFour().toString());
            md.setTargetAudienceCode(getTargetAudienceCode());
            md.setFormOfItemCode(getFormOfItemCode());
            md.setNatureOfContentsCode(getNatureOfContentOne().toString()
                    + getNatureOfContentTwo().toString()
                    + getNatureOfContentThree().toString()
                    + getNatureOfContentFour().toString());
            md.setGovernmentPublicationCode(getGovernmentPublicationCode());
            md.setConferencePublicationCode(getConferencePublicationCode());
            md.setBookFestschrift(getBookFestschrift());
            md.setBookIndexAvailabilityCode(getBookIndexAvailabilityCode());
            md.setBookLiteraryFormTypeCode(getBookLiteraryFormTypeCode());
            md.setBookBiographyCode(getBookBiographyCode());

        } else if (md.isSerial()) {
            md.setSerialFrequencyCode(getSerialFrequencyCode());
            md.setSerialRegularityCode(getSerialRegularityCode());
            md.setSerialTypeCode(getSerialTypeCode());
            md.setSerialFormOriginalItemCode(getSerialFormOriginalItemCode());
            md.setFormOfItemCode(getFormOfItemCode());

            md.setNatureOfContentsCode(getNatureOfContentOne().toString()
                    + getNatureOfContentTwo().toString()
                    + getNatureOfContentThree().toString()
                    + getNatureOfContentFour().toString());

            md.setGovernmentPublicationCode(getGovernmentPublicationCode());
            md.setConferencePublicationCode(getConferencePublicationCode());
            md.setSerialOriginalAlphabetOfTitleCode(getSerialOriginalAlphabetOfTitleCode());
            md.setSerialSuccessiveLatestCode(getSerialSuccessiveLatestCode());

        } else if (md.isComputerFile()) {
            md.setComputerTargetAudienceCode(getComputerTargetAudienceCode());

            md.setComputerFileFormCode(getComputerFileFormCode());
            md.setComputerFileTypeCode(getComputerFileTypeCode());
            md.setGovernmentPublicationCode(getGovernmentPublicationCode());

        } else if (md.isMap()) {
            md.setCartographicReliefCode(getCartographicReliefCode1().toString()
                    + getCartographicReliefCode2().toString()
                    + getCartographicReliefCode3().toString()
                    + getCartographicReliefCode4().toString());
            md.setCartographicProjectionCode(getCartographicProjectionCode());
            md.setCartographicMaterial(getCartographicMaterial());
            md.setGovernmentPublicationCode(getGovernmentPublicationCode());
            md.setFormOfItemCode(getFormOfItemCode());
            md.setCartographicIndexAvailabilityCode(getCartographicIndexAvailabilityCode());
            md.setCartographicFormatCode(getCartographicFormatCode1().toString() + getCartographicFormatCode2().toString());

        } else if (md.isMixedMaterial()) {
            md.setFormOfItemCode(getFormOfItemCode());

        } else if (md.isMusic()) {
            md.setMusicFormOfCompositionCode(getMusicFormOfCompositionCode());
            md.setMusicFormatCode(getMusicFormatCode());
            md.setTargetAudienceCode(getTargetAudienceCode());
            md.setFormOfItemCode(getFormOfItemCode());
            md.setMusicTextualMaterialCode(getMusicTextualMaterialCode1().toString()
                    + getMusicTextualMaterialCode2().toString()
                    + getMusicTextualMaterialCode3().toString()
                    + getMusicTextualMaterialCode4().toString()
                    + getMusicTextualMaterialCode5().toString()
                    + getMusicTextualMaterialCode6().toString());
            md.setMusicLiteraryTextCode(getMusicLiteraryTextCode1().toString() + getMusicLiteraryTextCode2().toString());

            md.setMusicPartsCode(getMusicPartsCode());
            md.setMusicTranspositionArrangementCode(getMusicTranspositionArrangementCode());

        } else if (md.isVisualMaterial()) {
            md.setVisualRunningTime(getVisualRunningTime());
            md.setVisualTargetAudienceCode(getVisualTargetAudienceCode());
            md.setGovernmentPublicationCode(getGovernmentPublicationCode());
            md.setFormOfItemCode(getFormOfItemCode());
            md.setVisualMaterialTypeCode(getVisualMaterialTypeCode());
            md.setVisualTechniqueCode(getVisualTechniqueCode());
        }
    }*/

    @Override
    public void deleteCatalogingFieldTemplate(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFieldTemplate(String lang, FieldTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
