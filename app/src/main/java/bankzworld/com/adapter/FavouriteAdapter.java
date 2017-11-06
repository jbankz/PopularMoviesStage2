package bankzworld.com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bankzworld.com.R;
import bankzworld.com.db.FavContract;

/**
 * Created by King Jaycee on 03/11/2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavViewHolder> {

    private Cursor cursor;
    private String title, backdrop, rating, date, overview;
    final private clickListener mOnclickedListener;


    public FavouriteAdapter(Cursor cursor, clickListener listener) {
        this.cursor = cursor;
        this.mOnclickedListener = listener;
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new FavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Context context = holder.mPoster.getContext();
            String poster = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_POSTER_PATH));
            title = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_TITLE));
            backdrop = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_BACKDROP_PATH));
            rating = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_VOTE_AVERAGE));
            date = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_RELEASE_DATE));
            overview = cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.FAVOURITE_OVERVIEW));

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w500/" + poster)
                    .into(holder.mPoster, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.mPoster.setImageResource(R.drawable.placeholder);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface clickListener {
        void onClickItem(View v, int clickedIndex);
    }

    class FavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar progressBar;
        ImageView mPoster;

        public FavViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_poster);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnclickedListener.onClickItem(view, this.getAdapterPosition());
        }
    }
}
