CREATE OR REPLACE TYPE ccc_autodunning_param_type AS OBJECT (
    PRODUCT_TYPE        VARCHAR2(30),
    DAYSPASTDUE         NUMBER(15,0),
    ENABLED             NUMBER(15,0)
);
/
--DROP PUBLIC SYNONYM CCC_AUTODUNNING_PARAM_TYPE;

--CREATE PUBLIC SYNONYM CCC_AUTODUNNING_PARAM_TYPE FOR CC.CCC_AUTODUNNING_PARAM_TYPE;

GRANT EXECUTE ON CCC_AUTODUNNING_PARAM_TYPE TO CC_APP_ROLE;
