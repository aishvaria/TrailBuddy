package com.trailbuddy.trailbuddy.photos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;

public class TrailPhotosPagerAdapter extends PagerAdapter {

    private Context context;
    private TrailDetails trailDetails;
    private LayoutInflater layoutInflater;

    public TrailPhotosPagerAdapter(Context context, TrailDetails trailDetails) {
        this.context = context;
        this.trailDetails = trailDetails;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return trailDetails.photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.photo_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
        Glide.with(context).load(trailDetails.photos.get(position)).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
