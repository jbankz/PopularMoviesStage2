package bankzworld.com.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bankzworld.com.R;
import bankzworld.com.db.FavContract;
import bankzworld.com.db.FavDbHelper;

import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_ID;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_TABLE_NAME;

public class FavouriteDetails extends AppCompatActivity {

    private TextView mRate, mDate, mOverview;
    private ImageView mBackdrop;
    private CollapsingToolbarLayout toolbarLayout;
    private ProgressBar mPb;
    private String title, date, overview;
    private SQLiteDatabase sqLiteDatabase;
    private FavDbHelper favDbHelper;
    private Cursor cursor;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getConfig();

        cursor = getFavMovie();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFav(id);
                fab.setImageResource(R.drawable.unfavourite);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDataAndSetThem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            createShareMovieIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private Integer removeFav(String id) {
        int i = sqLiteDatabase.delete(FAVOURITE_TABLE_NAME, "ID=?", new String[]{id});
        return i;
    }

    private void getConfig() {
        mRate = (TextView) findViewById(R.id.rate);
        mDate = (TextView) findViewById(R.id.date);
        mOverview = (TextView) findViewById(R.id.overview);
        mBackdrop = (ImageView) findViewById(R.id.image_backdrop);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mPb = (ProgressBar) findViewById(R.id.loading_backdrop);

        favDbHelper = new FavDbHelper(this);
        sqLiteDatabase = favDbHelper.getReadableDatabase();
    }

    private void getDataAndSetThem() {
        Intent i = getIntent();
        Context context = mBackdrop.getContext();

        if (i.hasExtra("title") || i.hasExtra("backdrop") ||
                i.hasExtra("rating") || i.hasExtra("date") || i.hasExtra("overview") || i.hasExtra("id")) {

            title = i.getStringExtra("title");
            date = i.getStringExtra("date");
            overview = i.getStringExtra("overview");

            toolbarLayout.setTitle(title);
            mRate.setText(i.getStringExtra("rating"));
            mDate.setText(date);
            mOverview.setText(overview);
            String backdrop = i.getStringExtra("backdrop");
            id = i.getStringExtra("id");

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w500/" + backdrop)
                    .into(mBackdrop, new Callback() {
                        @Override
                        public void onSuccess() {
                            mPb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mPb.setVisibility(View.GONE);
                            mBackdrop.setImageResource(R.drawable.placeholder);
                        }
                    });
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie"
                + "\n"
                + title
                + " \n"
                + "coming out on!"
                + " \n"
                + date
                + " \n"
                + "This an overview of the movie."
                + overview);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
        return shareIntent;
    }
}
