package net.branium.view.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.PlaylistItemMusicLayoutBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.musicplayer.MusicActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaylistMusicAdapter extends RecyclerView.Adapter<PlaylistMusicAdapter.MusicViewHolder> {
    private Context context;
    private List<Song> songList;


    public PlaylistMusicAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    public void setSongList(List<Song> songList) {
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
        Constants.TEMP_PLAYLIST_SONG_LIST.addAll(Constants.PLAYLIST_SONG_LIST);
        Constants.PLAYLIST_SONG_LIST.clear();
        Constants.PLAYLIST_SONG_LIST.addAll(songList);

//        notifyDataSetChanged();
    }

    public void updateList1() {
        songList = new ArrayList<>();
        songList.addAll(Constants.TEMP_PLAYLIST_SONG_LIST);
        Constants.PLAYLIST_SONG_LIST.clear();
        Constants.PLAYLIST_SONG_LIST.addAll(songList);

//        notifyDataSetChanged();
    }

    public void sortByTitle(ArrayList<Song> songs) {
//        songList = new ArrayList<>();
//        songList.addAll(songs);
//        songList.sort(Comparator.comparing(Song::getTitle));
        Constants.PLAYLIST_SONG_LIST.sort(Comparator.comparing(Song::getTitle));
    }
    public void sortByArtist(ArrayList<Song> songs) {
//        songList = new ArrayList<>();
//        songList.addAll(songs);
//        songList.sort(Comparator.comparing(Song::getArtist));
        Constants.PLAYLIST_SONG_LIST.sort(Comparator.comparing(Song::getArtist));
    }
    public void sortByDuration(ArrayList<Song> songs) {
//        songList = new ArrayList<>();
//        songList.addAll(songs);
//        songList.sort(Comparator.comparing(Song::getDuration));
        Constants.PLAYLIST_SONG_LIST.sort(Comparator.comparing(Song::getDuration));
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
//                    songList.get(getAdapterPosition()).getTitle()
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
