--
-- create_core_package_bodies
--
-- parent script that creates the core (non-test) package
-- bodies
--
prompt
prompt Re-Creating Package Bodies...
prompt

prompt re-creating ccc_user_pkg_body
@'../PACKAGE BODY/ccc_user_pkg_body.sql'
show errors

prompt re-creating admin_util_pkg_body
@'../PACKAGE BODY/admin_util_pkg_body.sql'
show errors

prompt re-creating ccc_rlink_publisher_pkg_body
@'../PACKAGE BODY/ccc_rlink_publisher_pkg_body.sql'
show errors

prompt re-creating ccc_autodunning_on_demand_pkg_body
@'../PACKAGE BODY/ccc_autodunning_on_demand_pkg_body.sql'
show errors

prompt re-creating ccc_order_partner_pkg_body
@'../PACKAGE BODY/ccc_order_partner_pkg_body.sql'
show errors