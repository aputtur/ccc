CREATE OR REPLACE PACKAGE BODY admin_util_pkg AS


PROCEDURE get_schema_version ( 
	p_ret_code OUT return_codes_type, 
	schema_version OUT VARCHAR )
	
	IS
	PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

	schema_version := what_schema_version;
	
	p_ret_code := NEW return_codes_type( 
        return_code_constants.CODE_SUCCESS, 
        return_code_constants.SUCCESS, 
        SQLCODE, 
        SQLERRM, 
        SQL%ROWCOUNT, 
        'get_schema_version', 
        'read' );
 			
	COMMIT;

EXCEPTION

    WHEN OTHERS THEN
    
		schema_version := NULL;

		p_ret_code := NEW return_codes_type( 
			return_code_constants.CODE_FAILURE, 
			return_code_constants.FAILURE_ORACLE_ERROR, 
			SQLCODE, 
			SQLERRM, 
			SQL%ROWCOUNT, 
			'get_schema_version', 
			'read' );

		ROLLBACK;

END get_schema_version;


END admin_util_pkg;
/
