package com.gis.manager.runner;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author:wangmeng
 * @Date:创建于2018/10/17 13:56
 * @Description:
 */
@Component
@Order(1)
public class ServerRegisterApplicationRunner implements ApplicationRunner{
    @Override
    public void run(ApplicationArguments args){
        gdal.AllRegister();
        //支持中文路径
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        //设置最大缓存
        gdal.SetCacheMax(Integer.MAX_VALUE);
        /*USE_RRD(YES,NO,IF_NEEDED,IF_SAFER)
            External overviews can be created in the BigTIFF format by using the BIGTIFF_OVERVIEW configuration option : –config BIGTIFF_OVERVIEW {IF_NEEDED|IF_SAFER|YES|NO}. The default value is IF_SAFER starting with GDAL 2.3.0 (previously was IF_NEEDED). The behaviour of this option is exactly the same as the BIGTIFF creation option documented in the GeoTIFF driver documentation.
            YES:forces BigTIFF.
            NO:forces classic TIFF.
            IF_NEEDED:will only create a BigTIFF if it is clearly needed (uncompressed, and overviews larger than 4GB).
            IF_SAFER:will create BigTIFF if the resulting file might exceed 4GB.
        */
        //gdal.SetConfigOption("USE_RRD", "YES");
        //linux最大允许值1024
        gdal.SetConfigOption("GDAL_MAX_DATASET_POOL_SIZE", "450");
        ogr.RegisterAll();
    }
}
