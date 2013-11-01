CREATE OR REPLACE PACKAGE BODY CCC_User_pkg AS

PROCEDURE ReadUserForUserIdentifier (
    RC_par OUT Return_Codes_type,
     User_par OUT User_type,
     User_identifier_par IN VARCHAR2)
  IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    User_par := NEW User_type ( 0, '', 0, '', '','', SYSDATE, 0, '', '', '','','','','','','','', '','', '', 0, 0, 0, SYSDATE, 'N', 'N','N' );

    SELECT
      u.USER_ID,
      u.USER_TYPE,
      u.PARTY_ID,
      u.USER_IDENTIFIER,
      u.OID_PK,
      u.VERSION,
      nvl( c.AU_ID, ' ' ),
      nvl( c.RL_SESSION_ID, ' ' ),
      nvl( c.LAST_SESSION_START, SYSDATE ),
      nvl( c.LAST_CART_ID, 0 ),
      nvl( c.ALWAYS_INVOICE, 'N' ),
      nvl( c.SP_PHOTOCOPY_ACADEMIC, 'N' ),
      nvl( c.SP_SHARE_ILL, 'N' ),
      nvl( c.SP_PHOTOCOPY_GENERAL, 'N' ),
      nvl( c.SP_COURSEPACK, 'N' ),
      nvl( c.SP_DIGITAL, 'N' ),
      nvl( c.SP_REPUBLISH, 'N' ),
      nvl( c.SP_BUSINESS_PHOTOCOPY, 'N' ),
      nvl( c.SP_BUSINESS_DIGITAL_INTERNAL, 'N' ),
      nvl( c.SP_ACADEMIC_LICENSE, 'N' ),
      nvl( c.SP_RIGHTSLINK_REPRINT, 'N' ),
      nvl( c.SP_RIGHTSLINK_DIGITAL, 'N' ),
      nvl( c.VERSION, 0 ),
      NVL( c.UPD_USER, nvl( c.CRE_USER, 0 ) ),
      NVL( c.UPD_DTM, nvl( c.CRE_DTM, SYSDATE ) ),
      nvl( c.SKIP_QUICKPRICE, 'N' ),
      nvl( u.AUTO_ACCEPT_SPORDER, 'N' ),
      nvl( u.ENFORCE_PWD_CHG, 'N' )
    INTO
      User_par.USER_ID,
      User_par.USER_TYPE,
      User_par.PARTY_ID,
      User_par.USER_IDENTIFIER,
      User_par.OID_PK,
      User_par.VERSION,
      User_par.AU_ID,
      User_par.RL_SESSION_ID,
      User_par.LAST_SESSION_START,
      User_par.LAST_CART_ID,
      User_par.ALWAYS_INVOICE,
      User_par.SP_PHOTOCOPY_ACADEMIC,
      User_par.SP_SHARE_ILL,
      User_par.SP_PHOTOCOPY_GENERAL,
      User_par.SP_COURSEPACK,
      User_par.SP_DIGITAL,
      User_par.SP_REPUBLISH,
      User_par.SP_BUSINESS_PHOTOCOPY,
      User_par.SP_BUSINESS_DIGITAL_INTERNAL,
      User_par.SP_ACADEMIC_LICENSE,
      User_par.SP_RIGHTSLINK_REPRINT,
      User_par.SP_RIGHTSLINK_DIGITAL,
      User_par.REQUEST_CLIENT_VERSION,
      User_par.CRE_UPD_USER,
      User_par.CRE_UPD_DTM,
      User_par.SKIP_QUICKPRICE,
      User_par.AUTO_ACCEPT_SPORDER,
      User_par.ENFORCE_PWD_CHG
    FROM CCC_USER u , CCC_USER_REQUEST_CLIENT c
    WHERE lower(u.user_identifier) = lower(User_identifier_par)
    AND c.user_id(+) = u.user_id
    AND rownum = 1;

    IF SQL%FOUND THEN
      RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
        SQLERRM, SQL%ROWCOUNT, 'ReadUserForUserIdentifier', 'read');

      dbms_output.put_line('Retrieved User: ' || User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');

      COMMIT;
    END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForUserIdentifier', 'read');

    ROLLBACK;

  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForUserIdentifier', 'read');

    ROLLBACK;
END ReadUserForUserIdentifier;

PROCEDURE ReadUserForAUID (
    RC_par OUT Return_Codes_type,
    User_par OUT User_type,
    AU_Id_par IN VARCHAR2)
  IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN

    User_par := NEW User_type ( 0, '', 0, '', '','', SYSDATE, 0, '', '', '', '','', '','','','','','','', '', 0, 0, 0, SYSDATE, '','N', 'N' );

    SELECT
      u.USER_ID,
      u.USER_TYPE,
      u.PARTY_ID,
      u.USER_IDENTIFIER,
      u.OID_PK,
      u.VERSION,
      c.AU_ID,
      c.RL_SESSION_ID,
      c.LAST_SESSION_START,
      c.LAST_CART_ID,
      c.ALWAYS_INVOICE,
      c.SP_PHOTOCOPY_ACADEMIC,
      c.SP_SHARE_ILL,
      c.SP_PHOTOCOPY_GENERAL,
      c.SP_COURSEPACK,
      c.SP_DIGITAL,
      c.SP_REPUBLISH,
      c.SP_BUSINESS_PHOTOCOPY,
      c.SP_BUSINESS_DIGITAL_INTERNAL,
      c.SP_ACADEMIC_LICENSE,
      c.SP_RIGHTSLINK_REPRINT,
      c.SP_RIGHTSLINK_DIGITAL,
      c.VERSION,
      NVL(c.UPD_USER, c.CRE_USER),
      NVL(c.UPD_DTM, c.CRE_DTM),
      c.SKIP_QUICKPRICE,
      u.AUTO_ACCEPT_SPORDER,
      u.ENFORCE_PWD_CHG
    INTO
      User_par.USER_ID,
      User_par.USER_TYPE,
      User_par.PARTY_ID,
      User_par.USER_IDENTIFIER,
      User_par.OID_PK,
      User_par.VERSION,
      User_par.AU_ID,
      User_par.RL_SESSION_ID,
      User_par.LAST_SESSION_START,
      User_par.LAST_CART_ID,
      User_par.ALWAYS_INVOICE,
      User_par.SP_PHOTOCOPY_ACADEMIC,
      User_par.SP_SHARE_ILL,
      User_par.SP_PHOTOCOPY_GENERAL,
      User_par.SP_COURSEPACK,
      User_par.SP_DIGITAL,
      User_par.SP_REPUBLISH,
      User_par.SP_BUSINESS_PHOTOCOPY,
      User_par.SP_BUSINESS_DIGITAL_INTERNAL,
      User_par.SP_ACADEMIC_LICENSE,
      User_par.SP_RIGHTSLINK_REPRINT,
      User_par.SP_RIGHTSLINK_DIGITAL,
      User_par.REQUEST_CLIENT_VERSION,
      User_par.CRE_UPD_USER,
      User_par.CRE_UPD_DTM,
      User_par.SKIP_QUICKPRICE,
      User_par.AUTO_ACCEPT_SPORDER,
      User_par.ENFORCE_PWD_CHG

    FROM CCC_USER u, CCC_USER_REQUEST_CLIENT c
    WHERE c.AU_Id = AU_Id_par
    AND c.USER_ID = u.USER_ID;

    IF SQL%FOUND THEN
      RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
        SQLERRM, SQL%ROWCOUNT, 'ReadUserForAUID', 'read');

      dbms_output.put_line('Retrieved User: ' || User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');

      COMMIT;
    END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForAUID', 'read');

    ROLLBACK;

  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForAUID', 'read');

    ROLLBACK;
END ReadUserForAUID;


PROCEDURE ReadUserForNewSession (
    RC_par OUT Return_Codes_type,
    User_par OUT User_type,
    AU_Id_par IN VARCHAR2)
  IS
    PRAGMA AUTONOMOUS_TRANSACTION;
    LOCAL_CRE_DTM DATE;
    NEW_USER_ID NUMBER;

BEGIN
    dbms_output.put_line('Starting Execution');

  BEGIN
    User_par := NEW User_type ( 0, '', 0, '', AU_Id_par, '',SYSDATE, 0, '', 'N', 'Y','N','N','N','N','N','N','N','N','N', 'N', 0, 0, 0, SYSDATE, 'N','N', 'N' );

      LOCAL_CRE_DTM := LOCALTIMESTAMP;

    dbms_output.put_line('AU ID Par: '  || au_id_par);

    SELECT
      u.USER_ID,
      u.USER_TYPE,
      u.PARTY_ID,
      u.USER_IDENTIFIER,
      u.OID_PK,
      u.VERSION,
      c.AU_ID,
      c.RL_SESSION_ID,
      LOCAL_CRE_DTM,
      c.LAST_CART_ID,
      c.ALWAYS_INVOICE,
      c.SP_PHOTOCOPY_ACADEMIC,
      c.SP_SHARE_ILL,
      c.SP_PHOTOCOPY_GENERAL,
      c.SP_COURSEPACK,
      c.SP_DIGITAL,
      c.SP_REPUBLISH,
      c.SP_BUSINESS_PHOTOCOPY,
      c.SP_BUSINESS_DIGITAL_INTERNAL,
      c.SP_ACADEMIC_LICENSE,
      c.SP_RIGHTSLINK_REPRINT,
      c.SP_RIGHTSLINK_DIGITAL,
      c.VERSION,
      NVL(c.UPD_USER, c.CRE_USER),
      NVL(c.UPD_DTM, c.CRE_DTM),
      c.SKIP_QUICKPRICE,
      u.AUTO_ACCEPT_SPORDER,
      u.ENFORCE_PWD_CHG
    INTO
      User_par.USER_ID,
      User_par.USER_TYPE,
      User_par.PARTY_ID,
      User_par.USER_IDENTIFIER,
      User_par.OID_PK,
      User_par.VERSION,
      User_par.AU_ID,
      User_par.RL_SESSION_ID,
      User_par.LAST_SESSION_START,
      User_par.LAST_CART_ID,
      User_par.ALWAYS_INVOICE,
      User_par.SP_PHOTOCOPY_ACADEMIC,
      User_par.SP_SHARE_ILL,
      User_par.SP_PHOTOCOPY_GENERAL,
      User_par.SP_COURSEPACK,
      User_par.SP_DIGITAL,
      User_par.SP_REPUBLISH,
      User_par.SP_BUSINESS_PHOTOCOPY,
      User_par.SP_BUSINESS_DIGITAL_INTERNAL,
      User_par.SP_ACADEMIC_LICENSE,
      User_par.SP_RIGHTSLINK_REPRINT,
      User_par.SP_RIGHTSLINK_DIGITAL,
      User_par.REQUEST_CLIENT_VERSION,
      User_par.CRE_UPD_USER,
      User_par.CRE_UPD_DTM,
      User_par.SKIP_QUICKPRICE,
      User_par.AUTO_ACCEPT_SPORDER,
      User_par.ENFORCE_PWD_CHG
    FROM CCC_USER u, CCC_USER_REQUEST_CLIENT c
    WHERE c.AU_Id = AU_Id_par
    AND c.USER_ID = u.USER_ID;

    IF SQL%FOUND THEN
        UPDATE CCC_USER_REQUEST_CLIENT
        SET LAST_SESSION_START = LOCALTIMESTAMP
        WHERE AU_Id = AU_Id_par;

      RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
        SQLERRM, SQL%ROWCOUNT, 'ReadUserForAUID', 'read');

      dbms_output.put_line('retrieved user: ' || User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');
      COMMIT;
      RETURN;
    END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
         dbms_output.put_line('NO DATA FOUND' );
         NULL;
END;
BEGIN
    dbms_output.put_line('selected users' );

--    IF SQL%Found THEN
--      RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
--        SQLERRM, SQL%ROWCOUNT, 'ReadUserForAUID', 'read');

