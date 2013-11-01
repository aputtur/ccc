CREATE OR REPLACE PACKAGE ccc_user_pkg AS

PROCEDURE ReadUserForAUID (
  RC_par OUT Return_Codes_type,
  User_par OUT User_type,
  AU_Id_par IN VARCHAR2);

PROCEDURE ReadUserForNewSession (
  RC_par OUT Return_Codes_type,
  User_par OUT User_type,
  AU_Id_par IN VARCHAR2);

PROCEDURE ReadUserForLoginUsername (
  RC_par OUT Return_Codes_type,
  User_par OUT User_type,
  User_in_par IN User_type);

PROCEDURE InsertUser (
  RC_par OUT Return_Codes_type,
  User_out_par OUT User_type,
  User_in_par IN User_type);

PROCEDURE UpdateUser (
  RC_par OUT Return_Codes_type,
  User_out_par OUT User_type,
  User_in_par IN User_type);
  
PROCEDURE ReadUserForUserIdentifier (RC_par OUT Return_Codes_type,
     User_par OUT User_type,
     User_identifier_par IN VARCHAR2);  

PROCEDURE UpdatePasswordEnforcement (RC_par OUT Return_Codes_type,
    User_out_par OUT User_type,
    User_in_par IN User_type);  

/*
PROCEDURE ReadUserCookie (RC_par OUT Return_Codes_type,
    User_par IN OUT User_type);

PROCEDURE ReadUserForUserIdentifier (RC_par OUT Return_Codes_type,
     User_par OUT User_type,
       User_identifier_par IN VARCHAR2,
       CUST_ID_par NUMBER);

PROCEDURE ReadUserForPartyID (RC_par OUT Return_Codes_type,
    User_par OUT User_type,
       Party_Id_par IN NUMBER);

PROCEDURE ReadUserForOIDPK (RC_par OUT Return_Codes_type,
    User_par OUT User_type,
       OID_PK_par IN VARCHAR2);

PROCEDURE ReadUser  (RC_par OUT Return_Codes_type,
    User_par OUT User_type,
       Usr_Id_par IN NUMBER);

PROCEDURE DeleteUser (RC_par OUT Return_Codes_type,
    User_par IN User_type);
   -- USR_ID_par IN NUMBER)
*/
END CCC_User_pkg;
/
