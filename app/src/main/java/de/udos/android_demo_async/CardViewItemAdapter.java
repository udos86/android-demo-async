package de.udos.android_demo_async;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CardViewItemAdapter extends RecyclerView.Adapter<CardViewItemAdapter.ViewHolder>
        implements HttpGetImageTask.OnTaskListener {

    private Context mContext;
    private ArrayList<Item> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItemTitle;
        private final ImageView mItemImage;

        public ViewHolder(View itemView) {

            super(itemView);
            mItemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            mItemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public CardViewItemAdapter(Context context, ArrayList<Item> data) {

        mContext = context;
        mData = data;
    }

    @Override
    public CardViewItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemRootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_item, parent, false);

        return new ViewHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(CardViewItemAdapter.ViewHolder holder, int position) {

        Item item = mData.get(position);
        Drawable itemImageDrawable = DrawableCache.getInstance().getDrawable(item.getImageUrl());

        holder.mItemTitle.setText(item.getName());
        holder.mItemImage.setVisibility(View.INVISIBLE);

        if (itemImageDrawable != null) {

            setItemImageDrawable(itemImageDrawable, holder.mItemImage);

        } else {

            HttpGetImageTask httpGetImageTask = new HttpGetImageTask(this, holder.mItemImage);
            httpGetImageTask.execute(item.getImageUrl());
        }
    }

    private void setItemImageDrawable(Drawable drawable, ImageView imageView) {

        imageView.setImageDrawable(drawable);
        imageView.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    @Override
    public void onImageLoaded(Drawable drawable, ImageView imageView, String url) {

        if (imageView != null) {

            setItemImageDrawable(drawable, imageView);
        }

        if (drawable != null) {

            DrawableCache.getInstance().setDrawable(url, drawable);
        }
    }
}
