package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.json.DecodeException;

/**
 * Description of one deployed module. Refers to one running instance of a
 * module on a node in the cluster.
 */
@JsonInclude(Include.NON_NULL)
public class DeploymentDescriptor {

  private String instId;
  private String srvcId;
  private String nodeId;
  private String url;
  private LaunchDescriptor descriptor;


  public DeploymentDescriptor() {
    this.instId = null;
    this.srvcId = null;
    this.url = null;
    this.descriptor = null;
  }

  public DeploymentDescriptor(String instId, String srvcId,
    String url, LaunchDescriptor descriptor/*, ModuleHandle moduleHandle*/) {
    this.instId = instId;
    this.srvcId = srvcId;
    this.url = url;
    this.descriptor = descriptor;
  }

  public DeploymentDescriptor(String instId, String srvcId,
          LaunchDescriptor descriptor) {
    this.instId = instId;
    this.srvcId = srvcId;
    this.url = null;
    this.descriptor = descriptor;
  }

  public String getInstId() {
    return instId;
  }

  public void setInstId(String id) {
    this.instId = id;
  }

  public String getSrvcId() {
    return srvcId;
  }

  public void setSrvcId(String srvcId) {
    if (srvcId.isEmpty()) {
      throw new DecodeException("Empty srvcId not allowed");
    }
    this.srvcId = srvcId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LaunchDescriptor getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(LaunchDescriptor descriptor) {
    this.descriptor = descriptor;
  }



}
