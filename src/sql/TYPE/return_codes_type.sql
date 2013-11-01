create or replace TYPE Return_Codes_type AS OBJECT(
      appReturnCode NUMBER,
      appReturnDesc VARCHAR2(250),
      dbReturnCode NUMBER,
      dbReturnDesc VARCHAR2(250),
      effectedRows NUMBER,
      procName VARCHAR2(250),
      procOperation VARCHAR2(250));
/