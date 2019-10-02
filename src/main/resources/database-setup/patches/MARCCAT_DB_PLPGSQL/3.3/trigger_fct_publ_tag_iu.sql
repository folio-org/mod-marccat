CREATE OR REPLACE FUNCTION amicus.trigger_fct_publ_tag_iu() RETURNS trigger
LANGUAGE plpgsql SECURITY DEFINER
AS $$

/*****************************************************************************************
   NAME: AMICUS.trigger_fct_publ_tag_iu
   PURPOSE:
   REVISIONS:
   Ver    Date        Author           Description
   -----  ----------  ---------------  ---------------------------------------------------
   1.0    29/03/2019  Mirko Fonzo      Updates Full Text Index on PUBL_TAG table
                                       insert/update.
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
        from publ_acs_pnt
        where publ_tag_nbr = c_hdg_nbr
          and usr_vw_ind = c_usr_vw_ind;
BEGIN
  v_hdg_nbr := NEW.publ_tag_nbr;
  v_usr_vw_ind := NEW.usr_vw_ind;
  ft_zones[1] := 'PUBL';

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
RETURN NEW;
END
$$;

DROP TRIGGER IF EXISTS publ_tag_iu on AMICUS.publ_tag;

CREATE TRIGGER publ_tag_iu
AFTER INSERT OR UPDATE ON AMICUS.publ_tag FOR EACH ROW
EXECUTE PROCEDURE AMICUS.trigger_fct_publ_tag_iu();
