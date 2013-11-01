package com.copyright.ccc.web.util;

import com.copyright.ccc.business.data.RLinkPublisher;
public class PublisherDisplay {

    private RLinkPublisher publisher;
    private Boolean selected;
    private String abbrevPermOptions;
    private String abbrevLearnMore;
    private String abbrevPubURL;

	public PublisherDisplay(RLinkPublisher publisher, boolean selected) {
        this.publisher = publisher;
        this.selected = selected;
        this.abbrevPermOptions = "";
        this.abbrevLearnMore = "";
        if (publisher.getPermOptionDesc() != null) {
            this.abbrevPermOptions = publisher.getPermOptionDesc().substring(0,publisher.getPermOptionDesc().length()>21?21:publisher.getPermOptionDesc().length());
        }
        
        if (publisher.getLearnMoreDesc() != null) {
            this.abbrevLearnMore = publisher.getLearnMoreDesc().substring(0,publisher.getLearnMoreDesc().length()>21?21:publisher.getLearnMoreDesc().length());
        }  
        
        if (publisher.getPubUrl() != null) {
        	this.abbrevPubURL = publisher.getPubUrl().substring(0,publisher.getPubUrl().length()>35?35:publisher.getPubUrl().length());
        }
    }
    
    public RLinkPublisher getPublisher() {
        return publisher;
    }
    
    public void setPublisher(RLinkPublisher publisher){
        this.publisher = publisher;
    }
    
    public String getName() {
        return publisher.getPubName();
    }
    public long getAccountNum() {
        return publisher.getAccountNum();
    }
    
    public String getUrl() {
       return publisher.getPubUrl();
    }
    
    public Boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String getAbbrevPermOptions() {
        return abbrevPermOptions;
    }
    
    public String getAbbrevLearnMore() {
        return abbrevLearnMore;
    }
    
    public long getPubID() {
       return publisher.getPubID();
    }
    
    public String getAbbrevPubURL() {
		return abbrevPubURL;
	}
}