--      dbms_output.put_line('retrieved user: ' || User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');

--      COMMIT;
--    ELSIF SQL%NOTFOUND THEN -- WHEN NO_DATA_FOUND THEN
--         dbms_output.put_line('Creating User: ' || au_id_par);

      SELECT CCC_USER_SEQ.NEXTVAL INTO NEW_USER_ID FROM DUAL;

      User_par.USER_ID := NEW_USER_ID;
      User_par.USER_TYPE := 'CUST';
      User_par.PARTY_ID := 0;
      User_par.USER_IDENTIFIER := null;
      User_par.OID_PK := null;
      User_par.VERSION := 1;
      User_par.AU_ID := AU_ID_par;
      User_par.RL_SESSION_ID := '';
      User_par.LAST_SESSION_START := LOCAL_CRE_DTM;
      User_par.LAST_CART_ID := 0;
      User_par.ALWAYS_INVOICE := 'N';
      User_par.SP_PHOTOCOPY_ACADEMIC := 'N';
      User_par.SP_SHARE_ILL := 'N';
      User_par.SP_PHOTOCOPY_GENERAL := 'N';
      User_par.SP_COURSEPACK := 'N';
      User_par.SP_DIGITAL := 'N';
      User_par.SP_REPUBLISH := 'N';
      User_par.SP_BUSINESS_PHOTOCOPY := 'N';
      User_par.SP_BUSINESS_DIGITAL_INTERNAL := 'N';
      User_par.SP_ACADEMIC_LICENSE := 'N';
      User_par.SP_RIGHTSLINK_REPRINT := 'N';
      User_par.SP_RIGHTSLINK_DIGITAL := 'N';
      User_par.REQUEST_CLIENT_VERSION := 1;
      User_par.CRE_UPD_USER := NEW_USER_ID;
      User_par.CRE_UPD_DTM := LOCAL_CRE_DTM;
      User_par.SKIP_QUICKPRICE := 'N';
      User_par.AUTO_ACCEPT_SPORDER:='N';
      User_par.ENFORCE_PWD_CHG := 'N';

         INSERT INTO CCC_USER (
            USER_ID,
            USER_TYPE,
            PARTY_ID,
            USER_IDENTIFIER,
            OID_PK,
            VERSION,
            CRE_USER,
            CRE_DTM,
            ENFORCE_PWD_CHG,
            AUTO_ACCEPT_SPORDER)
          VALUES (
            NEW_USER_ID,
            User_par.USER_TYPE,
            User_par.PARTY_ID,
            User_par.USER_IDENTIFIER,
            User_par.OID_PK,
            User_par.VERSION,
            User_par.CRE_UPD_USER,
            User_par.CRE_UPD_DTM,
            User_par.ENFORCE_PWD_CHG,
            User_par.AUTO_ACCEPT_SPORDER);

          INSERT INTO CCC_USER_REQUEST_CLIENT (
            USER_REQUEST_CLIENT_ID,
            AU_ID,
            USER_ID,
            RL_SESSION_ID,
            LAST_SESSION_START,
            LAST_CART_ID,
            ALWAYS_INVOICE,
            SP_PHOTOCOPY_ACADEMIC,
            SP_SHARE_ILL,
            SP_PHOTOCOPY_GENERAL,
            SP_COURSEPACK,
            SP_DIGITAL,
            SP_REPUBLISH,
            SP_BUSINESS_PHOTOCOPY,
            SP_BUSINESS_DIGITAL_INTERNAL,
            SP_ACADEMIC_LICENSE,
            SP_RIGHTSLINK_REPRINT,
            SP_RIGHTSLINK_DIGITAL,
            VERSION,
            CRE_USER,
            CRE_DTM,
            SKIP_QUICKPRICE)
          VALUES (
            CCC_USER_REQUEST_CLIENT_SEQ.NEXTVAL,
            AU_Id_par,
            NEW_USER_ID,
            User_par.RL_SESSION_ID,
            User_par.LAST_SESSION_START,
            User_par.LAST_CART_ID,
            User_par.ALWAYS_INVOICE,
            User_par.SP_PHOTOCOPY_ACADEMIC,
            User_par.SP_SHARE_ILL,
            User_par.SP_PHOTOCOPY_GENERAL,
            User_par.SP_COURSEPACK,
            User_par.SP_DIGITAL,
            User_par.SP_REPUBLISH,
            User_par.SP_BUSINESS_PHOTOCOPY,
            User_par.SP_BUSINESS_DIGITAL_INTERNAL,
            User_par.SP_ACADEMIC_LICENSE,
            User_par.SP_RIGHTSLINK_REPRINT,
            User_par.SP_RIGHTSLINK_DIGITAL,
            User_par.REQUEST_CLIENT_VERSION,
            NEW_USER_ID,
            LOCAL_CRE_DTM,
            User_par.SKIP_QUICKPRICE);

        IF SQL%FOUND THEN
            dbms_output.put_line('Creating User: ' || NEW_USER_ID);
            RC_par := NEW  Return_Codes_type (1, 'SUCCESS', SQLCODE,
                            SQLERRM, SQL%ROWCOUNT, 'InsertUser', 'insert');
            COMMIT;
        END IF;
--    END IF;

EXCEPTION
  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForNewSession', 'INSERT');
  ROLLBACK;
END;
END ReadUserForNewSession;



PROCEDURE ReadUserForLoginUsername (
    RC_par OUT Return_Codes_type,
    User_par OUT User_type,
    User_in_par IN User_type)
--    AU_ID_par IN VARCHAR2,
--    Username_par IN VARCHAR2)
  IS
    PRAGMA AUTONOMOUS_TRANSACTION;

    AUID_count NUMBER;

