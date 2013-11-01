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
/