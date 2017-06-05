package com.cherry.stunner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherry.stunner.R;
import com.cherry.stunner.model.domain.Tag;
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
                .inflate(R.layout.tags_cell, parent ,false);

        return new ImageTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageTagViewHolder holder, int position) {

        Tag tag = imageTags.get(position);
        if (tag.getImageWidth() <= 0) {
            tag.setImageWidth(640);
        }
        if (tag.getImageHeight() <= 0) {
            tag.setImageHeight(480);
        }

        holder.textView.setText(tag.getTitle());

        final float width = screenWidth / 2.0f;
        holder.itemView.getLayoutParams().height = (int) width * tag.getImageHeight() / tag.getImageWidth();

        Picasso.with(holder.itemView.getContext())
                .load("http://test.png.png")
                .placeholder(R.drawable.ic_image_loading)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageTags.size();
    }

    class ImageTagViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private TextView textView;

        ImageTagViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tags_cell_image_view);
            textView = (TextView)itemView.findViewById(R.id.tags_cell_text_view);
        }
    }
}
