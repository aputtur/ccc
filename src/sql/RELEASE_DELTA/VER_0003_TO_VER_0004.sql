
whenever sqlerror continue;

CREATE SEQUENCE  CCC_USER_REQUEST_CLIENT_SEQ  
MINVALUE 1000000 MAXVALUE 999999999999 INCREMENT BY 1;

--
-- Create new USER_REQUEST_CLIENT table
--
--
CREATE TABLE CCC_USER_REQUEST_CLIENT(
    USER_REQUEST_CLIENT_ID	NUMBER(38, 0) NOT NULL,
    USER_ID			NUMBER(38, 0) NOT NULL,
    AU_ID			VARCHAR2(40) NOT NULL,
    LAST_SESSION_START		TIMESTAMP(6)  DEFAULT SYSDATE NOT NULL,
    LAST_CART_ID		NUMBER(38, 0),
    ALWAYS_INVOICE		CHAR(1) DEFAULT 'N' NOT NULL,
    IS_ANONYMOUS		CHAR(1) DEFAULT 'Y' NOT NULL,
    SP_ALL			CHAR(1) DEFAULT 'Y' NOT NULL,
    SP_PHOTOCOPY_ACADEMIC       CHAR(1) DEFAULT 'N' NOT NULL,
    SP_PHOTOCOPY_GENERAL        CHAR(1) DEFAULT 'N' NOT NULL,
    SP_COURSEPACK               CHAR(1) DEFAULT 'N' NOT NULL,
    SP_DIGITAL                  CHAR(1) DEFAULT 'N' NOT NULL,
    SP_REPUBLISH                CHAR(1) DEFAULT 'N' NOT NULL,
    SP_BUSINESS_PHOTOCOPY       CHAR(1) DEFAULT 'N' NOT NULL,
    SP_BUSINESS_DIGITAL_INTERNAL CHAR(1) DEFAULT 'N' NOT NULL,
    SP_ACADEMIC_LICENSE         CHAR(1) DEFAULT 'N' NOT NULL,
    VERSION			NUMBER(38, 0) NOT NULL,
    CRE_USER			NUMBER(38, 0) NOT NULL,
    CRE_DTM			TIMESTAMP(6)  NOT NULL,
    UPD_USER			NUMBER(38, 0),
    UPD_DTM			TIMESTAMP(6)
)
TABLESPACE CC_DATA;

ALTER TABLE CCC_USER_REQUEST_CLIENT ADD 
	CONSTRAINT CCC_USER_REQUEST_CLIENT_PK PRIMARY KEY (USER_REQUEST_CLIENT_ID)
	USING INDEX 
TABLESPACE CC_INDEX
;

ALTER TABLE CCC_USER_REQUEST_CLIENT ADD CONSTRAINT FK_USER_REQUEST_CLIENT_USER 
    FOREIGN KEY (USER_ID)
    REFERENCES CCC_USER(USER_ID)
;

INSERT INTO CCC_USER_REQUEST_CLIENT
 (
    USER_REQUEST_CLIENT_ID,
    USER_ID,
    AU_ID,
    LAST_SESSION_START,
    LAST_CART_ID,
    ALWAYS_INVOICE,
    IS_ANONYMOUS,
    SP_ALL,
    SP_PHOTOCOPY_ACADEMIC,
    SP_PHOTOCOPY_GENERAL,
    SP_COURSEPACK,
    SP_DIGITAL,
    SP_REPUBLISH,
    SP_BUSINESS_PHOTOCOPY,
    SP_BUSINESS_DIGITAL_INTERNAL,
    SP_ACADEMIC_LICENSE,
    VERSION,
    CRE_USER,
    CRE_DTM,
    UPD_USER, 
    UPD_DTM) 
(SELECT CCC_USER_REQUEST_CLIENT_SEQ.NEXTVAL,
    USER_ID,
    AU_ID,
    LAST_SESSION_START,
    LAST_CART_ID,
    ALWAYS_INVOICE,
    IS_ANONYMOUS,
    SP_ALL,
    SP_PHOTOCOPY_ACADEMIC,
    SP_PHOTOCOPY_GENERAL,
    SP_COURSEPACK,
    SP_DIGITAL,
    SP_REPUBLISH,
    SP_BUSINESS_PHOTOCOPY,
    SP_BUSINESS_DIGITAL_INTERNAL,
    'N',
    VERSION,
    CRE_USER,
    CRE_DTM,
    UPD_USER,
    UPD_DTM
FROM CCC_USER);

ALTER TABLE CCC_USER DROP (AU_ID, LAST_SESSION_START, LAST_CART_ID,
    ALWAYS_INVOICE, IS_ANONYMOUS, SP_ALL, SP_PHOTOCOPY_ACADEMIC, 
    SP_PHOTOCOPY_GENERAL, SP_COURSEPACK, SP_DIGITAL, SP_REPUBLISH,
    SP_BUSINESS_PHOTOCOPY, SP_BUSINESS_DIGITAL_INTERNAL, SP_ACADEMIC_LICENSE);		

CREATE UNIQUE INDEX CCC_USER_REQ_AU_ID_X1 ON CCC_USER_REQUEST_CLIENT(AU_ID)
TABLESPACE CC_INDEX
;

CREATE UNIQUE INDEX CCC_USER_REQ_USER_AU_ID_X1 ON CCC_USER_REQUEST_CLIENT(USER_ID,AU_ID)
TABLESPACE CC_INDEX
;

CREATE UNIQUE INDEX CCC_USER_USER_IDENTIFIER_X1 ON CCC_USER(USER_IDENTIFIER)
TABLESPACE CC_INDEX
;


exit;

