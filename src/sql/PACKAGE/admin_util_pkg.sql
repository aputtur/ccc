CREATE OR REPLACE PACKAGE admin_util_pkg
AS
 
 PROCEDURE get_schema_version( p_ret_code OUT return_codes_type, schema_version OUT VARCHAR );

END;
/
