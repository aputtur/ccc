whenever sqlerror continue;



--statements needed for this release
-- go BELOW

ALTER TABLE CCC_USER ADD (
    SP_ACADEMIC_LICENSE CHAR(1 BYTE) DEFAULT 'N' NOT NULL
);

COMMIT;

-- the following is always executed during an incremental build
@./recreate_cc_logic_only_ddl.sql

--statements needed for this release
-- go ABOVE


exit;