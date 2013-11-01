package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.util.NumberTools;
import com.copyright.svc.artransaction.api.data.ARTransaction;

/**
 * Submit a user email address to perform user information lookup.
 * This is a user administrator form.
 * @author Jessop
 */
public class UnpaidInvoiceForm extends BasePaymentForm
{
    /**
     * Form to handle the display/selection and payment
     * of unpaid invoices.
     */
    private static final long serialVersionUID = 1L;
    //private static final Logger _logger = Logger.getLogger(UnpaidInvoiceForm.class);

    public static final int MODE_INVOICES = 0;
    public static final int MODE_PAYMENT = 1;

    private List<ARTransaction> _invoices;
    private String[] _selectedInvoices;
    private String _selectAll;
    private List<ARTransaction> _saveSelectedInvoices;
   
    private int _mode;
    private boolean _anonymous;
   
    public UnpaidInvoiceForm() {
    	super.clearSecureData();
        _mode = MODE_INVOICES;
        _anonymous = false;

        setCCOptions();
        setExpMonthOptions();
        setExpYearOptions();
    }

    public UnpaidInvoiceForm(boolean isAnonymous) {
        super.clearSecureData();
        _mode = MODE_INVOICES;
        _anonymous = isAnonymous;

        setCCOptions();
        setExpMonthOptions();
        setExpYearOptions();
    }

    public void setInvoices(List<ARTransaction> invoices) {
        _invoices = invoices;

        if (_invoices != null) {
            if (_selectedInvoices == null || _selectedInvoices.length != _invoices.size()) {
                if (_logger.isDebugEnabled()) {
                    _logger.info("\n\nSetting _selectedInvoices to " + 
                        String.valueOf(_invoices.size()) + "\n\n");
                }
                _selectedInvoices = new String[_invoices.size()];
            }
        }
        else _selectedInvoices = new String[0];
    }

    public List<ARTransaction> getInvoices() {
        return _invoices;
    }

    public String[] getSelectedInvoices() {
        return _selectedInvoices;
    }

    public void rebuildSelectedInvoices() {
        int i = 0;
        
        for (ARTransaction y : _invoices) {
            for (ARTransaction x : _saveSelectedInvoices) {
                if (y.getTransactionNumber().equals(x.getTransactionNumber())) {
                    _selectedInvoices[i] = x.getTransactionNumber();
                    break;
                }
            }
            i = i + 1;
        }
    }

    public void setSelectedInvoices(String[] selectedInvoices) {
        _selectedInvoices = selectedInvoices;

        if (_selectedInvoices != null)
        {
            if (_logger.isDebugEnabled())
            {
                StringBuffer obuf = new StringBuffer();

                obuf.append("\n\nReceived these selected invoices:\n");

                for (int i = 0; i < _selectedInvoices.length; i++)
                {
                    obuf.append("\nTransaction ID: ")
                        .append(_selectedInvoices[i]);
                }
                obuf.append("\n");
                _logger.info(obuf.toString());
            }

            _saveSelectedInvoices = new ArrayList<ARTransaction>();

            for (int i = 0; i < _selectedInvoices.length; i++)
            {
                for (ARTransaction x : _invoices)
                {
                    if (_selectedInvoices[i].equals(x.getTransactionNumber()))
                        _saveSelectedInvoices.add(x);
                }
            }
        }
    }

    public List<ARTransaction> getInvoicesToCredit()
    {
        return _saveSelectedInvoices;
    }

    public String getSelectAll() { return _selectAll; }

    public Double getCurrentAmount(Integer idx, Boolean isSelected)
    {
        double retval = 0.00;

        try {
            retval = getBalanceDue(idx, isSelected);
        }
        catch (Exception e) {
            //  Do nothing.
        }
        return retval;
    }
    
    public String getCurrencyCode(Integer idx)
    {
        String retval = "";

        try {
            retval = _invoices.get(idx.intValue()).getCurrencyCode().getDesc();
        }
        catch (Exception e) {
            //  Do nothing.
        }
        return retval;
    }

    public String getOriginalCurrencyCode(Integer idx, Boolean isSelected)
    {
        String retval = "";
    	if (isSelected){
    		retval = _saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyCode() == null ? 
    				"USD" :
    				_saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyCode().getDesc();        		
    	}else {
    		retval = _invoices.get(idx.intValue()).getOriginalCurrencyCode() == null ? 
    				"USD" :
    				_invoices.get(idx.intValue()).getOriginalCurrencyCode().getDesc();        		        		
    	}
        	
        return retval;
    }

