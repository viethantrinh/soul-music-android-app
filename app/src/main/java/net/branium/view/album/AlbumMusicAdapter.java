package net.branium.view.album;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.AlbumItemMusicLayoutBinding;
import net.branium.model.Song;
import net.branium.view.musicplayer.MusicActivity;

import java.util.List;

/**
 * Class này để là adapter của list các bài hát trong album (tất nhiên là khi ấn và album rồi xổ ra)
 */
public class AlbumMusicAdapter extends RecyclerView.Adapter<AlbumMusicAdapter.AlbumMusicViewHolder> {
    private List<Song> songList;
    private Context context;

    public AlbumMusicAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlbumItemMusicLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.album_item_music_layout,
                parent,
                false
        );
        return new AlbumMusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumMusicViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.binding.setSong(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class AlbumMusicViewHolder extends RecyclerView.ViewHolder {
        private AlbumItemMusicLayoutBinding binding;

        public AlbumMusicViewHolder(AlbumItemMusicLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            handleEventListener();
        }

        private void handleEventListener() {
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, MusicActivity.class);
                intent.putExtra("position", getAdapterPosition());
                intent.putExtra("type", 2);
                context.startActivity(intent);
            });
            binding.getRoot().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(getAdapterPosition(), v.getId(), 0, "Thêm vào danh sách yêu thích");
                }
            });

        }

    }

}
