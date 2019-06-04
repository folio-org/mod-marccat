package org.folio.marccat.spring.okapi;

import io.vertx.core.Future;
import org.folio.okapi.common.XOkapiHeaders;

import java.util.Map;

/**
 * @author: Christian Chiama
 * @date: 05/06/2019 01:30
 * <br/>
 * <br/>
 * @project: folio/mod-marccat 2019
 * <br/>
 * <br/>
 * @packageName: org.folio.marccat.spring.okapi
 * @className: OkapiParams
 * <br/>
 **/
public interface OkapiParams<K> {

  Future<K> get(final Map<String, String> okapiHeaders);
  Future<K> get(XOkapiHeaders okapiHeaders);

}
