CREATE OR REPLACE 
PACKAGE BODY CCC_RLINK_PUBLISHER_PKG AS

TYPE ref_cursor IS REF CURSOR;

PROCEDURE get_rlink_pub_by_pty_inst(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_pty_inst IN   NUMBER
      )
   IS PRAGMA AUTONOMOUS_TRANSACTION;
   
   l_row_count NUMBER := 0;
   
   CURSOR c_get_publisher_cur (p_pty_inst ccc_rlink_publisher.pub_pty_inst%TYPE) IS
   SELECT
        RLINK_PUB_ID,
        PUB_PTY_INST,
        WRK_INST,
        ACCOUNT_NUM,
        PUB_NAME,
        PUB_URL,
        PERM_OPTION_DESC,
        LEARN_MORE_DESC,
        CRE_USER,
        CRE_DTM,
        NVL(UPD_USER, CRE_USER) upd_user,
        NVL(UPD_DTM, CRE_DTM) upd_dtm
     FROM   ccc_rlink_publisher
     WHERE pub_pty_inst = p_pty_inst
     UNION
     SELECT
        a.RLINK_PUB_ID RLINK_PUB_ID,
        a.PUB_PTY_INST PUB_PTY_INST,
        a.WRK_INST WRK_INST,
        a.SUB_ACCOUNT_NUM ACCOUNT_NUM,
        c.ORGNAME PUB_NAME ,
        b.pub_url pub_url,
        b.perm_option_desc perm_option_desc ,
        b.learn_more_desc learn_more_desc ,
        a.CRE_USER cre_user,
        a.CRE_DTM cre_dtm,
        NVL(a.UPD_USER, a.CRE_USER) upd_user,
        NVL(a.UPD_DTM, a.CRE_DTM) upd_dtm
     FROM   ccc_rlink_publisher_detail a, ccc_rlink_publisher b, ccc_party c
     WHERE a.pub_pty_inst = p_pty_inst
     and b.rlink_pub_id = a.rlink_pub_id
     and a.pub_pty_inst = c.pty_inst;
     
     c_get_publisher_rec  c_get_publisher_cur%ROWTYPE;
   BEGIN
    p_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
    
    BEGIN
    
        FOR c_get_publisher_rec IN c_get_publisher_cur(p_pub_pty_inst) LOOP
        
            BEGIN
            
                p_pub_type.rlink_pub_id     := c_get_publisher_rec.rlink_pub_id;
                p_pub_type.PUB_PTY_INST     := c_get_publisher_rec.pub_pty_inst;
                p_pub_type.WRK_INST         := c_get_publisher_rec.wrk_inst;
                p_pub_type.ACCOUNT_NUM      := c_get_publisher_rec.account_num;
                p_pub_type.PUB_NAME         := c_get_publisher_rec.pub_name;
                p_pub_type.PUB_URL          := c_get_publisher_rec.pub_url;
                p_pub_type.PERM_OPTION_DESC := c_get_publisher_rec.perm_option_desc;
                p_pub_type.LEARN_MORE_DESC  := c_get_publisher_rec.learn_more_desc;
                p_pub_type.CRE_USER         := c_get_publisher_rec.cre_user;
                p_pub_type.CRE_DTM          := c_get_publisher_rec.cre_dtm;
                p_pub_type.UPD_USER         := c_get_publisher_rec.upd_user;
                p_pub_type.UPD_DTM          := c_get_publisher_rec.upd_dtm;    
            
                l_row_count := l_row_count + 1;
            
            END;  
        
        
        END LOOP;
    
         
          --  IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByPtyInst');
          --  END IF; 
          
          --dbms_output.put_line('Row count: ' || l_row_count);
          
          IF l_row_count = 0 THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByPtyInst');
          END IF;
            
                
          /* EXCEPTION  when no_data_found THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'getPublisher', 'read');

                
            WHEN OTHERS THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'getPublisher', 'read'); */
                    
    END;                    
   
END get_rlink_pub_by_pty_inst;

