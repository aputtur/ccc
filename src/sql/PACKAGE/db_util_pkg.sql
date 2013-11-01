create or replace package db_util
as

 procedure drop_user_objects;

 procedure drop_user_logic_objects;


 procedure enable_referring_constraints(p_table_name in varchar2);
 procedure disable_referring_constraints(p_table_name in varchar2);
 procedure drop_object(p_object_name in varchar2);
 
 function drop_object(p_object_name in varchar2) return number;

 TYPE dropset_type is VARRAY(13) OF VARCHAR2(64);

end;
/