create or replace package CCC_ORDER_PARTNER_PKG as

    --  The order partner cross-reference table contains the order confirmation
    --  number and the partner id (SID or MC.ID value passed in by permissions
    --  direct customers).  The ability to read the value probably is not
    --  necessary, we will only be populating the table - and any reference
    --  made to the table will be from other PL/SQL scripts and packages that
    --  will likely have direct access to the table.

    procedure READ_XREF_BY_CONFIRM_NUMBER(
        RC_PAR out RETURN_CODES_TYPE,
        ORDER_PARTNER_XREF_PAR out ORDER_PARTNER_XREF_TYPE,
        CONFIRM_NUMBER_PAR in NUMBER
    );

    procedure WRITE_XREF(
        RC_PAR out RETURN_CODES_TYPE,
        ORDER_PARTNER_XREF_PAR in ORDER_PARTNER_XREF_TYPE
    );

end CCC_ORDER_PARTNER_PKG;
/