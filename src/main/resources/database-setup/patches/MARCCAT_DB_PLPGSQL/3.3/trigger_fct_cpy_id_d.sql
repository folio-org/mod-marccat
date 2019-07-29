CREATE OR REPLACE FUNCTION amicus.trigger_fct_cpy_id_d() RETURNS trigger AS 
$BODY$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_cpy_id_d
   PURPOSE: 
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.0    29/03/2019  Mirko Fonzo      Updates Full Text Index on CPY_ID table delete.
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
  ft_zones[1] := 'CPYNTE';
       
  begin 
    perform trigger_ft(v_bib_itm_nbr, null, ft_zones);
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

DROP TRIGGER IF EXISTS cpy_id_d on AMICUS.cpy_id;

CREATE TRIGGER cpy_id_d
AFTER DELETE ON AMICUS.cpy_id FOR EACH ROW
EXECUTE PROCEDURE AMICUS.trigger_fct_cpy_id_d();
