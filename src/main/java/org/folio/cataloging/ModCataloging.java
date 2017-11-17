package org.folio.cataloging;

import org.folio.rest.RestLauncher;

/**
 * Main application entry point.
 *
 * @author Andrea Gazzarini
 * @since 1.0
 */
public class ModCataloging extends RestLauncher{
    public static void main(final String[] args) {
        new RestLauncher().dispatch(args);
    }
}