    public Double getTotalAmt(Integer idx, Boolean isSelected)
    {
    	Double currencyAmountDouble = 
    		Double.valueOf(isSelected ? (_saveSelectedInvoices.get(idx.intValue()).getOrderCurrencyAmount() != null ? 
					_saveSelectedInvoices.get(idx.intValue()).getOrderCurrencyAmount().doubleValue() :
					_saveSelectedInvoices.get(idx.intValue()).getTotalAmt().doubleValue())						
    				: (_invoices.get(idx.intValue()).getOrderCurrencyAmount() != null ? 
    						_invoices.get(idx.intValue()).getOrderCurrencyAmount().doubleValue() :    						
    						_invoices.get(idx.intValue()).getTotalAmt().doubleValue()));    	
    	return currencyAmountDouble;
    }

    public Double getBalanceDue(Integer idx, Boolean isSelected)
    {
    	Double currencyAmountDouble = Double.valueOf(isSelected ? _saveSelectedInvoices.get(idx.intValue()).getBalanceDue().doubleValue()
    			: _invoices.get(idx.intValue()).getBalanceDue().doubleValue());
    	String currencyCode;
    	Double currencyRate;

    	if (isSelected){
    		currencyCode = _saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyCode() == null ? 
    				_saveSelectedInvoices.get(idx.intValue()).getCurrencyCode().getDesc() :
    				_saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyCode().getDesc();
    		currencyRate = _saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyRate() == null ? 
    				1 : 
    				Double.valueOf(_saveSelectedInvoices.get(idx.intValue()).getOriginalCurrencyRate());
    	} else {
    		currencyCode = _invoices.get(idx.intValue()).getOriginalCurrencyCode() == null ? 
    				_invoices.get(idx.intValue()).getCurrencyCode().getDesc() :
    				_invoices.get(idx.intValue()).getOriginalCurrencyCode().getDesc();
    		currencyRate = _invoices.get(idx.intValue()).getOriginalCurrencyRate() == null ? 
    				1 : 
    				Double.valueOf(_invoices.get(idx.intValue()).getOriginalCurrencyRate());   		
    	}
    	
    	return NumberTools.getConvertedAmount( 
    			currencyAmountDouble, 
       			currencyCode,
    			currencyRate);
    }
   
    public String getMode() { return Integer.toString(_mode); }

    /*
     * this calculates the amount of all the selected invoices
     */
    public Double getTotalAmount(Boolean isSelected)
    {
        double amt = 0.00;
        int idx = 0;

        for (ARTransaction x : _saveSelectedInvoices)
        {
            amt += getBalanceDue(idx, isSelected );
            idx++;
        }
        return new Double(amt);
    }

    public String getTotalAmountCurrency()
    {
        String currencyCode = "";

        /*
         * Only need first invoice - all selected invoices are the same currency
         */
        for (ARTransaction x : _saveSelectedInvoices)
        {
        	if  (x.getOriginalCurrencyCode() != null){
            	currencyCode =  x.getOriginalCurrencyCode().getDesc();
            }
            else {
            	currencyCode = x.getCurrencyCode().getDesc();            	
            }
            
            break;
        }
        
        return currencyCode;
    }
    
    public void setMode(String mode) 
    {
        try
        {
            _mode = mode != null ? Integer.parseInt(mode) : MODE_INVOICES;
        }
        catch (Exception e)
        {
            //  Someone could be modifying the mode setting.  Process
            //  it as a PAYMENT, triggering extra data checks.
            
            _mode = MODE_PAYMENT;
        }
    }


    public void setSelectAll(String all) { _selectAll = all; }

    //  Build our dropdown data.

   

    //  Other form functions.

    @Override
    public String toString()
    {
        StringBuffer obuf = new StringBuffer();

        obuf.append("\n\nUnpaidInvoiceForm:\n");
        if (getPaymentMethod() == null || _mode == 0)
        {
            obuf.append("\nCreditCardInfo is null.");
        }
        else
        {
            obuf.append("\nCardholder: ")
                .append(this.getCreditCardNameOn())
                .append("\nExp. Date: ")
                .append(this.getExpirationDate())
                .append("\nCard Type:  ")
                .append(this.getCcType());

                obuf.append("\nMode     :  ").append(_mode);
        }
        if (_selectedInvoices != null)
        {
            obuf.append("\n\nSelected Invoices:");

            for (int i = 0; i < _selectedInvoices.length; i++)
            {
                obuf.append("\nTransaction ID: ")
                    .append(_selectedInvoices[i]);
            }
        }
        obuf.append("\n");
        return obuf.toString();
    }

    @Override
    public void reset( ActionMapping      mapping
                     , HttpServletRequest request )
    {
        _logger.info("\nUnpaidInvoiceForm.reset()\n");
        _selectedInvoices = null;

    }
    
    public void clearSaveSelectedInvoices() { _saveSelectedInvoices = null; }
    public boolean getIsAnonymous() { return _anonymous; }
    public void setIsAnonymous(boolean b) { _anonymous = b; }
}