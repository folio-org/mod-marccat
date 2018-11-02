package org.folio.marccat.dao.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * Local Cache alimentata da SAP
 */
public class SCasSapCache implements Serializable {

  /**
   * identifier field
   */
  private Long bibItmNbr;

  /* chiavi aggiuntive per dcm */
  private String idFile;
  private String idSpedizione;

  /**
   * nullable persistent field
   */
  private String tag900$a;

  /**
   * nullable persistent field
   */
  private String tag910$a;

  /**
   * nullable persistent field
   */
  private String tag910$b;

  /**
   * nullable persistent field
   */
  private String tag910$c;

  /**
   * nullable persistent field
   */
  private String tag910$d;

  /**
   * nullable persistent field
   */
  private String tag910$e;

  /**
   * nullable persistent field
   */
  private String tag910$f;

  /**
   * nullable persistent field
   */
  private String tag910$g;

  /**
   * nullable persistent field
   */
  private String tag920$a;

  /**
   * nullable persistent field
   */
  private String tag920$b;

  /**
   * nullable persistent field
   */
  private String tag920$d;

  /**
   * nullable persistent field
   */
  private String tag930$a;

  /**
   * nullable persistent field
   */
  private String tag937$b;

  /**
   * nullable persistent field
   */
  private String tag937$c;

  /**
   * nullable persistent field
   */
  private String tag952$a;

  /**
   * nullable persistent field
   */
  private String tag980$a;

  /**
   * nullable persistent field
   */
  private String tag980$b;

  /**
   * nullable persistent field
   */
  private String tag980$c;

  /**
   * nullable persistent field
   */
  private String tag980$d;

  /**
   * nullable persistent field
   */
  private String tag980$e;

  /**
   * nullable persistent field
   */
  private String tag980$f;

  /**
   * nullable persistent field
   */
  private String tag980$g;

  /**
   * nullable persistent field
   */
  private String tag980$i;

  /**
   * nullable persistent field
   */
  private String tag980$j;

  /**
   * nullable persistent field
   */
  private String tag980$l;

  /**
   * nullable persistent field
   */
  private String tag980$m;

  /**
   * nullable persistent field
   */
  private String tag980$n;

  /**
   * nullable persistent field
   */
  private String tag980$o;

  /**
   * nullable persistent field
   */
  private String tag980$p;

  /**
   * nullable persistent field
   */
  private String tag980$r;

  /**
   * nullable persistent field
   */
  private String tag980$u;

  /**
   * nullable persistent field
   */
  private String tag980$v;

  /**
   * nullable persistent field
   */
  private String tag981$b;

  /**
   * nullable persistent field
   */
  private String tag981$c;

  /**
   * nullable persistent field
   */
  private String tag981$d;

  /**
   * nullable persistent field
   */
  private String tag981$e;

  /**
   * nullable persistent field
   */
  private String tag981$i;

  /**
   * nullable persistent field
   */
  private String tag981$j;

  /**
   * nullable persistent field
   */
  private String tag981$k;

  /**
   * nullable persistent field
   */
  private String tag981$u;

  /**
   * nullable persistent field
   */
  private String tag981$v;

  /**
   * nullable persistent field
   */
  private String tag982$a;

  /**
   * nullable persistent field
   */
  private String tag982$b;

  /**
   * nullable persistent field
   */
  private String tag982$c;

  /**
   * nullable persistent field
   */
  private String tag982$d;

  /**
   * nullable persistent field
   */
  private String tag983$a;

  /**
   * nullable persistent field
   */
  private String tag983$b;

  /**
   * nullable persistent field
   */
  private String tag985$a;

  /**
   * nullable persistent field
   */
  private String tag986$a;

  /**
   * nullable persistent field
   */
  private String tag987$a;

  /**
   * nullable persistent field
   */
  private String tag990$b;

  /**
   * nullable persistent field
   */
  private String tag990$c;

  /**
   * nullable persistent field
   */
  private String tag990$d;

  /**
   * nullable persistent field
   */
  private String tag990$e;

