package com.copyright.ccc.business.services.user;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.data.access.OracleServicesBase;

/**
 * This is a RLink Publisher "Service" class in the true sense of the word.  It exposes
 * a collection of static methods for performing CRUD operations on objects
 * related to a particular functional slice of the app.
 *  
 * 
 * 1.  RLink Publisher related methods - methods for fetching, updating, creating
 *     instances of the application-specific user object RLinkPublisher.java
 *     
 */
public final class RLinkPublisherServices extends OracleServicesBase
{
    static Logger _logger = Logger.getLogger( RLinkPublisherServices.class );
     
    /**
     * Returns a newly-created <code>CCUser</code> and seeds fields with
     * defaults and values that need to be created for new user objects
     */
    public static RLinkPublisher createRLinkPublisher()
    {
        return new RLinkPublisherImpl();
    }
    
    /**
     * Returns array of <code>RLinkPublisher</code>.
     */
    public static RLinkPublisher[] getRLinkPublishers( )
    {
        RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
        return rPers.getPublishers();
    }
    
    /**
     * Returns the <code>RLinkPublisher</code> for the specified <code>ID</code>.
     */
    public static RLinkPublisher getRLinkPublisherById( long id )
    {
        RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
        return rPers.getPublisherById(id);
    }
    
    /**
     * Returns the <code>RLinkPublisher</code> for the specified <code>ID</code>.
     */
    public static RLinkPublisher getRLinkPublisherByPtyInst( long pty )
    {             
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         return rPers.getPublisherByPtyInst(pty);
                 
    }
    
    /**
     * Returns the <code>RLinkPublisher</code> for the specified <code>ID</code>.
     */
    public static RLinkPublisher getRLinkPublisherByPtyInstWrkInst( long pty, long wrk )
    {             
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         return rPers.getPublisherByPtyInstWrkInst(pty, wrk);
                 
    }
    
    /**
     * Creates row in ccc_rlink_publisher table for the <code>RLinkPublisher</code>
     * 
     */
    public static RLinkPublisher createRLinkPublisher( RLinkPublisher rLinkPub )
    {      
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         return rPers.createRLinkPublisher(rLinkPub);
                 
    }
    
    /**
     * Updates row in ccc_rlink_publisher table for the <code>RLinkPublisher</code>
     * 
     */
    public static void updateRLinkPublisher( RLinkPublisher rLinkPub )
    {
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         rPers.updateRLinkPublisher(rLinkPub);
                 
    }
    
    /**
     * Deletes row in ccc_rlink_publisher table for the <code>RLinkPublisher</code>
     * 
     */
    public static void deleteRLinkPublisher( RLinkPublisher rLinkPub )
    {
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         rPers.deleteRLinkPublisher(rLinkPub);
                 
    }
    
    
    /**
     * Creates row in ccc_rlink_publisher_detail table for the <code>RLinkPublisher</code>
     * 
     */
    public static void createRLinkPublisherDetail( RLinkPublisher rLinkPub )
    {      
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         rPers.createRLinkPublisherDetail(rLinkPub);
                 
    }
    
    /**
     * Deletes row in ccc_rlink_publisher_detail table for the <code>RLinkPublisher</code>
     * 
     */
    public static void deleteRLinkPublisherDetail( RLinkPublisher rLinkPub )
    {
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         rPers.deleteRLinkPublisherDetail(rLinkPub);
                 
    }
    
    /**
     * Returns the Publisher Name/Party Inst from CCC_PARTY with Account# as the input
     * 
     */
     public static RLinkPublisher getRLinkPublisherByAccount(long acct) 
     {
     
            RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
            return rPers.getPublisherByAccount(acct);
     }
     
    /**
     * Returns the array of RLinkPublisher with Publisher id/pty_inst/sub_acct/wrk_inst 
     * from CCC_RLINK_PUBLISHER_DETAIL with RLINK_PUB_ID as the input parameter
     * 
     */
     public static RLinkPublisher[] getPublisherDetailsById(long id) 
     {
         RLinkPublisherServicePersistence rPers = new RLinkPublisherServicePersistence();
         return rPers.getPublisherDetailsById(id);         
     }

}
