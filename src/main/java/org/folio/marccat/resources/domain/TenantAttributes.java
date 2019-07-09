package org.folio.marccat.resources.domain;

import java.io.Serializable;

public class TenantAttributes implements Serializable {

  private static final long serialVersionUID = -3616106908132631349L;

  private String moduleTo;

  private String moduleFrom;

  public TenantAttributes() {
    super();
  }

  public TenantAttributes(String moduleTo) {
    this();
    this.moduleTo = moduleTo;
  }

  public TenantAttributes(String moduleTo, String moduleFrom) {
    this(moduleTo);
    this.moduleFrom = moduleFrom;
  }

  public String getModuleTo() {
    return moduleTo;
  }

  public void setModuleTo(String moduleTo) {
    this.moduleTo = moduleTo;
  }

  public String getModuleFrom() {
    return moduleFrom;
  }

  public void setModuleFrom(String moduleFrom) {
    this.moduleFrom = moduleFrom;
  }

}
