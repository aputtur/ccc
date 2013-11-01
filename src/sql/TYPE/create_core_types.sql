prompt
prompt Re-Creating Object Types...
prompt
set echo on
@../TYPE/return_codes_type.sql
@../TYPE/user_type.sql
@../TYPE/stragg_type.sql
@../TYPE/ccc_create_pub_type.sql
@../TYPE/ccc_create_pub_list_type.sql
@../TYPE/ccc_create_pubs_type.sql
@../TYPE/autodunning_param_type.sql
@../TYPE/autodunning_param_list_type.sql
@../TYPE/autodunning_params_type.sql
@../TYPE/ORDER_PARTNER_XREF_TYPE.sql
@../TYPE/ORDER_PARTNER_XREFS_TYPE.sql

set echo off
