package com.copyright.ccc.business.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.BundleParm;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemDescriptionParm;
import com.copyright.svc.order.api.data.ItemParm;

public class ItemHelperServices {
	
    public static ItemDescriptionParm getItemDescriptionParm (Item item, String parmName, boolean initParm) {
    	  
 	 	ItemDescriptionParm returnItemDescriptionParm = null;

 	 	returnItemDescriptionParm = item.getItemDescriptionParms().get(parmName);
// 	 	for (ItemDescriptionParm itemDescriptionParm : item.getItemDescriptionParms()) {
// 		  if (itemDescriptionParm.getParmName().equalsIgnoreCase(parmName) ) {
//  			  returnItemDescriptionParm = itemDescriptionParm;
//  			  break;
//  		  }
// 	 	}
 	 	
 	 	if (returnItemDescriptionParm == null) {
  		  returnItemDescriptionParm = new ItemDescriptionParm();
  		  returnItemDescriptionParm.setParmName(parmName);
  		  initItemDescriptionParmAttributesFromEnum (returnItemDescriptionParm);
      	  if (returnItemDescriptionParm.getDataTypeCd() == null) {
      		  returnItemDescriptionParm.setDataTypeCd(ItemConstants.STRING);
      	  }
      	  if (initParm) {
      		  item.getItemDescriptionParms().put(parmName, returnItemDescriptionParm);
      	  }
//      	  item.getItemDescriptionParms().add(returnItemDescriptionParm);
 	 	}
 	 	else{
 	 		if (returnItemDescriptionParm.getDataTypeCd() == null) {
 	      		  returnItemDescriptionParm.setDataTypeCd(ItemConstants.STRING);
 	      	  }
 	 	}
  	  
  	  return returnItemDescriptionParm;
    }

    
    public static void initItemDescriptionParmAttributesFromEnum (ItemDescriptionParm itemDescriptionParm ) {

	  ItemDescriptionParmEnum itemDescriptionParmEnum = ItemDescriptionParmEnum.getEnumForName(itemDescriptionParm.getParmName());

	  if (itemDescriptionParmEnum != null) {
		  itemDescriptionParm.setDataTypeCd(itemDescriptionParmEnum.getDataTypeCd());
		  itemDescriptionParm.setLabel(itemDescriptionParmEnum.getLabel());
		  itemDescriptionParm.setDisplayWidth(itemDescriptionParmEnum.getDisplayWidth());
		  itemDescriptionParm.setFieldLength(itemDescriptionParmEnum.getFieldLength());
	  } else {
		  itemDescriptionParm.setDataTypeCd(ItemConstants.STRING);
		  itemDescriptionParm.setLabel("");
		  itemDescriptionParm.setDisplayWidth(0);
		  itemDescriptionParm.setFieldLength(0);		  
	  }

    }
      
