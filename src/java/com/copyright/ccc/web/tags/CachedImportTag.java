package com.copyright.ccc.web.tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copyright.ccc.web.contentmgmt.ContentManagementException;
import com.copyright.ccc.web.contentmgmt.ContentNotFoundException;


/**The <tt>ImportTag</tt> allows inclusion of static content from locations
 * outside of the root of the current web-context on the local server or via 
 * a URL from a remote server.
 */
public abstract class CachedImportTag extends ImportTag
{

	private static final long serialVersionUID = 1L;
	
    private static Pattern metaPattern;
    private static Pattern titlePattern;
    static {
        metaPattern = Pattern.compile("\\s*<[Mm][Ee][Tt][Aa](.*)");
        titlePattern = Pattern.compile("\\s*<title>(.*)</title>\\s*");
    }

    private void rebuildCacheIfNeeded() throws ContentManagementException
    {
        File original = null, cachedContent = null, cachedHeader = null;

        original = new File( getOriginalPath() );
        cachedContent = new File( getCachedContentPath() );
        cachedHeader = new File( getCachedHeaderPath() );

        if (!cachedContent.exists() || (original.lastModified() > cachedContent.lastModified()) || 
            !cachedHeader.exists() || (original.lastModified() > cachedHeader.lastModified()))
        {
            try
            {
                parseOriginal();
            }
            catch ( IOException e )
            {
                throw new ContentManagementException( e );
            }
        }
    }

    @Override
    protected void prepareForFileRead() throws ContentManagementException
    {
        rebuildCacheIfNeeded();
    }
    
    @Override
    protected String getContentFile()
    {
        return getCachedComponent();
    }
    

    private void parseOriginal() throws ContentNotFoundException, IOException
    {
        StringBuffer header = new StringBuffer();
        StringBuffer content = new StringBuffer();
        BufferedReader reader = null;
        
        try
        {
            reader = new BufferedReader(new FileReader(getOriginalPath()));
        
        try{
        	 boolean contentStarted = false;
             String line;
             Matcher titleMatcher;
             Matcher metaMatcher;
             
             while ((line = reader.readLine()) != null)
             {
                 if (contentStarted)
                     content.append(line + "\n");
                 else
                 {
                     titleMatcher = titlePattern.matcher(line);
                     metaMatcher = metaPattern.matcher(line);
                     if (titleMatcher.matches() || metaMatcher.matches())
                         header.append(line + "\n");
                     else
                     {
                         contentStarted = true;
                         content.append(line + "\n");
                     }
                 }
             }             
        }
        finally{
        	 //no need to check for null
            //any exceptions thrown here will be caught by 
            //the outer catch block
            reader.close();
        }
        }
        catch ( FileNotFoundException e )
        {
            throw new ContentNotFoundException( e );
        }
        
       
        writeFile(new File(getCachedHeaderPath()),header.toString());
        writeFile(new File(getCachedContentPath()),content.toString());
    }

    private void writeFile(File file, String content) throws IOException
    {
        File directory = new File(file.getParent());
        directory.mkdirs();
        FileWriter writer = new FileWriter(file);
        try
        {
        	writer.write(content);
        }
        finally
        {
        	writer.close();
        }
    }


    protected abstract String getCachedComponent();

    private String getOriginalPath()
    {
        return getRoot() + File.separator + getFile();
    }
    private String getCachedHeaderPath()
    {
        return getRoot() + File.separator + "parsed" + File.separator + "header_content" + 
                             File.separator + getFile();
    }
    private String getCachedContentPath()
    {
        return getRoot() + File.separator + "parsed" + File.separator + "content" + 
                             File.separator + getFile();
    }
}
