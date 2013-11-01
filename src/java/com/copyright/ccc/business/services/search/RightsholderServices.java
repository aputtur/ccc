package com.copyright.ccc.business.services.search;

import java.util.Date;

import com.copyright.ccc.business.data.Rightsholder;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.data.inventory.Right;
import com.copyright.service.inventory.RightServiceAPI;
import com.copyright.service.inventory.RightServiceFactory;
import com.copyright.svc.telesales.api.data.RightsholderOrganization;
import com.copyright.svc.tf.api.data.Party;
import com.copyright.svc.tf.api.data.TFConsumerContext;


/**
 * @author  Jay Arbo
 * Created 3/30/2007
 */
public final class RightsholderServices {


    public RightsholderServices() {
    }

    public static Rightsholder getRightsholderByRgtInst( long rgtInst, Date dateOfUse )
    {
        RightServiceAPI rApi = RightServiceFactory.getInstance().getService();
        Right right = rApi.getRightById(rgtInst,dateOfUse);
        
        RightsholderOrganization org = null;
        
        Party rhParty = ServiceLocator.getTFService().getOrgRightsholderPartyByPartyId(new TFConsumerContext(), right.getRightsHolderPartyId());
        
        if (rhParty != null)
        {
        	org = UserServices.getOrganizationByPtyInst(rhParty.getPtyInst());
        }
        
       /* This call is replaced because it is throwing Exception!!!!!!! Last minute fix 10/1/09
        * RightsholderOrganization org = 
            UserServices.getOrganizationByPartyId((Long) right.getRightsHolderPartyId()); */
        
        if (org != null) {
            return new Rightsholder(org);
        }
        return null;
    }
    

    public static Rightsholder getRightsholderByPartyId( long partyId )
    {          
        RightsholderOrganization org = UserServices.getOrganizationByPartyId(Long.valueOf(partyId));
        
        if (org != null) {
            return new Rightsholder(org);
        }
        return null;
    }
    
    public static Rightsholder getRightsholderByPtyInst( long ptyInst )
    {
        RightsholderOrganization org = UserServices.getOrganizationByPtyInst(Long.valueOf(ptyInst));
        
        if (org != null) {
            return new Rightsholder(org);
        }
        return null;
    }
}
