CREATE OR REPLACE PACKAGE BODY ccc_autodunning_on_demand_pkg
AS
   TYPE ref_cursor IS REF CURSOR;

   PROCEDURE get_autodunning_params (
      rc_par                          OUT   return_codes_type,
      p_ccc_autodunning_params_type   OUT   ccc_autodunning_params_type
   )
   IS
      -- PRAGMA AUTONOMOUS_TRANSACTION;
      TYPE cur_type IS REF CURSOR;

      CURSOR c_autodunning_param_cursor
      IS
         SELECT   *
             FROM ccc_autodunning_params
         ORDER BY days_past_due DESC;

      c_autodunning_param_cursor_rec   c_autodunning_param_cursor%ROWTYPE;
      l_autodunning_params_tbl         ccc_autodunningparam_list_type;
      l_ccc_autodunning_param_type     ccc_autodunning_param_type;
      l_rec_num                        NUMBER;
      l_row_count                      NUMBER                            := 0;
   BEGIN
      -- BEGIN
      BEGIN
         l_autodunning_params_tbl := NEW ccc_autodunningparam_list_type ();
         l_rec_num := 1;

         FOR c_autodunning_param_cursor_rec IN c_autodunning_param_cursor
         LOOP
            --DBMS_OUTPUT.put_line('Cursor Opened:');
            l_ccc_autodunning_param_type :=
                                    NEW ccc_autodunning_param_type ('', 0, 0);
            l_ccc_autodunning_param_type.product_type :=
                                  c_autodunning_param_cursor_rec.product_type;
            l_ccc_autodunning_param_type.dayspastdue :=
                                 c_autodunning_param_cursor_rec.days_past_due;
            l_ccc_autodunning_param_type.enabled :=
                                       c_autodunning_param_cursor_rec.enabled;
            DBMS_OUTPUT.put_line (   'product type: '
                                  || l_ccc_autodunning_param_type.product_type
                                 );
            DBMS_OUTPUT.put_line (   'days past due: '
                                  || l_ccc_autodunning_param_type.dayspastdue
                                 );
            DBMS_OUTPUT.put_line (   'enabled: '
                                  || l_ccc_autodunning_param_type.enabled
                                 );
            l_autodunning_params_tbl.EXTEND;
            l_autodunning_params_tbl (l_rec_num) :=
                                                  l_ccc_autodunning_param_type;
            l_rec_num := l_rec_num + 1;
            l_row_count := l_row_count + 1;
         END LOOP;

         p_ccc_autodunning_params_type := NEW ccc_autodunning_params_type;
         p_ccc_autodunning_params_type.autodunningparams :=
                                                      l_autodunning_params_tbl;
         /* dbms_output.put_line('Total Count: ' || l_autodunning_params_tbl.count);
            dbms_output.put_line('Obj Count 1: ' || p_ccc_autodunning_params_type.autodunningparams(1).product_type);
            dbms_output.put_line('Obj Count 2: ' || p_ccc_autodunning_params_type.autodunningparams(2).product_type); */
         rc_par :=
            return_codes_type (1,
                               'SUCCESS',
                               SQLCODE,
                               SQLERRM,
                               SQL%ROWCOUNT,
                               'ccc_autodunning_on_demand_pkg',
                               'get_autodunning_params'
                              );

         IF l_row_count = 0
         THEN
            p_ccc_autodunning_params_type := NULL;
            rc_par :=
               NEW return_codes_type (0,
                                      'NO_DATA_FOUND',
                                      SQLCODE,
                                      SQLERRM,
                                      SQL%ROWCOUNT,
                                      'ccc_autodunning_on_demand_pkg',
                                      'get_autodunning_params'
                                     );
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            rc_par :=
               return_codes_type (-1,
                                  'ORACLEERROR',
                                  SQLCODE,
                                  SQLERRM,
                                  SQL%ROWCOUNT,
                                  'ccc_autodunning_on_demand_pkg',
                                  'get_autodunning_params'
                                 );
      -- CLOSE c_pub_cursor;
      END;
   --    END;
   END get_autodunning_params;
END ccc_autodunning_on_demand_pkg;
/
