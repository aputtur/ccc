package com.copyright.ccc.business.services.cart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.base.Constants;
import com.copyright.base.enums.DurationEnum;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.order.UsageDataRepublication;

public class ECommerceUtils
{

  private static final int CONTENT_TYPE_UNITARY_QUANTITY = 1;
  private static final int CONTENT_TYPE_ZERO_QUANTITY = 0;
  private static final String FOUR_DIGIT_YEAR_MASK = "yyyy";
  
  
  /**
   * Returns a <code>Date</code> representation of an instance of <code>String</code>,
   * formatted using the YYYY mask.
   */
  public static Date transformYearToDate( String year ) throws ParseException
  {
    return new SimpleDateFormat( FOUR_DIGIT_YEAR_MASK ).parse( year );  
  }
  
  /**
   * Returns a <code>String</code> representation of an instance of <code>Date</code>,
   * formatted using the YYYY mask.
   */
  public static String getYearYYYY(Date date) throws ParseException
  {
        return new SimpleDateFormat(FOUR_DIGIT_YEAR_MASK).format( date );

  }
  
  public static String getTypeOfContent( UsageDataRepublication usageDataRepublication )
  {
    String typeOfContent= Constants.EMPTY_STRING;
    
    if( usageDataRepublication.getFullArticle() != null && usageDataRepublication.getFullArticle().equalsIgnoreCase(ECommerceConstants.FULL_ARTICLE_YES) )
    {
      typeOfContent = RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER;
      
    }else if( usageDataRepublication.getNumExcerpts() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_EXCERPT;
     
    }else if( usageDataRepublication.getNumQuotes() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_QUOTATION;
     
    }else if( usageDataRepublication.getNumCharts() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_CHART;
     
    }else if( usageDataRepublication.getNumFigures() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE;
     
    }else if( usageDataRepublication.getNumPhotos() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_PHOTOGRAPH;
     
    }else if( usageDataRepublication.getNumIllustrations() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_ILLUSTRATION;
     
    }else if( usageDataRepublication.getNumGraphs() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
    {
     typeOfContent = RepublicationConstants.CONTENT_GRAPH;
     
    }else if( usageDataRepublication.getRlsPages() > 0 )
    {
      typeOfContent = RepublicationConstants.CONTENT_SELECTED_PAGES;
    }else if ( usageDataRepublication.getNumCartoons() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY )
    {
      typeOfContent = RepublicationConstants.CONTENT_CARTOONS;
    }else if ( usageDataRepublication.getNumLogos() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY )
    {
      typeOfContent = RepublicationConstants.CONTENT_LOGOS;
    }
    
    return typeOfContent;
  }
    
  public static String getTypeOfContentDescription( UsageDataRepublication usageDataRepublication )
  {
      String typeOfContent= Constants.EMPTY_STRING;
      
      if( usageDataRepublication.getFullArticle() != null && usageDataRepublication.getFullArticle().equalsIgnoreCase(ECommerceConstants.FULL_ARTICLE_YES) )
      {
        typeOfContent = TransactionConstants.CONTENT_FULL_ARTICLE_CHAPTER;
        
      }else if( usageDataRepublication.getNumExcerpts() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_EXCERPT;
       
      }else if( usageDataRepublication.getNumQuotes() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_QUOTATION;
       
      }else if( usageDataRepublication.getNumCharts() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_CHART;
       
      }else if( usageDataRepublication.getNumFigures() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_FIGURE_DIAGRAM_TABLE;
       
      }else if( usageDataRepublication.getNumPhotos() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_PHOTOGRAPH;
       
      }else if( usageDataRepublication.getNumIllustrations() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_ILLUSTRATION;
       
      }else if( usageDataRepublication.getNumGraphs() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY)
      {
       typeOfContent = TransactionConstants.CONTENT_GRAPH;
       
      }else if( usageDataRepublication.getRlsPages() > 0 )
      {
        typeOfContent = TransactionConstants.CONTENT_SELECTED_PAGES;
      }else if ( usageDataRepublication.getNumCartoons() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY )
      {
        typeOfContent = TransactionConstants.CONTENT_CARTOONS;
      }else if ( usageDataRepublication.getNumLogos() == ECommerceUtils.CONTENT_TYPE_UNITARY_QUANTITY )
      {
        typeOfContent = TransactionConstants.CONTENT_LOGOS;
      }
      
      return typeOfContent;
  }

  public static UsageDataRepublication setTypeOfContent(UsageDataRepublication usageDataRepublication, String typeOfContent)
  {
    
    if( typeOfContent.equals( RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER )  )
    {
      usageDataRepublication.setFullArticle(ECommerceConstants.FULL_ARTICLE_YES);
    }
    else if( typeOfContent.equals( RepublicationConstants.CONTENT_EXCERPT ) )
    {
      usageDataRepublication.setNumExcerpts( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_QUOTATION ) )
    {
      usageDataRepublication.setNumQuotes( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_CHART ) )
    {
      usageDataRepublication.setNumCharts( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_GRAPH ) )
    {
      usageDataRepublication.setNumGraphs( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE ) )
    {
      usageDataRepublication.setNumFigures( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_PHOTOGRAPH ) )
    {
      usageDataRepublication.setNumPhotos( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_ILLUSTRATION ) )
    {
      usageDataRepublication.setNumIllustrations( CONTENT_TYPE_UNITARY_QUANTITY );
      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_SELECTED_PAGES ) )
    {
      //do nothing      
    }else if( typeOfContent.equals( RepublicationConstants.CONTENT_CARTOONS ) )
    {
      usageDataRepublication.setNumCartoons( CONTENT_TYPE_UNITARY_QUANTITY );
    }
    else
    {
      throw new IllegalArgumentException( "Invalid content type." );
    } 
    
    return usageDataRepublication;
  }


