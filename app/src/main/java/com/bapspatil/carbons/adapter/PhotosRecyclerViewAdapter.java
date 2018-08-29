package com.bapspatil.carbons.adapter;
/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bapspatil.carbons.R;
import com.bapspatil.carbons.model.PhotoItem;
import com.bapspatil.carbons.util.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// RecyclerView Adapter for the photo items
public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> {

    private ArrayList<PhotoItem> mPhotosArrayList;
    private Context mContext;
    private ItemClickListener mClickListener;

    // Interface to handle clicks on the photos
    public interface ItemClickListener {
        void onItemClick(int position, ImageView photoImageView);
    }

    // Constructor for the PhotosRecyclerViewAdapter
    public PhotosRecyclerViewAdapter(Context context, ArrayList<PhotoItem> photosArrayList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mPhotosArrayList = photosArrayList;
        this.mClickListener = itemClickListener;
    }

    // Creating the PhotoViewHolder item
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    // Binding data to the PhotoViewHolder item
    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        // Get the photo from the position of the ViewHolder and mPhotosArrayList
        PhotoItem thePhoto = mPhotosArrayList.get(position);

        // Load photos with Glide into the mPhotoImageView
        GlideApp.with(mContext)
                .load(thePhoto.getUrlM())
                .error(R.drawable.placeholder_error)
                .fallback(R.drawable.placeholder_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder_loading)
                .centerCrop()
                .into(holder.mPhotoImageView);
    }

    // Getting the number of items in the adapter
    @Override
    public int getItemCount() {
        // If list of items is empty, return 0
        if (mPhotosArrayList == null) return 0;
        else return mPhotosArrayList.size(); // Else, return number of items in mPhotosArrayList
    }

    // ViewHolder class for the photo items
    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.photoImageView)
        ImageView mPhotoImageView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this); // Setting click listener to the ViewHolder
        }

        @Override
        public void onClick(View v) {
            // Check if the listener has been created or not
            if (mClickListener != null)
                // If listener is not null, pass the clicked positino (getAdapterPosition()) and mPhotoImageView to the onItemClick method
                mClickListener.onItemClick(getAdapterPosition(), mPhotoImageView);
        }
    }

    // Helper methods for this adapter

    public void add(PhotoItem photoItem) {
        mPhotosArrayList.add(photoItem);
        notifyItemInserted(mPhotosArrayList.size() - 1);
    }

    public void addAll(ArrayList<PhotoItem> photoItems) {
        mPhotosArrayList.addAll(photoItems);
        notifyItemRangeInserted(mPhotosArrayList.size() - 1, photoItems.size());
    }

    public void remove(PhotoItem photoItem) {
        int position = mPhotosArrayList.indexOf(photoItem);
        if(position > -1) {
            mPhotosArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while(getItemCount() > 0) {
            remove(getPhotoItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public PhotoItem getPhotoItem(int position) {
        return mPhotosArrayList.get(position);
    }
}