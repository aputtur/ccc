
whenever sqlerror continue;



--statements needed for this release
-- go BELOW

ALTER TABLE CCC_USER ADD (
    IS_ANONYMOUS CHAR(1 BYTE) DEFAULT 'Y' NOT NULL,
    SP_ALL CHAR(1 BYTE) DEFAULT 'Y' NOT NULL,
    SP_PHOTOCOPY_ACADEMIC CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_PHOTOCOPY_GENERAL CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_COURSEPACK CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_DIGITAL CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_REPUBLISH CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_BUSINESS_PHOTOCOPY CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_BUSINESS_DIGITAL_INTERNAL CHAR(1 BYTE) DEFAULT 'N' NOT NULL
);

COMMIT;

-- the following is always executed during an incremental build
@./recreate_cc_logic_only_ddl.sql

--statements needed for this release
-- go ABOVE


exit;