    public static String getItemDescriptionParmString (Item item, String parmName) {

      ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, false);

     
      if (itemDescriptionParm != null) {
    	  if (ItemConstants.STRING.equalsIgnoreCase(itemDescriptionParm.getDataTypeCd())) {
    		  return itemDescriptionParm.getParmValue();			  
    	  }
    	  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    		  return itemDescriptionParm.getParmValue();			  
    	  }
    	  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    		  
    		  if (itemDescriptionParm.getParmValueNumeric() != null)
    		  {
    			  return itemDescriptionParm.getParmValueNumeric().toString();
    		  }
    		  else
    		  {
    			  return null;
    		  }
    	  }
    	  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {

    		  if (itemDescriptionParm.getParmValueNumeric() != null)
    		  {
    			  return itemDescriptionParm.getParmValueNumeric().toString();
    		  }
    		  else
    		  {
    			  return null;
    		  }
    	  }
    	  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    		  
    		  if (itemDescriptionParm.getParmValueDate() != null)
    		  {
    			  GregorianCalendar calendar = new GregorianCalendar();
    			  calendar.setTime(itemDescriptionParm.getParmValueDate());
    			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
    			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    			  int year = calendar.get(GregorianCalendar.YEAR);
    			  String mdy =  Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
    			  return mdy;
    		  }
    		  else
    		  {
    			  return null;
    		  }
    	  }
		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
			  return itemDescriptionParm.getParmValue();			  
		  }

  	  }
   	  return null;
    }
      
      public static void setItemDescriptionParmString (Item item, String parmName, String parmValue) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, true);
    	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  itemDescriptionParm.setParmValue(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  itemDescriptionParm.setParmValue(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  itemDescriptionParm.setParmValue(parmValue);
    			  itemDescriptionParm.setParmValueNumeric(new BigDecimal(parmValue));
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  itemDescriptionParm.setParmValueNumeric(new BigDecimal(parmValue));
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy"); 
    			  Date parmValueDate;
    			  try        
    			  {             
    				 parmValueDate = df.parse(parmValue);             
    			  } 
    			  catch (ParseException e) { 
    			  	 parmValueDate = null;
    			  }
    			  itemDescriptionParm.setParmValueDate(parmValueDate);
    			  GregorianCalendar calendar = new GregorianCalendar();
    			  calendar.setTime(itemDescriptionParm.getParmValueDate());
    			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
    			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    			  int year = calendar.get(GregorianCalendar.YEAR);
    			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
    			  itemDescriptionParm.setParmValue(mdy);		
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  itemDescriptionParm.setParmValue(parmValue);			  
    		  }
    	  }
      }
    		  
      public static void setItemDescriptionParmNumber (Item item, String parmName, BigDecimal parmValue) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, true);
    	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  if (parmValue != null) {
      				  itemDescriptionParm.setParmValue(parmValue.toString());			  
      			  }
    			  itemDescriptionParm.setParmValueNumeric(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  if (parmValue != null) {
      				  itemDescriptionParm.setParmValue(parmValue.toString());			  
      			  }
    			  itemDescriptionParm.setParmValueNumeric(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  if (parmValue != null) {
      				  itemDescriptionParm.setParmValue(parmValue.toString());			  
      			  }
    			  itemDescriptionParm.setParmValueNumeric(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  if (parmValue != null) {
      				  itemDescriptionParm.setParmValue(parmValue.toString());			  
      			  }
    			  itemDescriptionParm.setParmValueNumeric(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  // Nothing to do possibly throw exception
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  if (parmValue != null) {
  			  		if (parmValue.intValue() > 0) {
  			  			itemDescriptionParm.setParmValue(ItemConstants.YES_CD);			  
  			  		} else {
  			  			itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	
  			  		}		  
      			  }	
    		  }
    	  }
      }
      
      public static void setItemDescriptionParmDate (Item item, String parmName, Date parmValue) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, true);
    	  
    	  if (itemDescriptionParm != null && parmValue != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  GregorianCalendar calendar = new GregorianCalendar();
    			  calendar.setTime(parmValue);
    			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
    			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    			  int year = calendar.get(GregorianCalendar.YEAR);
    			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
    			  itemDescriptionParm.setParmValue(mdy);		
    			  itemDescriptionParm.setParmValueDate(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  GregorianCalendar calendar = new GregorianCalendar();
    			  calendar.setTime(parmValue);
    			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
    			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    			  int year = calendar.get(GregorianCalendar.YEAR);
    			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
    			  itemDescriptionParm.setParmValue(mdy);		
    			  itemDescriptionParm.setParmValueDate(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  // Nothing to do, perhaps throw exception
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  // Nothing to do, perhaps throw exception
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  GregorianCalendar calendar = new GregorianCalendar();
    			  calendar.setTime(parmValue);
    			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
    			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    			  int year = calendar.get(GregorianCalendar.YEAR);
    			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
    			  itemDescriptionParm.setParmValue(mdy);		
    			  itemDescriptionParm.setParmValueDate(parmValue);			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  // Nothing to do, perhaps throw exception
    		  }
    	  }
      }
      
      public static void setItemDescriptionParmBoolean (Item item, String parmName, Boolean parmValue) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, true);
    	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  if (parmValue.booleanValue() == true) {
    				  itemDescriptionParm.setParmValue(ItemConstants.YES_CD);	
    			  } else {
    				  itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	        				  
    			  }
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  if (parmValue.booleanValue() == true) {
    				  itemDescriptionParm.setParmValue(ItemConstants.YES_CD);	
    			  } else {
    				  itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	        				  
    			  }
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  if (parmValue.booleanValue() == true) {
    				  itemDescriptionParm.setParmValue(ItemConstants.YES_CD);
    				  itemDescriptionParm.setParmValueNumeric(new BigDecimal(1));
    			  } else {
    				  itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	        				  
    				  itemDescriptionParm.setParmValueNumeric(new BigDecimal(1));
       			  }
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  if (parmValue.booleanValue() == true) {
    				  itemDescriptionParm.setParmValue(ItemConstants.YES_CD);
    				  itemDescriptionParm.setParmValueNumeric(new BigDecimal(1));
    			  } else {
    				  itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	        				  
    				  itemDescriptionParm.setParmValueNumeric(new BigDecimal(1));
       			  }
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  // DO nothing
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  if (parmValue.booleanValue() == true) {
    				  itemDescriptionParm.setParmValue(ItemConstants.YES_CD);	
    			  } else {
    				  itemDescriptionParm.setParmValue(ItemConstants.NO_CD);	        				  
    			  }
    		  }
    	  }
      }

      
      
      public static BigDecimal getItemDescriptionParmNumber (Item item, String parmName) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, false);
    	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  return new BigDecimal(itemDescriptionParm.getParmValue());			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  return new BigDecimal(itemDescriptionParm.getParmValue());	
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  return itemDescriptionParm.getParmValueNumeric();			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  return itemDescriptionParm.getParmValueNumeric();			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  return null;
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
  			  	if (itemDescriptionParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
  			  		return new BigDecimal(1);
  			  	} else {
  			  		return new BigDecimal(0);	
  			  	}		  
    		  }
    	  }
     	  return null;
      }
      
      public static Date getItemDescriptionParmDate (Item item, String parmName) {

    	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, false);
    	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  return null;			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  return null;			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  return null;			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  return null;			  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  return itemDescriptionParm.getParmValueDate();
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  return null;			  
    		  }
    	  }
     	  return null;
      }
      
      public static Boolean getItemDescriptionParmBoolean (Item item, String parmName) {

       	  ItemDescriptionParm itemDescriptionParm = getItemDescriptionParm(item, parmName, false);
                  	  
    	  if (itemDescriptionParm != null) {
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
  			  	if (itemDescriptionParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
  			  		return true;
			  	} else {
			  		return false;
			  	}		  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
    			  if (itemDescriptionParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
    			  	return true;
    			  } else {
    			  	return false;
    			  }		  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
  			  	if (itemDescriptionParm.getParmValueNumeric().intValue() > 0) {
			  		return true;
			  	} else {
			  		return false;	
			  	}		  	  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
    			  if (itemDescriptionParm.getParmValueNumeric().intValue() > 0) {
    			  	return true;
    			  } else {
    			  	return false;	
    			  }		  	  
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
    			  return null;
    		  }
    		  if (itemDescriptionParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  if (itemDescriptionParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
      			  	return true;
    			  } else {
    			  	return false;
    			  }		  
    		  }
    	  }
     	  return null;
      }
      
      public static ItemParm getItemParm (Item item, String parmName, boolean initParm) {
      	  
    	  	ItemParm returnItemParm = null;;
    	  
//      	  for (ItemParm itemParm : item.getItemParms()) {
//      		  if (itemParm.getParmName().equalsIgnoreCase(parmName) ) {
//      			  returnItemParm = itemParm;
//      			  break;
//      		  }
//      	  }
  
   	 		returnItemParm = item.getItemParms().get(parmName);

    	  
      	  if (returnItemParm == null) {
      		  returnItemParm = new ItemParm();
      		  returnItemParm.setParmName(parmName);
      		  initItemParmAttributesFromEnum (returnItemParm);
          	  if (returnItemParm.getDataTypeCd() == null) {
          		  returnItemParm.setDataTypeCd(ItemConstants.STRING);
          	  }
          	  if (initParm) {
              	  item.getItemParms().put(parmName, returnItemParm);
          	  }
//          	  item.getItemParms().add(returnItemParm);
      	  }
 
      	  return returnItemParm;
        }

      public static void initItemParmAttributesFromEnum (ItemParm itemParm ) {
 
    	  ItemParmEnum itemParmEnum = ItemParmEnum.getEnumForName(itemParm.getParmName());
    	  if (itemParmEnum != null) {
    		  itemParm.setDataTypeCd(itemParmEnum.getDataTypeCd());
    		  itemParm.setLabel(itemParmEnum.getLabel());
    		  itemParm.setDisplayWidth(itemParmEnum.getDisplayWidth());
    		  itemParm.setFieldLength(itemParmEnum.getFieldLength());
    	  } else {
    		  itemParm.setDataTypeCd(ItemConstants.STRING);
    		  itemParm.setLabel("");
    		  itemParm.setDisplayWidth(0);
    		  itemParm.setFieldLength(0);		  
    	  }

      }
      
        public static String getItemParmString (Item item, String parmName) {

      	  ItemParm itemParm = getItemParm(item, parmName, false);
      	  	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  return itemParm.getParmValue();			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  return itemParm.getParmValue();			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  
      			  if (itemParm.getParmValueNumeric() != null)
      			  {
      				  return itemParm.getParmValueNumeric().toString();
      			  }
      			  {
      				  return null;
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  
      			  if (itemParm.getParmValueNumeric() != null)
      			  {
      				  return itemParm.getParmValueNumeric().toString();
      			  }
      			  else
      			  {
      				  return null;
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  
      			if (itemParm.getParmValueDate() != null)
      			{
      				GregorianCalendar calendar = new GregorianCalendar();
      				calendar.setTime(itemParm.getParmValueDate());
      				int month = calendar.get(GregorianCalendar.MONTH) + 1;
      				int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      				int year = calendar.get(GregorianCalendar.YEAR);
      				String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
      				return mdy;
      			}
      			else
      			  {
      				  return null;
      			  }
      		  }
    		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  return itemParm.getParmValue();			  
    		  }
      	  }
       	  return null;
        }
        
        public static void setItemParmString (Item item, String parmName, String parmValue) {

      	  ItemParm itemParm = getItemParm(item, parmName, true);
      	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  itemParm.setParmValue(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  itemParm.setParmValue(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  itemParm.setParmValue(parmValue);
      			  if ( StringUtils.isNotEmpty(parmValue) ) {
      				if ( NumberUtils.isNumber(parmValue.trim() ) )  {
      					  itemParm.setParmValueNumeric(BigDecimal.valueOf(Long.valueOf(parmValue.trim())));
      				  }
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  if ( StringUtils.isNotEmpty(parmValue) ) {
      				  if ( NumberUtils.isNumber(parmValue.trim() ) )  {
      					  itemParm.setParmValueNumeric(BigDecimal.valueOf(Double.valueOf(parmValue.trim())));
      				  }
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy"); 
      			  Date parmValueDate;
      			  try        
      			  {             
      				 parmValueDate = df.parse(parmValue);             
      			  } 
      			  catch (ParseException e) { 
      			  	 parmValueDate = null;
      			  }
      			  
      			  if (parmValueDate != null)
      			  {
      				  itemParm.setParmValueDate(parmValueDate);
      				  GregorianCalendar calendar = new GregorianCalendar();
      				  calendar.setTime(itemParm.getParmValueDate());
      				  int month = calendar.get(GregorianCalendar.MONTH) + 1;
      				  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      				  int year = calendar.get(GregorianCalendar.YEAR);
      				  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
      				  itemParm.setParmValue(mdy);
      			  }
      			  else
      			  {
      				  itemParm.setParmValue(null);
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  itemParm.setParmValue(parmValue);			  
      		  }
      	  }
        }
      		  
        public static void setItemParmNumber (Item item, String parmName, BigDecimal parmValue) {

      	  ItemParm itemParm = getItemParm(item, parmName, true);
      	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  if (parmValue != null) {
      				  itemParm.setParmValue(parmValue.toString());			  
      			  }
      			  itemParm.setParmValueNumeric(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  if (parmValue != null) {
      				  itemParm.setParmValue(parmValue.toString());			  
      			  }
      			  itemParm.setParmValueNumeric(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  if (parmValue != null) {
      				  itemParm.setParmValue(parmValue.toString());			  
      			  }
      			  itemParm.setParmValueNumeric(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  if (parmValue != null) {
      				  itemParm.setParmValue(parmValue.toString());
      			  }
      			  itemParm.setParmValueNumeric(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  // Nothing to do possibly throw exception
      		  }
    		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  if (parmValue != null) {
    			  	if (parmValue.intValue() > 0) {
    			  		itemParm.setParmValue(ItemConstants.YES_CD);			  
    			  	} else {
    			  	  	itemParm.setParmValue(ItemConstants.NO_CD);	
    			  	}
      			  }
      		  }
      	  }
        }
        
        public static void setItemParmDate (Item item, String parmName, Date parmValue) {

      	  ItemParm itemParm = getItemParm(item, parmName, true);

      	  if (itemParm != null && parmValue != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  GregorianCalendar calendar = new GregorianCalendar();
      			  calendar.setTime(parmValue);
      			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
      			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      			  int year = calendar.get(GregorianCalendar.YEAR);
      			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
      			  itemParm.setParmValue(mdy);		
     			  itemParm.setParmValueDate(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  GregorianCalendar calendar = new GregorianCalendar();
      			  calendar.setTime(parmValue);
      			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
      			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      			  int year = calendar.get(GregorianCalendar.YEAR);
      			  String mdy =Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
      			  itemParm.setParmValue(mdy);		
      			  itemParm.setParmValueDate(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  // Nothing to do, perhaps throw exception
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  // Nothing to do, perhaps throw exception
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  GregorianCalendar calendar = new GregorianCalendar();
      			  calendar.setTime(parmValue);
      			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
      			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      			  int year = calendar.get(GregorianCalendar.YEAR);
      			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
      			  itemParm.setParmValue(mdy);		
      			  itemParm.setParmValueDate(parmValue);			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  // Nothing to do, perhaps throw exception
      		  }

      	  }
        }
        
        public static void setItemParmBoolean (Item item, String parmName, Boolean parmValue) {

      	  ItemParm itemParm = getItemParm(item, parmName, true);
      	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  if (parmValue.booleanValue() == true) {
      				  itemParm.setParmValue(ItemConstants.YES_CD);	
      			  } else {
      				  itemParm.setParmValue(ItemConstants.NO_CD);	        				  
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  if (parmValue.booleanValue() == true) {
      				  itemParm.setParmValue(ItemConstants.YES_CD);	
      			  } else {
      				  itemParm.setParmValue(ItemConstants.NO_CD);	        				  
      			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  if (parmValue.booleanValue() == true) {
      				  itemParm.setParmValue(ItemConstants.YES_CD);
      				  itemParm.setParmValueNumeric(new BigDecimal(1));
      			  } else {
      				  itemParm.setParmValue(ItemConstants.NO_CD);	        				  
      				  itemParm.setParmValueNumeric(new BigDecimal(1));
         			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  if (parmValue.booleanValue() == true) {
      				  itemParm.setParmValue(ItemConstants.YES_CD);
      				  itemParm.setParmValueNumeric(new BigDecimal(1));
      			  } else {
      				  itemParm.setParmValue(ItemConstants.NO_CD);	        				  
      				  itemParm.setParmValueNumeric(new BigDecimal(1));
         			  }
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  // DO nothing
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  if (parmValue.booleanValue() == true) {
      				  itemParm.setParmValue(ItemConstants.YES_CD);	
      			  } else {
      				  itemParm.setParmValue(ItemConstants.NO_CD);	        				  
      			  }
      		  }
      	  }
        }
        
        public static BigDecimal getItemParmNumber (Item item, String parmName) {

      	  ItemParm itemParm = getItemParm(item, parmName, false);
      	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  return new BigDecimal(itemParm.getParmValue());			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  return new BigDecimal(itemParm.getParmValue());	
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//     			  if (itemParm.getParmValueNumeric() == null) {
//      				  itemParm.setParmValueNumeric(new BigDecimal(0));
//     			  }
      			  return itemParm.getParmValueNumeric();			        				  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//    			  if (itemParm.getParmValueNumeric() == null) {
//     				  itemParm.setParmValueNumeric(new BigDecimal(0));
//     			  }
       			  return itemParm.getParmValueNumeric();			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  return null;
      		  }
    		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
    			  	if (itemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
    			  		return new BigDecimal(1);
    			  	} else {
    			  		return new BigDecimal(0);	
    			  	}		  
      		  }

      	  }
       	  return null;
        }
        
        public static Date getItemParmDate (Item item, String parmName) {

      	  ItemParm itemParm = getItemParm(item, parmName, false);
      	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  return null;			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  return null;			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  return null;			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  return null;			  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  return itemParm.getParmValueDate();
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  return null;			  
      		  }
      	  }
       	  return null;
        }

        public static Boolean getItemParmBoolean (Item item, String parmName) {

         	  ItemParm itemParm = getItemParm(item, parmName, false);
                    	  
      	  if (itemParm != null) {
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
    			  	if (itemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
    			  		return true;
  			  	} else {
  			  		return false;
  			  	}		  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
      			  if (itemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
      			  	return true;
      			  } else {
      			  	return false;
      			  }		  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
    			  	if (itemParm.getParmValueNumeric().intValue() > 0) {
  			  		return true;
  			  	} else {
  			  		return false;	
  			  	}		  	  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
      			  if (itemParm.getParmValueNumeric().intValue() > 0) {
      			  	return true;
      			  } else {
      			  	return false;	
      			  }		  	  
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
      			  return null;
      		  }
      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
      			  if (itemParm.getParmValue() != null) {
      			  	  if (itemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
        			  	return true;
      			  	  } else {
      			  		  return false;
      			  	}		  
      			  }
      			  else
      			  {
      				  return false;
      			  }
      		  }
      	  }
       	  return null;
        }
        public static BundleParm getBundleParm (Bundle bundle, String parmName, boolean initParm) {
        	  
      	  	BundleParm returnBundleParm = null;;
      	  
   	 		returnBundleParm = bundle.getBundleParms().get(parmName);
     	  
//        	  for (BundleParm bundleParm : bundle.getBundleParms()) {
//        		  if (bundleParm.getParmName().equalsIgnoreCase(parmName) ) {
//        			  returnBundleParm = bundleParm;
//        			  break;
//        		  }
//        	  }
        	  
        	  if (returnBundleParm == null) {
        		  returnBundleParm = new BundleParm();
          		  returnBundleParm.setParmName(parmName);
        		  initBundleParmAttributesFromEnum (returnBundleParm);
            	  if (returnBundleParm.getDataTypeCd() == null) {
            		  returnBundleParm.setDataTypeCd(ItemConstants.STRING);
            	  }
              	  if (initParm) {
              		  bundle.getBundleParms().put(parmName, returnBundleParm);
              	  }
              		  //              	  bundle.getBundleParms().add(returnBundleParm);
        	  }
   
        	  return returnBundleParm;
          }

        public static void initBundleParmAttributesFromEnum (BundleParm bundleParm ) {
   
        	BundleParmEnum bundleParmEnum = BundleParmEnum.getEnumForName(bundleParm.getParmName());
      	  	if (bundleParmEnum != null) {
      	  		bundleParm.setDataTypeCd(bundleParmEnum.getDataTypeCd());
      	  		bundleParm.setLabel(bundleParmEnum.getLabel());
      	  		bundleParm.setDisplayWidth(bundleParmEnum.getDisplayWidth());
      	  		bundleParm.setFieldLength(bundleParmEnum.getFieldLength());
      	  } else {
      		  	bundleParm.setDataTypeCd(ItemConstants.STRING);
      		  	bundleParm.setLabel("");
      		  	bundleParm.setDisplayWidth(0);
      		  	bundleParm.setFieldLength(0);		  
    	  }
    
        }
        
          public static String getBundleParmString (Bundle bundle, String parmName) {

        	  BundleParm bundleParm = getBundleParm(bundle, parmName, false);
        	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  return bundleParm.getParmValue();			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  return bundleParm.getParmValue();			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {

        			  if (bundleParm.getParmValueNumeric() != null)
        			  {
        				  return bundleParm.getParmValueNumeric().toString();	
        			  }
        			  else
        			  {
        				  return null;
        			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  
        			  if (bundleParm.getParmValueNumeric() != null)
        			  {
        				  return bundleParm.getParmValueNumeric().toString();	
        			  }
        			  else
        			  {
        				  return null;
        			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  GregorianCalendar calendar = new GregorianCalendar();
        			  calendar.setTime(bundleParm.getParmValueDate());
        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        			  int year = calendar.get(GregorianCalendar.YEAR);
        			  String mdy =  Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
        			  return mdy;
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  return bundleParm.getParmValue();			  
        		  }
        	  }
         	  return null;
          }
          
          public static void setBundleParmString (Bundle bundle, String parmName, String parmValue) {

        	  BundleParm bundleParm = getBundleParm(bundle, parmName, true);
        	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  bundleParm.setParmValue(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  bundleParm.setParmValue(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
        			  bundleParm.setParmValue(parmValue);
        			  bundleParm.setParmValueNumeric(new BigDecimal(parmValue));
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  bundleParm.setParmValueNumeric(new BigDecimal(parmValue));
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy"); 
        			  Date parmValueDate;
        			  try        
        			  {             
        				 parmValueDate = df.parse(parmValue);             
        			  } 
        			  catch (ParseException e) { 
        			  	 parmValueDate = null;
        			  }
        			  bundleParm.setParmValueDate(parmValueDate);
        			  GregorianCalendar calendar = new GregorianCalendar();
        			  calendar.setTime(bundleParm.getParmValueDate());
        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        			  int year = calendar.get(GregorianCalendar.YEAR);
        			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
        			  bundleParm.setParmValue(mdy);		
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  bundleParm.setParmValue(parmValue);			  
        		  }

        	  }
          }
        		  
          public static void setBundleParmNumber (Bundle bundle, String parmName, BigDecimal parmValue) {

           	  BundleParm bundleParm = getBundleParm(bundle, parmName, true);
                      	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
          			  if (parmValue != null) {
          				  bundleParm.setParmValue(parmValue.toString());			  
          			  }
        			  bundleParm.setParmValueNumeric(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
          			  if (parmValue != null) {
          				  bundleParm.setParmValue(parmValue.toString());			  
          			  }
        			  bundleParm.setParmValueNumeric(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
          			  if (parmValue != null) {
          				  bundleParm.setParmValue(parmValue.toString());			  
          			  }
        			  bundleParm.setParmValueNumeric(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
          			  if (parmValue != null) {
          				  bundleParm.setParmValue(parmValue.toString());			  
          			  }
        			  bundleParm.setParmValueNumeric(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  // Nothing to do possibly throw exception
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
          			  if (parmValue != null) {
        			  	if (parmValue.intValue() > 0) {
              			  bundleParm.setParmValue(ItemConstants.YES_CD);			  
        			  	} else {
        			  	  bundleParm.setParmValue(ItemConstants.NO_CD);	
        			  	}
          			  }
          		  }
        		  
        	  }
          }
          
          public static void setBundleParmDate (Bundle bundle, String parmName, Date parmValue) {

           	  BundleParm bundleParm = getBundleParm(bundle, parmName, true);
                      	  
        	  if (bundleParm != null && parmValue != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  GregorianCalendar calendar = new GregorianCalendar();
        			  calendar.setTime(parmValue);
        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        			  int year = calendar.get(GregorianCalendar.YEAR);
        			  String mdy = Integer.valueOf(month).toString() + "/" +Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
        			  bundleParm.setParmValue(mdy);		
         			  bundleParm.setParmValueDate(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  GregorianCalendar calendar = new GregorianCalendar();
        			  calendar.setTime(parmValue);
        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        			  int year = calendar.get(GregorianCalendar.YEAR);
        			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
        			  bundleParm.setParmValue(mdy);		
        			  bundleParm.setParmValueDate(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
        			  // Nothing to do, perhaps throw exception
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  // Nothing to do, perhaps throw exception
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  GregorianCalendar calendar = new GregorianCalendar();
        			  calendar.setTime(parmValue);
        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        			  int year = calendar.get(GregorianCalendar.YEAR);
        			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
        			  bundleParm.setParmValue(mdy);		
        			  bundleParm.setParmValueDate(parmValue);			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  // Nothing to do, perhaps throw exception
        		  }

        	  }
          }
          
          public static void setBundleParmBoolean (Bundle bundle, String parmName, Boolean parmValue) {

        	  BundleParm bundleParm = getBundleParm(bundle, parmName, true);
        	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  if (parmValue.booleanValue() == true) {
        				  bundleParm.setParmValue(ItemConstants.YES_CD);	
        			  } else {
        				  bundleParm.setParmValue(ItemConstants.NO_CD);	        				  
        			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  if (parmValue.booleanValue() == true) {
        				  bundleParm.setParmValue(ItemConstants.YES_CD);	
        			  } else {
        				  bundleParm.setParmValue(ItemConstants.NO_CD);	        				  
        			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
        			  if (parmValue.booleanValue() == true) {
        				  bundleParm.setParmValue(ItemConstants.YES_CD);
        				  bundleParm.setParmValueNumeric(new BigDecimal(1));
        			  } else {
        				  bundleParm.setParmValue(ItemConstants.NO_CD);	        				  
        				  bundleParm.setParmValueNumeric(new BigDecimal(1));
           			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  if (parmValue.booleanValue() == true) {
        				  bundleParm.setParmValue(ItemConstants.YES_CD);
        				  bundleParm.setParmValueNumeric(new BigDecimal(1));
        			  } else {
        				  bundleParm.setParmValue(ItemConstants.NO_CD);	        				  
        				  bundleParm.setParmValueNumeric(new BigDecimal(1));
           			  }
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  // DO nothing
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  if (parmValue.booleanValue() == true) {
        				  bundleParm.setParmValue(ItemConstants.YES_CD);	
        			  } else {
        				  bundleParm.setParmValue(ItemConstants.NO_CD);	        				  
        			  }
        		  }
        	  }
          }
          
          public static BigDecimal getBundleParmNumber (Bundle bundle, String parmName) {

           	  BundleParm bundleParm = getBundleParm(bundle, parmName, false);
                      	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  return new BigDecimal(bundleParm.getParmValue());			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  return new BigDecimal(bundleParm.getParmValue());	
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
        			  return bundleParm.getParmValueNumeric();			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  return bundleParm.getParmValueNumeric();			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  return null;
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  	if (bundleParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
        			  		return new BigDecimal(1);
        			  	} else {
        			  		return new BigDecimal(0);	
        			  	}		  
        		  }


        	  }
         	  return null;
          }
          
          public static Date getBundleParmDate (Bundle bundle, String parmName) {

           	  BundleParm bundleParm = getBundleParm(bundle, parmName, false);
                      	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
        			  return null;			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  return null;			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
        			  return null;			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  return null;			  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  return bundleParm.getParmValueDate();
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  return null;
        		  }

        	  }
         	  return null;
          }

          public static Boolean getBundleParmBoolean (Bundle bundle, String parmName) {

           	  BundleParm bundleParm = getBundleParm(bundle, parmName, false);
                      	  
        	  if (bundleParm != null) {
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
      			  	if (bundleParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
      			  		return true;
    			  	} else {
    			  		return false;
    			  	}		  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
        			  if (bundleParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
        			  	return true;
        			  } else {
        			  	return false;
        			  }		  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
      			  	if (bundleParm.getParmValueNumeric().intValue() > 0) {
    			  		return true;
    			  	} else {
    			  		return false;	
    			  	}		  	  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
        			  if (bundleParm.getParmValueNumeric().intValue() > 0) {
        			  	return true;
        			  } else {
        			  	return false;	
        			  }		  	  
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
        			  return null;
        		  }
        		  if (bundleParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
        			  if (bundleParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
          			  	return true;
        			  } else {
        			  	return false;
        			  }		  
        		  }
        	  }
         	  return null;
          }

