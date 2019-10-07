CREATE OR REPLACE FUNCTION amicus.trigger_fct_publ_acs_pnt_iu() RETURNS trigger
AS $$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_publ_acs_pnt_iu
   PURPOSE:
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.0    29/03/2019  Mirko Fonzo      Updates Full Text Index on PUBL_ACS_PNT table
                                       insert/updates.
*****************************************************************************************/

DECLARE
  v_state text;
  v_msg text;
  v_detail text;
  v_hint text;
  v_context text;
  v_operation text;
  v_bib_itm_nbr bigint;
  v_usr_vw_ind char(16);
  ft_zones text[];
BEGIN
  v_bib_itm_nbr := NEW.bib_itm_nbr;
  v_usr_vw_ind := NEW.usr_vw_ind;
  ft_zones[1] := 'PUBL';

  begin
    perform trigger_ft(v_bib_itm_nbr, v_usr_vw_ind, ft_zones);
  exception
  when others
  then
    GET STACKED DIAGNOSTICS
    v_state = RETURNED_SQLSTATE,
    v_msg = MESSAGE_TEXT,
    v_detail = PG_EXCEPTION_DETAIL,
    v_hint = PG_EXCEPTION_HINT,
    v_context = PG_EXCEPTION_CONTEXT;
    v_operation := 'trigger_ft - ' || ' [' || coalesce(v_state, '') || '] [' || coalesce(v_msg, '') || '] [' ||
                   coalesce(v_detail, '') || '] [' || coalesce(v_hint, '') || '] [' || coalesce(v_context, '') || ']';
    insert into FT_DATA_LOG(operation, log_lvl, dte) values(v_operation, 'ERROR', clock_timestamp());
  end;
RETURN NEW;
END
$$ LANGUAGE 'plpgsql' SECURITY DEFINER;

DROP TRIGGER IF EXISTS publ_acs_pnt_iu on AMICUS.publ_acs_pnt;

CREATE TRIGGER publ_acs_pnt_iu
AFTER INSERT OR UPDATE ON AMICUS.publ_acs_pnt FOR EACH ROW
EXECUTE PROCEDURE AMICUS.trigger_fct_publ_acs_pnt_iu();