PROCEDURE get_rlink_pub_by_pty_wrk_inst(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_pty_inst IN   NUMBER, 
      p_wrk_inst     IN   NUMBER 
      )
   IS PRAGMA AUTONOMOUS_TRANSACTION;
   
   l_row_count NUMBER := 0;
   
   CURSOR c_get_publisher_cur (p_pty_inst ccc_rlink_publisher.pub_pty_inst%TYPE, 
                               p_work_inst ccc_rlink_publisher.wrk_inst%TYPE ) IS
   SELECT
        RLINK_PUB_ID,
        PUB_PTY_INST,
        WRK_INST,
        ACCOUNT_NUM,
        PUB_NAME,
        PUB_URL,
        PERM_OPTION_DESC,
        LEARN_MORE_DESC,
        CRE_USER,
        CRE_DTM,
        NVL(UPD_USER, CRE_USER) upd_user,
        NVL(UPD_DTM, CRE_DTM) upd_dtm
     FROM   ccc_rlink_publisher
     WHERE pub_pty_inst = p_pty_inst
     AND wrk_inst = p_work_inst
     UNION
     SELECT
        a.RLINK_PUB_ID RLINK_PUB_ID,
        a.PUB_PTY_INST PUB_PTY_INST,
        a.WRK_INST WRK_INST,
        a.SUB_ACCOUNT_NUM ACCOUNT_NUM,
        '' pub_name ,
        b.pub_url pub_url,
        b.perm_option_desc perm_option_desc ,
        b.learn_more_desc learn_more_desc ,
        a.CRE_USER cre_user,
        a.CRE_DTM cre_dtm,
        NVL(a.UPD_USER, a.CRE_USER) upd_user,
        NVL(a.UPD_DTM, a.CRE_DTM) upd_dtm
     FROM   ccc_rlink_publisher_detail a, ccc_rlink_publisher b
     WHERE a.pub_pty_inst = p_pty_inst
     AND a.wrk_inst = p_work_inst
     and b.rlink_pub_id = a.rlink_pub_id ; 
     
     c_get_publisher_rec  c_get_publisher_cur%ROWTYPE;
   BEGIN
    p_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
    
    BEGIN
            
            FOR c_get_publisher_rec IN c_get_publisher_cur(p_pub_pty_inst, p_wrk_inst) LOOP
        
                BEGIN
                    p_pub_type.rlink_pub_id     := c_get_publisher_rec.rlink_pub_id;
                    p_pub_type.PUB_PTY_INST     := c_get_publisher_rec.pub_pty_inst;
                    p_pub_type.WRK_INST         := c_get_publisher_rec.wrk_inst;
                    p_pub_type.ACCOUNT_NUM      := c_get_publisher_rec.account_num;
                    p_pub_type.PUB_NAME         := c_get_publisher_rec.pub_name;
                    p_pub_type.PUB_URL          := c_get_publisher_rec.pub_url;
                    p_pub_type.PERM_OPTION_DESC := c_get_publisher_rec.perm_option_desc;
                    p_pub_type.LEARN_MORE_DESC  := c_get_publisher_rec.learn_more_desc;
                    p_pub_type.CRE_USER         := c_get_publisher_rec.cre_user;
                    p_pub_type.CRE_DTM          := c_get_publisher_rec.cre_dtm;
                    p_pub_type.UPD_USER         := c_get_publisher_rec.upd_user;
                    p_pub_type.UPD_DTM          := c_get_publisher_rec.upd_dtm;    
            
                    l_row_count := l_row_count + 1;
            
                END;  
        
        
            END LOOP;
    
    
     
           -- IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByPtyWrkInst');
          --  END IF; 
          
          IF l_row_count = 0 THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByPtyInstWrkInst');
          END IF;
                
         /*  EXCEPTION  when no_data_found THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'getPublisher', 'read');

                
            WHEN OTHERS THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'getPublisher', 'read'); */
                    
    END;                    
   
END get_rlink_pub_by_pty_wrk_inst;
   
