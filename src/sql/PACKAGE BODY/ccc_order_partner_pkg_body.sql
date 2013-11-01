create or replace
package body CCC_ORDER_PARTNER_PKG as

    --  The order partner cross-reference table contains the order confirmation
    --  number and the partner id (SID or MC.ID value passed in by permissions
    --  direct customers).  The ability to read the value probably is not
    --  necessary, we will only be populating the table - and any reference
    --  made to the table will be from other PL/SQL scripts and packages that
    --  will likely have direct access to the table.

    procedure READ_XREF_BY_CONFIRM_NUMBER(
        RC_PAR out RETURN_CODES_TYPE,
        ORDER_PARTNER_XREF_PAR out ORDER_PARTNER_XREF_TYPE,
        CONFIRM_NUMBER_PAR in NUMBER )
    is
        pragma AUTONOMOUS_TRANSACTION;
    begin
        ORDER_PARTNER_XREF_PAR := new ORDER_PARTNER_XREF_TYPE( CONFIRM_NUMBER_PAR, '' );

        select ORD.PARTNER_ID
            into ORDER_PARTNER_XREF_PAR.PARTNER_ID
            from CCCTF.CCC_ORDER ORD
            where ORD.PUR_INST = CONFIRM_NUMBER_PAR;

        RC_PAR := new RETURN_CODES_TYPE( 1, 'SUCCESS', SQLCODE,
            SQLERRM, SQL%ROWCOUNT, 'READ_XREF_BY_CONFIRM_NUMBER', 'read' );

        commit;

    exception
        when NO_DATA_FOUND then
            ORDER_PARTNER_XREF_PAR := null;
            RC_PAR := new RETURN_CODES_TYPE( 0, 'NO_DATA_FOUND', SQLCODE,
                SQLERRM, SQL%ROWCOUNT, 'READ_XREF_BY_CONFIRM_NUMBER', 'read' );

            rollback;

        when OTHERS then
            ORDER_PARTNER_XREF_PAR := null;
            RC_PAR := new RETURN_CODES_TYPE( -1, 'ORACLEERROR', SQLCODE,
                SQLERRM, SQL%ROWCOUNT, 'READ_XREF_BY_CONFIRM_NUMBER', 'read' );

            rollback;

    end READ_XREF_BY_CONFIRM_NUMBER;

    procedure WRITE_XREF(
        RC_PAR out RETURN_CODES_TYPE,
        ORDER_PARTNER_XREF_PAR in ORDER_PARTNER_XREF_TYPE )
    is
        pragma AUTONOMOUS_TRANSACTION;
    begin
        update CCCTF.CCC_ORDER ORD
            set ORD.PARTNER_ID = ORDER_PARTNER_XREF_PAR.PARTNER_ID
            where ORD.PUR_INST = ORDER_PARTNER_XREF_PAR.ORDER_CONFIRM;

        RC_PAR := new RETURN_CODES_TYPE( 1, 'SUCCESS', SQLCODE,
            SQLERRM, SQL%ROWCOUNT, 'WRITE_XREF', 'update' );

        commit;

    exception
        when OTHERS then
            RC_PAR := new RETURN_CODES_TYPE( -1, 'ORACLEERROR', SQLCODE,
                SQLERRM, SQL%ROWCOUNT, 'READ_XREF_BY_CONFIRM_NUMBER', 'update' );

            rollback;

    end WRITE_XREF;

end CCC_ORDER_PARTNER_PKG;
/