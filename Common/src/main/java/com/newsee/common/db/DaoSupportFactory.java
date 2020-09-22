package com.newsee.common.db;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/6/28 19:19
 * 说明:
 * ====================================
 */
public class DaoSupportFactory {

    /**
     * 默认的数据库文件夹
     */
    private static final String DEFAULT_ROOT = "database";

    private volatile static DaoSupportFactory mInstance;

    private SQLiteDatabase mSQLiteDatabase;

    private Context mContext;
    private String mDbName;

    private DaoSupportFactory() {

    }

    public static DaoSupportFactory getInstance() {
        if (mInstance == null) {
            synchronized (DaoSupportFactory.class) {
                if (mInstance == null) {
                    mInstance = new DaoSupportFactory();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        init(context, null);
    }

    /**
     * 指定db文件名称
     *
     * @param context
     * @param dbName
     */
    public void init(Context context, String dbName) {
        init(context, context.getCacheDir().getAbsolutePath(), dbName);
    }

    /**
     * 指定缓存目录,db文件名称
     *
     * @param context
     * @param cacheDir
     * @param dbName
     */
    public void init(Context context, String cacheDir, String dbName) {
        if (TextUtils.isEmpty(cacheDir)) {
            throw new RuntimeException("No find db cache dir!");
        }

        // 判断是否有读写权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        closeDb();
        this.mContext = context.getApplicationContext();
        this.mDbName = checkDbName(context, dbName);
        cacheDir.trim();

        String dbPath = null;
        if (cacheDir.contains(mContext.getCacheDir().getAbsolutePath()) // 内置缓存目录
                || cacheDir.contains(mContext.getExternalCacheDir().getAbsolutePath())  // 外置缓存目录
                || cacheDir.contains(Environment.getExternalStorageDirectory().getAbsolutePath())   // 外置SD卡目录
        ) {
            dbPath = cacheDir + File.separator;
        } else {
            // 没有指定那一块存储默认使用内部缓存
            if (!cacheDir.startsWith(File.separator)) {
                cacheDir = File.separator + cacheDir;
            }
            if (!cacheDir.endsWith(File.separator)) {
                cacheDir = cacheDir + File.separator;
            }
            dbPath = mContext.getCacheDir() + cacheDir;
        }
        dbPath = dbPath + DEFAULT_ROOT;

        // 创建db文件存储的目录
        File dbRoot = new File(dbPath);
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }

        // 创建db文件
        File dbFile = new File(dbRoot, mDbName);
        // 打开/创建db文件
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    /**
     * 检查数据库名称
     *
     * @param context
     * @param dbName  默认使用项目名称作为数据库名称
     * @return
     */
    private String checkDbName(Context context, String dbName) {
        if (TextUtils.isEmpty(dbName)) {
            StringBuffer sb = new StringBuffer();
            PackageManager pm = context.getPackageManager();
            sb.append(context.getApplicationInfo().loadLabel(pm).toString())    // 默认使用项目名称作为db文件的名称
                    .append(".db");
            return sb.toString();
        } else {
            if (!dbName.endsWith(".db")) {
                dbName = dbName + ".db";
            }
        }
        return dbName;
    }

    /**
     * 获取数据库支持的Dao
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSQLiteDatabase, clazz);
        return daoSupport;
    }

    /**
     * 获取db文件的名称
     *
     * @return
     */
    public String getDbName() {
        return this.mDbName;
    }

    public String getDbPath() {
        return this.mSQLiteDatabase.getPath();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    /**
     * 关闭数据库
     */
    public void closeDb() {
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase = null;
        }
    }

}
