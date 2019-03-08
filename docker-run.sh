#!/usr/bin/env bash
docker run -d --name okapi2 -e 'JAVA_OPTIONS=-Dokapiurl=http://151.1.165.20:9130 -Dstorage=postgres -Dpostgres_host=151.1.165.20 -Dpostgres_port=5432 -Dpostgres_user=amicus -Dpostgres_password=oracle -Dpostgres_database=folio_marccat_test1' -p9130:9130 folioorg/okapi:2.25.0 cluster
