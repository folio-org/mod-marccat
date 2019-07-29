DROP TRIGGER IF EXISTS clstn_itm_acs_pnt_iu on AMICUS.clstn_itm_acs_pnt;

CREATE TRIGGER clstn_itm_acs_pnt_iu
AFTER INSERT OR UPDATE ON amicus.clstn_itm_acs_pnt FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_clstn_itm_acs_pnt_iu();