package com.example.roomie_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String[] imgUrl;

    public ViewPagerAdapter(Context context, List<Upload> uList) {
        imgUrl=new String[uList.size()];
        for (int i = 0; i < uList.size(); i++) {
            imgUrl[i]=uList.get(i).getImageUrl();
        }
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        //ModelObject modelObject = ModelObject.values()[position];
        //LayoutInflater inflater = LayoutInflater.from(mContext);
        //ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        //collection.addView(layout);
        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext).load(imgUrl[position]).into(imageView);
        collection.addView(imageView);
        //return layout;
        return imageView;
    }



    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imgUrl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            ModelObject customPagerEnum = ModelObject.values()[position];
//            return mContext.getString(customPagerEnum.getTitleResId());
//        }

}