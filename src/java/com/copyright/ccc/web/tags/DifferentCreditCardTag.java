package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.data.OrderLicense;

public class DifferentCreditCardTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	public DifferentCreditCardTag() {
	}

	protected String bean; // The page scoped bean

	protected String scope = null;

	protected String license;

	@Override
	public int doStartTag() throws JspException {
		TagUtils utils = TagUtils.getInstance();

		// List<OrderLicense> licenses = (List<OrderLicense>)
		// utils.lookup(pageContext, bean, "orderDetails", "request");

		Object ordLcnObj = utils.lookup(pageContext, bean, "orderDetails",
				scope);

		@SuppressWarnings("unchecked")
		List<OrderLicense> licenses = (List<OrderLicense>) ordLcnObj;
		Long lastFourCC = 0L;

		Object objLastFourCC = utils.lookup(pageContext, license,
				"lastFourCreditCard", "page");
		
		if (objLastFourCC != null)
			lastFourCC = (Long) objLastFourCC;
		
		



		
		
		

		// double total = 0D;
		boolean differentCC = false;
		if (lastFourCC != null && lastFourCC != 0) {
			if (licenses != null) {
				try {
					Iterator<OrderLicense> itr = licenses.iterator();
					while (itr.hasNext()) {
						OrderLicense aLicense = itr.next();
						Long licenseLastFourCC = aLicense
								.getLastFourCreditCard();

						// Check if the two credit card numbers are different
						if (licenseLastFourCC != null && licenseLastFourCC != 0
								&& licenseLastFourCC.compareTo(lastFourCC) != 0) {

							differentCC = true;
							break;

						}
					}
					if (differentCC) {
						String diffCCString = String.valueOf(lastFourCC);
						try {
							pageContext.getOut().print(
									"(CC Ending in " + diffCCString + ")");
						} catch (IOException ioe) {
							throw new JspException(
									"Tag.OrderTotalTag: IOException while writing to client "
											+ ioe.getMessage());
						}
					}
				} catch (Exception e) {
					throw new JspException(
							"Tag.DifferentCreditCardTag: ParseException while writing to client "
									+ e.getMessage());
				}
			}

		}

		return SKIP_BODY;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getBean() {
		return bean;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
