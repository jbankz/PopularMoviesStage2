package bankzworld.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import bankzworld.com.R;

import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_ID;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_TABLE_NAME;

/**
 * Created by jaycee on 8/11/2017.
 */

public class FavDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "FavDHelper";

    public static final String DATABASE_NAME = "fav.db";

    public static final int DATABASE_VERSION = 1;

    public FavDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAV_TABLE =

                "CREATE TABLE " + FavContract.FavEntry.FAVOURITE_TABLE_NAME + " (" +
                        FavContract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavContract.FavEntry.FAVOURITE_BACKDROP_PATH + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_OVERVIEW + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_POSTER_PATH + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_RELEASE_DATE + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_TITLE + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                        FavContract.FavEntry.FAVOURITE_ID + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_FAV_TABLE);

        Log.v(TAG, "Database created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + FavContract.FavEntry.FAVOURITE_TABLE_NAME);
        onCreate(db);
    }
}