  /**
   * nullable persistent field
   */
  private String tag990$i;

  /**
   * nullable persistent field
   */
  private String tag990$j;

  /**
   * nullable persistent field
   */
  private String tag990$u;

  /**
   * nullable persistent field
   */
  private String tag990$v;

  /** full constructor */
//    public SCasSapCache(Long bibItmNbr, String tag900$a, String tag910$a, String tag910$b, String tag910$c, String tag910$d, String tag910$e, String tag910$f, String tag910$g, String tag920$a, String tag920$b, String tag920$d, String tag930$a, String tag937$b, String tag937$c, String tag952$a, String tag980$b, String tag980$c, String tag980$d, String tag980$e, String tag980$i, String tag980$j, String tag980$m, String tag980$n, String tag980$o, String tag980$p, String tag980$r, String tag980$u, String tag980$v, String tag981$b, String tag981$c, String tag981$d, String tag981$e, String tag981$i, String tag981$j, String tag981$k, String tag981$u, String tag981$v, String tag982$a, String tag982$b, String tag982$d, String tag985$a, String tag987$a, String tag990$b, String tag990$c, String tag990$d, String tag990$e, String tag990$i, String tag990$j, String tag990$u, String tag990$v) {
//        this.bibItmNbr = bibItmNbr;
//        
//        this.tag900$a = tag900$a;
//        this.tag910$a = tag910$a;
//        this.tag910$b = tag910$b;
//        this.tag910$c = tag910$c;
//        this.tag910$d = tag910$d;
//        this.tag910$e = tag910$e;
//        this.tag910$f = tag910$f;
//        this.tag910$g = tag910$g;
//        this.tag920$a = tag920$a;
//        this.tag920$b = tag920$b;
//        this.tag920$d = tag920$d;
//        this.tag930$a = tag930$a;
//        this.tag937$b = tag937$b;
//        this.tag937$c = tag937$c;
//        this.tag952$a = tag952$a;
////        this.tag980$a = tag980$a;
//        this.tag980$b = tag980$b;
//        this.tag980$c = tag980$c;
//        this.tag980$d = tag980$d;
//        this.tag980$e = tag980$e;
////        this.tag980$f = tag980$f;
////        this.tag980$g = tag980$g;
//        this.tag980$i = tag980$i;
//        this.tag980$j = tag980$j;
////        this.tag980$l = tag980$l;
//        this.tag980$m = tag980$m;
//        this.tag980$n = tag980$n;
//        this.tag980$o = tag980$o;
//        this.tag980$p = tag980$p;
//        this.tag980$r = tag980$r;
//        this.tag980$u = tag980$u;
//        this.tag980$v = tag980$v;
//        this.tag981$b = tag981$b;
//        this.tag981$c = tag981$c;
//        this.tag981$d = tag981$d;
//        this.tag981$e = tag981$e;
//        this.tag981$i = tag981$i;
//        this.tag981$j = tag981$j;
//        this.tag981$k = tag981$k;
//        this.tag981$u = tag981$u;
//        this.tag981$v = tag981$v;
//        this.tag982$a = tag982$a;
//        this.tag982$b = tag982$b;
////        this.tag982$c = tag982$c;
//        this.tag982$d = tag982$d;
////        this.tag983$a = tag983$a;
////        this.tag983$b = tag983$b;
//        this.tag985$a = tag985$a;
////        this.tag986$a = tag986$a;
//        this.tag987$a = tag987$a;
//        this.tag990$b = tag990$b;
//        this.tag990$c = tag990$c;
//        this.tag990$d = tag990$d;
//        this.tag990$e = tag990$e;
//        this.tag990$i = tag990$i;
//        this.tag990$j = tag990$j;
//        this.tag990$u = tag990$u;
//        this.tag990$v = tag990$v;
//    }

  /**
   * default constructor
   */
  public SCasSapCache() {
  }