BEGIN
    User_par := NEW User_type ( 0, '', 0, '', '', '',SYSDATE, 0, '', '','','','','','','','','','','', '', 0, 0, 0, SYSDATE, '','N', 'N' );
  BEGIN

--    RC_par := NEW  Return_Codes_type (1, 'SUCCESS', SQLCODE,
--                                SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'read');

    SELECT
      u.USER_ID,
      u.USER_TYPE,
      u.PARTY_ID,
      u.USER_IDENTIFIER,
      u.OID_PK,
      u.VERSION,
      c.AU_ID,
      c.RL_SESSION_ID,
      c.LAST_SESSION_START,
      c.LAST_CART_ID,
      c.ALWAYS_INVOICE,
      c.SP_PHOTOCOPY_ACADEMIC,
      c.SP_SHARE_ILL,
      c.SP_PHOTOCOPY_GENERAL,
      c.SP_COURSEPACK,
      c.SP_DIGITAL,
      c.SP_REPUBLISH,
      c.SP_BUSINESS_PHOTOCOPY,
      c.SP_BUSINESS_DIGITAL_INTERNAL,
      c.SP_ACADEMIC_LICENSE,
      c.SP_RIGHTSLINK_REPRINT,
      c.SP_RIGHTSLINK_DIGITAL,
      c.VERSION,
      NVL(c.UPD_USER, c.CRE_USER),
      NVL(c.UPD_DTM, c.CRE_DTM),
      c.SKIP_QUICKPRICE,
      u.AUTO_ACCEPT_SPORDER,
      u.ENFORCE_PWD_CHG
    INTO
      User_par.USER_ID,
      User_par.USER_TYPE,
      User_par.PARTY_ID,
      User_par.USER_IDENTIFIER,
      User_par.OID_PK,
      User_par.VERSION,
      User_par.AU_ID,
      User_par.RL_SESSION_ID,
      User_par.LAST_SESSION_START,
      User_par.LAST_CART_ID,
      User_par.ALWAYS_INVOICE,
      User_par.SP_PHOTOCOPY_ACADEMIC,
      User_par.SP_SHARE_ILL,
      User_par.SP_PHOTOCOPY_GENERAL,
      User_par.SP_COURSEPACK,
      User_par.SP_DIGITAL,
      User_par.SP_REPUBLISH,
      User_par.SP_BUSINESS_PHOTOCOPY,
      User_par.SP_BUSINESS_DIGITAL_INTERNAL,
      User_par.SP_ACADEMIC_LICENSE,
      User_par.SP_RIGHTSLINK_REPRINT,
      User_par.SP_RIGHTSLINK_DIGITAL,
      User_par.REQUEST_CLIENT_VERSION,
      User_par.CRE_UPD_USER,
      User_par.CRE_UPD_DTM,
      User_par.SKIP_QUICKPRICE,
      User_par.AUTO_ACCEPT_SPORDER,
      User_par.ENFORCE_PWD_CHG
    FROM CCC_USER u, CCC_USER_REQUEST_CLIENT c
    WHERE c.AU_ID = User_in_par.AU_ID
    AND c.USER_ID = u.USER_ID;

    IF SQL%FOUND THEN
        IF lower(User_par.USER_IDENTIFIER) = lower(User_in_par.USER_IDENTIFIER) THEN
            dbms_output.put_line('user/client match: ' ||
            User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');
            UPDATE CCC_USER_REQUEST_CLIENT
            SET LAST_SESSION_START = LOCALTIMESTAMP
            WHERE AU_Id = User_in_par.AU_ID;
            RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                SQLERRM, SQL%ROWCOUNT, 'ReadUserForUsername', 'read');
            dbms_output.put_line('retrieved user: ' || User_par.USER_IDENTIFIER || '(' || User_par.USER_ID || ')');
            COMMIT;
            RETURN;
        END IF;
    END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
     dbms_output.put_line('NO DATA FOUND ON FIRST QUERY' );
