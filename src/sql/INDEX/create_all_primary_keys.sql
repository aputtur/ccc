ALTER TABLE CCC_USER
    ADD CONSTRAINT PK_CCC_USER PRIMARY KEY (USER_ID)
    USING INDEX
TABLESPACE CC_INDEX
;

ALTER TABLE CCC_USER_REQUEST_CLIENT ADD 
	CONSTRAINT CCC_USER_REQUEST_CLIENT_PK PRIMARY KEY (USER_REQUEST_CLIENT_ID)
	USING INDEX 
TABLESPACE CC_INDEX
;

ALTER TABLE CCC_RLINK_PUBLISHER
    ADD CONSTRAINT PK_RLINK_PUB_ID PRIMARY KEY (RLINK_PUB_ID)
	USING INDEX 
TABLESPACE CC_INDEX
;

ALTER TABLE CCC_RLINK_PUBLISHER_DETAIL
    ADD CONSTRAINT PK_CCC_RLINK_PUBLISHER_DETAIL PRIMARY KEY (RLINK_PUB_ID, PUB_PTY_INST)
    USING INDEX
TABLESPACE CC_INDEX
;