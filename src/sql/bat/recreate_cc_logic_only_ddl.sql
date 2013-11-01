set serveroutput on size 1000000; 

@../PACKAGE/db_util_pkg.sql 
@'../PACKAGE BODY/db_util_body.sql' 

prompt ***** 
prompt ***** drop all existing logic objects
prompt ***** 
execute db_util.drop_user_logic_objects; 

--@../SYNONYM/create_all_synonyms.sql

@../TYPE/create_core_types.sql 
@'../TYPE BODY/create_core_type_bodies.sql' 
@../FUNCTION/create_all_functions.sql 
--@../PROCEDURE/create_all_procedures.sql 

@../PACKAGE/create_core_package_headers.sql
@'../PACKAGE BODY/create_core_package_bodies.sql'


-- privs
@../GRANT/grants_to_others.sql



