package com.cherry.stunner.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.stunner.R;
import com.cherry.stunner.model.domain.Image;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>{

    private List<Image> mImages = new ArrayList<>();

    private int mScreenWidth;

    public ImageDetailAdapter(List<Image> images, int screenWidth) {
        this.mImages.addAll(images);
        this.mScreenWidth = screenWidth;
    }

    public void reset(List<Image> images) {

        final int originSize = mImages.size();
        mImages.clear();
        mImages.addAll(images);

        notifyItemRangeRemoved(0, originSize);
        notifyItemRangeInserted(0, images.size());
    }

    public void relayout(int screenWidth) {
        this.mScreenWidth = screenWidth;

        final int count = mImages.size();

        notifyItemRangeRemoved(0, count);
        notifyItemRangeInserted(0, count);
    }

    @Override
    public ImageDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_detail_cell, parent ,false);

        return new ImageDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageDetailViewHolder holder, int position) {

        Image image = mImages.get(position);

        if (0 == image.getImageWidth()) {
            image.setImageWidth(480);
        }

        if (0 == image.getImageHeight()) {
            image.setImageHeight(640);
        }

        final float width = mScreenWidth;
        holder.itemView.getLayoutParams().height = (int) width * image.getImageHeight() / image.getImageWidth();

        Uri uri = Uri.parse(image.getImageUrl());
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
        return mImages.size();
    }

    class ImageDetailViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView imageView;

        ImageDetailViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.image_detail_cell_image_view);
        }
    }
}


