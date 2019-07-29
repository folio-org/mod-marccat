DO $$
-- Checks if database patch is already installed
DECLARE
  v_cnt int;
  check_msg1 text;
  check_msg2 text;
BEGIN
  check_msg1 := 'Check MARCCAT DB PLPGSQL version: ' || current_setting('myvars.patch_rel_nbr') || '.' || current_setting('myvars.patch_sp_nbr') || ' found.';
  check_msg2 := 'Check MARCCAT DB PLPGSQL version: ' || current_setting('myvars.patch_rel_nbr') || '.' || current_setting('myvars.patch_sp_nbr') || ' not found.';
  select count(*) into v_cnt
  from olisuite.s_patch_history
  where release_number = current_setting('myvars.patch_rel_nbr')::bigint and service_pack_number = current_setting('myvars.patch_sp_nbr')::bigint and component_typ = current_setting('myvars.patch_comp_typ')::bigint;

  if v_cnt > 0
  then
    RAISE EXCEPTION '%', check_msg1;
  else 
    RAISE notice '%', check_msg2;
  end if;
END
$$;
