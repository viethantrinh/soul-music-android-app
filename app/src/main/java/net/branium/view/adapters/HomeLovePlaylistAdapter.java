package net.branium.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.HomeItemLovePlaylistLayoutBinding;
import net.branium.model.Playlist;

import java.util.List;

public class HomeLovePlaylistAdapter extends RecyclerView.Adapter<HomeLovePlaylistAdapter.HomeLovePlaylistViewHolder> {
    private List<Playlist> playlistList;
    private Context context;

    public HomeLovePlaylistAdapter(List<Playlist> playlistList, Context context) {
        this.playlistList = playlistList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeLovePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeItemLovePlaylistLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.home_item_love_playlist_layout,
                parent,
                false
        );
        return new HomeLovePlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeLovePlaylistViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        holder.binding.setPlaylist(playlist);
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class HomeLovePlaylistViewHolder extends RecyclerView.ViewHolder {
        private HomeItemLovePlaylistLayoutBinding binding;

        public HomeLovePlaylistViewHolder(HomeItemLovePlaylistLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
