 package com.copyright.ccc.business.services.user;

import java.sql.SQLData;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.data.RLinkPublisherExpectedException;
import com.copyright.ccc.business.data.RLinkPublisherUnexpectedException;
import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;
import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.InvocationRuntimeException;
import com.copyright.opi.InvocationUnexpectedErrorRuntimeException;
import com.copyright.opi.NoOutputProcedureInvoker;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.opi.TypedParameter;
import com.copyright.opi.data.DOBase;
import com.copyright.workbench.util.ArrayUtils2;

/**
 * @author PPAI
 *
 */
 public class RLinkPublisherServicePersistence
 {

       public RLinkPublisherServicePersistence()
        {
                
        }
        
        
     public RLinkPublisher[] getPublishers() 
     {              
         RLinkPublisher[] publishers = null;   
                       
         SingleDTOProcedureInvoker invoker = CC2OracleProcedureInvokerFactory
                      .getInstance().singleDTOInvoker();
                                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHERS,
                                         RLinkPublisherResultTypeDTO.getRefInstance());                                        
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
                              
         RLinkPublisherResultTypeDTO prData = (RLinkPublisherResultTypeDTO) invoker.getDTO();
         
         SQLData[] data;
         
         if ( prData != null)
         {

            data = (SQLData[]) prData.getPublishers();
         }
         else 
         {
             data = null;
         }
                  
         if (data == null)
         {
                        
                return publishers;
                                  
         }
                                                                              
         publishers = (RLinkPublisher[]) ArrayUtils2.convertArray(data,
                                  RLinkPublisher.class);
                                  
      /*   for (Iterator i = new ArrayIterator(publishers); i.hasNext();)
         {
                 RLinkPublisher singlePub = (RLinkPublisher) i.next();
                 
                 if (singlePub != null)
                 { 
                 
                    String name = singlePub.getPubName();
                 
                 }
         }    */                              
                                                                                                                           
         return publishers;
         
     }
         
     public RLinkPublisher getPublisherById(long id) 
     {              
         RLinkPublisher publisher = null;
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter(id);
                       
         SingleDTOProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHER_BY_PUB_ID,
                                 RLinkPublisherDTO.getRefInstance(), inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
                                   
         publisher = (RLinkPublisherDTO) invoker.getDTO();

         return publisher;
         
     }
     
    public RLinkPublisher getPublisherByAccount(long acct) 
    {              
        RLinkPublisher publisher = null;
        TypedParameter[] inputParameters = new TypedParameter[1];
        inputParameters[0] = new TypedParameter(acct);
                      
        SingleDTOProcedureInvoker invoker =
                    CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
                    
        invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHER_BY_ACCOUNT,
                                RLinkPublisherDTO.getRefInstance(), inputParameters);
               
        invoker.setNoDataFoundAcceptable( false );
                
            try
            {
                    invoker.invoke();
            } 
              catch (InvocationExpectedEventRuntimeException e)
            {

                RLinkPublisherExpectedException rpee = new RLinkPublisherExpectedException(
                                        invoker.getReturnCodes().getOracleDescription());
                throw rpee;

            } catch (InvocationUnexpectedErrorRuntimeException e)
            {
                RLinkPublisherUnexpectedException rpue = new RLinkPublisherUnexpectedException(e.getMessageCode());
                    throw rpue;
            }
            
            catch (InvocationRuntimeException e) 
            {
                RLinkPublisherUnexpectedException rpue = new RLinkPublisherUnexpectedException(invoker
                                .getReturnCodes().getOracleDescription());
                throw rpue;
            } 
                            
        publisher = (RLinkPublisherDTO) invoker.getDTO();

        return publisher;
        
    }
    
     public RLinkPublisher getPublisherByPtyInst(long ptyInst) 
     {              
         RLinkPublisher publisher = null;
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter(ptyInst);
                       
         SingleDTOProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHER_BY_PTY_INST,
                                 RLinkPublisherDTO.getRefInstance(), inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
         
         publisher = (RLinkPublisherDTO) invoker.getDTO();

         return publisher;
         
     }
     
     public RLinkPublisher getPublisherByPtyInstWrkInst(long ptyInst, long wrkInst) 
     {              
         RLinkPublisher publisher = null;
         TypedParameter[] inputParameters = new TypedParameter[2];
         inputParameters[0] = new TypedParameter(ptyInst);
         inputParameters[1] = new TypedParameter(wrkInst);
                       
         SingleDTOProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHER_BY_PTY_WRK_INST,
                                 RLinkPublisherDTO.getRefInstance(), inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
         
         publisher = (RLinkPublisherDTO) invoker.getDTO();

         return publisher;
         
     }
     
     public RLinkPublisher createRLinkPublisher(RLinkPublisher rLinkPub) 
     {                
         RLinkPublisherDTO rLinkPubDTO = (RLinkPublisherDTO) rLinkPub;
         RLinkPublisher publisher = null;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter((DOBase) rLinkPubDTO);
                                
         SingleDTOProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.CREATE_RLINK_PUBLISHER,
                                RLinkPublisherDTO.getRefInstance(), inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
         
         publisher = (RLinkPublisherDTO) invoker.getDTO(); 
         
         return publisher;
                        
     }
     
     public void updateRLinkPublisher(RLinkPublisher rLinkPub) 
     {              
         RLinkPublisherDTO rLinkPubDTO = (RLinkPublisherDTO) rLinkPub;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter((DOBase) rLinkPubDTO);
                               
         NoOutputProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.UPDATE_RLINK_PUBLISHER,
                                 inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
                                                 
     }
     
     public void deleteRLinkPublisher(RLinkPublisher rLinkPub) 
     {              
         RLinkPublisherDTO rLinkPubDTO = (RLinkPublisherDTO) rLinkPub;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter((DOBase) rLinkPubDTO);
                             
         NoOutputProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.DELETE_RLINK_PUBLISHER,
                                 inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
                                                   
     }
     
     public void createRLinkPublisherDetail(RLinkPublisher rLinkPub) 
     {                
         RLinkPublisherDTO rLinkPubDTO = (RLinkPublisherDTO) rLinkPub;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter((DOBase) rLinkPubDTO);
                                
         NoOutputProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.CREATE_RLINK_PUBLISHER_DETAIL,
                                 inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
         
        // invoker.invoke();
        
         try
         {
                 invoker.invoke();
         } 
           catch (InvocationExpectedEventRuntimeException e)
         {

             RLinkPublisherExpectedException rpee = new RLinkPublisherExpectedException(
                                     invoker.getReturnCodes().getOracleDescription());
             throw rpee;

         } catch (InvocationUnexpectedErrorRuntimeException e)
         {
             RLinkPublisherUnexpectedException rpue = new RLinkPublisherUnexpectedException(e.getMessageCode());
                 throw rpue;
         }
         
         catch (InvocationRuntimeException e) 
         {
             RLinkPublisherUnexpectedException rpue = new RLinkPublisherUnexpectedException(invoker
                             .getReturnCodes().getOracleDescription());
             throw rpue;
         } 
                                          
     }
     
     public void deleteRLinkPublisherDetail(RLinkPublisher rLinkPub) 
     {              
         RLinkPublisherDTO rLinkPubDTO = (RLinkPublisherDTO) rLinkPub;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter((DOBase) rLinkPubDTO);
                             
         NoOutputProcedureInvoker invoker =
                     CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();
                     
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.DELETE_RLINK_PUBLISHER_DETAIL,
                                 inputParameters);
                
         invoker.setNoDataFoundAcceptable( true );
                         
         invoker.invoke();
                        
     }
     
     public RLinkPublisher[] getPublisherDetailsById(long id) 
     {              
         RLinkPublisher[] publishers = null;
         
         TypedParameter[] inputParameters = new TypedParameter[1];
         inputParameters[0] = new TypedParameter(id);
                       
         SingleDTOProcedureInvoker invoker = CC2OracleProcedureInvokerFactory
                      .getInstance().singleDTOInvoker();
                                      
         invoker.configure(CC2DataAccessConstants.RLinkPublisher.GET_PUBLISHER_DETAIL_BY_ID,
                                         RLinkPublisherResultTypeDTO.getRefInstance(), inputParameters);                                        
                
         invoker.setNoDataFoundAcceptable( true );
         
         invoker.invoke();
         
         RLinkPublisherResultTypeDTO prData = (RLinkPublisherResultTypeDTO) invoker.getDTO();
         
         //ReturnCodes ret = invoker.getReturnCodes();
         
         SQLData[] data;
         
         if ( prData != null)
         {

            data = (SQLData[]) prData.getPublishers();
         }
         else 
         {
             data = null;
         }
         
         if ( data == null )
         {
                        
                return publishers;
                                  
         }
                                                                              
         publishers = (RLinkPublisher[]) ArrayUtils2.convertArray(data,
                                  RLinkPublisher.class);                                                               
                                                                                                                           
         return publishers;
         
     }
          
 }