-- Set the out values to be consistent with the existing user context
     User_par.LAST_SESSION_START := user_in_par.LAST_SESSION_START;
     User_par.LAST_CART_ID := user_in_par.LAST_CART_ID;
     User_par.ALWAYS_INVOICE := user_in_par.ALWAYS_INVOICE;
     User_par.SP_PHOTOCOPY_ACADEMIC := user_in_par.SP_PHOTOCOPY_ACADEMIC;
     User_par.SP_SHARE_ILL := user_in_par.SP_SHARE_ILL;
     User_par.SP_PHOTOCOPY_GENERAL := user_in_par.SP_PHOTOCOPY_GENERAL;
     User_par.SP_COURSEPACK := user_in_par.SP_COURSEPACK;
     User_par.SP_DIGITAL := user_in_par.SP_DIGITAL;
     User_par.SP_REPUBLISH := user_in_par.SP_REPUBLISH;
     User_par.SP_BUSINESS_PHOTOCOPY := user_in_par.SP_BUSINESS_PHOTOCOPY;
     User_par.SP_BUSINESS_DIGITAL_INTERNAL := user_in_par.SP_BUSINESS_DIGITAL_INTERNAL;
     User_par.SP_ACADEMIC_LICENSE := user_in_par.SP_ACADEMIC_LICENSE;
     User_par.SP_RIGHTSLINK_REPRINT := user_in_par.SP_RIGHTSLINK_REPRINT;
     User_par.SP_RIGHTSLINK_DIGITAL := user_in_par.SP_RIGHTSLINK_DIGITAL;
     User_par.REQUEST_CLIENT_VERSION := user_in_par.REQUEST_CLIENT_VERSION;
     User_par.CRE_UPD_USER := user_in_par.CRE_UPD_USER;
     User_par.CRE_UPD_DTM := user_in_par.CRE_UPD_DTM;
     User_par.SKIP_QUICKPRICE := user_in_par.SKIP_QUICKPRICE;
     User_par.ENFORCE_PWD_CHG := 'K';
     NULL;

  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'First Read');
    ROLLBACK;
    RETURN;
