CREATE OR REPLACE PACKAGE ccc_autodunning_on_demand_pkg
AS
   TYPE ref_cursor IS REF CURSOR;

   PROCEDURE get_autodunning_params (
      rc_par                          OUT   return_codes_type,
      p_ccc_autodunning_params_type   OUT   ccc_autodunning_params_type
   );
END;
/
