package com.copyright.ccc.business.services.search;

import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.service.inventory.MapperServiceAPI;
import com.copyright.service.inventory.MapperServiceFactory;

public final class MapperServices
{
    private MapperServices()
    {
    }
    
    public static int getUsageDescriptorFromTpuInstAndPrdInst( long tpuInst, long prdInst )
    {
        if( prdInst == 1 && tpuInst == 1 )
            return UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY;
        else if( prdInst == 2 && tpuInst == 1 )
            return UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY;
        else if( prdInst == 3 && tpuInst == 1 )
            return UsageDescriptor.NON_ACADEMIC_LICENSE_PHOTOCOPY;
        else if( prdInst == 48 && tpuInst == 133 )
            return UsageDescriptor.NON_ACADEMIC_LICENSE_EMAIL;
        else if( prdInst == 48 && tpuInst == 134 )
            return UsageDescriptor.NON_ACADEMIC_LICENSE_EXTRANET;
        else if( prdInst == 48 && tpuInst == 203 )
            return UsageDescriptor.NON_ACADEMIC_LICENSE_INTERNET;
        else if( prdInst == 48 && tpuInst == 204 )
            return UsageDescriptor.NON_ACADEMIC_LICENSE_INTRANET;
        else if( prdInst == 12 && tpuInst == 8 )
            return UsageDescriptor.ACADEMIC_TRX_SCAN;
        else if( prdInst == 36 && tpuInst == 134 )
            return UsageDescriptor.NON_ACADEMIC_TRX_EXTRANET;
        else if( prdInst == 36 && tpuInst == 203 )
            return UsageDescriptor.NON_ACADEMIC_TRX_INTERNET;
        else if( prdInst == 36 && tpuInst == 204 )
            return UsageDescriptor.NON_ACADEMIC_TRX_INTRANET;
        else if( prdInst == 36 && tpuInst == 133 )
            return UsageDescriptor.NON_ACADEMIC_TRX_EMAIL;
        else if( prdInst == 44 && tpuInst == 203 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTERNET;
        else if( prdInst == 44 && tpuInst == 204 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_INTRANET;
        else if( prdInst == 44 && tpuInst == 205 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_LINKING;
        else if( prdInst == 44 && tpuInst == 206 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_FRAMING;
        else if( prdInst == 44 && tpuInst == 207 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL;
        else if( prdInst == 44 && tpuInst == 189 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_CDROM;
        else if( prdInst == 44 && tpuInst == 190 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DVD;
        else if( prdInst == 44 && tpuInst == 177 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_JOURNAL;
        else if( prdInst == 44 && tpuInst == 178 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_MAGAZINE;
        else if( prdInst == 44 && tpuInst == 184 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSPAPER;
        else if( prdInst == 44 && tpuInst == 185 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSLETTER;
        else if( prdInst == 44 && tpuInst == 186 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DISSERTATION;
        else if( prdInst == 44 && tpuInst == 187 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE;
        else if( prdInst == 44 && tpuInst == 188 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PAMPHLET;
        else if( prdInst == 44 && tpuInst == 211 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_ADVERTISEMENT;
        else if( prdInst == 44 && tpuInst == 214 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PRESENTATION;
        else if( prdInst == 44 && tpuInst == 172 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TEXTBOOK;
        else if( prdInst == 44 && tpuInst == 173 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TRADEBOOK;
        else if( prdInst == 44 && tpuInst == 209 )
            return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_OTHERBOOK;
        
        else return -1;
    }
    
    public static long getTpuInstFromUsageDescriptor( int usageDescriptorTypeOfUse )
    {
        MapperServiceAPI mapperService = MapperServiceFactory.getInstance().getService();
        
        return mapperService.getUnderlyingTpuInst( usageDescriptorTypeOfUse );
    }
}
