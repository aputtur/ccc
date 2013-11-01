CREATE OR REPLACE TYPE ccc_autodunning_params_type 
AS OBJECT 
(
    autodunningparams       ccc_autodunningparam_list_type,
    CONSTRUCTOR FUNCTION ccc_autodunning_params_type
      RETURN SELF AS RESULT
);
/



