-- Start of DDL Script for Type CC.CCC_PUB_TYPE
-- Generated 4/16/2009 11:06:39 AM from CC@CCCD2.COPYRIGHT.COM

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
/

-- Grants for Type
--GRANT DEBUG ON ccc_pub_type TO cc_user
--/
--GRANT EXECUTE ON ccc_pub_type TO cc_app_role
--/


-- End of DDL Script for Type CC.CCC_PUB_TYPE

