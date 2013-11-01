/*
 * grants_to_others
 *
 * This script defines the privileges that cc grants on it's
 * objects to other users and/or roles
 *
 * The cc_user user is the database account through which the copyright.com app
 * will access this schema.
 *
 * The cc_app_role role is granted to the cc_user user. All privs
 * that cc_user needs should be provided through the cc_app_role
 */
grant execute on ccc_user_pkg to cc_app_role;
grant execute on return_codes_type to cc_app_role;
grant execute on user_type to cc_app_role;
grant execute on admin_util_pkg to cc_app_role;
grant execute on ccc_pub_type to cc_app_role;
grant execute on ccc_pubs_type to cc_app_role;
grant execute on ccc_pub_list_type to cc_app_role;
grant all on ccc_rlink_publisher to cc_app_role;
grant all on ccc_rlink_publisher_detail to cc_app_role;
grant execute on ccc_rlink_publisher_pkg to cc_app_role;
grant execute on ccc_autodunning_param_type to cc_app_role;
grant execute on ccc_autodunningparam_list_type to cc_app_role;
grant execute on ccc_autodunning_params_type to cc_app_role;
grant execute on ccc_autodunning_on_demand_pkg to cc_app_role;
grant execute on ccc_order_partner_pkg to cc_app_role;
GRANT EXECUTE ON ORDER_PARTNER_XREF_TYPE TO CC_APP_ROLE;
GRANT EXECUTE ON ORDER_PARTNER_XREFS_TYPE TO CC_APP_ROLE;