//          public static AdjustmentItemParm getAdjustmentItemParm (Item item, AdjustmentItem adjustmentItem, String parmName, boolean initParm) {
//          	  
//        	  AdjustmentItemParm returnAdjustmentItemParm = null;;
//      	  
//        	  ItemParm itemParm = getItemParm(item, parmName, initParm);
//        	  
//        	  returnAdjustmentItemParm = adjustmentItem.getAdjItemParms().get(parmName);
//      	  
//        	  if (returnAdjustmentItemParm == null) {
//        		  returnAdjustmentItemParm = new AdjustmentItemParm();
//        		  returnAdjustmentItemParm.setParmName(parmName);
//        		  returnAdjustmentItemParm.setItemParmId(itemParm.getItemId());
//            	  if (initParm) {
//                	  adjustmentItem.getAdjItemParms().put(parmName, returnAdjustmentItemParm);
//            	  }
//        	  }
//  
//        	  return returnAdjustmentItemParm;
//          }
        
//          public static String getAdjustmentItemParmString (Item item, AdjustmentItem adjustmentItem, String parmName) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, false);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, false);
//        	  	  
//        	  if (adjustmentItemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//        			  return adjustmentItemParm.getParmValue();			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  return adjustmentItemParm.getParmValue();			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//        			  
//        			  if (adjustmentItemParm.getParmValueNumeric() != null)
//        			  {
//        				  return adjustmentItemParm.getParmValueNumeric().toString();
//        			  }
//        			  {
//        				  return null;
//        			  }
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//        			  
//        			  if (adjustmentItemParm.getParmValueNumeric() != null)
//        			  {
//        				  return adjustmentItemParm.getParmValueNumeric().toString();
//        			  }
//        			  else
//        			  {
//        				  return null;
//        			  }
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  
//        			if (adjustmentItemParm.getParmValueDate() != null)
//        			{
//        				GregorianCalendar calendar = new GregorianCalendar();
//        				calendar.setTime(adjustmentItemParm.getParmValueDate());
//        				int month = calendar.get(GregorianCalendar.MONTH) + 1;
//        				int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
//        				int year = calendar.get(GregorianCalendar.YEAR);
//        				String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
//        				return mdy;
//        			}
//        			else
//        			  {
//        				  return null;
//        			  }
//        		  }
//      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//      			  return adjustmentItemParm.getParmValue();			  
//      		  }
//        	  }
//         	  return null;
//          }
//          
//          public static void setAdjustmentItemParmString (Item item, AdjustmentItem adjustmentItem, String parmName, String parmValue) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, true);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, true);
//        	  
//        	  if (itemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//        			  adjustmentItemParm.setParmValue(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  adjustmentItemParm.setParmValue(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//        			  adjustmentItemParm.setParmValue(parmValue);
//        			  if ( StringUtils.isNotEmpty(parmValue) ) {
//        				if ( NumberUtils.isNumber(parmValue.trim() ) )  {
//        					adjustmentItemParm.setParmValueNumeric(BigDecimal.valueOf(Long.valueOf(parmValue.trim())));
//        				  }
//        			  }
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//        			  if ( StringUtils.isNotEmpty(parmValue) ) {
//        				  if ( NumberUtils.isNumber(parmValue.trim() ) )  {
//        					  adjustmentItemParm.setParmValueNumeric(BigDecimal.valueOf(Long.valueOf(parmValue.trim())));
//        				  }
//        			  }
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy"); 
//        			  Date parmValueDate;
//        			  try        
//        			  {             
//        				 parmValueDate = df.parse(parmValue);             
//        			  } 
//        			  catch (ParseException e) { 
//        			  	 parmValueDate = null;
//        			  }
//        			  
//        			  if (itemParm.getParmValueDate() != null)
//        			  {
//        				  adjustmentItemParm.setParmValueDate(parmValueDate);
//        				  GregorianCalendar calendar = new GregorianCalendar();
//        				  calendar.setTime(itemParm.getParmValueDate());
//        				  int month = calendar.get(GregorianCalendar.MONTH) + 1;
//        				  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
//        				  int year = calendar.get(GregorianCalendar.YEAR);
//        				  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
//        				  adjustmentItemParm.setParmValue(mdy);
//        			  }
//        			  else
//        			  {
//        				  adjustmentItemParm.setParmValue(null);
//        			  }
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//        			  adjustmentItemParm.setParmValue(parmValue);			  
//        		  }
//        	  }
//          }
//        		  
//          public static void setAdjustmentItemParmNumber (Item item, AdjustmentItem adjustmentItem, String parmName, BigDecimal parmValue) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, true);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, true);
//        	  
//        	  if (itemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//          			  if (parmValue != null) {
//          				  adjustmentItemParm.setParmValue(parmValue.toString());			  
//          			  }
//        			  adjustmentItemParm.setParmValueNumeric(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//          			  if (parmValue != null) {
//          				  adjustmentItemParm.setParmValue(parmValue.toString());			  
//          			  }
//        			  adjustmentItemParm.setParmValueNumeric(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//          			  if (parmValue != null) {
//          				  adjustmentItemParm.setParmValue(parmValue.toString());			  
//          			  }
//        			  adjustmentItemParm.setParmValueNumeric(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//          			  if (parmValue != null) {
//          				  adjustmentItemParm.setParmValue(parmValue.toString());			  
//          			  }
//        			  adjustmentItemParm.setParmValueNumeric(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  // Nothing to do possibly throw exception
//        		  }
//      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//      			 if (parmValue != null) {
//      			  	if (parmValue.intValue() > 0) {
//      			  		adjustmentItemParm.setParmValue(ItemConstants.YES_CD);			  
//      			  	} else {
//      			  		adjustmentItemParm.setParmValue(ItemConstants.NO_CD);	
//      			  	}		  
//        		  }
//      		  	}
//        	  }
//          }
//          
//          public static void setAdjustmentItemParmDate (Item item, AdjustmentItem adjustmentItem, String parmName, Date parmValue) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, true);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, true);
//
//        	  if (itemParm != null && parmValue != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//        			  GregorianCalendar calendar = new GregorianCalendar();
//        			  calendar.setTime(parmValue);
//        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
//        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
//        			  int year = calendar.get(GregorianCalendar.YEAR);
//        			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
//        			  adjustmentItemParm.setParmValue(mdy);		
//        			  adjustmentItemParm.setParmValueDate(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  GregorianCalendar calendar = new GregorianCalendar();
//        			  calendar.setTime(parmValue);
//        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
//        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
//        			  int year = calendar.get(GregorianCalendar.YEAR);
//        			  String mdy =Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
//        			  adjustmentItemParm.setParmValue(mdy);		
//        			  adjustmentItemParm.setParmValueDate(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//        			  // Nothing to do, perhaps throw exception
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//        			  // Nothing to do, perhaps throw exception
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  GregorianCalendar calendar = new GregorianCalendar();
//        			  calendar.setTime(parmValue);
//        			  int month = calendar.get(GregorianCalendar.MONTH) + 1;
//        			  int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
//        			  int year = calendar.get(GregorianCalendar.YEAR);
//        			  String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
//        			  adjustmentItemParm.setParmValue(mdy);		
//        			  adjustmentItemParm.setParmValueDate(parmValue);			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//        			  // Nothing to do, perhaps throw exception
//        		  }
//
//        	  }
//          }
//          
//          public static BigDecimal getAdjustmentItemParmNumber (Item item, AdjustmentItem adjustmentItem, String parmName) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, false);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, false);
//        	  
//        	  if (itemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//        			  return new BigDecimal(adjustmentItemParm.getParmValue());			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  return new BigDecimal(adjustmentItemParm.getParmValue());	
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
////        			  if (adjustmentItemParm.getParmValueNumeric() == null) {
////        				  adjustmentItemParm.setParmValueNumeric(new BigDecimal(0));
////        			  }
//        			  return adjustmentItemParm.getParmValueNumeric();			        				  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
////        			  if (adjustmentItemParm.getParmValueNumeric() == null) {
////        				  adjustmentItemParm.setParmValueNumeric(new BigDecimal(0));
////        			  }
//         			  return adjustmentItemParm.getParmValueNumeric();			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  return null;
//        		  }
//      		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//      			  	if (adjustmentItemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
//      			  		return new BigDecimal(1);
//      			  	} else {
//      			  		return new BigDecimal(0);	
//      			  	}		  
//        		  }
//
//        	  }
//         	  return null;
//          }
//          
//          public static Date getAdjustmentItemParmDate (Item item, AdjustmentItem adjustmentItem, String parmName) {
//
//        	  ItemParm itemParm = getItemParm(item, parmName, false);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, false);
//        	  
//        	  if (itemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//        			  return null;			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  return null;			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//        			  return null;			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//        			  return null;			  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  return adjustmentItemParm.getParmValueDate();
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//        			  return null;			  
//        		  }
//        	  }
//         	  return null;
//          }
//
//          public static Boolean getAdjustmentItemParmBoolean (Item item, AdjustmentItem adjustmentItem, String parmName) {
//
//           	  ItemParm itemParm = getItemParm(item, parmName, false);
//        	  AdjustmentItemParm adjustmentItemParm = getAdjustmentItemParm(item, adjustmentItem, parmName, false);
//                      	  
//        	  if (itemParm != null) {
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.STRING)) {
//      			  	if (adjustmentItemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
//      			  		return true;
//    			  	} else {
//    			  		return false;
//    			  	}		  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.CLOB)) {
//        			  if (adjustmentItemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
//        			  	return true;
//        			  } else {
//        			  	return false;
//        			  }		  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.INTEGER)) {
//      			  	if (adjustmentItemParm.getParmValueNumeric().intValue() > 0) {
//    			  		return true;
//    			  	} else {
//    			  		return false;	
//    			  	}		  	  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.NUMBER)) {
//        			  if (adjustmentItemParm.getParmValueNumeric().intValue() > 0) {
//        			  	return true;
//        			  } else {
//        			  	return false;	
//        			  }		  	  
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.DATE)) {
//        			  return false;
//        		  }
//        		  if (itemParm.getDataTypeCd().equalsIgnoreCase(ItemConstants.BOOLEAN)) {
//        			  if (adjustmentItemParm.getParmValue().equalsIgnoreCase(ItemConstants.YES_CD)) {
//          			  	return true;
//        			  } else {
//        			  	return false;
//        			  }		  
//        		  }
//        	  }
//         	  return false;
//          }

      	public static boolean isFieldChanged(Item item, Date newFieldValue, String parameterName) {
    		
    		Date preUpdateDate = ItemHelperServices.getItemParmDate(item, parameterName);
    		if (preUpdateDate==null) {
    			return newFieldValue != null;
    		} else if (newFieldValue == null) {
    			return true;
    		} else if (!preUpdateDate.equals(newFieldValue)) {
    			return true;
    		}
    		return false;
      	}
          
      	public static boolean isFieldChanged(Item item, String newFieldValue, String parameterName) {
    		
    		String preUpdateString = ItemHelperServices.getItemParmString(item, parameterName);
    		if (preUpdateString == null) {
    			return newFieldValue != null;
    		} else if (newFieldValue == null) {
    			return true;
    		} else if (!preUpdateString.equalsIgnoreCase(newFieldValue)) {
    			return true;
    		}
    		return false;
      	}
      	
      	public static boolean isFieldChanged(Item item, boolean newFieldValue, String parameterName) {
    		
    		boolean preUpdateBoolean = ItemHelperServices.getItemParmBoolean(item, parameterName).booleanValue();
    		
    		if (preUpdateBoolean != newFieldValue) 
    		{
    			return true;
    		}
    		return false;
      	}
      	
      	
      	public static boolean isFieldChanged(Item item, long newFieldValue, String parameterName) {
    		
    		BigDecimal preUpdateNumber = ItemHelperServices.getItemParmNumber(item, parameterName);
    		if (preUpdateNumber==null) {
    			return true;
    		} else if (preUpdateNumber.longValue() != newFieldValue) {
    			return true;
    		}
    		return false;
      	}
      	
     	public static boolean isFieldChanged(Item item, int newFieldValue, String parameterName) {
    		
    		BigDecimal preUpdateNumber = ItemHelperServices.getItemParmNumber(item, parameterName);
    		if (preUpdateNumber==null) {
    			return true;
    		} else if (preUpdateNumber.intValue() != newFieldValue) {
    			return true;
    		}
    		return false;
      	}
      	
          
          
}