END;
BEGIN

          dbms_output.put_line('user ID did not match request client: fetching correct user: '
            || User_par.USER_IDENTIFIER || ' <> ' || User_in_par.USER_IDENTIFIER
            || ' (' || User_par.USER_ID || ')');

          SELECT
            u.USER_ID,
            u.USER_TYPE,
            u.PARTY_ID,
            u.USER_IDENTIFIER,
            u.OID_PK,
            u.VERSION,
            u.ENFORCE_PWD_CHG,
            u.AUTO_ACCEPT_SPORDER
          INTO
            User_par.USER_ID,
            User_par.USER_TYPE,
            User_par.PARTY_ID,
            User_par.USER_IDENTIFIER,
            User_par.OID_PK,
            User_par.VERSION,
            User_par.ENFORCE_PWD_CHG,
            User_par.AUTO_ACCEPT_SPORDER
          FROM CCC_USER u
          WHERE LOWER(u.USER_IDENTIFIER) = LOWER(User_in_par.USER_IDENTIFIER);

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    User_par.USER_ID := User_in_par.USER_ID;
    User_par.USER_TYPE := User_in_par.USER_TYPE;
    User_par.PARTY_ID := User_in_par.PARTY_ID;
    User_par.USER_IDENTIFIER := User_in_par.USER_IDENTIFIER;
    User_par.OID_PK := User_in_par.OID_PK;
    User_par.VERSION := User_in_par.VERSION;
    User_par.AUTO_ACCEPT_SPORDER:=User_in_par.AUTO_ACCEPT_SPORDER;
    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Read from User Table by user identifier');
    ROLLBACK;
    RETURN;
  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Read the user only');
    ROLLBACK;
    RETURN;

END;
BEGIN

    SELECT COUNT(*)
    INTO AUID_count
    FROM CCC_USER_REQUEST_CLIENT
    WHERE AU_ID = User_in_par.AU_ID;

    IF AUID_count > 0 THEN
        UPDATE CCC_USER_REQUEST_CLIENT
        SET USER_ID = User_par.USER_ID,
            LAST_SESSION_START = LOCALTIMESTAMP
        WHERE AU_ID = User_in_par.AU_ID;
        IF SQL%FOUND THEN
            RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                 SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Update Request Client for User');
            COMMIT;
            RETURN;
        END IF;
    ELSE
        User_Par.AU_ID := User_in_par.AU_ID;
        RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                 SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Found User did not update AU_ID User');
        COMMIT;
    END IF;


EXCEPTION
  WHEN NO_DATA_FOUND THEN
    User_par.USER_ID := User_in_par.USER_ID;
    User_par.USER_TYPE := User_in_par.USER_TYPE;
    User_par.PARTY_ID := User_in_par.PARTY_ID;
    User_par.USER_IDENTIFIER := User_in_par.USER_IDENTIFIER;
    User_par.OID_PK := User_in_par.OID_PK;
    User_par.VERSION := User_in_par.VERSION;
    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Update AU_ID to User ID: ' || User_par.USER_ID);
    ROLLBACK;
    RETURN;
  WHEN OTHERS THEN
    User_par := NULL;
    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'ReadUserForLoginUsername', 'Update AU_ID to User ID: ' || User_par.USER_ID);
    ROLLBACK;
    RETURN;
END;
END ReadUserForLoginUsername;

PROCEDURE InsertUser (
    RC_par OUT Return_Codes_type,
    User_out_par OUT User_type,
    User_in_par IN User_type)
       IS
       PRAGMA AUTONOMOUS_TRANSACTION;
 CCC_PARENT_KEY_NOT_FOUND EXCEPTION;
 PRAGMA EXCEPTION_INIT (CCC_PARENT_KEY_NOT_FOUND, -2291);

 RECORD_count  NUMBER;
 LOCAL_CRE_DTM DATE;
 NEW_USER_ID NUMBER;
