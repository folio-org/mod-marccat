package org.folio.marccat.spring.rest;

import io.vertx.core.Future;

/**
 * @author: Christian Chiama
 * @date: 05/06/2019 01:23
 * <br/>
 * <br/>
 * @project: folio/mod-marccat 2019
 * <br/>
 * <br/>
 * @packageName: org.folio.marccat.spring.rest
 * @className: Configuration
 * <br/>
 **/
public interface Configuration<K> {

  Future<K> findConfiguration(String... configurationSets);
  Future<K> findConfigurationByCode(String code);
}
