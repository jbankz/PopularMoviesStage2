package bankzworld.com.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import bankzworld.com.R;
import bankzworld.com.adapter.FavouriteAdapter;
import bankzworld.com.db.FavContract;
import bankzworld.com.db.FavDbHelper;

public class Favourite extends AppCompatActivity implements FavouriteAdapter.clickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private static SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FavDbHelper favDbHelper = new FavDbHelper(this);
        sqLiteDatabase = favDbHelper.getWritableDatabase();

        cursor = getFavMovie();

        mRecyclerView = (RecyclerView) findViewById(R.id.fav_rv);
        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new FavouriteAdapter(cursor, this));

    }

    private Cursor getFavMovie() {
        return sqLiteDatabase.query(
                FavContract.FavEntry.FAVOURITE_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onClickItem(View v, int clickedIndex) {

        if (cursor.moveToPosition(clickedIndex)) {
            String title = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_TITLE));
            String backdrop = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_BACKDROP_PATH));
            String rating = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_VOTE_AVERAGE));
            String date = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_RELEASE_DATE));
            String overview = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_OVERVIEW));
            String id = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_ID));

            Intent intent = new Intent(this, FavouriteDetails.class);
            intent.putExtra("title", title);
            intent.putExtra("backdrop", backdrop);
            intent.putExtra("rating", rating);
            intent.putExtra("date", date);
            intent.putExtra("overview", overview);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}