PROCEDURE get_rlink_publisher_by_pub_id(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_id       IN   NUMBER
    )
   IS PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
    p_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
    
    BEGIN
    
    SELECT
        RLINK_PUB_ID,
        PUB_PTY_INST,
        WRK_INST,
        ACCOUNT_NUM,
        PUB_NAME,
        PUB_URL,
        PERM_OPTION_DESC,
        LEARN_MORE_DESC,
        CRE_USER,
        CRE_DTM,
        NVL(UPD_USER, CRE_USER),
        NVL(UPD_DTM, CRE_DTM)
    INTO
        p_pub_type.rlink_pub_id,
        p_pub_type.PUB_PTY_INST,
        p_pub_type.WRK_INST,
        p_pub_type.ACCOUNT_NUM,
        p_pub_type.PUB_NAME,
        p_pub_type.PUB_URL,
        p_pub_type.PERM_OPTION_DESC,
        p_pub_type.LEARN_MORE_DESC,
        p_pub_type.CRE_USER,
        p_pub_type.CRE_DTM,
        p_pub_type.UPD_USER,
        p_pub_type.UPD_DTM
     FROM   ccc_rlink_publisher
     WHERE rlink_pub_id = p_pub_id;
          
              --  IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherById');
              --  END IF; 
                
           EXCEPTION  when no_data_found THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherById');

                
            WHEN OTHERS THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherById'); 
                    
    END;                    
   
END get_rlink_publisher_by_pub_id;   

PROCEDURE create_rlink_publisher(
       RC_par         OUT  Return_Codes_type,
       p_pub_type_ret OUT  ccc_pub_type,
       p_pub_type     IN   ccc_pub_type 
    )
   IS PRAGMA AUTONOMOUS_TRANSACTION;
   l_rlink_pub_id NUMBER ;
   --p_pub_type2 := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
   
   BEGIN
   
        BEGIN
        
            SELECT CCC_RLINK_PUB_ID_SEQ.nextval 
            INTO l_rlink_pub_id
            FROM dual;
        
            INSERT INTO ccc_rlink_publisher VALUES
                ( l_rlink_pub_id,
                  p_pub_type.pub_pty_inst,
                  p_pub_type.account_num,
                  p_pub_type.wrk_inst,
                  p_pub_type.pub_name,
                  p_pub_type.pub_url,
                  p_pub_type.perm_option_desc,
                  p_pub_type.learn_more_desc,
                  p_pub_type.cre_user,
                  SYSDATE,
                  p_pub_type.cre_user,
                  SYSDATE);
                  
                  COMMIT;
                  
              --  IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'createRLinkPublisher');
              --  END IF;
              
                 p_pub_type_ret := p_pub_type;
              
                 p_pub_type_ret.rlink_pub_id := l_rlink_pub_id; 
                
           EXCEPTION  WHEN OTHERS THEN
              --  p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'createRLinkPublisher');  
                    
               ROLLBACK;                                             
        
        END;  
   
END create_rlink_publisher; 

   
PROCEDURE update_rlink_publisher(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    )
    IS PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
   
        BEGIN
        
            UPDATE ccc_rlink_publisher
                SET pub_url = nvl(p_pub_type.pub_url, pub_url),
                perm_option_desc = nvl(p_pub_type.perm_option_desc, perm_option_desc),
                learn_more_desc = nvl(p_pub_type.learn_more_desc, learn_more_desc),
                upd_dtm = SYSDATE,
                upd_user = p_pub_type.upd_user
           WHERE rlink_pub_id = p_pub_type.rlink_pub_id;
           --pub_pty_inst = p_pub_type.pub_pty_inst
           --AND wrk_inst = decode ( wrk_inst, NULL, wrk_inst, p_pub_type.wrk_inst);
           
           COMMIT;
           
                 RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'updateRLinkPublisher');
                                
           EXCEPTION  WHEN OTHERS THEN
               -- p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'updateRLinkPublisher');
                    
                ROLLBACK;                                           
        
        END ;         
   