  /**
   * minimal constructor
   */
  public SCasSapCache(Long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public Long getBibItmNbr() {
    return this.bibItmNbr;
  }

  public void setBibItmNbr(Long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public String getTag910$a() {
    return this.tag910$a;
  }

  public void setTag910$a(String tag910$a) {
    this.tag910$a = tag910$a;
  }

  public String getTag910$b() {
    return this.tag910$b;
  }

  public void setTag910$b(String tag910$b) {
    this.tag910$b = tag910$b;
  }

  public String getTag910$c() {
    return this.tag910$c;
  }

  public void setTag910$c(String tag910$c) {
    this.tag910$c = tag910$c;
  }

  public String getTag910$d() {
    return this.tag910$d;
  }

  public void setTag910$d(String tag910$d) {
    this.tag910$d = tag910$d;
  }

  public String getTag910$e() {
    return this.tag910$e;
  }

  public void setTag910$e(String tag910$e) {
    this.tag910$e = tag910$e;
  }

  public String getTag910$f() {
    return this.tag910$f;
  }

  public void setTag910$f(String tag910$f) {
    this.tag910$f = tag910$f;
  }

  public String getTag910$g() {
    return this.tag910$g;
  }

  public void setTag910$g(String tag910$g) {
    this.tag910$g = tag910$g;
  }

//    public String getTag980$a() {
//        return this.tag980$a;
//    }
//
//    public void setTag980$a(String tag980$a) {
//        this.tag980$a = tag980$a;
//    }

  public String getTag980$b() {
    return this.tag980$b;
  }

  public void setTag980$b(String tag980$b) {
    this.tag980$b = tag980$b;
  }

  public String getTag980$c() {
    return this.tag980$c;
  }

  public void setTag980$c(String tag980$c) {
    this.tag980$c = tag980$c;
  }

  public String getTag980$d() {
    return this.tag980$d;
  }

  public void setTag980$d(String tag980$d) {
    this.tag980$d = tag980$d;
  }

  public String getTag980$e() {
    return this.tag980$e;
  }

  public void setTag980$e(String tag980$e) {
    this.tag980$e = tag980$e;
  }

//    public String getTag980$f() {
//        return this.tag980$f;
//    }
//
//    public void setTag980$f(String tag980$f) {
//        this.tag980$f = tag980$f;
//    }
//
//    public String getTag980$g() {
//        return this.tag980$g;
//    }
//
//    public void setTag980$g(String tag980$g) {
//        this.tag980$g = tag980$g;
//    }

  public String getTag980$i() {
    return this.tag980$i;
  }

  public void setTag980$i(String tag980$i) {
    this.tag980$i = tag980$i;
  }

  public String getTag980$j() {
    return this.tag980$j;
  }

  public void setTag980$j(String tag980$j) {
    this.tag980$j = tag980$j;
  }

//    public String getTag980$l() {
//        return this.tag980$l;
//    }
//
//    public void setTag980$l(String tag980$l) {
//        this.tag980$l = tag980$l;
//    }

  public String getTag980$m() {
    return this.tag980$m;
  }

  public void setTag980$m(String tag980$m) {
    this.tag980$m = tag980$m;
  }

  public String getTag980$n() {
    return this.tag980$n;
  }

  public void setTag980$n(String tag980$n) {
    this.tag980$n = tag980$n;
  }

  public String getTag980$o() {
    return this.tag980$o;
  }

  public void setTag980$o(String tag980$o) {
    this.tag980$o = tag980$o;
  }

  public String getTag980$p() {
    return this.tag980$p;
  }

  public void setTag980$p(String tag980$p) {
    this.tag980$p = tag980$p;
  }

  public String getTag980$r() {
    return this.tag980$r;
  }

  public void setTag980$r(String tag980$r) {
    this.tag980$r = tag980$r;
  }

  public String getTag980$u() {
    return this.tag980$u;
  }

  public void setTag980$u(String tag980$u) {
    this.tag980$u = tag980$u;
  }

  public String getTag980$v() {
    return this.tag980$v;
  }

  public void setTag980$v(String tag980$v) {
    this.tag980$v = tag980$v;
  }

  public String getTag981$b() {
    return this.tag981$b;
  }

  public void setTag981$b(String tag981$b) {
    this.tag981$b = tag981$b;
  }

  public String getTag981$c() {
    return this.tag981$c;
  }

  public void setTag981$c(String tag981$c) {
    this.tag981$c = tag981$c;
  }

  public String getTag981$d() {
    return this.tag981$d;
  }

  public void setTag981$d(String tag981$d) {
    this.tag981$d = tag981$d;
  }

  public String getTag981$e() {
    return this.tag981$e;
  }

  public void setTag981$e(String tag981$e) {
    this.tag981$e = tag981$e;
  }

  public String getTag981$i() {
    return this.tag981$i;
  }

  public void setTag981$i(String tag981$i) {
    this.tag981$i = tag981$i;
  }

  public String getTag981$j() {
    return this.tag981$j;
  }

  public void setTag981$j(String tag981$j) {
    this.tag981$j = tag981$j;
  }

  public String getTag981$k() {
    return this.tag981$k;
  }

  public void setTag981$k(String tag981$k) {
    this.tag981$k = tag981$k;
  }

  public String getTag981$u() {
    return this.tag981$u;
  }

  public void setTag981$u(String tag981$u) {
    this.tag981$u = tag981$u;
  }

  public String getTag981$v() {
    return this.tag981$v;
  }

  public void setTag981$v(String tag981$v) {
    this.tag981$v = tag981$v;
  }

  public String getTag982$a() {
    return this.tag982$a;
  }

  public void setTag982$a(String tag982$a) {
    this.tag982$a = tag982$a;
  }

  public String getTag982$b() {
    return this.tag982$b;
  }

  public void setTag982$b(String tag982$b) {
    this.tag982$b = tag982$b;
  }

//    public String getTag982$c() {
//        return this.tag982$c;
//    }
//
//    public void setTag982$c(String tag982$c) {
//        this.tag982$c = tag982$c;
//    }

  public String getTag982$d() {
    return this.tag982$d;
  }

  public void setTag982$d(String tag982$d) {
    this.tag982$d = tag982$d;
  }

//    public String getTag983$a() {
//        return this.tag983$a;
//    }
//
//    public void setTag983$a(String tag983$a) {
//        this.tag983$a = tag983$a;
//    }
//
//    public String getTag983$b() {
//        return this.tag983$b;
//    }
//
//    public void setTag983$b(String tag983$b) {
//        this.tag983$b = tag983$b;
//    }

  public String getTag985$a() {
    return this.tag985$a;
  }

  public void setTag985$a(String tag985$a) {
    this.tag985$a = tag985$a;
  }

//    public String getTag986$a() {
//        return this.tag986$a;
//    }
//
//    public void setTag986$a(String tag986$a) {
//        this.tag986$a = tag986$a;
//    }

  public String getTag987$a() {
    return this.tag987$a;
  }

  public void setTag987$a(String tag987$a) {
    this.tag987$a = tag987$a;
  }

  public String getTag990$b() {
    return this.tag990$b;
  }

  public void setTag990$b(String tag990$b) {
    this.tag990$b = tag990$b;
  }

  public String getTag990$c() {
    return this.tag990$c;
  }

  public void setTag990$c(String tag990$c) {
    this.tag990$c = tag990$c;
  }

  public String getTag990$d() {
    return this.tag990$d;
  }

  public void setTag990$d(String tag990$d) {
    this.tag990$d = tag990$d;
  }

  public String getTag990$e() {
    return this.tag990$e;
  }

  public void setTag990$e(String tag990$e) {
    this.tag990$e = tag990$e;
  }

  public String getTag990$i() {
    return this.tag990$i;
  }

  public void setTag990$i(String tag990$i) {
    this.tag990$i = tag990$i;
  }

  public String getTag990$j() {
    return this.tag990$j;
  }

  public void setTag990$j(String tag990$j) {
    this.tag990$j = tag990$j;
  }

  public String getTag990$u() {
    return this.tag990$u;
  }

  public void setTag990$u(String tag990$u) {
    this.tag990$u = tag990$u;
  }

  public String getTag990$v() {
    return this.tag990$v;
  }

  public void setTag990$v(String tag990$v) {
    this.tag990$v = tag990$v;
  }

  public String toString() {
    return new ToStringBuilder(this)
      .append("bibItmNbr", getBibItmNbr())
      .toString();
  }

  public String getTag900$a() {
    return tag900$a;
  }

  public void setTag900$a(String tag900$a) {
    this.tag900$a = tag900$a;
  }

  public String getTag920$a() {
    return tag920$a;
  }

  public void setTag920$a(String tag920$a) {
    this.tag920$a = tag920$a;
  }

  public String getTag920$b() {
    return tag920$b;
  }

  public void setTag920$b(String tag920$b) {
    this.tag920$b = tag920$b;
  }

  public String getTag920$d() {
    return tag920$d;
  }

  public void setTag920$d(String tag920$d) {
    this.tag920$d = tag920$d;
  }

  public String getTag930$a() {
    return tag930$a;
  }

  public void setTag930$a(String tag930$a) {
    this.tag930$a = tag930$a;
  }

  public String getTag937$b() {
    return tag937$b;
  }

  public void setTag937$b(String tag937$b) {
    this.tag937$b = tag937$b;
  }

  public String getTag937$c() {
    return tag937$c;
  }

  public void setTag937$c(String tag937$c) {
    this.tag937$c = tag937$c;
  }

  public String getTag952$a() {
    return tag952$a;
  }

  public void setTag952$a(String tag952$a) {
    this.tag952$a = tag952$a;
  }

  public String getTag980$a() {
    return tag980$a;
  }

  public void setTag980$a(String tag980$a) {
    this.tag980$a = tag980$a;
  }

  public String getTag980$f() {
    return tag980$f;
  }

  public void setTag980$f(String tag980$f) {
    this.tag980$f = tag980$f;
  }

  public String getTag980$g() {
    return tag980$g;
  }

  public void setTag980$g(String tag980$g) {
    this.tag980$g = tag980$g;
  }

  public String getTag980$l() {
    return tag980$l;
  }

  public void setTag980$l(String tag980$l) {
    this.tag980$l = tag980$l;
  }

  public String getTag982$c() {
    return tag982$c;
  }

  public void setTag982$c(String tag982$c) {
    this.tag982$c = tag982$c;
  }

  public String getTag983$a() {
    return tag983$a;
  }

  public void setTag983$a(String tag983$a) {
    this.tag983$a = tag983$a;
  }

  public String getTag983$b() {
    return tag983$b;
  }

  public void setTag983$b(String tag983$b) {
    this.tag983$b = tag983$b;
  }

  public String getTag986$a() {
    return tag986$a;
  }

  public void setTag986$a(String tag986$a) {
    this.tag986$a = tag986$a;
  }

  public String getIdFile() {
    return idFile;
  }

  public void setIdFile(String idFile) {
    this.idFile = idFile;
  }

  public String getIdSpedizione() {
    return idSpedizione;
  }

  public void setIdSpedizione(String idSpedizione) {
    this.idSpedizione = idSpedizione;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((bibItmNbr == null) ? 0 : bibItmNbr.hashCode());
    result = prime * result + ((idFile == null) ? 0 : idFile.hashCode());
    result = prime * result
      + ((idSpedizione == null) ? 0 : idSpedizione.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final SCasSapCache other = (SCasSapCache) obj;
    if (bibItmNbr == null) {
      if (other.bibItmNbr != null)
        return false;
    } else if (!bibItmNbr.equals(other.bibItmNbr))
      return false;
    if (idFile == null) {
      if (other.idFile != null)
        return false;
    } else if (!idFile.equals(other.idFile))
      return false;
    if (idSpedizione == null) {
      if (other.idSpedizione != null)
        return false;
    } else if (!idSpedizione.equals(other.idSpedizione))
      return false;
    return true;
  }

}
