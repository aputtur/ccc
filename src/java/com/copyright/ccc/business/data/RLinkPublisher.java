package com.copyright.ccc.business.data;

import java.util.Date;

public interface  RLinkPublisher
{
    void setPubID( long id );
    long getPubID();
    
    void setPtyInst( long inst);
    long getPtyInst();
    
    void setWrkInst( long wrkInst);
    long getWrkInst();
    
    void setAccountNum(long acct);
    long getAccountNum();
    
    void setPubName(String name);
    String getPubName();
    
    void setPubUrl(String url);
    String getPubUrl();
    
    void setPermOptionDesc(String desc);
    String getPermOptionDesc();
    
    void setLearnMoreDesc(String desc);
    String getLearnMoreDesc();
    
    void setCreUser(String usr);
    String getCreUser();
    
    void setCreDate(Date dt);
    Date getCreDate();
    
    void setUpdUser(String usr);
    String getUpdUser();
    
    void setUpdDate(Date dt);
    Date getUpdDate();
        
}
