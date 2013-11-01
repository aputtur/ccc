CREATE OR REPLACE TYPE ccc_autodunning_params_type 
AS OBJECT 
(
    autodunningparams       ccc_autodunningparam_list_type,
    CONSTRUCTOR FUNCTION ccc_autodunning_params_type
      RETURN SELF AS RESULT
);
/


--DROP TYPE BODY CCC_AUTODUNNING_PARAMS_TYPE;

CREATE OR REPLACE TYPE BODY ccc_autodunning_params_type
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
/
