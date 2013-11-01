CREATE or replace FUNCTION stragg(input varchar2) RETURN varchar2
PARALLEL_ENABLE AGGREGATE USING string_agg_type;
/