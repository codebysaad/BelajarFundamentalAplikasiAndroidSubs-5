package xyz.webflutter.moviecatalogue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.models.ResultTvShow;

public class AdapterTvShow extends RecyclerView.Adapter<AdapterTvShow.AdapterTvHolder> {
    private final Context context;
    private final List<ResultTvShow> list;

    public AdapterTvShow(Context context, List<ResultTvShow> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterTvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_tv, parent, false);
        return new AdapterTvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTvHolder holder, int position) {

        holder.tvTitle.setText(list.get(position).getName());
        Glide.with(context)
                .load(BuildConfig.IMAGE + list.get(position).getPosterPath())
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdapterTvHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final ImageView thumbnail;

        AdapterTvHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_tv_show);
            thumbnail = itemView.findViewById(R.id.thumbnail_tv_show);
        }
    }
}
