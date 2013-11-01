CREATE OR REPLACE 
PACKAGE ccc_rlink_publisher_pkg
  IS
--
-- CCC_RLINK_PUBLISHER_PKG
--
-- Purpose: Intended to be the point of entry for all
--          RightsLink Publisher data interaction
--          in CC schema
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  ------------------------------------------
   -- Enter package declarations as shown below

TYPE ref_cursor IS REF CURSOR;

   PROCEDURE get_rlink_pub_by_pty_inst(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_pty_inst IN   NUMBER
      );
      
   PROCEDURE get_rlink_pub_by_pty_wrk_inst(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_pty_inst IN   NUMBER, 
      p_wrk_inst     IN   NUMBER 
      );      
    
   PROCEDURE get_rlink_publisher_by_pub_id(
      RC_par         OUT  Return_Codes_type,
      p_pub_type     OUT  ccc_pub_type,
      p_pub_id       IN   NUMBER
    );
    
   PROCEDURE create_rlink_publisher(
       RC_par         OUT  Return_Codes_type,
       p_pub_type_ret OUT  ccc_pub_type,
       p_pub_type     IN   ccc_pub_type 
    );
    
   PROCEDURE update_rlink_publisher(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    );
    
   PROCEDURE delete_rlink_publisher(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    );
          
   PROCEDURE get_rightsholder_by_account(
        RC_par      OUT Return_Codes_type,
        p_pub_type  OUT   ccc_pub_type,
        p_account   IN    NUMBER
    );
    
   PROCEDURE get_publishers(
        RC_par          OUT Return_Codes_type,
        p_ccc_pubs_type OUT ccc_pubs_type
    );
    
   PROCEDURE create_rlink_publisher_detail(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type 
    );
    
   PROCEDURE delete_rlink_publisher_detail(
       RC_par        OUT  Return_Codes_type,
       p_pub_type    IN   ccc_pub_type
    );
    
   PROCEDURE get_rlink_pub_detail_by_id(
        RC_par           OUT   Return_Codes_type,
        p_ccc_pubs_type  OUT   ccc_pubs_type,
        p_rlink_pub_id   IN    NUMBER
    );
END; -- Package spec
/
