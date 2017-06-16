package com.cherry.stunner.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherry.stunner.R;
import com.cherry.stunner.model.domain.Album;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class ImageAlbumsAdapter extends RecyclerView.Adapter<ImageAlbumsAdapter.ImageAlbumViewHolder> {

    private List<Album> mImageAlbums = new ArrayList<>();

    private int mScreenWidth;

    public ImageAlbumsAdapter(List<Album> imageAlbums, int screenWidth) {
        this.mImageAlbums.addAll(imageAlbums);
        this.mScreenWidth = screenWidth;
    }

    public void reset(List<Album> albums) {

        final int originSize = mImageAlbums.size();
        mImageAlbums.clear();
        mImageAlbums.addAll(albums);

        notifyItemRangeRemoved(0, originSize);
        notifyItemRangeInserted(0, albums.size());
    }

    public void append(List<Album> albums){

        int lastIndex = mImageAlbums.size();
        mImageAlbums.addAll(albums);

        notifyItemRangeInserted(lastIndex, albums.size());
    }

    public void relayout(int screenWidth) {
        this.mScreenWidth = screenWidth;

        final int count = mImageAlbums.size();

        notifyItemRangeRemoved(0, count);
        notifyItemRangeInserted(0, count);
    }

    public Album getItem(int position) {
        return mImageAlbums.get(position);
    }

    @Override
    public ImageAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.albums_cell, parent ,false);

        return new ImageAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAlbumViewHolder holder, int position) {

        Album album = mImageAlbums.get(position);
        if (album.getCoverWidth() <= 0) {
            album.setCoverWidth(480);
        }
        if (album.getCoverHeight() <= 0) {
            album.setCoverHeight(640);
        }

        holder.textView.setText(album.getTitle());

        final float width = mScreenWidth / 3.0f;
        holder.itemView.getLayoutParams().height = (int) width * album.getCoverHeight() / album.getCoverWidth();

        Uri uri = Uri.parse(album.getCoverUrl());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .setOldController(holder.imageView.getController())
                .build();
        holder.imageView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return mImageAlbums.size();
    }

    class ImageAlbumViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView imageView;

        private TextView textView;

        ImageAlbumViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.albums_cell_image_view);
            textView = (TextView)itemView.findViewById(R.id.albums_cell_text_view);
        }
    }
}