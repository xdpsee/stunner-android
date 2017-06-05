package com.cherry.stunner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherry.stunner.R;
import com.cherry.stunner.model.domain.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAlbumsAdapter extends RecyclerView.Adapter<ImageAlbumsAdapter.ImageAlbumViewHolder> {

    private List<Album> imageAlbums = new ArrayList<>();

    private int screenWidth;

    public ImageAlbumsAdapter(List<Album> imageAlbums, int screenWidth) {
        this.imageAlbums.addAll(imageAlbums);
        this.screenWidth = screenWidth;
    }

    public void reset(List<Album> albums) {
        imageAlbums.clear();
        imageAlbums.addAll(albums);

        notifyDataSetChanged();
    }

    public void relayout(int screenWidth) {
        this.screenWidth = screenWidth;

        notifyDataSetChanged();
    }

    public Album getItem(int position) {
        return imageAlbums.get(position);
    }

    @Override
    public ImageAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.albums_cell, parent ,false);

        return new ImageAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAlbumViewHolder holder, int position) {

        Album album = imageAlbums.get(position);
        if (album.getCoverWidth() <= 0) {
            album.setCoverWidth(480);
        }
        if (album.getCoverHeight() <= 0) {
            album.setCoverHeight(640);
        }

        holder.textView.setText(album.getTitle());

        final float width = screenWidth / 3.0f;
        holder.itemView.getLayoutParams().height = (int) width * album.getCoverHeight() / album.getCoverWidth();

        Picasso.with(holder.itemView.getContext())
                .load(album.getCoverUrl())
                .placeholder(R.drawable.ic_image_loading)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageAlbums.size();
    }

    class ImageAlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private TextView textView;

        ImageAlbumViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.albums_cell_image_view);
            textView = (TextView)itemView.findViewById(R.id.albums_cell_text_view);
        }
    }
}