  public static UsageDataRepublication zeroContentTypeQuantities( UsageDataRepublication usageDataRepublication)
  {
    return zeroContentTypeQuantities( usageDataRepublication, false );
  }

  private static UsageDataRepublication zeroContentTypeQuantities( UsageDataRepublication usageDataRepublication, boolean mustZeroRLSPages )
  {
  
    usageDataRepublication.setFullArticle(ECommerceConstants.FULL_ARTICLE_NO);
    usageDataRepublication.setNumExcerpts(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumQuotes(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumCharts(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumGraphs(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumFigures(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumPhotos(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    usageDataRepublication.setNumIllustrations(ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY);
    
    
    if ( mustZeroRLSPages )
    {
      usageDataRepublication.setRlsPages( ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY );
    }
    
    usageDataRepublication.setNumCartoons( ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY );
    usageDataRepublication.setNumLogos( ECommerceUtils.CONTENT_TYPE_ZERO_QUANTITY );
    
    return usageDataRepublication;
  
  }
  
  public static List<LabelValueBean> getDurationCodes() {
		
	List<LabelValueBean> durationCodes = new ArrayList<LabelValueBean>();

	LabelValueBean labelValueBean = new LabelValueBean();

	labelValueBean.setLabel(DurationEnum.TO_30_DAYS.getDesc());
	labelValueBean.setValue("0");
	durationCodes.add(labelValueBean);

	labelValueBean.setLabel(DurationEnum.TO_180_DAYS.getDesc());
	labelValueBean.setValue("1");
	durationCodes.add(labelValueBean);

	labelValueBean.setLabel(DurationEnum.TO_365_DAYS.getDesc());
	labelValueBean.setValue("2");
	durationCodes.add(labelValueBean);

	labelValueBean.setLabel(DurationEnum.UNLIMITED_DAYS.getDesc());
	labelValueBean.setValue("3");
	durationCodes.add(labelValueBean);

	return durationCodes;

  }
  

}
