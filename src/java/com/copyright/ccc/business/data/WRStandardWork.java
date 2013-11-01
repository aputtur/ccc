package com.copyright.ccc.business.data;

import java.util.List;

import com.copyright.data.inventory.StandardWork;

public class WRStandardWork extends StandardWork {

	private static final long serialVersionUID = 1L;
	
    private long wrWrkInst;
    private List<Long> tfWksInstList;
    
    //  2009-10-23  MSJ
    //  I need these for display purposes only.
    
    private String idnoLabel = null;
    private String idnoTypeCd = null;
    private String series = null;
    private String seriesNumber = null;
    private String publicationType = null;
    private String country = null;
    private String language = null;
    private String pages = null;
    
    public WRStandardWork() {
    }

    public void setWrwrkinst(long wrWrkInst) {
        this.wrWrkInst = wrWrkInst;
    }

    public long getWrWrkInst() {
        return wrWrkInst;
    }

    public void setTfWksInstList(List<Long> tfWksInstList) {
        this.tfWksInstList = tfWksInstList;
    }

    public List<Long> getTfWksInstList() {
        return tfWksInstList;
    }
    
    //  2009-10-23  MSJ
    //  Accessors for additional fields for new metadata.
    
    public String getSeries()          { return series;          }
    public String getSeriesNumber()    { return seriesNumber;    }
    public String getPublicationType() { return publicationType; }
    public String getCountry()         { return country;         }
    public String getLanguage()        { return language;        }
    public String getIdnoLabel()       { return idnoLabel;       }
    public String getIdnoTypeCd()      { return idnoTypeCd;      }
    public String getPages()           { return pages;           }
    
    public void setSeries(String s)          { series = s;          }
    public void setSeriesNumber(String s)    { seriesNumber = s;    }
    public void setPublicationType(String s) { publicationType = s; }
    public void setCountry(String s)         { country = s;         }
    public void setLanguage(String s)        { language = s;        }
    public void setIdnoLabel(String s)     	 { idnoLabel = s;      }
    public void setIdnoTypeCd(String s)      { idnoTypeCd = s;      }
    public void setPages(String s)           { pages = s;           }
}
