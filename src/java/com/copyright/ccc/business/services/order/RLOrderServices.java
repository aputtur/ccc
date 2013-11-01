/*
 * RLOrderServices.java
 */
package com.copyright.ccc.business.services.order;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.RLOrder;
import com.copyright.ccc.business.data.RLOrders;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.util.RLByDateComparator;
import com.copyright.ccc.util.RLByFeeComparator;
import com.copyright.ccc.util.RLByPublicationComparator;
import com.copyright.ccc.util.RLByTitleComparator;
import com.copyright.ccc.util.RLByTypeOfUseComparator;
import com.copyright.svc.rlOrder.api.data.RightslinkRuntimeException;
import com.copyright.svc.rlOrder.api.data.UserOrder;

//  This class contains methods to retrieve Rightslink user
//  orders from a web service.

public class RLOrderServices
{
    private static Logger _logger = Logger.getLogger( RLOrderServices.class );

        private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    //  Helper methods...

    private static Date getMinDate() {
        //  History starts 4 years prior to this date.

        GregorianCalendar yesteryear = new GregorianCalendar();
        Date today = new Date();

        yesteryear.setTime(today);
        yesteryear.add(Calendar.YEAR, -4);
        yesteryear.set(Calendar.HOUR_OF_DAY, 0);
        yesteryear.set(Calendar.MINUTE, 0);
        yesteryear.set(Calendar.SECOND, 0);
        yesteryear.set(Calendar.MILLISECOND, 0);

        return yesteryear.getTime();
    }

    private static Date getMaxDate() {
        return (new SimpleDateFormat("yyyyMMdd")).parse("30000101", new ParsePosition(0));
    }

    private static ArrayList<RLOrder> getOrders(String forUser, int state) {
        RLOrder rlOrder;
        ArrayList<UserOrder> userOrders = new ArrayList<UserOrder>();
        ArrayList<RLOrder> myOrders = new ArrayList<RLOrder>();

        // Grab Rightslink orders for said user.

        try {
            switch(state) {
                case DisplaySpecServices.RLCOMPLETEDSTATE:
                    userOrders = ServiceLocator.getRLOrderService().getUserCompletedOrders(forUser, getMinDate(), getMaxDate());
                    break;
                case DisplaySpecServices.RLPENDINGSTATE:
                    userOrders = ServiceLocator.getRLOrderService().getUserPendingOrders(forUser, getMinDate(), getMaxDate());
                    break;
                case DisplaySpecServices.RLCANCELEDSTATE:
                    userOrders = ServiceLocator.getRLOrderService().getUserCancelledOrders(forUser, getMinDate(), getMaxDate());
                    break;
                case DisplaySpecServices.RLCREDITEDSTATE:
                    userOrders = ServiceLocator.getRLOrderService().getUserCreditedOrders(forUser, getMinDate(), getMaxDate());
                    break;
                case DisplaySpecServices.RLDENIEDSTATE:
                    userOrders = ServiceLocator.getRLOrderService().getUserDeniedOrders(forUser, getMinDate(), getMaxDate());
                    break;
                case DisplaySpecServices.RLALLSTATES:
                //  Go ahead and fall through here... this is the
                //  default as well.
                default:
                    userOrders = ServiceLocator.getRLOrderService().getAllUserOrders(forUser, getMinDate(), getMaxDate());
            }
        }
        catch (RightslinkRuntimeException e) {
            _logger.debug("Rightslink service failed.  Service might be down or item not found.");
            _logger.debug(e.getMessage());
        }

        // Convert our UserOrders into RLOrder objects then stuff them
        // into the RLOrders object - makes it easier to manage the
        // UI stuff.

        for (UserOrder userOrder : userOrders) {
            rlOrder = new RLOrderImpl(userOrder);
            myOrders.add(rlOrder);
        }
        return myOrders;
    }

    private static List<RLOrder> getSortedAscBy(String forUser, int field, int state) {
        //  Grab our orders, then grab the correct comparator.

        ArrayList<RLOrder> rlOrders = getOrders(forUser, state);
        Comparator<RLOrder> rlComparator;

        switch(field) {
            case DisplaySpecServices.RLORDERDATESORT:
                rlComparator = new RLByDateComparator();
                break;
            case DisplaySpecServices.RLPUBLICATIONTITLESORT:
                rlComparator = new RLByPublicationComparator();
                break;
            case DisplaySpecServices.RLARTICLETITLESORT:
                rlComparator = new RLByTitleComparator();
                break;
            case DisplaySpecServices.RLTYPEOFUSESORT:
                rlComparator = new RLByTypeOfUseComparator();
                break;
            case DisplaySpecServices.RLFEESORT:
                rlComparator = new RLByFeeComparator();
                break;
            default:
                rlComparator = new RLByDateComparator();
        }
        //  Perform the sort and return the list.

        Collections.sort(rlOrders, rlComparator);
        return rlOrders;
    }

