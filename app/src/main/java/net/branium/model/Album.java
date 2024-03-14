package net.branium.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.branium.BR;

import java.util.ArrayList;
import java.util.List;

public class Album extends BaseObservable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String image;

    @BindingAdapter({"image"})
    public static void loadImage(ShapeableImageView shapeableImageView, String imageUrl) {
        Glide.with(shapeableImageView.getContext())
                .load(imageUrl)
                .into(shapeableImageView);
    }

    @SerializedName("songs")
    @Expose
    private List<Song> songs = new ArrayList<>();

    public Album() {
    }

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyPropertyChanged(BR.songs);
    }
}
