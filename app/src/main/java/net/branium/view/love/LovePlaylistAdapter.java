package net.branium.view.love;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.LoveItemPlaylistLayoutBinding;
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
        LoveItemPlaylistLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.love_item_playlist_layout,
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
        private LoveItemPlaylistLayoutBinding binding;

        public LovePlaylistViewHolder(LoveItemPlaylistLayoutBinding binding) {
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