BEGIN
  LOCAL_CRE_DTM := LOCALTIMESTAMP;
  SELECT CCC_USER_SEQ.NEXTVAL INTO NEW_USER_ID FROM DUAL;

 INSERT INTO CCC_USER (
    USER_ID,
    USER_TYPE,
    PARTY_ID,
    USER_IDENTIFIER,
    OID_PK,
    VERSION,
    CRE_USER,
    CRE_DTM,
    ENFORCE_PWD_CHG,
    AUTO_ACCEPT_SPORDER)
  VALUES (
    NEW_USER_ID,
    User_in_par.USER_TYPE,
    User_in_par.PARTY_ID,
    User_in_par.USER_IDENTIFIER,
    User_in_par.OID_PK,
    1,
    User_in_par.CRE_UPD_USER,
    LOCAL_CRE_DTM,
    User_in_par.ENFORCE_PWD_CHG,
    User_in_par.AUTO_ACCEPT_SPORDER);

  INSERT INTO CCC_USER_REQUEST_CLIENT (
    USER_REQUEST_CLIENT_ID,
    AU_ID,
    RL_SESSION_ID,
    USER_ID,
    LAST_SESSION_START,
    LAST_CART_ID,
    ALWAYS_INVOICE,
    SP_PHOTOCOPY_ACADEMIC,
    SP_PHOTOCOPY_GENERAL,
    SP_COURSEPACK,
    SP_DIGITAL,
    SP_REPUBLISH,
    SP_BUSINESS_PHOTOCOPY,
    SP_BUSINESS_DIGITAL_INTERNAL,
    SP_ACADEMIC_LICENSE,
    SP_RIGHTSLINK_REPRINT,
    SP_RIGHTSLINK_DIGITAL,
    SP_SHARE_ILL,
    VERSION,
    CRE_USER,
    CRE_DTM,
    SKIP_QUICKPRICE)
  VALUES (
    CCC_USER_REQUEST_CLIENT_SEQ.NEXTVAL,
    User_in_par.AU_ID,
    User_in_par.RL_SESSION_ID,
    NEW_USER_ID,
    LOCAL_CRE_DTM,
    User_in_par.LAST_CART_ID,
    User_in_par.ALWAYS_INVOICE,
    User_in_par.SP_PHOTOCOPY_ACADEMIC,
    User_in_par.SP_PHOTOCOPY_GENERAL,
    User_in_par.SP_COURSEPACK,
    User_in_par.SP_DIGITAL,
    User_in_par.SP_REPUBLISH,
    User_in_par.SP_BUSINESS_PHOTOCOPY,
    User_in_par.SP_BUSINESS_DIGITAL_INTERNAL,
    User_in_par.SP_ACADEMIC_LICENSE,
    User_in_par.SP_RIGHTSLINK_REPRINT,
    User_in_par.SP_RIGHTSLINK_DIGITAL,
    User_in_par.SP_SHARE_ILL,
    1,
    User_in_par.CRE_UPD_USER,
    LOCAL_CRE_DTM,
    User_in_par.SKIP_QUICKPRICE);

 IF SQL%FOUND THEN
    User_out_par:= User_in_par;
    User_out_par.USER_ID := NEW_USER_ID;
    User_out_par.VERSION := 1;
    User_out_par.REQUEST_CLIENT_VERSION := 1;  --CZC 4/6/07
    User_out_par.CRE_UPD_DTM := LOCAL_CRE_DTM;
    RC_par := NEW  Return_Codes_type (1, 'SUCCESS', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'InsertUser', 'insert');

    COMMIT;

 ELSIF SQL%NOTFOUND THEN
    RAISE NO_DATA_FOUND;
 END IF;

EXCEPTION
WHEN NO_DATA_FOUND THEN
       User_out_par := NULL;
       RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'InsertUser', 'insert');
 ROLLBACK;
WHEN CCC_PARENT_KEY_NOT_FOUND THEN
 User_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'CCC_PARENT_KEY_NOT_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'InsertUser','insert');
 ROLLBACK;
WHEN DUP_VAL_ON_INDEX THEN
 User_out_par := NULL;
       RC_par := NEW  Return_Codes_type (0, 'DUP_VAL_ON_INDEX', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'InsertUser', 'insert');
       ROLLBACK;
WHEN OTHERS THEN
 User_out_par := NULL;
       RC_par := NEW  Return_Codes_type (-1, 'ORACLEERROR', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'InsertUser', 'insert');
       ROLLBACK;
END InsertUser;

PROCEDURE UpdateUser (RC_par OUT Return_Codes_type,
    User_out_par OUT User_type,
    User_in_par IN User_type)
       IS
       PRAGMA AUTONOMOUS_TRANSACTION;

 CCC_CHILD_RECORD_NOT_FOUND  EXCEPTION;
 CCC_PARENT_KEY_NOT_FOUND  EXCEPTION;

 PRAGMA EXCEPTION_INIT (CCC_PARENT_KEY_NOT_FOUND, -2291);

 RECORD_count  NUMBER;
 LOCAL_UPD_DTM   DATE;
 USER_VERSION_MISMATCH_COUNT NUMBER;
 VERSION_UPDATE_IND NUMBER;
 NEW_REQUEST_CLIENT_VERSION NUMBER;

