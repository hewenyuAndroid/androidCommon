package com.newsee.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.newsee.common.global.FileProvider7;
import com.newsee.common.global.PermissionManager;

import java.io.File;
import java.util.List;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/6/21 15:29
 * 说明: App工具类
 * ====================================
 */
public class AppUtil {

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 安装应用
     *
     * @param activity
     * @param path
     * @return
     */
    public static void installApp(Activity activity, final String path) {
        PermissionManager.requestPermission(activity, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new PermissionManager.OnRequestPermissionListener() {
            @Override
            public void onPermissionGranted(Context context) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    FileProvider7.setIntentDataAndType(
                            context,
                            intent,
                            "application/vnd.android.package-archive",
                            new File(path),
                            true
                    );
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(Context context) {
                PermissionManager.showTipsDialog(context);
            }
        });
        
    }

    /**
     * 获取某个包名的App是否已经安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasAppInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 根据包名启动第三方App
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean launchAppByPackageName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断APP是否运行在前台
     *
     * @param context
     * @return
     */
    public static boolean isAppRunningForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        try {
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
            if (runningTasks != null && runningTasks.size() >= 1) {
                boolean isRunning = context.getPackageName().equalsIgnoreCase((runningTasks.get(0)).baseActivity.getPackageName());
                return isRunning;
            } else {
                return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取清单文件中Application层级的值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getManifestApplicationValue(Context context, String key) {
        String value = "";
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断当前是否是Debug
     *
     * @param context
     * @return
     */
    public static boolean isApkDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 拨打电话
     *
     * @param activity
     * @param phone
     */
    public static void callPhone(final Activity activity, final String phone) {
        if (activity == null || activity.isFinishing() || TextUtils.isEmpty(phone)) {
            return;
        }
        PermissionManager.requestPermission(
                activity,
                Manifest.permission.CALL_PHONE,
                new PermissionManager.OnRequestPermissionListener() {
                    @Override
                    public void onPermissionGranted(Context context) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phone.trim());
                        intent.setData(data);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(Context context) {
                        PermissionManager.showTipsDialog(activity);
                    }
                });
    }

}
