package com.copyright.ccc.web.tags;

import java.io.File;


/**The <tt>ImportTag</tt> allows inclusion of static content from locations
 * outside of the root of the current web-context on the local server or via 
 * a URL from a remote server.
 */
public class ImportHeaderTag extends CachedImportTag
{

        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	    @Override
		protected String getCachedComponent()
        {
            StringBuffer filename= new StringBuffer(getRoot());
            filename.append(File.separator);
            filename.append("parsed");
            filename.append(File.separator);
            filename.append("header_content");
            filename.append(File.separator);
            filename.append(getFile());
            
            return filename.toString();
        }

}
