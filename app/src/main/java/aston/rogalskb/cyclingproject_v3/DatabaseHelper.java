package aston.rogalskb.cyclingproject_v3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DB_NAME = "SavedRoutes.DB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "saved_routes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ROUTE = "route_name";
    public static final String COLUMN_ORIGIN_ADDRESS = "origin_address";
    public static final String COLUMN_DESTINATION_ADDRESS = "destination_address";
    public static final String COLUMN_ORIGIN_LAT = "origin_latitude";
    public static final String COLUMN_ORIGIN_LON = "origin_longitude";
    public static final String COLUMN_DESTINATION_LAT = "destination_latitude";
    public static final String COLUMN_DESTINATION_LON = "destination_longitude";



    public DatabaseHelper(@Nullable Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ROUTE + " TEXT, " +
                        COLUMN_ORIGIN_ADDRESS + " TEXT, " +
                        COLUMN_ORIGIN_LAT + " REAL, " +
                        COLUMN_ORIGIN_LON + " REAL, " +
                        COLUMN_DESTINATION_ADDRESS + " TEXT, " +
                        COLUMN_DESTINATION_LAT + " REAL, " +
                        COLUMN_DESTINATION_LON + " REAL);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addRoute(String routeName, String originAddress, Double originLat, Double originLon, String destinationAddress, Double destinationLat,Double destinationLon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ROUTE,routeName);
        contentValues.put(COLUMN_ORIGIN_ADDRESS,originAddress);
        contentValues.put(COLUMN_ORIGIN_LAT,originLat);
        contentValues.put(COLUMN_ORIGIN_LON,originLon);
        contentValues.put(COLUMN_DESTINATION_ADDRESS,destinationAddress);
        contentValues.put(COLUMN_DESTINATION_LAT,destinationLat);
        contentValues.put(COLUMN_DESTINATION_LON,destinationLon);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            //If data wasn't added
            Toast.makeText(context,"Route not saved",Toast.LENGTH_SHORT).show();
        } else {
            //Data was added to the database
            Toast.makeText(context,"Route Saved",Toast.LENGTH_SHORT).show();

        }

    }


    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

    void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context,"Update failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Update successfull",Toast.LENGTH_SHORT).show();
        }

    }


    void updateNameOnly(String row_id,String routeName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ROUTE,routeName);

        Log.i("bartek", "Update name only was triggered with the variable of " + routeName);

        long update = db.update(TABLE_NAME, contentValues, "id=?", new String[]{row_id});
        if(update == -1){
            Toast.makeText(context,"Update failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Update successfull",Toast.LENGTH_SHORT).show();
        }
    }



    void updateOriginData(String row_id,String routeName, String origin, Double oriLat, Double oriLon ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ROUTE,routeName);
        contentValues.put(COLUMN_ORIGIN_ADDRESS,origin);
        contentValues.put(COLUMN_ORIGIN_LAT,oriLat);
        contentValues.put(COLUMN_ORIGIN_LON,oriLon);

        long update = db.update(TABLE_NAME, contentValues, "id=?", new String[]{row_id});
        if(update == -1){
           Toast.makeText(context,"Update failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Update successfull",Toast.LENGTH_SHORT).show();
        }


    }
    void updateDestinationData(String row_id,String routeName, String destination, Double desLat, Double desLon ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ROUTE,routeName);
        contentValues.put(COLUMN_DESTINATION_ADDRESS,destination);
        contentValues.put(COLUMN_DESTINATION_LAT,desLat);
        contentValues.put(COLUMN_DESTINATION_LON,desLon);

        long update = db.update(TABLE_NAME, contentValues, "id=?", new String[]{row_id});
        if(update == -1){
            Toast.makeText(context,"Update failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Update successfull",Toast.LENGTH_SHORT).show();
        }

    }


    void updateBoth(String row_id,String routeName, String origin, Double oriLat, Double oriLon,String destination, Double desLat, Double desLon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ROUTE,routeName);
        contentValues.put(COLUMN_ORIGIN_ADDRESS,origin);
        contentValues.put(COLUMN_ORIGIN_LAT,oriLat);
        contentValues.put(COLUMN_ORIGIN_LON,oriLon);
        contentValues.put(COLUMN_DESTINATION_ADDRESS,destination);
        contentValues.put(COLUMN_DESTINATION_LAT,desLat);
        contentValues.put(COLUMN_DESTINATION_LON,desLon);

        long update = db.update(TABLE_NAME, contentValues, "id=?", new String[]{row_id});
        if(update == -1){
            Toast.makeText(context,"Update failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Update successfull",Toast.LENGTH_SHORT).show();
        }

    }



}
