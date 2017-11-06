package bankzworld.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bankzworld.com.R;
import bankzworld.com.activity.DetailsActivity;
import bankzworld.com.model.movie;

/**
 * Created by King Jaycee on 26/10/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<movie> modelArrayList;
    private String imageUrl;

    public MovieAdapter(ArrayList<movie> pojoArrayList) {
        this.modelArrayList = pojoArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        movie pojo = modelArrayList.get(position);

        Context context = holder.mPoster.getContext();

        imageUrl = pojo.getPoster_path();

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w500/" + imageUrl)
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

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar progressBar;
        ImageView mPoster;

        public ViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_poster);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, DetailsActivity.class);
            movie data = modelArrayList.get(getLayoutPosition());
            intent.putExtra("data", data);
            context.startActivity(intent);
        }
    }
}
