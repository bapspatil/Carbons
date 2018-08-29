package com.bapspatil.carbons.adapter;
/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> {

    private ArrayList<PhotoItem> mPhotosArrayList;
    private Context mContext;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(int position, ImageView photoImageView);
    }

    public PhotosRecyclerViewAdapter(Context context, ArrayList<PhotoItem> photosArrayList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mPhotosArrayList = photosArrayList;
        this.mClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoItem thePhoto = mPhotosArrayList.get(position);

        GlideApp.with(mContext)
                .load(thePhoto.getUrlM())
                .error(R.drawable.placeholder_error)
                .fallback(R.drawable.placeholder_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder_loading)
                .centerCrop()
                .into(holder.mPhotoImageView);
    }

    @Override
    public int getItemCount() {
        if (mPhotosArrayList == null) return 0;
        else return mPhotosArrayList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.photoImageView)
        ImageView mPhotoImageView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(getAdapterPosition(), mPhotoImageView);
        }
    }
}