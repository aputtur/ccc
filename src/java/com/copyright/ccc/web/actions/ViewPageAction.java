package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.contentmgmt.SiteMap;
import com.copyright.ccc.web.contentmgmt.SitePage;

public class ViewPageAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response ) 
    {
        String pageCode = request.getParameter("pageCode");
        if (pageCode==null)  pageCode="i404-n"; // page not found
        
        SiteMap pageMap= SiteMap.getInstance();
        SitePage page= pageMap.getPage(pageCode);
        
        // if we didn't find the page, build the friendly "page not found" page
        if (page == null)
        {
            page = pageMap.getPage("i404-n");
        }
        
        // if we can't even find that, then there's some serious problem with
        // the static content system.  Forward to the "page not found" page.
        // TODO: this forward and the above setting of page to "i404-n" may be
        // redundant.  We may want to show different messages for these two cases
        // in the future, so keep the checks separate for now.
        if ( page == null )
        {
            return mapping.findForward( "pageNotFound" );
        }
        
        String layout = page.getLayout(); 

        if (!layout.equals("popup")) {
            String banner = page.getBanner();
            request.setAttribute("Banner", banner);
            String bannerName=banner.substring(7,banner.lastIndexOf("."));
            bannerName = bannerName.substring(0,1).toUpperCase() + bannerName.substring(1) + " Banner";
            request.setAttribute("BannerName",bannerName);
            if (banner.equals("generic_banner.html"))
                request.setAttribute("BannerHeight","");
            else
                request.setAttribute("BannerHeight"," height: 106px;");
        }        
        if (layout.equals("three-column"))
        {
            request.setAttribute("RightSidebar", page.getRightSidebar());
            request.setAttribute("LeftSidebar", page.getLeftSidebar());
        }
        else if (layout.equals("two-column"))
        {
            request.setAttribute("RightSidebar", page.getRightSidebar());            
        }
        request.setAttribute("Layout",layout);
        request.setAttribute("Page",page.getContent());

        String targetForward = "showMain";
        if (layout.equals("popup")) targetForward="showPopup";
        return mapping.findForward(targetForward);
       // getServletConfig().getServletContext().getRequestDispatcher(targetJsp).forward(request, response);

      
                                                
    }
    

    
}
