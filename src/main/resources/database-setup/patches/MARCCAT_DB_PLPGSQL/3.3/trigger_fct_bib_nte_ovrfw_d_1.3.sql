CREATE OR REPLACE FUNCTION amicus.trigger_fct_bib_nte_ovrfw_d() RETURNS trigger AS
  AS $$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_bib_nte_ovrfw_d
   PURPOSE:
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.1    06/11/2017  Mirko Fonzo      First release Postgres compliant.
   1.2    05/07/2018  Mirko Fonzo      - FIXING: trigger compatibility updated for
                                         trigger_ft function v. 1.2.
                                       - FIXING: call to trigger_ft executed via "perform".
   1.3    28/03/2019  Mirko Fonzo      - FIXING: modifications introduced to let this
                                                 funcion work by firing the trigger on
                                                 "after delete" event.
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
  v_bib_itm_nbr := OLD.bib_itm_nbr;
  v_usr_vw_ind := OLD.usr_vw_ind;
  ft_zones[1] := 'NTE';

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
RETURN OLD;
END
$$ LANGUAGE 'plpgsql' SECURITY DEFINER;


DROP TRIGGER IF EXISTS bib_nte_ovrfw_d on AMICUS.bib_nte_ovrfw;

CREATE TRIGGER bib_nte_ovrfw_d
AFTER DELETE ON AMICUS.bib_nte_ovrfw FOR EACH ROW
EXECUTE PROCEDURE AMICUS.trigger_fct_bib_nte_ovrfw_d();