END update_rlink_publisher ;

PROCEDURE delete_rlink_publisher(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    )
    IS PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
   
        BEGIN
        
            DELETE FROM ccc_rlink_publisher_detail
            WHERE 
                rlink_pub_id = p_pub_type.rlink_pub_id;
        
            DELETE FROM ccc_rlink_publisher
            WHERE 
                rlink_pub_id = p_pub_type.rlink_pub_id;
                        
            COMMIT;
                
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'deleteRLinkPublisher');
                                
           EXCEPTION  WHEN OTHERS THEN
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'deleteRLinkPublisher');
                    
                ROLLBACK;                                                 
                
        END ;
   
END delete_rlink_publisher;

PROCEDURE create_rlink_publisher_detail(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type 
    )
   IS PRAGMA AUTONOMOUS_TRANSACTION;
   
   l_app_msg VARCHAR2(250);
   l_row_count NUMBER;
   BEGIN
   
        BEGIN
        
            BEGIN
            
                SELECT COUNT (*)
                INTO l_row_count
                FROM ccc_rlink_publisher_detail
                WHERE sub_account_num = p_pub_type.account_num
                AND pub_pty_inst = p_pub_type.pub_pty_inst;
                
            END ;
            
            IF l_row_count = 0 THEN
        
                BEGIN
                
                INSERT INTO ccc_rlink_publisher_detail VALUES
                    ( p_pub_type.rlink_pub_id,
                    p_pub_type.pub_pty_inst,
                    p_pub_type.account_num,
                    p_pub_type.wrk_inst,
                    p_pub_type.cre_user,
                    SYSDATE,
                    p_pub_type.cre_user,
                    SYSDATE);
                  
                    COMMIT;
                  
                --  IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'createRLinkPublisher');
                --  END IF; 
                
                EXCEPTION  WHEN OTHERS THEN
              --  p_pub_type := NULL;
                    RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'createRLinkPublisher');  
                    
                ROLLBACK;
                
                END ;
            
            ELSE
                l_app_msg := 'Account# ' || p_pub_type.account_num || ' is already a RLink Publisher Sub Account';
                RC_par := NEW  Return_codes_type (0, 'CCC_CHILD_RECORD_FOUND', SQLCODE,
                        l_app_msg, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'createRLinkPublisher');
            END IF;  
                                                             
        
        END;  
   
END create_rlink_publisher_detail; 

PROCEDURE delete_rlink_publisher_detail(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    )
    IS PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
   
        BEGIN
        
            DELETE FROM ccc_rlink_publisher_detail
            WHERE 
                rlink_pub_id = p_pub_type.rlink_pub_id
                and sub_account_num = p_pub_type.account_num
                and pub_pty_inst = p_pub_type.pub_pty_inst
                and wrk_inst = decode(p_pub_type.wrk_inst, null, wrk_inst, p_pub_type.wrk_inst) ;
                                        
            COMMIT;
                
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'deleteRLinkPublisher');
                                
           EXCEPTION  WHEN OTHERS THEN
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'deleteRLinkPublisher');
                    
                ROLLBACK;                                                 
                
        END ;
   
END delete_rlink_publisher_detail;

