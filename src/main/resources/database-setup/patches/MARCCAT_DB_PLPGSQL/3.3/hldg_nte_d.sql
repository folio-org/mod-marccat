DROP TRIGGER IF EXISTS hldg_nte_d on AMICUS.hldg_nte;

CREATE TRIGGER hldg_nte_d
AFTER DELETE ON amicus.hldg_nte FOR EACH ROW
EXECUTE PROCEDURE amicus.trigger_fct_hldg_nte_d();