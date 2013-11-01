package com.copyright.ccc.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import com.copyright.svc.artransaction.api.data.ARTransaction;
import com.copyright.ccc.business.services.payment.PaymentResponse;

//  This form extends UnpaidInvoiceForm which in turn extends
//  BasePaymentForm.  The reason for the extension is to provide
//  the ability to pay user invoices by a non-registered 3rd party.
//  To do this we need to add a few fields.

public class AnonymousUnpaidInvoiceForm extends UnpaidInvoiceForm
{
    private String emailAddress;
    private String emailAddressConfirm;
    private String invoiceNumber;
    private String originalInvoiceAmount;
    private String arAccountNumber;
    private PaymentResponse paymentResponse;

    //  We don't have a saved profile.  Just keep some
    //  display fields...  These will be given BACK to
    //  us from cybersource.

    private String cardTypeDisplay;
    private String cardHolderDisplay;
    private String cardExpirationDisplay;
    private String cardLastFourDisplay;

    //  Simple constructure, defers to parent first.

    public AnonymousUnpaidInvoiceForm() {
        super( true );

        emailAddress          = null;
        emailAddressConfirm   = null;
        invoiceNumber         = null;
        originalInvoiceAmount = null;
        arAccountNumber       = null;
        paymentResponse       = null;

        cardTypeDisplay       = null;
        cardHolderDisplay     = null;
        cardExpirationDisplay = null;
        cardLastFourDisplay   = null;
    }

    public void setEmailAddress( String s )             { emailAddress = s;          }
    public void setEmailAddressConfirm( String s )      { emailAddressConfirm = s;   }
    public void setArAccountNumber( String s )          { arAccountNumber = s;       }
    public void setCardTypeDisplay( String s )          { cardTypeDisplay = s;       }
    public void setCardHolderDisplay( String s )        { cardHolderDisplay = s;     }
    public void setCardExpirationDisplay( String s )    { cardExpirationDisplay = s; }
    public void setCardLastFourDisplay( String s )      { cardLastFourDisplay = s;   }
    public void setPaymentResponse( PaymentResponse x ) { paymentResponse = x;       }
    public void setOriginalInvoiceAmount( String s )
    { 
        //  Minor format fixing. Strip all space-like chars (tab, space).
        //  Remove a leading "$" sign.

        originalInvoiceAmount = s.replaceAll( "\\s", "" );

        if (originalInvoiceAmount.startsWith( "$" ))
            originalInvoiceAmount = originalInvoiceAmount.substring( 1 );
    }
    public void setInvoiceNumber( String s )
    {
        if (s != null) {
            //  If we have something, uppercase it... in case we have
            //  an RLNK invoice number.
            
            invoiceNumber = s.toUpperCase(); 
        }
        else {
            invoiceNumber = s;
        }
    }

    public String getEmailAddress()          { return emailAddress;          }
    public String getEmailAddressConfirm()   { return emailAddressConfirm;   }
    public String getInvoiceNumber()         { return invoiceNumber;         }
    public String getOriginalInvoiceAmount() { return originalInvoiceAmount; }
    public String getArAccountNumber()       { return arAccountNumber;       }
    public String getCardTypeDisplay()       { return cardTypeDisplay;       }
    public String getCardHolderDisplay()     { return cardHolderDisplay;     }
    public String getCardExpirationDisplay() { return cardExpirationDisplay; }
    public String getCardLastFourDisplay()   { return cardLastFourDisplay;   }

    public PaymentResponse getPaymentResponse()
    {
        return paymentResponse;
    }

    @Override
    public void reset( ActionMapping      mapping
                     , HttpServletRequest request )
    {
        _logger.info("\nAnonymousUnpaidInvoiceForm.reset()\n");
        this.setSelectedInvoices( null );
    }

    @Override
    public String toString()
    {
        StringBuffer obuf = new StringBuffer();

        obuf.append( "\nAnonymousUnpaidInvoiceForm.toString()\n" );
        obuf.append( "\n  Email Address           : " ).append( emailAddress );
        obuf.append( "\n  Email Address Confirm   : " ).append( emailAddressConfirm );
        obuf.append( "\n  Invoice Number          : " ).append( invoiceNumber );
        obuf.append( "\n  Original Invoice Amount : " ).append( originalInvoiceAmount );
        obuf.append( "\n  Invoice Account Number  : " ).append( arAccountNumber );
        obuf.append( "\n  *********************** " );
        obuf.append( "\n  Card Type               : " ).append( cardTypeDisplay );
        obuf.append( "\n  Card Holder             : " ).append( cardHolderDisplay );
        obuf.append( "\n  Last Four Digits        : " ).append( cardLastFourDisplay );
        obuf.append( "\n  *********************** " );

        String[] _invoices = this.getSelectedInvoices();

        if (_invoices != null)
        {
            for (int i = 0; i < _invoices.length; i++)
            {
                obuf.append("\n  Invoice                  : ")
                    .append(_invoices[i]);
            }
        }
        obuf.append( "\n" );

        return obuf.toString();
    }
}