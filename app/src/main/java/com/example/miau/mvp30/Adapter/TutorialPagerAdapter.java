package com.example.miau.mvp30.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miau.mvp30.R;

public class TutorialPagerAdapter extends PagerAdapter {

    private Context mContext;

    public TutorialPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(getLayoutResId(position), collection, false);
        collection.addView(layout);
        if (getLayoutResId(position) == R.layout.screen_tutorial_3) {
            TextView saltar = layout.findViewById(R.id.saltar);
            saltar.setText("Continuar");
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    private int getLayoutResId(int position) {
        int id = 0;
        switch (position) {
            case 0:
                id = R.layout.screen_tutorial_1;
                break;
            case 1:
                id = R.layout.screen_tutorial_2;
                break;
            case 2:
                id = R.layout.screen_tutorial_3;
                break;
            default:
                break;
        }

        return id;
    }

}