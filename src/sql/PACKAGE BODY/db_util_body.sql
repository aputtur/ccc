create or replace package body db_util
as
    dropset dropset_type;
    nodrops dropset_type;

    procedure current_objects
    is

    begin
        for orec in (select object_name,object_type from user_objects order by 2,1)
        loop
            dbms_output.put_line(orec.object_type||': '||orec.object_name);
        end loop;
    end;

    procedure drop_user_objects_internal
    is
        stmt varchar2(500);
        cursor drop_cur
        is
           select 'drop ' || object_type || ' ' || object_name
                || decode(object_type,
                     'CLUSTER', ' including tables cascade constraints',
                     'TABLE', ' cascade constraints',
                     'TYPE', ' FORCE',
                     '')
           from user_objects
           where object_type in
                (dropset(1), dropset(2), dropset(3), dropset(4), dropset(5), dropset(6), dropset(7), dropset(8), dropset(9))
           and object_name not in (nodrops(1),nodrops(2),nodrops(3),nodrops(4),nodrops(5),
                                   nodrops(6),nodrops(7),nodrops(8),nodrops(9), 
                                   nodrops(10), nodrops(11), nodrops(12), nodrops(13) )
           and object_name not like 'DR$%'
           order by decode(object_type,'TABLE',1,
                                       'VIEW',2,
                                       'SEQUENCE',3,
                                       'SYNONYM', 4,
                                       'FUNCTION',5,
                                       'PROCEDURE',6,
                                       'PACKAGE',7,
                                       'TYPE',8);
    begin
        open drop_cur;
        loop
            fetch drop_cur into stmt;
            exit when drop_cur%notfound;
            dbms_output.put_line(stmt);
            execute immediate stmt;
        end loop;
        close drop_cur;

        dbms_output.put_line('Objects remaining in schema');
        current_objects;
    end;

    procedure drop_user_objects
    is
    begin
        dbms_output.put_line('Dropping all database artifacts from ' || user || '...');

        dropset := dropset_type ('CLUSTER', 'TABLE', 'VIEW', 'SEQUENCE', 'SYNONYM', 'FUNCTION','PROCEDURE', 'PACKAGE', 'TYPE');
        nodrops := dropset_type ('DROPSET_TYPE','DB_UTIL','~','~','~','~','~','~','~','~','~','~','~');
        drop_user_objects_internal;
    end;

    procedure drop_user_logic_objects
    is
    begin
        dbms_output.put_line('Dropping only logic components from ' || user || '...');

        dropset := dropset_type ('', '', '', '', '', 'FUNCTION','PROCEDURE', 'PACKAGE', 'TYPE');
        nodrops := dropset_type('~', 'DROPSET_TYPE', 'DB_UTIL', 'TITLE_TYPE','TITLE_VARRAY','IDNO_TYPE',
                                    'IDNO_VARRAY','CONTRIB_TYPE','CONTRIB_VARRAY',
                                    'SEARCH_RESULT_TYPE','SEARCH_RESULT_SET_TYPE',
                                    'SEARCH_RIGHT_TABLE_TYPE', 'SEARCH_RIGHT_TYPE');
        drop_user_objects_internal;
    end;


    function drop_object(p_object_name in varchar2) return number
    is
        drop_stmt varchar2(200);
    begin
        begin
           select 'alter table '||table_name||' drop constraint '||constraint_name
           into drop_stmt
           from user_constraints
           where constraint_name = upper(p_object_name);

           execute immediate drop_stmt;

           exception
            when NO_DATA_FOUND then
                null;
        end;

       select 'drop ' || object_type || ' ' || object_name
            || decode(object_type,
                 'CLUSTER', ' including tables cascade constraints',
                 'TABLE', ' cascade constraints',
                 'TYPE', ' force',
                 '')
       into drop_stmt
       from user_objects
       where object_name = upper(p_object_name);

       dbms_output.put_line(drop_stmt);

       execute immediate drop_stmt;

       return 1;

       exception
        when NO_DATA_FOUND then
            return 0;
    end;

 	procedure drop_object(p_object_name in varchar2)
 	is
 		x	number;
 	begin
 		x := drop_object(p_object_name);
 	end;
 	

    procedure disable_referring_constraints(p_table_name in varchar2)
    is
    begin
        for cmd in (select 'alter table '||table_name||' disable constraint '||constraint_name as stmt
                    from user_constraints fk
                    where fk.r_constraint_name in (select constraint_name
                                                    from user_constraints
                                                    where table_name = p_table_name -- table you want to truncate
                                                    AND CONSTRAINT_TYPE = 'P')
                    and fk.constraint_type = 'R')
        loop
               dbms_output.put_line(cmd.stmt);
               execute immediate cmd.stmt;
        end loop;

    end;

    procedure enable_referring_constraints(p_table_name in varchar2)
    is
    begin
        for cmd in (select 'alter table '||table_name||' enable constraint '||constraint_name as stmt
                    from user_constraints fk
                    where fk.r_constraint_name in (select constraint_name
                                                    from user_constraints
                                                    where table_name = p_table_name -- table you want to truncate
                                                    AND CONSTRAINT_TYPE = 'P')
                    and fk.constraint_type = 'R')
        loop
               dbms_output.put_line(cmd.stmt);
               execute immediate cmd.stmt;
        end loop;

    end;


end;
/