whenever sqlerror exit failure rollback;
whenever oserror exit failure rollback;

define file_to_run=&1
define envkey=&2
define username=&3
define pwd=&&&username._pwd

prompt
prompt Preparing to run the &file_to_run script
prompt


@../build/build_properties_&envkey


@./connect.sql &username&pwd

set serveroutput on size 1000000; 

@&file_to_run
exit;