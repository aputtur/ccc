

spool set_username.sql
prompt define usr=&&&1
spool off

@set_username.sql

prompt
prompt Connecting to &usr@&sid
prompt

spool connect.log
connect &usr@&sid
set termout off

select 'connect success'
from dual;

set termout on

spool off

show user
undef 1
undef 2

