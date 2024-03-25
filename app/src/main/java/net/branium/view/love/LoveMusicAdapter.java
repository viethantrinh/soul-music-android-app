package net.branium.view.love;

import static net.branium.view.love.LoveActivity.loveMusicAdapter;
import static net.branium.view.love.LoveActivity.playlist;
import static net.branium.view.love.LoveActivity.viewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.LoveItemMusicLayoutBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.musicplayer.MusicActivity;

import java.util.List;

public class LoveMusicAdapter extends RecyclerView.Adapter<LoveMusicAdapter.LoveViewHolder> {
    private Context context;
    private List<Song> songs;

    public LoveMusicAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public LoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoveItemMusicLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.love_item_music_layout,
                parent,
                false
        );
        return new LoveViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoveViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.binding.setSong(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class LoveViewHolder extends RecyclerView.ViewHolder{
        private LoveItemMusicLayoutBinding binding;

        public LoveViewHolder(LoveItemMusicLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            handleEventListener();
        }

        private void handleEventListener() {
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, MusicActivity.class);
                intent.putExtra("position", getAdapterPosition());
                intent.putExtra("type", 1);
                context.startActivity(intent);
            });

            binding.getRoot().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem deleteMenuItem = menu.add(getAdapterPosition(), v.getId(), 0, "Xóa khỏi danh sách yêu thích");

                    deleteMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            showConfirmationDialog("Xác nhận xóa", "Bạn có chắc chắn muốn xóa?", "Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(v.getContext(), "Xóa thành công: " + item.getGroupId(), Toast.LENGTH_SHORT).show();
                                    int position = item.getGroupId();
                                    if(playlist != null) {
                                        playlist.deleteSong(position);
                                        playlist.setSongNumber(playlist.getSongs().size());
                                        Constants.PLAYLIST_SONG_LIST.remove(position);
                                        viewModel.updatePlaylist(playlist);
                                        loveMusicAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            return true;
                        }
                    });
                }
            });
        }
    }

    private void showConfirmationDialog(String title, String message, String button, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        // Thiết lập nút xác nhận và màu sắc cho nó
        builder.setPositiveButton(button, positiveClickListener);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Hủy bỏ thao tác xóa
                dialog.dismiss();
            }
        });
        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
