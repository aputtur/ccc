CREATE OR REPLACE
type ORDER_PARTNER_XREF_TYPE as OBJECT (
    ORDER_CONFIRM NUMBER(38,0),
    PARTNER_ID VARCHAR2(50 BYTE)
);
/