package net.branium.model;

import android.content.Intent;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.branium.BR;

import java.util.Objects;



public class Song extends BaseObservable {
    @SerializedName(value = "id")
    @Expose
    private String id;

    @SerializedName(value = "title")
    @Expose
    private String title;

    @SerializedName(value = "artist")
    @Expose
    private String artist;

    @SerializedName(value = "source")
    @Expose
    private String source;

    @SerializedName(value = "image")
    @Expose
    private String image;

    @BindingAdapter({"image"})
    public static void loadImage(ShapeableImageView shapeableImageView, String imageUrl) {
        Glide.with(shapeableImageView.getContext()).load(imageUrl).into(shapeableImageView);
    }

    @SerializedName(value = "duration")
    @Expose
    private Integer duration;

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        notifyPropertyChanged(BR.artist);

    }

    @Bindable
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        notifyPropertyChanged(BR.source);

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
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", source='" + source + '\'' +
                ", image='" + image + '\'' +
                ", duration=" + duration +
                '}';
    }
}
