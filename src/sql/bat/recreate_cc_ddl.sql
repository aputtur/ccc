set serveroutput on size 1000000; 

@../PACKAGE/db_util_pkg.sql 
@'../PACKAGE BODY/db_util_body.sql' 

execute db_util.drop_user_objects; 

@../SYNONYM/create_all_synonyms.sql

@../TABLE/create_all_tables.sql
@../INDEX/create_all_primary_keys.sql
@../INDEX/create_all_indexes.sql
@../TABLE/create_all_foreign_keys.sql
@../SEQUENCE/create_all_sequences.sql

@../TYPE/create_core_types.sql 
@'../TYPE BODY/create_core_type_bodies.sql' 
@../FUNCTION/create_all_functions.sql 
@../PROCEDURE/create_all_procedures.sql 

@../PACKAGE/create_core_package_headers.sql
@'../PACKAGE BODY/create_core_package_bodies.sql'


-- privs
@../GRANT/grants_to_others.sql



