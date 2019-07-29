CREATE or replace FUNCTION amicus.trigger_fct_hldg_nte_d() RETURNS trigger
AS $BODY$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_hldg_nte_d
   PURPOSE: 
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.1    07/11/2017  Mirko Fonzo      First release Postgres compliant.
   1.2    05/07/2018  Mirko Fonzo      - FIXING: trigger compatibility updated for 
                                         trigger_ft function v. 1.2.
                                       - FIXING: call to trigger_ft executed via "perform".
   1.3    01/04/2019  Mirko Fonzo      - FIXING: modifications introduced to let this
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
  v_bib_itm_nbr bigint;
  ft_zones text[];
  bib_itm_cur
    CURSOR (c_hdg_nbr bigint) 
    FOR SELECT bib_itm_nbr 
        FROM CPY_ID
        WHERE CPY_ID_NBR = c_hdg_nbr;
BEGIN
  v_hdg_nbr := OLD.hldg_nbr;
  ft_zones[1] := 'CPYNTE';
  begin 
    OPEN bib_itm_cur (v_hdg_nbr);    
      LOOP
        FETCH bib_itm_cur INTO v_bib_itm_nbr;        
        IF NOT FOUND THEN        
          EXIT;
        END IF;
        perform trigger_ft(v_bib_itm_nbr, null, ft_zones);
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
$BODY$
LANGUAGE 'plpgsql' SECURITY DEFINER;