DROP TRIGGER IF EXISTS ttl_acs_pnt_plus_refs_iu on AMICUS.ttl_acs_pnt_plus_refs;

CREATE TRIGGER ttl_acs_pnt_plus_refs_iu
AFTER INSERT OR UPDATE ON amicus.ttl_acs_pnt_plus_refs FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_ttl_acs_pnt_plus_refs_iu();