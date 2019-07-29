DROP TRIGGER IF EXISTS sbjct_acs_pnt_plus_refs_d on AMICUS.sbjct_acs_pnt_plus_refs;

CREATE TRIGGER sbjct_acs_pnt_plus_refs_d
AFTER DELETE ON amicus.sbjct_acs_pnt_plus_refs FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_sbjct_acs_pnt_plus_refs_d();