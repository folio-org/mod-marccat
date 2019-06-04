package org.folio.marccat.spring.service;

import io.vertx.core.Future;
import org.folio.common.OkapiParams;

import java.util.List;

/**
 * @author: Christian Chiama
 * @date: 05/06/2019 01:14
 * <br/>
 * <br/>
 * @project: folio/mod-marccat 2019
 * <br/>
 * <br/>
 * @packageName: org.folio.marccat.resources.service
 * @className: BaseService
 * <br/>
 **/
public interface BaseService<K> {

  Future<K> findByQuery(String query, int offset, int limit, String lang, String tenantId);

  Future<K> findById(String id, String tenantId);

  Future<List<K>> findByIds(List<String> ids, String tenantId);

  Future<K> save(K entity, OkapiParams params);

  Future<Void> update(String id, K entity, OkapiParams params);

  Future<Void> delete(String id, String tenantId);
}
