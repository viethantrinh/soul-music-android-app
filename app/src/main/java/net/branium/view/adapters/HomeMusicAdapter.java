package net.branium.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.HomeItemMusicLayoutBinding;
import net.branium.model.Song;

import java.util.List;

public class HomeMusicAdapter extends RecyclerView.Adapter<HomeMusicAdapter.HomeMusicViewHolder> {

    private List<Song> songList;
    private Context context;

    public HomeMusicAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeItemMusicLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.home_item_music_layout,
                parent,
                false
        );

        return new HomeMusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMusicViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.homeItemMusicLayoutBinding.setSong(song);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class HomeMusicViewHolder extends RecyclerView.ViewHolder {
        private HomeItemMusicLayoutBinding homeItemMusicLayoutBinding;

        public HomeMusicViewHolder(HomeItemMusicLayoutBinding homeItemMusicLayoutBinding) {
            super(homeItemMusicLayoutBinding.getRoot());
            this.homeItemMusicLayoutBinding = homeItemMusicLayoutBinding;

            homeItemMusicLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ấn vào xử lý hiện bài hát lên
                }
            });
        }
    }
}
