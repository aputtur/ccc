-- Copyright.com changes required for Summit May 2010 Milestone

alter table CCC_USER_REQUEST_CLIENT drop column SP_ALL;

alter table CCC_USER_REQUEST_CLIENT add (
    SP_RIGHTSLINK_REPRINT CHAR(1 BYTE) DEFAULT 'N' NOT NULL,
    SP_RIGHTSLINK_DIGITAL CHAR(1 BYTE) DEFAULT 'N' NOT NULL
);

