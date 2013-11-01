--
-- This function should be updated and redeployed with every release of the 
-- copyright.com database code.
--
-- author: jarbo
--

-- 
-- FUNCTION: WHAT_SCHEMA_VERSION 
--

-- BEGIN PL/SQL BLOCK (do not remove this line) -------------------------------- 
CREATE OR REPLACE FUNCTION WHAT_SCHEMA_VERSION RETURN VARCHAR 
IS 
BEGIN 
 
   RETURN '@schema.version@';
 
END; 
-- END PL/SQL BLOCK (do not remove this line) ---------------------------------- 
/ 