    private static List<RLOrder> getSortedDescBy(String forUser, int field, int state) {
        //  Grab our orders, then grab the correct comparator.

        ArrayList<RLOrder> rlOrders = getOrders(forUser, state);
        Comparator<RLOrder> rlComparator;

        switch(field) {
            case DisplaySpecServices.RLORDERDATESORT:
                rlComparator = new RLByDateComparator();
                break;
            case DisplaySpecServices.RLPUBLICATIONTITLESORT:
                rlComparator = new RLByPublicationComparator();
                break;
            case DisplaySpecServices.RLARTICLETITLESORT:
                rlComparator = new RLByTitleComparator();
                break;
            case DisplaySpecServices.RLTYPEOFUSESORT:
                rlComparator = new RLByTypeOfUseComparator();
                break;
            case DisplaySpecServices.RLFEESORT:
                rlComparator = new RLByFeeComparator();
                break;
            default:
                rlComparator = new RLByDateComparator();
        }
        //  Perform the sort, reverse it and return the list.

        Collections.sort(rlOrders, rlComparator);
        Collections.reverse(rlOrders);
        
        return rlOrders;
    }

    //  **************************************************************
    //  Now we basically duplicate the OrderLicenseServices module.
    //  We do that by replacing our general order and licensing
    //  calls with the order services given to us from the RIGHTSLINK
    //  folks (ie. Dave Marcou).

    public static RLOrders getAllOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        // FOR NOW RETURN ALL ORDERS SORTED BY DATE - PAGE BY 5...
        // I think that since we are not using the display spec data
        // stucture for handling the sorting and filtering, we might
        // as well alter how we manage the records.  Theoretically this
        // routine would only have to be called ONCE and then stuffed into
        // the user session and manipulated in the action...  that is for
        // the base data... sorting will still be handled by the service.

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList =
            getSortedDescBy( userName
                           , sortBy
                           , DisplaySpecServices.RLALLSTATES
            );
        }
        else {
            sortedList =
            getSortedAscBy( userName
                          , sortBy
                          , DisplaySpecServices.RLALLSTATES
            );
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(DisplaySpecServices.RLALLSTATES);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getPendingOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList =
            getSortedDescBy( userName
                           , sortBy
                           , DisplaySpecServices.RLPENDINGSTATE
            );
        }
        else {
            sortedList =
            getSortedAscBy( userName
                          , sortBy
                          , DisplaySpecServices.RLPENDINGSTATE
            );
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(DisplaySpecServices.RLPENDINGSTATE);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getDeniedOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();
        int state = DisplaySpecServices.RLDENIEDSTATE;

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList = getSortedDescBy(userName, sortBy, state);
        }
        else {
            sortedList = getSortedAscBy(userName, sortBy, state);
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(state);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getCompletedOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList =
            getSortedDescBy( userName
                           , sortBy
                           , DisplaySpecServices.RLCOMPLETEDSTATE
            );
        }
        else {
            sortedList =
            getSortedAscBy( userName
                          , sortBy
                          , DisplaySpecServices.RLCOMPLETEDSTATE
            );
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(DisplaySpecServices.RLCOMPLETEDSTATE);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getCanceledOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList =
            getSortedDescBy( userName
                           , sortBy
                           , DisplaySpecServices.RLCANCELEDSTATE
            );
        }
        else {
            sortedList =
            getSortedAscBy( userName
                          , sortBy
                          , DisplaySpecServices.RLCANCELEDSTATE
            );
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(DisplaySpecServices.RLCANCELEDSTATE);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getCreditedOrders(int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList =
            getSortedDescBy( userName
                           , sortBy
                           , DisplaySpecServices.RLCREDITEDSTATE
            );
        }
        else {
            sortedList =
            getSortedAscBy( userName
                          , sortBy
                          , DisplaySpecServices.RLCREDITEDSTATE
            );
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(DisplaySpecServices.RLCREDITEDSTATE);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }

    public static RLOrders getOrdersWith(int state, int sortBy, int dir) {
        RLOrder rlOrder;
        RLOrders rlOrders = new RLOrders();

        // Who are we?

        CCUserContext userContext = UserContextService.getUserContext();
        String userName = userContext.getActiveAppUser().getUsername();

        List<RLOrder> sortedList;

        if (dir == DisplaySpecServices.SORTDESCENDING) {
            sortedList = getSortedDescBy(userName, sortBy, state);
        }
        else {
            sortedList = getSortedAscBy(userName, sortBy, state);
        }

        rlOrders.addOrderList(sortedList);
        rlOrders.setPageSize(5);
        rlOrders.setState(state);
        rlOrders.setSortBy(sortBy);
        rlOrders.setDirection(dir);
        
        return rlOrders;
    }
}