package net.branium.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.ItemLovePlaylistLayoutBinding;
import net.branium.model.Playlist;

import java.util.List;

/**
 * Class này là adapter hiển thị danh sách các love playlist
 */
public class LovePlaylistAdapter extends RecyclerView.Adapter<LovePlaylistAdapter.LovePlaylistViewHolder> {
    private List<Playlist> playlistList;
    private Context context;

    public LovePlaylistAdapter(List<Playlist> playlistList, Context context) {
        this.playlistList = playlistList;
        this.context = context;
    }

    @NonNull
    @Override
    public LovePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLovePlaylistLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_love_playlist_layout,
                parent,
                false
        );
        return new LovePlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LovePlaylistViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        holder.binding.setPlaylist(playlist);
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class LovePlaylistViewHolder extends RecyclerView.ViewHolder {
        private ItemLovePlaylistLayoutBinding binding;

        public LovePlaylistViewHolder(ItemLovePlaylistLayoutBinding binding) {
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
