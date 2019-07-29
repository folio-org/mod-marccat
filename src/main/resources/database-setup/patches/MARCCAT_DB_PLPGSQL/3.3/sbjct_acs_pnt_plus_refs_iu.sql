DROP TRIGGER IF EXISTS sbjct_acs_pnt_plus_refs_iu on AMICUS.sbjct_acs_pnt_plus_refs;

CREATE TRIGGER sbjct_acs_pnt_plus_refs_iu
AFTER INSERT OR UPDATE ON amicus.sbjct_acs_pnt_plus_refs FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_sbjct_acs_pnt_plus_refs_iu();