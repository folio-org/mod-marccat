package org.folio.marccat.integration.search;


/**
 * Enumeration of all SQL Commands
 *
 * @author Carment
 * @since 1.0
 */
public class SQLCommand {
	
	private SQLCommand() {
    // empty constructor because throw error
	}

  /**
   * build the query for the join of the title for the bibliographic
   */
  public final static String TITLE_JOIN = new StringBuilder()
    .append(" join ttl_acs_pnt t1 on t1.bib_itm_nbr = smtc.bib_itm_nbr ")
    .append(" join ttl_hdg t2 on t2.ttl_hdg_nbr = t1.ttl_hdg_nbr and t2.usr_vw_ind = t1.usr_vw_ind ")
    .append(" where  t1.ttl_fnctn_cde = 1 ").toString();

  /**
   * build the query for the join of the uniform title for the bibliographic
   */
  public final static String UNIFORM_TITLE_JOIN = new StringBuilder()
    .append(" join ttl_acs_pnt t1 on t1.bib_itm_nbr = smtc.bib_itm_nbr ")
    .append(" join ttl_hdg t2 on t2.ttl_hdg_nbr = t1.ttl_hdg_nbr and t2.usr_vw_ind = t1.usr_vw_ind ")
    .append(" where t1.ttl_fnctn_cde = 2 or t1.ttl_fnctn_cde = 48 ").toString();

  /**
   * build the query for the join of the name for the bibliographic
   */
  public final static String NAME_JOIN = new StringBuilder()
    .append(" join nme_acs_pnt t1 on t1.bib_itm_nbr = smtc.bib_itm_nbr ")
    .append(" join nme_hdg t2 on t2.nme_hdg_nbr = t1.nme_hdg_nbr and t2.usr_vw_ind = t1.usr_vw_ind ")
    .append(" where t1.nme_fnctn_cde not in (7,8) ").toString();

  /**
   * build the query for the join of the subject for the bibliographic
   */
  public final static String SUBJECT_JOIN = new StringBuilder()
    .append(" join sbjct_acs_pnt t1 on t1.bib_itm_nbr = smtc.bib_itm_nbr ")
    .append(" join sbjct_hdg t2 on t2.sbjct_hdg_nbr = t1.sbjct_hdg_nbr and t2.usr_vw_ind = t1.usr_vw_ind ").toString();

  /**
   * build the query for the join of the date1 or date2 for the bibliographic
   */
  public final static String DATE_JOIN = new StringBuilder()
    .append(" join bib_itm t1 on t1.bib_itm_nbr = smtc.bib_itm_nbr ").toString();

  /**
   * build the query for the join of the title for the authority
   */
  public final static String TITLE_AUT_JOIN = new StringBuilder()
    .append(" join aut t1 on t1.aut_nbr = smtc.aut_nbr ")
    .append(" join ttl_hdg t2 on t2.ttl_hdg_nbr = t1.hdg_nbr ").toString();

  /**
   * build the query for the join of the name for the authority
   */
  public final static String NME_AUT_JOIN = new StringBuilder()
    .append(" join aut t1 on t1.aut_nbr = smtc.aut_nbr ")
    .append(" join nme_hdg t2 on t2.nme_hdg_nbr = t1.hdg_nbr ").toString();

  /**
   * build the query for the join of the subject for the authority
   */
  public final static String SUBJECT_AUT_JOIN = new StringBuilder()
    .append(" join aut t1 on t1.aut_nbr = smtc.aut_nbr ")
    .append(" join sbjct_hdg t2 on t2.sbjct_hdg_nbr = t1.hdg_nbr ").toString();


}
