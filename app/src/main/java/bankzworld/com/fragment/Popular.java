package bankzworld.com.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import bankzworld.com.activity.Favourite;
import bankzworld.com.R;
import bankzworld.com.adapter.MovieAdapter;
import bankzworld.com.model.movie;
import bankzworld.com.model.movieResponse;
import bankzworld.com.utilities.APiService;
import bankzworld.com.utilities.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by King Jaycee on 31/10/2017.
 */

public class Popular extends Fragment {

    private static final String TAG = Popular.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private APiService aPiService;
    private String API_KEY = "21563b5c7baa733f9cd84fc9e3380d81"; // YOUR API_KEY HERE
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private final String KEY_RECYCLER_STATE_LAND = "recycler_state_land";
    private static Parcelable mBundleRecyclerViewState;
    private static Parcelable mBundleRecyclerLan;
    private TextView mErrorMessage;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);

        setHasOptionsMenu(true);

        mErrorMessage = (TextView) view.findViewById(R.id.error_indicator);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        testForNetwork();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_fav) {
            Intent intent = new Intent(getContext(), Favourite.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_refresh) {
            testForNetwork();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBundleRecyclerViewState = layoutManager.onSaveInstanceState();
            outState.putParcelable(KEY_RECYCLER_STATE, mBundleRecyclerViewState);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBundleRecyclerLan = layoutManager.onSaveInstanceState();
            outState.putParcelable(KEY_RECYCLER_STATE_LAND, mBundleRecyclerLan);
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Retrieve list state and list/item positions
        if (savedInstanceState != null) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mBundleRecyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mBundleRecyclerLan = savedInstanceState.getParcelable(KEY_RECYCLER_STATE_LAND);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager.onRestoreInstanceState(mBundleRecyclerViewState);
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager.onRestoreInstanceState(mBundleRecyclerLan);
            }

        }
    }


    private void testForNetwork() {
        if (RetrofitUtil.isConnected(getContext())) {
            if (API_KEY != null) {
                progressBar.setVisibility(View.VISIBLE);
                mErrorMessage.setVisibility(View.GONE);
                getPopularMovies();
            } else {
                progressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mErrorMessage.setText("please obtain your API_KEY");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void getPopularMovies() {
        RetrofitUtil retrofitUtil = new RetrofitUtil(getContext());
        aPiService = retrofitUtil.provideRetrofit().create(APiService.class);
        aPiService.getPopular(API_KEY).enqueue(new Callback<movieResponse>() {
            @Override
            public void onResponse(Call<movieResponse> call, Response<movieResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ArrayList<movie> movieList = response.body().getResults();
                    mRecyclerView.setAdapter(new MovieAdapter(movieList));
                }
            }

            @Override
            public void onFailure(Call<movieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}