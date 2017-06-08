package com.cherry.stunner.adapter;

import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.contract.ImagePortalContract;
import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.Tag;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageTagsAdapter extends RecyclerView.Adapter<ImageTagsAdapter.ImageTagViewHolder> {

    private List<Tag> imageTags = new ArrayList<>();

    private int screenWidth;

    public ImageTagsAdapter(List<Tag> imageTags, int screenWidth) {
        this.imageTags.addAll(imageTags);
        this.screenWidth = screenWidth;
    }

    public void reset(List<Tag> tags) {
        imageTags.clear();
        imageTags.addAll(tags);

        notifyDataSetChanged();
    }

    public void relayout(int screenWidth) {
        this.screenWidth = screenWidth;

        notifyDataSetChanged();
    }

    public Tag getItem(int position) {
        return imageTags.get(position);
    }

    @Override
    public ImageTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags_cell, parent, false);

        return new ImageTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageTagViewHolder holder, int position) {

        Tag tag = imageTags.get(position);
        if (tag.getImageWidth() <= 0) {
            tag.setImageWidth(640);
        }
        if (tag.getImageHeight() <= 0) {
            tag.setImageHeight(480);
        }

        holder.textView.setText(tag.getTitle());
        holder.overflowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "haha", Toast.LENGTH_LONG).show();
            }
        });
        holder.resetPreviewAlbums(tag.getAlbums());
    }

    @Override
    public int getItemCount() {
        return imageTags.size();
    }

    class ImageTagViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        private ImageView overflowImageView;

        private RecyclerView previewRecyclerView;

        private PreviewAlbumAdapter previewAlbumAdapter = new PreviewAlbumAdapter();

        ImageTagViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tags_cell_text_view);
            overflowImageView = (ImageView) itemView.findViewById(R.id.tags_cell_text_overlay_image_view);
            previewRecyclerView = (RecyclerView) itemView.findViewById(R.id.tags_cell_preview_recycler_view);
            previewRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            previewRecyclerView.setAdapter(previewAlbumAdapter);
        }

        void resetPreviewAlbums(List<Tag.AlbumBrief> albumBriefs) {
            previewAlbumAdapter.reset(albumBriefs);
        }
    }

    class PreviewAlbumAdapter extends RecyclerView.Adapter<PreviewAlbumAdapter.PreviewAlbumImageViewHolder> {

        private final List<Tag.AlbumBrief> mAlbumBriefs = new ArrayList<>();

        @Override
        public PreviewAlbumImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tags_cell_preview_cell, parent, false);

            return new PreviewAlbumImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PreviewAlbumImageViewHolder holder, int position) {
            Tag.AlbumBrief album = mAlbumBriefs.get(position);
            if (album.getCoverWidth() <= 0) {
                album.setCoverWidth(200);
            }
            if (album.getCoverHeight() <= 0) {
                album.setCoverHeight(300);
            }

            int width = screenWidth / 3;
            holder.itemView.getLayoutParams().height = width;//width * album.getCoverHeight() / album.getCoverWidth();

            Uri uri = Uri.parse(album.getCoverUrl());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setImageRequest(request)
                    .setTapToRetryEnabled(true)
                    .setOldController(holder.mPreviewImageView.getController())
                    .build();
            holder.mPreviewImageView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0.35f));
            holder.mPreviewImageView.setController(controller);



        }

        @Override
        public int getItemCount() {
            return Math.min(mAlbumBriefs.size(), 6);
        }

        void reset(List<Tag.AlbumBrief> albumBriefs) {
            this.mAlbumBriefs.clear();
            this.mAlbumBriefs.addAll(albumBriefs);

            notifyDataSetChanged();
        }

        class PreviewAlbumImageViewHolder extends RecyclerView.ViewHolder {

            private SimpleDraweeView mPreviewImageView;

            PreviewAlbumImageViewHolder(View itemView) {
                super(itemView);

                mPreviewImageView = (SimpleDraweeView) itemView.findViewById(R.id.tags_cell_preview_cell_image_view);
            }
        }
    }
}
