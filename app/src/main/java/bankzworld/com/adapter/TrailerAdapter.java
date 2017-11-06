package bankzworld.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bankzworld.com.R;
import bankzworld.com.model.Result;
import bankzworld.com.video.YouTubeActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by King Jaycee on 02/11/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    List<Result> results;
    String key;

    public TrailerAdapter(List<Result> results) {
        this.results = results;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Result result = results.get(position);

        holder.mTrailerName.setText(result.getName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mPoster;
        TextView mTrailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.trailer_poster);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, YouTubeActivity.class);
            Result data = results.get(getLayoutPosition());
            intent.putExtra("data", data);
            context.startActivity(intent);
        }
    }
}
