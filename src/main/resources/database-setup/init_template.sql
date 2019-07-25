DO $$
-- Creates a sample template in BIBLIOGRAPHIC_MODEL
DECLARE
  v_cnt int;
BEGIN
  select count(*) into v_cnt from olisuite.BIBLIOGRAPHIC_MODEL;
  if v_cnt = 0
  then
    ALTER SEQUENCE olisuite.bibliographic_model_sequence RESTART WITH 1;
    insert into olisuite.BIBLIOGRAPHIC_MODEL(id, xml_fields, label, frbr_type_first)
    values(nextval('olisuite.bibliographic_model_sequence'), '{"id":' || currval('olisuite.bibliographic_model_sequence') || ',"fields":[{"code":"001","mandatory":true,"fieldStatus":"unchanged","fixedField":{"keyNumber":0,"categoryCode":1,"headerTypeCode":39,"code":"001","displayValue":"00006590527","sequenceNumber":0,"attributes":{}},"added":false},{"code":"005","mandatory":true,"fieldStatus":"unchanged","fixedField":{"categoryCode":1,"description":"005 Data/ora di transazione","headerTypeCode":41,"code":"005","displayValue":"20190705105614.","sequenceNumber":0,"attributes":{}},"added":false},{"code":"008","mandatory":true,"fieldStatus":"unchanged","fixedField":{"keyNumber":285348,"categoryCode":1,"headerTypeCode":31,"code":"008","displayValue":"910906s1971    it     e      000 0 ita c","sequenceNumber":0,"attributes":{}},"added":false},{"code":"040","mandatory":true,"fieldStatus":"unchanged","variableField":{"keyNumber":0,"categoryCode":1,"headingTypeCode":"1","itemTypeCode":"-1","functionCode":"-1","ind1":" ","ind2":" ","code":"040","displayValue":"\u001FaItFiC","subfields":[],"sequenceNumber":0,"skipInFiling":0},"added":false}]}', 'Monografia', null);
  end if;
END
$$;