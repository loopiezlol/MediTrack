package ro.laflamme.meditrack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "meditrack.db";

    private RuntimeExceptionDao<Pharm,Integer> pharmRuntimeDao =null;

    public DatabaseHelper(Context context ){
        super(context, DATABASE_NAME,null, DatabaseConfigUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Pharm.class);
        } catch (Exception e){
            Log.e(LOG_TAG,"Can't create database",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource,Pharm.class,true);
            onCreate(database,connectionSource);
        } catch (Exception e){
            Log.e(LOG_TAG,"Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<Pharm, Integer> getPharmDao () {
        if(pharmRuntimeDao == null){
            pharmRuntimeDao = getRuntimeExceptionDao(Pharm.class);
        }
        return pharmRuntimeDao;
    }

    @Override
    public void close(){
        super.close();
        pharmRuntimeDao =null;
    }
}