BEGIN
  BEGIN

  LOCAL_UPD_DTM := LOCALTIMESTAMP;
    USER_OUT_PAR := USER_IN_PAR;

    SELECT COUNT(*)
    INTO USER_VERSION_MISMATCH_COUNT
    FROM CCC_USER
    WHERE CCC_USER.USER_ID = User_in_par.USER_ID AND
         (CCC_USER.USER_TYPE <> User_in_par.USER_TYPE OR
         CCC_USER.PARTY_ID <> User_in_par.PARTY_ID OR
         LOWER(CCC_USER.USER_IDENTIFIER) <> LOWER(User_in_par.USER_IDENTIFIER) OR
         CCC_USER.OID_PK <> User_in_par.OID_PK) AND
         CCC_USER.VERSION <> User_in_par.VERSION;

    IF USER_VERSION_MISMATCH_COUNT > 0 THEN
        RC_par := NEW RETURN_CODES_type (0, 'CCC_VERSION_MISMATCH',
          SQLCODE, SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
        ROLLBACK;
        RETURN; -- CZC 4/6/07
    END IF;

    UPDATE CCC_USER SET
        CCC_USER.USER_TYPE = User_in_par.USER_TYPE,
        CCC_USER.PARTY_ID = User_in_par.PARTY_ID,
        CCC_USER.USER_IDENTIFIER = User_in_par.USER_IDENTIFIER,
        CCC_USER.OID_PK = User_in_par.OID_PK,
        CCC_USER.VERSION = User_in_par.VERSION + 1,
        CCC_USER.UPD_USER = User_in_par.CRE_UPD_USER,
        CCC_USER.UPD_DTM = LOCAL_UPD_DTM,
        CCC_USER.AUTO_ACCEPT_SPORDER=User_in_par.AUTO_ACCEPT_SPORDER
    WHERE
        CCC_USER.USER_ID = User_in_par.USER_ID AND
        (CCC_USER.USER_TYPE <> User_in_par.USER_TYPE OR
         CCC_USER.PARTY_ID <> User_in_par.PARTY_ID OR
         CCC_USER.AUTO_ACCEPT_SPORDER<>User_in_par.AUTO_ACCEPT_SPORDER OR
         LOWER(CCC_USER.USER_IDENTIFIER) <> LOWER(User_in_par.USER_IDENTIFIER) OR
         CCC_USER.OID_PK <> User_in_par.OID_PK);
--        AND CCC_USER.VERSION = User_in_par.VERSION;

EXCEPTION
WHEN NO_DATA_FOUND THEN
 USER_out_par := NULL;
        RC_par := NEW RETURN_CODES_type (0, 'NO_DATA_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'Update User Table');
-- ROLLBACK;
WHEN CCC_PARENT_KEY_NOT_FOUND THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'CCC_PARENT_KEY_NOT_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'Update User Table');
  ROLLBACK;
WHEN DUP_VAL_ON_INDEX THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'DUP_VAL_ON_INDEX', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'Update User Table');
 ROLLBACK;
WHEN OTHERS THEN
       RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'Update User Table');
 ROLLBACK;
