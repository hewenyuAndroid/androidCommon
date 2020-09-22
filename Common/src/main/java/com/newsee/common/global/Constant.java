package com.newsee.common.global;

import android.content.Context;
import android.os.Environment;

import com.newsee.common.utils.PathUtil;

import java.io.File;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/22 16:43
 * 说明:
 * ====================================
 */
public class Constant {

    /**
     * 缓存根目录
     *
     * @param context
     * @return
     */
    public static String getRootPath(Context context) {
        String rootPath = PathUtil.getCacheDir(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  // SD 卡可用
            rootPath = PathUtil.getExternalStoragePath() + File.separator + "newsee";
        }
        return rootPath;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getImageCachePath(Context context) {
        return getRootPath(context) + File.separator + "image";
    }

    /**
     * 获取录音文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getAudioCachePath(Context context) {
        return getRootPath(context) + File.separator + "audio";
    }

    /**
     * 获取视频文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getMediaCachePath(Context context) {
        return getRootPath(context) + File.separator + "media";
    }

    /**
     * 获取缓存文件路径
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        return getRootPath(context) + File.separator + "file";
    }

    /**
     * 获取数据库文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getDbPath(Context context) {
        return getRootPath(context) + File.separator + "db";
    }

}
