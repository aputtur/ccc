
whenever sqlerror continue;

drop index CCC_USER_USER_IDENTIFIER_X1;

create index CCC_USER_LOWER_USR_IDENT_IX on CCC_USER (LOWER(USER_IDENTIFIER))
TABLESPACE CC_INDEX;

exit;

