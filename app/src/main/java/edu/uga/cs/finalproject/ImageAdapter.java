package edu.uga.cs.finalproject;

/*
 * This class controls how each image in the gallery list is
 * displayed. RecyclerView reuses views efficiently, and this
 * adapter inflates the "item_image.xml" layout and binds image
 * data to it.
 *
 * Right now it only uses drawable IDs as placeholders.
 *
 * TODO:
 *  - Change List<Integer> to List<Image> or List<Uri> or List<String> (URLs)
 *  - Load images using Glide or Picasso
 *  - Add click events for each image
 *  - Add delete/update functionality
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class aImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    // Temporary list: drawable resource IDs
    // TODO: Replace with real image objects
    private List<Integer> images;

    public ImageAdapter(List<Integer> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate one row of the list
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Replace this with actual bitmap or URL loading
        holder.imageView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    // Holds UI elements for each row
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.itemImageView);
        }
    }
}
