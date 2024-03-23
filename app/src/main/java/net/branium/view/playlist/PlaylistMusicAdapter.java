package net.branium.view.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.PlaylistItemMusicLayoutBinding;
import net.branium.model.Song;
import net.branium.view.musicplayer.MusicActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMusicAdapter extends RecyclerView.Adapter<PlaylistMusicAdapter.MusicViewHolder> {
    private Context context;
    private List<Song> songList;


    public PlaylistMusicAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaylistItemMusicLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.playlist_item_music_layout,
                parent,
                false
        );
        return new MusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song = songList.get(position);
        holder.binding.setSong(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void updateList(ArrayList<Song> songs) {
        songList = new ArrayList<>();
        songList.addAll(songs);
        notifyDataSetChanged();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private PlaylistItemMusicLayoutBinding binding;

        public MusicViewHolder(PlaylistItemMusicLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            handleEventListener();
        }

        private void handleEventListener() {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MusicActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("type", 1);
                    context.startActivity(intent);
                }
            });

            binding.ivMusicOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
