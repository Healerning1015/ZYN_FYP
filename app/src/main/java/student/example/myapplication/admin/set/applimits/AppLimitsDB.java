package student.example.myapplication.admin.set.applimits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import student.example.myapplication.admin.set.applimits.AppInfo;
import student.example.myapplication.home.LimitAppsList;
import student.example.myapplication.home.LimitTimeList;

public class AppLimitsDB extends SQLiteOpenHelper {
    private	static final int DATABASE_VERSION =	1;
    private static final String DATABASE_NAME = "limitApp.db"; // db name
    private static final String TABLE_NAME = "limitApp"; // table name
    private static final String[] COLUMNS = {"id", "appName", "pkgName", "logo", "limitHour", "limitMinute", "alwaysAllowed"}; // column names

    public AppLimitsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table creation sql statement
        final String TABLE_CREATION = "create table if not exists " + TABLE_NAME + "( "
                + COLUMNS[0] + " integer primary key autoincrement, "
                + COLUMNS[1] + " text not null, "
                + COLUMNS[2] + " text not null, "
                + COLUMNS[3] + " blob not null, "
                + COLUMNS[4] + " integer not null, "
                + COLUMNS[5] + " integer not null, "
                + COLUMNS[6] + " boolean not null default 1" + ")";
        db.execSQL(TABLE_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Get all jobs
    public LimitAppsList getAllLimitApps(LimitAppsList limitAppsList) {
        limitAppsList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        try{
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String appName = cursor.getString(1);
                String pkgName = cursor.getString(2);
                byte[] logo = cursor.getBlob(3);
                int limitHour = cursor.getInt(4);
                int limitMinute = cursor.getInt(5);
                boolean alwaysAllowed = (cursor.getInt(6) == 1);

                //Drawable logo_drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(logo, 0, logo.length));
                Bitmap bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;
                limitAppsList.addLimit(id, appName, pkgName, drawable, limitHour, limitMinute, alwaysAllowed);
            }
        } finally {
            cursor.close();
        }

        return limitAppsList;
    }

    // Get all time
    public LimitTimeList getAllLimitTime(LimitTimeList limitTimeList) {
        limitTimeList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        try{
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String appName = cursor.getString(1);
                String pkgName = cursor.getString(2);
                byte[] logo = cursor.getBlob(3);
                int limitHour = cursor.getInt(4);
                int limitMinute = cursor.getInt(5);
                boolean alwaysAllowed = (cursor.getInt(6) == 1);

                //Drawable logo_drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(logo, 0, logo.length));
                Bitmap bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;
                limitTimeList.addTime(pkgName, limitHour, limitMinute);
            }
        } finally {
            cursor.close();
        }

        return limitTimeList;
    }

    // Add limit
    public void addLimit(String appName, String pkgName, byte[] logo, int limitHour, int limitMinute, boolean alwaysAllowed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNS[1], appName);
        values.put(COLUMNS[2], pkgName);
        values.put(COLUMNS[3], logo);
        values.put(COLUMNS[4], limitHour);
        values.put(COLUMNS[5], limitMinute);
        values.put(COLUMNS[6], alwaysAllowed);

        long resultedRowID = db.insert(TABLE_NAME, null, values);
    }

    // Update limit details
    public void updateLimit(AppInfo appInfo, int limitHour, int limitMinute, boolean alwaysAllowed){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNS[4], limitHour);
        values.put(COLUMNS[5], limitMinute);
        values.put(COLUMNS[6], alwaysAllowed);

        db.update(TABLE_NAME, values, COLUMNS[2]	+ "	= ?", new String[] { String.valueOf(appInfo.getPackageName())});
    }

    public void deleteLimit(AppInfo appInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMNS[2]	+ "	= ?", new String[] { String.valueOf(appInfo.getPackageName())});
    }

}
