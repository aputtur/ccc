package com.copyright.ccc.business.services.user;

import java.util.Date;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.opi.data.StandardDO;

public class RLinkPublisherImpl extends StandardDO implements RLinkPublisher
{
	private static final long serialVersionUID = 1L;
	
    /**
	 * 
	 */
         
    private long _pubID;
    private long _ptyInst;
    private long _wrkInst;
    private long _accountNum;
    private String _pubName;
    private String _pubUrl;
    private String _permOptionDesc;
    private String _learnMoreDesc;
    private String _creUser;
    private Date _creDate;
    private String _updUser;
    private Date _updDate;        
    
    public void setPubID(long id) 
    {
       _pubID = id; 
    }
    
    public long getPubID() 
    {
        return _pubID;    
    }

    public void setPtyInst(long pty) 
    {
        _ptyInst = pty;
    }
    
    public long getPtyInst() 
    {
        return _ptyInst;   
    }
    
    public void setWrkInst(long wrkInst) 
    {
        _wrkInst = wrkInst;  
    }
    
    public long getWrkInst() 
    {
        return _wrkInst;    
    }
    
    public void setAccountNum(long acct) 
    {
        _accountNum = acct;
    }
    
    public long getAccountNum() 
    {
        return _accountNum;
    }
    
    public void setPubName(String pub) 
    {
        _pubName = pub;
    }
    
    public String getPubName() {
        return _pubName;
    }
    
    public void setPubUrl(String url) 
    {
        _pubUrl = url;
    }
    
    public String getPubUrl() 
    {
        return _pubUrl;
    }
    
    public void setPermOptionDesc(String perm) 
    {
        _permOptionDesc = perm;
    }
    
    public String getPermOptionDesc() {
        return _permOptionDesc;
    }
    
    public void setLearnMoreDesc(String lrn) 
    {
        _learnMoreDesc = lrn;
    }
    
    public String getLearnMoreDesc() 
    {
        return _learnMoreDesc;
    }
    
    public void setCreUser(String usr)
    {
        _creUser = usr;
    }
    
    public String getCreUser() 
    {
        return _creUser;
    }
    
    public void setUpdUser(String usr)
    {
        _updUser = usr;
    }
    
    public String getUpdUser() 
    {
        return _updUser;
    }
    
    public void setCreDate(Date dt)
    {
        _creDate = dt;
    }

    public Date getCreDate() {
        return _creDate;
    }

    public void setUpdDate(Date dt) 
    {
        _updDate = dt;
    }

    public Date getUpdDate() 
    {
        return _updDate;
    }
        
    // protected default constructor for safety
    protected RLinkPublisherImpl()
    {
        //setUserType( DEFAULT_USER_TYPE );
    }
            
}
