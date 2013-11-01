package com.copyright.ccc.web.tags;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.web.WebConstants;
import com.copyright.workbench.logging.LoggerHelper;

public class ContextualHelpTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean rollover = false;
	protected boolean rolloverOnly = false;
    protected String helpId = null;
    protected String styleClass = null;
    protected String styleId = null;
    protected String style = null;
    protected String scope = null;
    protected String titleName = null;
    protected String titleProperty = null;
    protected String bodyName = null;
    protected String bodyProperty = null;
    
    private String jsTitleVarName = "";
    private String jsBodyVarName = "";
    private String contextualHelpClass = "";
    private String titleText = "";
    private String bodyText = "";

    private static final Logger _logger = LoggerHelper.getLogger();
    
    /**Method called at start of tag.
     * @return EVAL_BODY_BUFFERED
     */
    @Override
    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = pageContext.getOut();
                        
            StringBuffer results = new StringBuffer();
            
            initializeHelpContent();
            
            String url = TagUtils.getInstance().getActionMappingURL( "contextualHelp.do", pageContext );
            String query = "?operation=defaultOperation&" + WebConstants.RequestKeys.HELP_TITLE_ID + "=" + jsTitleVarName + "&" + 
                WebConstants.RequestKeys.HELP_BODY_ID + "=" + jsBodyVarName;
            String completeUrl = url + query;
            
            results.append(createJavascriptHelpConstants());
            results.append("<a href=\"" + completeUrl + "\" target=\"helpWindow\" ");
            if (this.rolloverOnly) {
                results.append("onclick=\"return false;\" ");
            }
            else {
                results.append("onclick=\"openHelpWindow(this);return false;\" ");
            }
            /*if( rollover )
            {
                results.append("onmouseover=\"openHelpPopup(event.clientX + 15, event.clientY + 15, " + jsTitleVarName + ", " + jsBodyVarName + ");\" ");
                results.append("onmouseout=\"closeHelpPopup();\" ");
            }*/
            results.append( createStyleAttributes() );
            results.append(">");
            
            out.print( results.toString() );
        } catch (Exception e)
        {
    		_logger.error( ExceptionUtils.getFullStackTrace(e) );
        }
        return EVAL_BODY_INCLUDE;
    }

   
    /**Method called at end of tag.
     * @return EVAL_PAGE
     */
    @Override
    public int doEndTag() throws JspException
    {
        StringBuffer results = new StringBuffer();
       
        results.append("</a>");

        TagUtils.getInstance().write(pageContext, results.toString());
        
        return EVAL_PAGE;
    }
    
    private void initializeHelpContent() throws JspException
    {
        TagUtils tagUtils = TagUtils.getInstance();
        
        if( StringUtils.isNotEmpty( helpId ) )
        {
            titleText = tagUtils.message( pageContext, "contextualHelp", null, "help.title." + helpId );
            bodyText = tagUtils.message( pageContext, "contextualHelp", null, "help.body." + helpId );
        }
        else
        {
            if( StringUtils.isNotEmpty( titleName ) )
                titleText = (String) tagUtils.lookup( pageContext, titleName, titleProperty, scope );
            if( StringUtils.isNotEmpty( bodyName ) )
                bodyText = (String) tagUtils.lookup( pageContext, bodyName, bodyProperty, scope );
        }
        
        String uniqueId = String.valueOf((titleText + " " + bodyText).hashCode()).replaceAll("-","");
        jsTitleVarName = "contextualHelp_title_" + uniqueId;
        jsBodyVarName = "contextualHelp_body_" + uniqueId;
        contextualHelpClass = "contextualHelp_rollover_" + uniqueId;
    }
    
    private String createJavascriptHelpConstants() throws JspException
    {
        StringBuffer script = new StringBuffer();
            
        script.append( "<script>" );
        script.append("var " + jsTitleVarName + " = '" + escapeStringForJavascript(titleText) + "'; ");
        script.append("\n");
        script.append("var " + jsBodyVarName + " = '" + escapeStringForJavascript(bodyText) + "'; ");
        script.append( "</script>" );
        
        return script.toString();
    }
    
    private String escapeStringForJavascript( String originalString )
    {
        String escapedSingleQuotes = originalString.replaceAll( "'", "&#39;" );

        try
        {
            String encodedText = URLEncoder.encode(escapedSingleQuotes, "UTF-8");
            String replaced = encodedText.replaceAll("\\%0D\\%0A","<br>");
            return URLDecoder.decode(replaced, "UTF-8");
        } 
        catch( UnsupportedEncodingException e)
        {
            return escapedSingleQuotes;
        }
    }
    
    private String createStyleAttributes()
    {
        StringBuffer styleAttributes = new StringBuffer();
     
        if(StringUtils.isNotEmpty( style ))
            styleAttributes.append( "style=\"" + style + "\" ");

        String completeClassName = "";
        if( this.rollover || this.rolloverOnly ) {
            completeClassName = contextualHelpClass;
        }
        if( StringUtils.isNotEmpty( styleClass) ) {
            completeClassName = (StringUtils.isEmpty(completeClassName)) ? styleClass : completeClassName + " " + styleClass;
        }
        if( StringUtils.isNotEmpty(completeClassName) ) {
            styleAttributes.append( "class=\"" + completeClassName + "\" ");
        }
        if(StringUtils.isNotEmpty( id )) {
            styleAttributes.append( "id=\"" + id + "\" ");
        }
        if(StringUtils.isNotEmpty( styleId )) {
            styleAttributes.append( "id=\"" + styleId + "\" ");
        }
        return styleAttributes.toString();
    }

    public void setRollover(boolean rollover)
    {
        this.rollover = rollover;
    }
    
    public boolean isRollover()
    {
        return rollover;
    }
    
    public void setRolloverOnly(boolean rollover)
    {
        this.rolloverOnly = rollover;
    }
    
    public boolean isRolloverOnly()
    {
        return rolloverOnly;
    }
    
    public void setHelpId(String helpId)
    {
        this.helpId = helpId;
    }
    
    public String getHelpId()
    {
        return helpId;
    }
    
    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }
    
    public String getStyleClass()
    {
        return styleClass;
    }
    
    public void setStyleId(String styleId)
    {
        this.styleId = styleId;
    }
    
    public String getStyleId()
    {
        return styleId;
    }
    
    public void setStyle(String style)
    {
        this.style = style;
    }
    
    public String getStyle()
    {
        return style;
    }

    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getTitleName()
    {
        return titleName;
    }

    public void setTitleProperty(String titleProperty)
    {
        this.titleProperty = titleProperty;
    }

    public String getTitleProperty()
    {
        return titleProperty;
    }

    public void setBodyName(String bodyName)
    {
        this.bodyName = bodyName;
    }

    public String getBodyName()
    {
        return bodyName;
    }

    public void setBodyProperty(String bodyProperty)
    {
        this.bodyProperty = bodyProperty;
    }

    public String getBodyProperty()
    {
        return bodyProperty;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getScope()
    {
        return scope;
    }
}