PROCEDURE get_rightsholder_by_account(
        RC_par OUT Return_Codes_type,
        p_pub_type  OUT   ccc_pub_type,
        p_account   IN    NUMBER
    )
    IS PRAGMA AUTONOMOUS_TRANSACTION;
    
    l_rh_count NUMBER := 0;
    l_pub_count NUMBER := 0;
    l_pub_detail_count NUMBER := 0;
    l_app_msg VARCHAR2(250);
    
    BEGIN
        p_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
    
        BEGIN
        
            SELECT DISTINCT a.pty_inst, a.orgname, a.account_num
                INTO p_pub_type.pub_pty_inst, 
                     p_pub_type.pub_name,
                     p_pub_type.account_num
                FROM ccc_party a, ccc_pty_pty_type b, ccc_party_type c
                WHERE a.account_num = p_account
                AND a.person_org_type = 'O'
                AND b.pty_inst = a.pty_inst
                AND b.ptt_inst = c.ptt_inst
                AND ( c.party_type_dscr = 'RIGHTSHOLDER'
                        OR c.party_type_dscr = 'PUBLISHER')
                AND a.pty_inst NOT IN
                ( select pub_pty_inst
                  from ccc_rlink_publisher)
                  AND a.pty_inst NOT IN
                ( select pub_pty_inst
                  from ccc_rlink_publisher_detail);
                
               -- IF SQL%FOUND THEN
                    RC_par := NEW  Return_codes_type (1, 'SUCCESS', SQLCODE,
                                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByAccount');
              --  END IF; 
                
           EXCEPTION  when no_data_found THEN
           
                SELECT count(*)
                INTO l_rh_count
                FROM ccc_party a, ccc_pty_pty_type b, ccc_party_type c
                WHERE a.account_num = p_account
                AND a.person_org_type = 'O'
                AND b.pty_inst = a.pty_inst
                AND b.ptt_inst = c.ptt_inst
                AND ( c.party_type_dscr = 'RIGHTSHOLDER'
                OR c.party_type_dscr = 'PUBLISHER')  ;
                
                SELECT count(*)
                INTO l_pub_count
                FROM ccc_rlink_publisher
                WHERE account_num = p_account;
                
                SELECT count(*)
                INTO l_pub_detail_count
                FROM ccc_rlink_publisher_detail
                WHERE sub_account_num = p_account;
                
                IF l_pub_count > 0 THEN
                    l_app_msg := 'Account# ' || p_account || ' is already a RLink Publisher';
                ELSIF l_pub_detail_count > 0 THEN
                    l_app_msg := 'Account# ' || p_account || ' is already a RLink Publisher Sub Account';
                ELSIF
                    l_rh_count = 0 THEN
                    l_app_msg := 'Account# ' || p_account || ' is not a Publisher' ; 
                END IF; 
                
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        l_app_msg, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByAccount');

                
            WHEN OTHERS THEN
                p_pub_type := NULL;
                RC_par := NEW  Return_codes_type (-1, 'ORACLEERROR', SQLCODE,
                    SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherByAccount');
                
        END ;
    
    
    
END get_rightsholder_by_account ;

PROCEDURE get_publishers(
        RC_par          OUT Return_Codes_type,
        p_ccc_pubs_type OUT ccc_pubs_type
    )
    IS
   -- PRAGMA AUTONOMOUS_TRANSACTION;
       
    TYPE cur_type IS REF CURSOR;
    
    CURSOR c_pub_cursor  IS
    SELECT * from ccc_rlink_publisher order by account_num;
    
    c_pub_cursor_rec  c_pub_cursor%ROWTYPE;
    l_publishers_tbl  ccc_pub_list_type;
    l_ccc_pub_type    ccc_pub_type;
   -- l_query     VARCHAR2(1000);
    l_rec_num NUMBER;
    l_row_count NUMBER := 0;
    
    BEGIN
              
       -- BEGIN
        
           -- l_query := 'SELECT * FROM ccc_rlink_publisher;';
            
            BEGIN
            
              --  p_ccc_pubs_type := NEW ccc_pubs_type( );
                l_publishers_tbl := NEW ccc_pub_list_type( );
                
                l_rec_num := 1;
                
                FOR c_pub_cursor_rec IN c_pub_cursor LOOP
            
                    --DBMS_OUTPUT.put_line('Cursor Opened:');
                
                    l_ccc_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
                    
                    l_ccc_pub_type.rlink_pub_id := c_pub_cursor_rec.rlink_pub_id;
                    l_ccc_pub_type.pub_pty_inst := c_pub_cursor_rec.pub_pty_inst;
                    l_ccc_pub_type.wrk_inst := c_pub_cursor_rec.wrk_inst;
                    l_ccc_pub_type.account_num := c_pub_cursor_rec.account_num;
                    l_ccc_pub_type.pub_name := c_pub_cursor_rec.pub_name;
                    l_ccc_pub_type.pub_url := c_pub_cursor_rec.pub_url;
                    l_ccc_pub_type.perm_option_desc := c_pub_cursor_rec.perm_option_desc;
                    l_ccc_pub_type.learn_more_desc := c_pub_cursor_rec.learn_more_desc;
                    l_ccc_pub_type.cre_user := c_pub_cursor_rec.cre_user;
                    l_ccc_pub_type.cre_dtm := c_pub_cursor_rec.cre_dtm;
                    l_ccc_pub_type.upd_user:= c_pub_cursor_rec.upd_user;
                    l_ccc_pub_type.upd_dtm := c_pub_cursor_rec.upd_dtm;
                
               /* dbms_output.put_line('Pub Id: ' || l_ccc_pub_type.rlink_pub_id);
                dbms_output.put_line('Pub Name: ' || l_ccc_pub_type.pub_name); */
            
               /* LOOP
                    FETCH c_pub_cursor;
                    INTO c_pub_cursor_rec; */
                    
                    --    dbms_output.put_line('Pub Id: ' || c_pub_cursor_rec.rlink_pub_id);
                    
                    l_publishers_tbl.EXTEND;
                    l_publishers_tbl( l_rec_num ) := l_ccc_pub_type;
                    l_rec_num := l_rec_num + 1;
                    l_row_count := l_row_count + 1;                    
                    
                END LOOP;
                
                p_ccc_pubs_type := new ccc_pubs_type;
                p_ccc_pubs_type.publishers := l_publishers_tbl;
                                
              --  dbms_output.put_line('Total Count: ' || l_publishers_tbl.count);
             /*   dbms_output.put_line('Obj Count 1: ' || p_ccc_pubs_type.publishers(1).rlink_pub_id);
                dbms_output.put_line('Obj Count 2: ' || p_ccc_pubs_type.publishers(2).rlink_pub_id);
                dbms_output.put_line('Obj Count 3: ' || p_ccc_pubs_type.publishers(3).rlink_pub_id);
                dbms_output.put_line('Obj Count 4: ' || p_ccc_pubs_type.publishers(4).rlink_pub_id); */
                
                RC_par :=
                            return_codes_type( 1,
                                'SUCCESS',
                                SQLCODE,
                                SQLERRM,
                                SQL%ROWCOUNT,
                                'ccc_rlink_publisher_pkg',
                                'getPublishers' );
                                
                IF l_row_count = 0 THEN
                    p_ccc_pubs_type := NULL;
                    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublishers');
                END IF;
                
                EXCEPTION
                    WHEN OTHERS THEN
                        RC_par :=
                            return_codes_type( -1,
                               'ORACLEERROR',
                               SQLCODE,
                               SQLERRM,
                               SQL%ROWCOUNT,
                               'ccc_rlink_publisher_pkg',
                               'getPublishers' );
                    
               -- CLOSE c_pub_cursor;
            END;
        
        
        
        
    --    END;
    
    
    
END get_publishers; 

PROCEDURE get_rlink_pub_detail_by_id(
        RC_par           OUT   Return_Codes_type,
        p_ccc_pubs_type  OUT   ccc_pubs_type,
        p_rlink_pub_id   IN    NUMBER
    )
    IS
    
    CURSOR c_pub_detail_cursor  IS
    SELECT a.rlink_pub_id rlink_pub_id, a.pub_pty_inst pub_pty_inst, 
            a.sub_account_num sub_account_num, a.wrk_inst wrk_inst,
            a.cre_user cre_user, a.cre_dtm cre_dtm, a.upd_user upd_user, a.upd_dtm upd_dtm,
             b.orgname orgname
    FROM ccc_rlink_publisher_detail a, ccc_party b
    WHERE a.rlink_pub_id = p_rlink_pub_id
    AND b.pty_inst = a.pub_pty_inst
    AND b.person_org_type = 'O'
    ORDER BY a.sub_account_num ;
    
    c_pub_detail_cursor_rec  c_pub_detail_cursor%ROWTYPE;
    l_publishers_tbl  ccc_pub_list_type;
    l_ccc_pub_type    ccc_pub_type;
    --l_query     VARCHAR2(1000);
    l_rec_num NUMBER;
    l_row_count NUMBER := 0;
    
BEGIN

    BEGIN
            
                l_publishers_tbl := NEW ccc_pub_list_type( );
                
                l_rec_num := 1;
                
                FOR c_pub_detail_cursor_rec IN c_pub_detail_cursor LOOP
            
                DBMS_OUTPUT.put_line('Cursor Opened:');
                
                l_ccc_pub_type := new ccc_pub_type ( 0, 0, 0, 0, '', '', '', '', '', SYSDATE, '', SYSDATE);
                
                l_ccc_pub_type.rlink_pub_id := c_pub_detail_cursor_rec.rlink_pub_id;
                l_ccc_pub_type.pub_pty_inst := c_pub_detail_cursor_rec.pub_pty_inst;
                l_ccc_pub_type.wrk_inst     := c_pub_detail_cursor_rec.wrk_inst;
                l_ccc_pub_type.account_num  := c_pub_detail_cursor_rec.sub_account_num;
                l_ccc_pub_type.pub_name     := c_pub_detail_cursor_rec.orgname;
                l_ccc_pub_type.cre_user     := c_pub_detail_cursor_rec.cre_user;
                l_ccc_pub_type.cre_dtm      := c_pub_detail_cursor_rec.cre_dtm;
                l_ccc_pub_type.upd_user     := c_pub_detail_cursor_rec.upd_user;
                l_ccc_pub_type.upd_dtm      := c_pub_detail_cursor_rec.upd_dtm;
                
                dbms_output.put_line('Pub Id: ' || l_ccc_pub_type.rlink_pub_id);
                dbms_output.put_line('Pub Name: ' || l_ccc_pub_type.pub_name);
            
               /* LOOP
                    FETCH c_pub_cursor;
                    INTO c_pub_cursor_rec; */
                    
                    --    dbms_output.put_line('Pub Id: ' || c_pub_cursor_rec.rlink_pub_id);
                    
                    l_publishers_tbl.EXTEND;
                    l_publishers_tbl( l_rec_num ) := l_ccc_pub_type;
                    l_rec_num := l_rec_num + 1; 
                    l_row_count := l_row_count + 1;                   
                    
                END LOOP;
                
                p_ccc_pubs_type := new ccc_pubs_type;
                p_ccc_pubs_type.publishers := l_publishers_tbl;
                
               -- CLOSE c_pub_detail_cursor;
                
               -- dbms_output.put_line('Total Count: ' || l_publishers_tbl.count);
                             
                RC_par :=
                            return_codes_type( 1,
                                'SUCCESS',
                                SQLCODE,
                                SQLERRM,
                                SQL%ROWCOUNT,
                                'ccc_rlink_publisher_pkg',
                                'getPublishersDetailById' );
                                
                IF l_row_count = 0 THEN
                    p_ccc_pubs_type := NULL;
                    RC_par := NEW  Return_codes_type (0, 'NO_DATA_FOUND', SQLCODE,
                        SQLERRM, SQL%ROWCOUNT, 'ccc_rlink_publisher_pkg', 'getPublisherDetailById');
                END IF;
                                
                EXCEPTION
                    WHEN OTHERS THEN
                        RC_par :=
                            return_codes_type( -1,
                               'ORACLEERROR',
                               SQLCODE,
                               SQLERRM,
                               SQL%ROWCOUNT,
                               'ccc_rlink_publisher_pkg',
                               'getPublishersDetailById' );
                    
               -- CLOSE c_pub_cursor;
            END;
    
    
    
END get_rlink_pub_detail_by_id ;      

END CCC_RLINK_PUBLISHER_PKG;
/
