DROP TRIGGER IF EXISTS clstn_itm_acs_pnt_d on AMICUS.clstn_itm_acs_pnt;

CREATE TRIGGER clstn_itm_acs_pnt_d
AFTER DELETE ON amicus.clstn_itm_acs_pnt FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_clstn_itm_acs_pnt_d();