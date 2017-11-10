package bankzworld.com.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import bankzworld.com.R;
import bankzworld.com.adapter.TrailerAdapter;
import bankzworld.com.db.FavDbHelper;
import bankzworld.com.model.Result;
import bankzworld.com.model.TrailerResponse;
import bankzworld.com.model.movie;
import bankzworld.com.utilities.APiService;
import bankzworld.com.utilities.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Response;

import static bankzworld.com.db.FavContract.FavEntry.CONTENT_URI;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_BACKDROP_PATH;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_ID;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_OVERVIEW;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_POSTER_PATH;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_RELEASE_DATE;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_TABLE_NAME;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_TITLE;
import static bankzworld.com.db.FavContract.FavEntry.FAVOURITE_VOTE_AVERAGE;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private ImageView mBackdrop;
    private TextView mOverView, mRating, mDate;
    private CollapsingToolbarLayout ctb;
    private ProgressBar pb;
    private String getId;
    private APiService aPiService;
    private static String API_KEY = "21563b5c7baa733f9cd84fc9e3380d81"; // YOUR API_KEY HERE
    private movie model;
    private FloatingActionButton floatingActionButton;
    private SQLiteDatabase sqLiteDatabase;
    private FavDbHelper favDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getConfig();

        ctb.setTitle(model.getTitle());
        mOverView.setText(model.getOverview());
        mRating.setText(String.valueOf(model.getVote_average()));
        mDate.setText(model.getRelease_date());

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500/" + model.getBackdrop_path())
                .into(mBackdrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mBackdrop.setImageResource(R.drawable.placeholder);
                    }
                });

        isFav(String.valueOf(model.getId()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForFav();
            }
        });

        getTrailerKey(getId, API_KEY);

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

    private void getConfig() {
        model = getIntent().getParcelableExtra("data");

        favDbHelper = new FavDbHelper(this);
        sqLiteDatabase = favDbHelper.getReadableDatabase();

        mRecyclerView = (RecyclerView) findViewById(R.id.trailer_rv);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mBackdrop = (ImageView) findViewById(R.id.image_backdrop);
        mOverView = (TextView) findViewById(R.id.overview);
        ctb = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        pb = (ProgressBar) findViewById(R.id.loading_backdrop);
        mRating = (TextView) findViewById(R.id.rate);
        mDate = (TextView) findViewById(R.id.date);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        getId = String.valueOf(model.getId());
    }

    private void getTrailerKey(String id, String api_key) {
        RetrofitUtil retrofitUtil = new RetrofitUtil(this);
        aPiService = retrofitUtil.provideRetrofit().create(APiService.class);
        aPiService.getTrailerKey(id, api_key).enqueue(new retrofit2.Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.raw());
                    List<Result> resultList = response.body().getResults();
                    mRecyclerView.setAdapter(new TrailerAdapter(resultList));
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie"
                + "\n"
                + model.getTitle()
                + " \n"
                + "coming out on!"
                + " \n"
                + model.getRelease_date()
                + " \n"
                + "This an overview of the movie."
                + model.getOverview());
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
        return shareIntent;
    }

    private void checkForFav() {

        if (isFav(String.valueOf(model.getId()))) {
            // remove it from favourite
            removeFav(String.valueOf(model.getId()));
            floatingActionButton.setImageResource(R.drawable.unfavourite);
        } else {
            // add it to favourite
            addFav(String.valueOf(model.getId()), String.valueOf(model.getTitle()),
                    String.valueOf(model.getPoster_path()),
                    String.valueOf(model.getBackdrop_path()),
                    String.valueOf(model.getOverview()),
                    String.valueOf(model.getRelease_date()),
                    String.valueOf(model.getVote_average()));
            floatingActionButton.setImageResource(R.drawable.favourite);
        }
    }

    private void addFav(String id, String title, String poster_path,
                        String backdrop_path, String overview,
                        String release_date,
                        String vote_average) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVOURITE_ID, id);
        contentValues.put(FAVOURITE_TITLE, title);
        contentValues.put(FAVOURITE_POSTER_PATH, poster_path);
        contentValues.put(FAVOURITE_BACKDROP_PATH, backdrop_path);
        contentValues.put(FAVOURITE_OVERVIEW, overview);
        contentValues.put(FAVOURITE_RELEASE_DATE, release_date);
        contentValues.put(FAVOURITE_VOTE_AVERAGE, vote_average);

        getContentResolver().insert(CONTENT_URI, contentValues);

    }

    private boolean isFav(String id) {
        Cursor cursor = sqLiteDatabase.query(FAVOURITE_TABLE_NAME, new String[]{FAVOURITE_ID},
                FAVOURITE_ID + "=?",
                new String[]{id}, null, null, null, null);

        if (cursor.moveToFirst()) {
            floatingActionButton.setImageResource(R.drawable.favourite);
            return true;
        } else {
            floatingActionButton.setImageResource(R.drawable.unfavourite);
            return false;
        }
    }

    private Integer removeFav(String id) {
        int i = sqLiteDatabase.delete(FAVOURITE_TABLE_NAME, "ID=?", new String[]{id});
        return i;
    }

}
