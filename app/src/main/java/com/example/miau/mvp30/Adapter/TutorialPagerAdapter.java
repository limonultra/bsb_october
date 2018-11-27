package com.example.miau.mvp30.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.miau.mvp30.R;
import com.example.miau.mvp30.TutorialActivity;

public class TutorialPagerAdapter extends PagerAdapter {

    private Context mContext;
    private boolean first;

    public TutorialPagerAdapter(Context context, boolean first) {
        mContext = context;
        this.first = first;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(getLayoutResId(position), collection, false);
        collection.addView(layout);
        final TextView saltar = layout.findViewById(R.id.saltar);
        if (first) {
            saltar.setVisibility(View.INVISIBLE);
        }
        if (getLayoutResId(position) == R.layout.screen_tutorial_3) {
            CheckBox checkBox = layout.findViewById(R.id.checkBox);
            TextView terminos = layout.findViewById(R.id.textView13);
            if (first) {
                terminos.setMovementMethod(LinkMovementMethod.getInstance());
                saltar.setTextColor(Color.parseColor("#4d4d4d"));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked)
                            saltar.setTextColor(Color.parseColor("#4d4d4d"));
                        else
                            saltar.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));
                    }
                });
            } else {

                checkBox.setVisibility(View.INVISIBLE);
                terminos.setVisibility(View.INVISIBLE);
                saltar.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));
            }
            saltar.setVisibility(View.VISIBLE);
            saltar.setText("Continuar");
        } else {
            saltar.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));
        }

        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first) {
                    if (saltar.getCurrentTextColor() == mContext.getResources().getColor(R.color.colorAccent)) {
                        mContext.getSharedPreferences("config", Context.MODE_PRIVATE).edit()
                                .putBoolean("tutorial", true).apply();
                        ((TutorialActivity) mContext).finish();
                    }
                } else {
                    ((TutorialActivity) mContext).finish();
                }
            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 4;
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
                id = R.layout.screen_tutorial_4;
                break;
            case 2:
                id = R.layout.screen_tutorial_2;
                break;
            case 3:
                id = R.layout.screen_tutorial_3;
                break;
            default:
                break;
        }

        return id;
    }

}