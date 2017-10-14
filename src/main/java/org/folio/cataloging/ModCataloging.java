package org.folio.cataloging;

import org.folio.rest.RestLauncher;

public class ModCataloging extends RestLauncher{
    public static void main(final String[] args) {
        new RestLauncher().dispatch(args);
    }
}