package com.copyright.ccc.business.data;

import java.util.Date;
import java.util.List;

public interface CCInvoicePaymentLog
{
    public Date getTimestamp();
    public String getAccountNumber();
    public String getReceiptNumber();
    public String getLastFourDigits();
    public List<String> getTransactionNumbers();
    public String getTotalAmount();
    
    public void setTimestamp(Date ts);
    public void setAccountNumber(String acctno);
    public void setReceiptNumber(String rcptno);
    public void setLastFourDigits(String digits);
    public void setTransactionNumbers(List<String> trnxnos);
    public void setTotalAmount(String amt);
}