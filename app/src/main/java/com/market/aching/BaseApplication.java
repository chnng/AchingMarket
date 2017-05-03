package com.market.aching;

import android.app.Application;

import com.market.aching.database.DataBaseHelper;
import com.market.aching.util.Global;

/**
 * Created by Administrator on 2016/11/5.
 */

public class BaseApplication extends Application
{
    private static BaseApplication mApplication;

    public static BaseApplication getInstance()
    {
        return mApplication;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mApplication = this;
        Global.getAppPath();
        DataBaseHelper.initDBManager(this);
    }
///**
// *
// * 开源Imageloder的全局配置
// */
//    /**
//     * 初始化图片加载相关
//     *
//     * @param context
//     */
//    private void initImageLoader(Context context) {
//        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 4);
//        MemoryCache memoryCache;
//        if (Build.VERSION.SDK_INT >= 9) {
//            memoryCache = new LruMemoryCache(memoryCacheSize);
//        } else {
//            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
//        }
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 3)
//                .memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
//    }

}
