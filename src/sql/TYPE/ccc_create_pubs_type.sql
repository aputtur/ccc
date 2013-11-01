CREATE OR REPLACE 
TYPE CCC_PUBS_TYPE 
AS OBJECT 
(
    publishers       ccc_pub_list_type,
    CONSTRUCTOR FUNCTION ccc_pubs_type
      RETURN SELF AS RESULT
);
/
--grant execute on ccc_pubs_type to cc_app_role
--/


