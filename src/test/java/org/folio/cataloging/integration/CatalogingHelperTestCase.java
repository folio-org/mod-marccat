package org.folio.cataloging.integration;

import org.junit.Test;

import static org.folio.cataloging.integration.CatalogingHelper.BASE_CQUERY;
import static org.folio.cataloging.integration.CatalogingHelper.cQuery;
import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link CatalogingHelper}.
 *
 * @author agazzarini
 * @since 1.0
 */
public class CatalogingHelperTestCase {
    /**
     * If no selection criteria has given, then only the "datasource" group must be loaded.
     */
    @Test
    public void configurationQueryWithNoSelectionCriteria() {
        assertEquals(BASE_CQUERY + ")", cQuery(null));
        assertEquals(BASE_CQUERY + ")", cQuery());
        assertEquals(BASE_CQUERY + ")", cQuery(new String[0]));
    }

    /**
     * Configuration query with one single selection criterion.
     */
    @Test
    public void configurationQueryWithOneSelectionCriterion() {
        final String criterion = "bibliographic";
        assertEquals(
                BASE_CQUERY + " or " + criterion + ")",
                cQuery(criterion));

    }

    /**
     * Configuration query with three selection criteria.
     */
    @Test
    public void configurationQueryWithThreeSelectionCriteria() {
        final String [] criteria = { "bibliographic", "material", "authority" };
        assertEquals(
                BASE_CQUERY + " or " + criteria[0] + " or " + criteria[1] + " or " + criteria[2] + ")",
                cQuery(criteria));
    }
}