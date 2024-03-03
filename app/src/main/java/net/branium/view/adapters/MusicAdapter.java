package net.branium.view.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
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
import net.branium.model.MusicFiles;

import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private Context context;
    private List<MusicFiles> musicFilesList;


    public MusicAdapter(Context context, List<MusicFiles> musicFilesList) {
        this.context = context;
        this.musicFilesList = musicFilesList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music_layout, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicFiles musicFiles = musicFilesList.get(position);

        holder.tvMusicTitle.setText(musicFiles.getTitle());
        holder.tvMusicArtist.setText(musicFiles.getArtist());
        byte[] image = new byte[0];
        try {
            image = getMusicPhoto(musicFiles.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.ivMusicPhoto);
        } else {
            Glide.with(context).load(R.drawable.default_user_image).into(holder.ivMusicPhoto);
        }

    }


    @Override
    public int getItemCount() {
        return musicFilesList.size();
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
}
