package net.branium.view.love;

import static net.branium.view.love.LoveAddActivity.lovePlaylistIds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.LoveAddItemPlaylistLayoutBinding;
import net.branium.model.Playlist;

import java.util.List;

public class LoveAddPlaylistAdapter extends RecyclerView.Adapter<LoveAddPlaylistAdapter.LoveAddPlaylistViewHolder> {
    private List<Playlist> lovePlaylistList;
    private Context context;

    public LoveAddPlaylistAdapter(List<Playlist> lovePlaylistList, Context context) {
        this.lovePlaylistList = lovePlaylistList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoveAddPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoveAddItemPlaylistLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.love_add_item_playlist_layout,
                parent,
                false
        );
        return new LoveAddPlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoveAddPlaylistViewHolder holder, int position) {
        Playlist playlist = lovePlaylistList.get(position);
        holder.binding.setPlaylist(playlist);
    }

    @Override
    public int getItemCount() {
        return lovePlaylistList.size();
    }


    public class LoveAddPlaylistViewHolder extends RecyclerView.ViewHolder {
        private LoveAddItemPlaylistLayoutBinding binding;

        public LoveAddPlaylistViewHolder(LoveAddItemPlaylistLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.checkboxLoveAddItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer currentCheckedPlaylistId = lovePlaylistList.get(getAdapterPosition()).getId();
                    if (isChecked) {
                        lovePlaylistIds.add(currentCheckedPlaylistId);
                    } else {
                        lovePlaylistIds.removeIf(lovePlaylistId -> lovePlaylistId.intValue() == currentCheckedPlaylistId.intValue());
                    }
                }
            });

        }
    }
}
