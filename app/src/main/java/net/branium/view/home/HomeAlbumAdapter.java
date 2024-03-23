package net.branium.view.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.HomeItemAlbumLayoutBinding;
import net.branium.model.Album;
import net.branium.view.album.AlbumActivity;

import java.util.List;

public class HomeAlbumAdapter extends RecyclerView.Adapter<HomeAlbumAdapter.HomeAlbumViewHolder> {

    private List<Album> albumList;
    private Context context;

    public HomeAlbumAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeItemAlbumLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.home_item_album_layout,
                parent,
                false
        );

        return new HomeAlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.homeItemAlbumLayoutBinding.setAlbum(album);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class HomeAlbumViewHolder extends RecyclerView.ViewHolder {
        private HomeItemAlbumLayoutBinding homeItemAlbumLayoutBinding;

        public HomeAlbumViewHolder(HomeItemAlbumLayoutBinding homeItemAlbumLayoutBinding) {
            super(homeItemAlbumLayoutBinding.getRoot());
            this.homeItemAlbumLayoutBinding = homeItemAlbumLayoutBinding;

            homeItemAlbumLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AlbumActivity.class);
                    intent.putExtra("album_position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}
