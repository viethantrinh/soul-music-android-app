package net.branium.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import net.branium.R;
import net.branium.model.Song;
import net.branium.view.activities.MusicPlayerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMusicAdapter extends RecyclerView.Adapter<PlaylistMusicAdapter.MusicViewHolder> {
    private Context context;
    public static List<Song> songList;


    public PlaylistMusicAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item_music_layout, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song = songList.get(position);

        holder.tvMusicTitle.setText(song.getTitle());
        holder.tvMusicArtist.setText(song.getArtist());
        byte[] image = new byte[0];
        try {
            image = getMusicPhoto(song.getSource());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.ivMusicPhoto);
        } else {
            Glide.with(context).load(R.drawable.default_user_image).into(holder.ivMusicPhoto);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivMusicPhoto;
        TextView tvMusicTitle;
        TextView tvMusicArtist;
        ImageView ivMusicOption;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMusicPhoto = itemView.findViewById(R.id.iv_music_photo);
            tvMusicTitle = itemView.findViewById(R.id.tv_music_title);
            tvMusicArtist = itemView.findViewById(R.id.tv_music_artist);
            ivMusicOption = itemView.findViewById(R.id.iv_music_option);

        }
    }

    private byte[] getMusicPhoto(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] photo = retriever.getEmbeddedPicture();
        retriever.release();
        return photo;
    }

    public void updateList(ArrayList<Song> songs) {
        songList = new ArrayList<>();
        songList.addAll(songs);
        notifyDataSetChanged();
    }
}
