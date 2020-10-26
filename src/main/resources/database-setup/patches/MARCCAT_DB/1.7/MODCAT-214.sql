-- Table: olisuite.authority_model

-- DROP TABLE olisuite.authority_model;

CREATE TABLE olisuite.AUTHORITY_MODEL
(
    id bigint NOT NULL,
    xml_fields text NOT NULL,
    label character varying(196) NOT NULL,
    frbr_type_first smallint,
    frbr_type_second smallint,
    frbr_type_third smallint,
    CONSTRAINT authority_model_pk PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE olisuite.authority_model OWNER to marccat;

GRANT ALL ON TABLE olisuite.authority_model TO marccat;

COMMENT ON TABLE olisuite.authority_model IS 'Contains the authority models.';


-- SEQUENCE: olisuite.authority_model_sequence

-- DROP SEQUENCE olisuite.authority_model_sequence;

CREATE SEQUENCE olisuite.authority_model_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 20;

ALTER SEQUENCE olisuite.authority_model_sequence
    OWNER TO marccat;

GRANT ALL ON SEQUENCE olisuite.authority_model_sequence TO marccat;

ALTER SEQUENCE olisuite.authority_model_sequence RESTART WITH 1;

-- Init authority template
insert into olisuite.AUTHORITY_MODEL(id, xml_fields, label, frbr_type_first, frbr_type_second, frbr_type_third)
values(nextval('olisuite.authority_model_sequence'), '{"id":' || currval('olisuite.authority_model_sequence') || ',"name":"Authority","group":1,"type":"A","leader":{"code":"000","value":"00215nz   2200097n  4500"},"fields":[{"code":"001","mandatory":true,"fieldStatus":"unchanged","fixedField":{"keyNumber":0,"categoryCode":1,"headerTypeCode":11,"code":"001","displayValue":"00000000000","sequenceNumber":0,"attributes":{}},"added":false},{"code":"005","mandatory":true,"fieldStatus":"unchanged","fixedField":{"categoryCode":1,"description":"005 Data/hour of transaction","headerTypeCode":12,"code":"005","displayValue":"20201022105614.","sequenceNumber":0,"attributes":{}},"added":false},{"code":"008","mandatory":true,"fieldStatus":"unchanged","fixedField":{"keyNumber":0,"categoryCode":1,"headerTypeCode":10,"code":"008","displayValue":"201001 n acanaaabn           n aaa     u","sequenceNumber":0,"attributes":{}},"added":false},{"code":"040","mandatory":true,"fieldStatus":"unchanged","variableField":{"keyNumber":0,"categoryCode":1,"headingTypeCode":"1","itemTypeCode":"-1","functionCode":"-1","ind1":" ","ind2":" ","code":"040","displayValue":"\u001faSCA\u001fbeng","subfields":[],"sequenceNumber":0,"skipInFiling":0},"added":false}]}', 'Authority', null, null, null);


-- Table: olisuite.authority_model_item

-- DROP TABLE olisuite.authority_model_item;

CREATE TABLE olisuite.authority_model_item
(
    item bigint NOT NULL,
    model bigint NOT NULL,
    xml_fields text NOT NULL,
    CONSTRAINT authority_model_item_pk PRIMARY KEY (item),
    CONSTRAINT authority_model_item_fk1 FOREIGN KEY (model)
        REFERENCES olisuite.authority_model (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        DEFERRABLE
)
WITH (
    OIDS = FALSE
);

ALTER TABLE olisuite.authority_model_item OWNER to marccat;

GRANT ALL ON TABLE olisuite.authority_model_item TO marccat;

COMMENT ON TABLE olisuite.authority_model_item IS 'Contains the items created from a authority model.';

-- Index: authority_model_item_model_idx

-- DROP INDEX olisuite.authority_model_item_model_idx;

CREATE INDEX authority_model_item_model_idx
    ON olisuite.authority_model_item USING btree
    (model ASC NULLS LAST);