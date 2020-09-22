package com.newsee.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 15:55
 * 说明:
 * ====================================
 */
public class PathUtil {

    // region ------------- 外部存储 -------------

    /**
     * 获取SD卡存储的根目录
     * /storage/emulated/0
     *
     * @return
     */
    public static String getExternalStoragePath() {
        if (isSDCardEnable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            // 返回内部存储的根目录
            return getDataDirectory();
        }
    }

    /**
     * 获取应用在外部存储中的 cache 文件夹路径(卸载时会删除该数据)
     * /storage/emulated/0/Android/data/包名/cache
     *
     * @return
     */
    public static String getExternalCacheDir(Context context) {
        if (isSDCardEnable()) {
            return context.getExternalCacheDir().getAbsolutePath();
        } else {
            // 返回内部存储的 cache 路径
            return getCacheDir(context);
        }
    }

    /**
     * 获取应用在外部存储中的 file 文件夹路径(卸载时会删除该数据)
     * /storage/emulated/0/Android/data/包名/files/{dirName}
     *
     * @param context
     * @param dirName 自定义文件夹
     * @return
     */
    public static String getExternalFileDir(Context context, String dirName) {
        if (isSDCardEnable()) {
            return context.getExternalFilesDir(dirName).getAbsolutePath();
        } else {
            // 返回内部存储的file路径
            return getFileDir(context);
        }
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // endregion --------------------------------

    // region ------------- 内部存储 -------------

    /**
     * 获取内部存储的根目录
     * /data
     *
     * @return
     */
    public static String getDataDirectory() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 获取应用在内部存储中的 files 文件夹路径
     * /data/user/0/包名/files
     *
     * @param context
     * @return
     */
    public static String getFileDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取应用在内部存储中的 cache 文件夹路径
     * /data/user/0/包名/cache
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获取应用在内部存储中的自定义路径
     * /data/user/0/包名/{dirName}
     *
     * @param context
     * @param dirName
     * @return
     */
    public static String getDir(Context context, String dirName) {
        return context.getDir(dirName, Context.MODE_PRIVATE).getAbsolutePath();
    }

    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param pPath
     */
    public static void deleteDir(String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param dir
     */
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    // endregion --------------------------------

}
