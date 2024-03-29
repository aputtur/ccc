create or replace TYPE User_type AS OBJECT
(
    USER_ID NUMBER(38, 0),
    USER_TYPE VARCHAR2(15),
    PARTY_ID NUMBER(38, 0),
    USER_IDENTIFIER VARCHAR2(100),
    AU_ID VARCHAR2 (40),
    RL_SESSION_ID VARCHAR2 (40),
    LAST_SESSION_START DATE,
    LAST_CART_ID NUMBER (38, 0),
    OID_PK VARCHAR2(300),
    ALWAYS_INVOICE CHAR(1 BYTE),
    SP_PHOTOCOPY_ACADEMIC CHAR(1 BYTE),
    SP_PHOTOCOPY_GENERAL CHAR(1 BYTE),
    SP_COURSEPACK CHAR(1 BYTE),
    SP_DIGITAL CHAR(1 BYTE),
    SP_REPUBLISH CHAR(1 BYTE),
    SP_BUSINESS_PHOTOCOPY CHAR(1 BYTE),
    SP_BUSINESS_DIGITAL_INTERNAL CHAR(1 BYTE),
    SP_ACADEMIC_LICENSE CHAR(1 BYTE),
	SP_RIGHTSLINK_REPRINT CHAR(1 BYTE),
	SP_RIGHTSLINK_DIGITAL CHAR(1 BYTE),
	SP_SHARE_ILL CHAR(1 BYTE),
    VERSION NUMBER(38,0),
    REQUEST_CLIENT_VERSION NUMBER(38,0),
    CRE_UPD_USER NUMBER(38,0),
    CRE_UPD_DTM DATE,
    SKIP_QUICKPRICE CHAR(1 BYTE),
    AUTO_ACCEPT_SPORDER CHAR(1 BYTE),
    ENFORCE_PWD_CHG CHAR(1 BYTE)
);
/