
whenever sqlerror continue;

-- the following is always executed during an incremental build
@./recreate_cc_logic_only_ddl.sql


exit;

