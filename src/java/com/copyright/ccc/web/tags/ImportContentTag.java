package com.copyright.ccc.web.tags;

import java.io.File;

public class ImportContentTag extends CachedImportTag
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
        filename.append("content");
        filename.append(File.separator);
        filename.append(getFile());
        
        return filename.toString();
    }
}