END;
BEGIN
    SELECT COUNT(*)
      INTO VERSION_UPDATE_IND
      FROM CCC_USER_REQUEST_CLIENT
     WHERE CCC_USER_REQUEST_CLIENT.AU_ID = User_in_par.AU_ID AND
           CCC_USER_REQUEST_CLIENT.LAST_CART_ID <> User_in_par.LAST_CART_ID;

    IF VERSION_UPDATE_IND > 0 THEN
        User_out_par.REQUEST_CLIENT_VERSION := User_in_par.REQUEST_CLIENT_VERSION + 1;
    END IF;
    User_out_par.CRE_UPD_DTM := LOCAL_UPD_DTM;

    UPDATE CCC_USER_REQUEST_CLIENT SET
        CCC_USER_REQUEST_CLIENT.AU_ID = User_out_par.AU_ID,
        CCC_USER_REQUEST_CLIENT.RL_SESSION_ID = User_out_par.RL_SESSION_ID,
        CCC_USER_REQUEST_CLIENT.LAST_SESSION_START = User_out_par.LAST_SESSION_START,
        CCC_USER_REQUEST_CLIENT.LAST_CART_ID = User_out_par.LAST_CART_ID,
        CCC_USER_REQUEST_CLIENT.ALWAYS_INVOICE = User_out_par.ALWAYS_INVOICE,
        CCC_USER_REQUEST_CLIENT.SP_PHOTOCOPY_ACADEMIC = User_out_par.SP_PHOTOCOPY_ACADEMIC,
        CCC_USER_REQUEST_CLIENT.SP_SHARE_ILL = User_out_par.SP_SHARE_ILL,
        CCC_USER_REQUEST_CLIENT.SP_PHOTOCOPY_GENERAL = User_out_par.SP_PHOTOCOPY_GENERAL,
        CCC_USER_REQUEST_CLIENT.SP_COURSEPACK = User_out_par.SP_COURSEPACK,
        CCC_USER_REQUEST_CLIENT.SP_DIGITAL = User_out_par.SP_DIGITAL,
        CCC_USER_REQUEST_CLIENT.SP_REPUBLISH = User_out_par.SP_REPUBLISH,
        CCC_USER_REQUEST_CLIENT.SP_BUSINESS_PHOTOCOPY = User_out_par.SP_BUSINESS_PHOTOCOPY,
        CCC_USER_REQUEST_CLIENT.SP_BUSINESS_DIGITAL_INTERNAL = User_out_par.SP_BUSINESS_DIGITAL_INTERNAL,
        CCC_USER_REQUEST_CLIENT.SP_ACADEMIC_LICENSE = User_out_par.SP_ACADEMIC_LICENSE,
        CCC_USER_REQUEST_CLIENT.SP_RIGHTSLINK_REPRINT = User_out_par.SP_RIGHTSLINK_REPRINT,
        CCC_USER_REQUEST_CLIENT.SP_RIGHTSLINK_DIGITAL = User_out_par.SP_RIGHTSLINK_DIGITAL,
        CCC_USER_REQUEST_CLIENT.VERSION = User_out_par.REQUEST_CLIENT_VERSION,
        CCC_USER_REQUEST_CLIENT.UPD_USER = User_out_par.CRE_UPD_USER,
        CCC_USER_REQUEST_CLIENT.UPD_DTM = User_out_par.CRE_UPD_DTM,
        CCC_USER_REQUEST_CLIENT.SKIP_QUICKPRICE = User_out_par.SKIP_QUICKPRICE
        

    WHERE
        CCC_USER_REQUEST_CLIENT.AU_ID = User_in_par.AU_ID
        AND CCC_USER_REQUEST_CLIENT.VERSION = User_in_par.REQUEST_CLIENT_VERSION;

  IF SQL%FOUND THEN
     RC_par := NEW RETURN_CODES_type (1, 'SUCCESS', SQLCODE,
     SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
     COMMIT;
  ELSIF SQL%NOTFOUND THEN -- WHEN NO_DATA_FOUND or CCC_VERSION_MISMATCH
    SELECT COUNT (*) INTO RECORD_count
    FROM CCC_USER_REQUEST_CLIENT
   WHERE CCC_USER_REQUEST_CLIENT.AU_ID = User_in_par.AU_ID;
  IF RECORD_count > 0 THEN -- actually version mismatch
     USER_out_par := USER_in_par;
     RC_par := NEW RETURN_CODES_type (0, 'CCC_VERSION_MISMATCH',
          SQLCODE, SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
    ROLLBACK;
  ELSE -- actually no data found
    USER_out_par := USER_in_par;
    RC_par := NEW RETURN_CODES_type (0, 'NO_DATA_FOUND',
          SQLCODE, SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
     ROLLBACK;
  END IF;
END IF;

EXCEPTION
WHEN NO_DATA_FOUND THEN
 USER_out_par := NULL;
        RC_par := NEW RETURN_CODES_type (0, 'NO_DATA_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
 ROLLBACK;
WHEN CCC_CHILD_RECORD_NOT_FOUND THEN
 USER_out_par := NULL;
        RC_par := NEW RETURN_CODES_type (0, 'CCC_CHILD_RECORD_NOT_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
 ROLLBACK;
WHEN CCC_PARENT_KEY_NOT_FOUND THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'CCC_PARENT_KEY_NOT_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
  ROLLBACK;
WHEN DUP_VAL_ON_INDEX THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'DUP_VAL_ON_INDEX', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
 ROLLBACK;
WHEN OTHERS THEN
       RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdateUser', 'update');
 ROLLBACK;
END;

END UpdateUser;

PROCEDURE UpdatePasswordEnforcement (RC_par OUT Return_Codes_type,
    User_out_par OUT User_type,
    User_in_par IN User_type)
       IS
       PRAGMA AUTONOMOUS_TRANSACTION;

 CCC_CHILD_RECORD_NOT_FOUND  EXCEPTION;
 CCC_PARENT_KEY_NOT_FOUND  EXCEPTION;

 PRAGMA EXCEPTION_INIT (CCC_PARENT_KEY_NOT_FOUND, -2291);

 RECORD_count  NUMBER;
 LOCAL_UPD_DTM   DATE;
 USER_VERSION_MISMATCH_COUNT NUMBER;
 VERSION_UPDATE_IND NUMBER;


BEGIN
  BEGIN

  LOCAL_UPD_DTM := LOCALTIMESTAMP;
  USER_OUT_PAR := USER_IN_PAR;


    SELECT COUNT(*)
    INTO USER_VERSION_MISMATCH_COUNT
    FROM CCC_USER
    WHERE CCC_USER.USER_ID = User_in_par.USER_ID AND
         (CCC_USER.USER_TYPE <> User_in_par.USER_TYPE OR
         CCC_USER.PARTY_ID <> User_in_par.PARTY_ID OR
         LOWER(CCC_USER.USER_IDENTIFIER) <> LOWER(User_in_par.USER_IDENTIFIER) OR
         CCC_USER.OID_PK <> User_in_par.OID_PK) AND
         CCC_USER.VERSION <> User_in_par.VERSION;

    IF USER_VERSION_MISMATCH_COUNT > 0 THEN
        RC_par := NEW RETURN_CODES_type (0, 'CCC_VERSION_MISMATCH',
          SQLCODE, SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'update');
        ROLLBACK;
        RETURN;
    END IF;

    UPDATE CCC_USER SET
        CCC_USER.USER_TYPE = User_in_par.USER_TYPE,
        CCC_USER.OID_PK = User_in_par.OID_PK,
        CCC_USER.VERSION = User_in_par.VERSION + 1,
        CCC_USER.UPD_USER = User_in_par.CRE_UPD_USER,
        CCC_USER.UPD_DTM = LOCAL_UPD_DTM,
        CCC_USER.ENFORCE_PWD_CHG = User_in_par.ENFORCE_PWD_CHG,
        CCC_USER.AUTO_ACCEPT_SPORDER=User_in_par.AUTO_ACCEPT_SPORDER
    WHERE
        CCC_USER.USER_ID = User_in_par.USER_ID;


    IF SQL%FOUND THEN
      User_out_par:= User_in_par;
      User_out_par.VERSION := User_in_par.VERSION + 1;
      User_out_par.CRE_UPD_DTM := LOCAL_UPD_DTM;
      RC_par := NEW  Return_Codes_type (1, 'SUCCESS', SQLCODE,
      SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'update');
      COMMIT;
    ELSIF SQL%NOTFOUND THEN
      RAISE NO_DATA_FOUND;
    END IF;

EXCEPTION
WHEN NO_DATA_FOUND THEN
 USER_out_par := NULL;
        RC_par := NEW RETURN_CODES_type (0, 'NO_DATA_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'Update User Table');
  ROLLBACK;
WHEN CCC_PARENT_KEY_NOT_FOUND THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'CCC_PARENT_KEY_NOT_FOUND', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'Update User Table');
  ROLLBACK;
WHEN DUP_VAL_ON_INDEX THEN
 USER_out_par := NULL;
 RC_par := NEW RETURN_CODES_type (0, 'DUP_VAL_ON_INDEX', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'Update User Table');
 ROLLBACK;
WHEN OTHERS THEN
       RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
    SQLERRM, SQL%ROWCOUNT, 'UpdatePasswordEnforcement', 'Update User Table');
 ROLLBACK;
END;
END UpdatePasswordEnforcement;
END CCC_User_pkg;
/
