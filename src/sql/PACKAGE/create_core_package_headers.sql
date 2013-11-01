--
-- create_core_package_headers
--
-- parent script that creates the core (non-test) package
-- headers
--
prompt
prompt Re-Creating Package Headers...
prompt

@../PACKAGE/return_code_constants_pkg.sql
show errors
@../PACKAGE/ccc_user_pkg.sql
show errors
@../PACKAGE/admin_util_pkg.sql
show errors
@../PACKAGE/ccc_rlink_publisher_pkg.sql
show errors
@../PACKAGE/ccc_autodunning_on_demand_pkg.sql
show errors
@../PACKAGE/ccc_order_partner_pkg.sql
show errors