CREATE OR REPLACE FUNCTION amicus.trigger_fct_nme_hdg_d() RETURNS trigger
LANGUAGE plpgsql SECURITY DEFINER
 AS $$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_nme_hdg_d
   PURPOSE:
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.1    06/11/2017  Mirko Fonzo      First release Postgres compliant.
   1.2    05/07/2018  Mirko Fonzo      - FIXING: trigger compatibility updated for
                                         trigger_ft function v. 1.2.
                                       - FIXING: call to trigger_ft executed via "perform".
   1.3    29/03/2019  Mirko Fonzo      - FIXING: modifications introduced to let this
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
  v_hdg_nbr bigint NOT NULL DEFAULT 0;
  v_usr_vw_ind char(16);
  v_bib_itm_nbr bigint;
  v_usr_vw_ind_2 char(16);
  ft_zones text[];
  bib_itm_cur
    CURSOR (c_hdg_nbr bigint, c_usr_vw_ind char(16))
    FOR select bib_itm_nbr, usr_vw_ind
        from nme_acs_pnt
        where nme_hdg_nbr = c_hdg_nbr
          and usr_vw_ind = c_usr_vw_ind;
BEGIN
  v_hdg_nbr := OLD.nme_hdg_nbr;
  v_usr_vw_ind := OLD.usr_vw_ind;
  ft_zones[1] := 'NME';

  begin
    OPEN bib_itm_cur (v_hdg_nbr, v_usr_vw_ind);
      LOOP
        FETCH bib_itm_cur INTO v_bib_itm_nbr, v_usr_vw_ind_2;
        IF NOT FOUND THEN
          EXIT;
        END IF;
        perform trigger_ft(v_bib_itm_nbr, v_usr_vw_ind_2, ft_zones);
      END LOOP;
    CLOSE bib_itm_cur;
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
$$;


DROP TRIGGER IF EXISTS nme_hdg_d on AMICUS.nme_hdg;

CREATE TRIGGER nme_hdg_d
AFTER DELETE ON AMICUS.nme_hdg FOR EACH ROW
EXECUTE PROCEDURE AMICUS.trigger_fct_nme_hdg_d();
