
whenever sqlerror continue;



--statements needed for this release
-- go BELOW

CREATE TABLE CC.CCC_AUTODUNNING_PARAMS
(
    PRODUCT_TYPE                VARCHAR2(15 CHAR) NOT NULL,
    DAYS_PAST_DUE               NUMBER(38) NOT NULL,
    ENABLED                     NUMBER(38) DEFAULT 1 NOT NULL
)
TABLESPACE CC_DATA;

insert into CC.CCC_AUTODUNNING_PARAMS (product_type, days_past_due, enabled) values ('RLS',90, 1);
insert into CC.CCC_AUTODUNNING_PARAMS (product_type, days_past_due, enabled) values ('TRS',90, 1);
insert into CC.CCC_AUTODUNNING_PARAMS (product_type, days_past_due, enabled) values ('APS',90, 1);
insert into CC.CCC_AUTODUNNING_PARAMS (product_type, days_past_due, enabled) values ('ECC',90, 1);
insert into CC.CCC_AUTODUNNING_PARAMS (product_type, days_past_due, enabled) values ('DPS',90, 1);

CREATE OR REPLACE TYPE CC.ccc_autodunning_param_type AS OBJECT (
    PRODUCT_TYPE        VARCHAR2(30),
    DAYSPASTDUE         NUMBER(15,0),
    ENABLED             NUMBER(15,0)
);

drop public synonym ccc_autodunning_param_type;

create public synonym ccc_autodunning_param_type for cc.ccc_autodunning_param_type;

grant execute on cc.ccc_autodunning_param_type to cc_app_role;


create or replace type cc.ccc_autodunningparam_list_type as table of ccc_autodunning_param_type;

drop public synonym ccc_autodunningparam_list_type;

create public synonym ccc_autodunningparam_list_type for cc.ccc_autodunningparam_list_type;

grant execute on cc.ccc_autodunningparam_list_type to cc_app_role;


CREATE OR REPLACE TYPE CC.ccc_autodunning_params_type 
AS OBJECT 
(
    autodunningparams       ccc_autodunningparam_list_type,
    CONSTRUCTOR FUNCTION ccc_autodunning_params_type
      RETURN SELF AS RESULT
);


DROP TYPE BODY CC.CCC_AUTODUNNING_PARAMS_TYPE;

CREATE OR REPLACE TYPE BODY CC.ccc_autodunning_params_type
AS

   CONSTRUCTOR FUNCTION ccc_autodunning_params_type
      RETURN SELF AS RESULT
   IS
      l_params_list   ccc_autodunningparam_list_type := ccc_autodunningparam_list_type();
   BEGIN
      SELF.autodunningparams := l_params_list;
      RETURN;
   END;
END;


DROP PUBLIC SYNONYM CCC_AUTODUNNING_PARAMS_TYPE;

CREATE PUBLIC SYNONYM CCC_AUTODUNNING_PARAMS_TYPE FOR CC.CCC_AUTODUNNING_PARAMS_TYPE;

GRANT EXECUTE ON CC.CCC_AUTODUNNING_PARAMS_TYPE TO CC_APP_ROLE;


CREATE TABLE CCC_INV_PMT_LOG (
    LOGGED_ON                   VARCHAR(14) NOT NULL,
    ACCOUNT_NUMBER              VARCHAR(10) NOT NULL,
    RECEIPT_NUMBER              VARCHAR(80) NOT NULL,
    INVOICE_NUMBER              VARCHAR(20) NOT NULL,
    SENT_TO                     VARCHAR(80) NOT NULL,
    SUB_TOTAL_AMOUNT            VARCHAR(10) NOT NULL
)
TABLESPACE CC_DATA;

CREATE INDEX IPL_LOGGED_ON_IDX ON CCC_INV_PMT_LOG(LOGGED_ON)
TABLESPACE  cc_index
;

CREATE INDEX IPL_ACCOUNT_NUMBER_IDX ON CCC_INV_PMT_LOG(ACCOUNT_NUMBER)
TABLESPACE  cc_index
;

CREATE INDEX IPL_RECEIPT_NUMBER ON CCC_INV_PMT_LOG(RECEIPT_NUMBER)
TABLESPACE  cc_index
;

CREATE TYPE CCC_INV_PMT_LOG_TYPE AS OBJECT (
LOGGED_ON VARCHAR(14),
ACCOUNT_NUMBER VARCHAR(10),
RECEIPT_NUMBER VARCHAR(80),
INVOICE_NUMBER VARCHAR(20),
SENT_TO VARCHAR(80),
SUB_TOTAL_AMOUNT VARCHAR(10));
/

CREATE TYPE CCC_INV_PMT_LOG_LIST_TYPE AS TABLE OF CCC_INV_PMT_LOG_TYPE;
/

CREATE OR REPLACE TYPE CCC_INV_PMT_LOGS_TYPE AS OBJECT (
INV_PMT_LOGS CCC_INV_PMT_LOG_LIST_TYPE,
CONSTRUCTOR FUNCTION CCC_INV_PMT_LOGS_TYPE RETURN SELF AS RESULT
);
/

grant execute on ccc_inv_pmt_log_type to cc_user;
grant execute on ccc_inv_pmt_logs_type to cc_user;
grant execute on ccc_inv_pmt_log_list_type to cc_user;





--statements needed for this release
-- go ABOVE


exit;

