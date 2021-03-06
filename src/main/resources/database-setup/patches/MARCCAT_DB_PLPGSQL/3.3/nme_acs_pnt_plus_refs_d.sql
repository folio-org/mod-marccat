DROP TRIGGER IF EXISTS nme_acs_pnt_plus_refs_d on AMICUS.nme_acs_pnt_plus_refs;

CREATE TRIGGER nme_acs_pnt_plus_refs_d
AFTER DELETE ON amicus.nme_acs_pnt_plus_refs FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_nme_acs_pnt_plus_refs_d();