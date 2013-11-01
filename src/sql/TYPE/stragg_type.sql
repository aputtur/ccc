create or replace type string_agg_type as object
(
   total varchar2(4000),

   static function
        ODCIAggregateInitialize(sctx IN OUT string_agg_type )
        return number,

   member function
       ODCIAggregateIterate(self IN OUT string_agg_type ,
                            value IN varchar2
                             )
       return number,

  member function
       ODCIAggregateTerminate(self IN string_agg_type,
                              returnValue OUT  varchar2,
                             flags IN number)
       return number,

  member function
       ODCIAggregateMerge(self IN OUT string_agg_type,
                          ctx2 IN string_agg_type)
       return number
);
/

