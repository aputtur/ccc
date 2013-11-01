package com.copyright.ccc.business.services.user;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.data.RLinkPublisherResultType;
import com.copyright.opi.data.PersistentDO;

public class RLinkPublisherResultTypeImpl extends PersistentDO implements RLinkPublisherResultType
{
	private static final long serialVersionUID = 1L;
	
    /**
	 * 
	 */
    
    private RLinkPublisher[] publishers = null;


    /**
     * @param pub
     */
    public void setPublishers(RLinkPublisher[] pub) 
    {
        this.publishers = pub;
    }
    
    public RLinkPublisher[] getPublishers() 
    {
        return publishers;
    }
                        
    // protected default constructor for safety
    protected RLinkPublisherResultTypeImpl()
    {
        //setUserType( DEFAULT_USER_TYPE );
    }
            
}
