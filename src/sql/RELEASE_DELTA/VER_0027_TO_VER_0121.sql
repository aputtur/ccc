
whenever sqlerror continue;



--statements needed for this release
-- go BELOW


CREATE TABLE CCC_RLINK_PUBLISHER(
    RLINK_PUB_ID                NUMBER (15, 0) NOT NULL,
    PUB_PTY_INST                NUMBER (15, 0) NOT NULL,
    ACCOUNT_NUM                 NUMBER (15, 0) NOT NULL,
    WRK_INST                    NUMBER (15, 0),
    PUB_NAME                    VARCHAR2 (80),
    PUB_URL                     VARCHAR2 (1000),
    PERM_OPTION_DESC            VARCHAR2 (4000),
    LEARN_MORE_DESC             VARCHAR2 (4000),
    CRE_USER                    VARCHAR2 (30),
    CRE_DTM                     TIMESTAMP (6),
    UPD_USER                    VARCHAR2 (30),
    UPD_DTM                     TIMESTAMP (6)
)
TABLESPACE CC_DATA;

ALTER TABLE CCC_RLINK_PUBLISHER
    ADD CONSTRAINT PK_RLINK_PUB_ID PRIMARY KEY (RLINK_PUB_ID)
	USING INDEX 
TABLESPACE CC_INDEX
;

CREATE UNIQUE INDEX CCC_RLINK_PUB_PTY_INST_IDX ON CCC_RLINK_PUBLISHER(PUB_PTY_INST)
TABLESPACE  cc_index
;

CREATE TABLE CCC_RLINK_PUBLISHER_DETAIL(
    RLINK_PUB_ID                NUMBER (15, 0) NOT NULL,
    PUB_PTY_INST                NUMBER (15, 0) NOT NULL,
    SUB_ACCOUNT_NUM             NUMBER (15, 0) NOT NULL,
    WRK_INST                    NUMBER (15, 0),
    CRE_USER                    VARCHAR2 (30),
    CRE_DTM                     TIMESTAMP (6),
    UPD_USER                    VARCHAR2 (30),
    UPD_DTM                     TIMESTAMP (6)
)
TABLESPACE CC_DATA;

ALTER TABLE CCC_RLINK_PUBLISHER_DETAIL
    ADD CONSTRAINT PK_CCC_RLINK_PUBLISHER_DETAIL PRIMARY KEY (RLINK_PUB_ID, PUB_PTY_INST)
    USING INDEX
TABLESPACE CC_INDEX
;

CREATE UNIQUE INDEX CCC_RLINK_DET_PUB_PTY_INST_IDX ON CCC_RLINK_PUBLISHER_DETAIL(PUB_PTY_INST)
TABLESPACE  cc_index
;

CREATE UNIQUE INDEX CCC_USER_LOWER_USR_IDENT_IX2 ON CCC_USER ("USER_IDENTIFIER" ASC)
TABLESPACE  cc_index
;

CREATE SEQUENCE CCC_RLINK_PUB_ID_SEQ
start with 1000;

CREATE OR REPLACE 
TYPE ccc_pub_type AS OBJECT (
    rlink_pub_id        NUMBER(15,0),
    pub_pty_inst		NUMBER(15,0), 
    wrk_inst			NUMBER(15,0),
    account_num         NUMBER(15,0),
    pub_name			VARCHAR2(80),
    pub_url			    VARCHAR2(1000),
    perm_option_desc    VARCHAR2(4000),
    learn_more_desc	    VARCHAR2(4000),
    cre_user			VARCHAR2(30),
    cre_dtm			    DATE,
    upd_user			VARCHAR2(30),
    upd_dtm			    DATE
);

CREATE OR REPLACE 
TYPE ccc_pub_list_type As table of ccc_pub_type;


CREATE OR REPLACE 
TYPE CCC_PUBS_TYPE 
AS OBJECT 
(
    publishers       ccc_pub_list_type,
    CONSTRUCTOR FUNCTION ccc_pubs_type
      RETURN SELF AS RESULT
);

--grant execute on ccc_pubs_type to cc_app_role
--/

CREATE OR REPLACE 
TYPE BODY ccc_pubs_type
AS
   CONSTRUCTOR FUNCTION ccc_pubs_type
      RETURN SELF AS RESULT
   IS
      l_pub_list   ccc_pub_list_type := ccc_pub_list_type();
   BEGIN
      SELF.publishers := l_pub_list;
      RETURN;
   END;
END;









--statements needed for this release
-- go ABOVE


exit;

