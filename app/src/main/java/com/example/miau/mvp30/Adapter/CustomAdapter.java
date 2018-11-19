package com.example.miau.mvp30.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miau.mvp30.R;

public class CustomAdapter extends ArrayAdapter<String> {

    private String[] dataSet;
    private SharedPreferences sharedPreferences;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView info;
    }

    public CustomAdapter(String[] data, Context context) {
        super(context, R.layout.item_idioma, data);
        this.dataSet = data;
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);

    }

    @Nullable
    @Override
    public String getItem(int position) {
        return dataSet[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String string = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_idioma, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.imageView);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(string);

        if (getStringFromIdiom(sharedPreferences.getString("idioma", "es_ES")).equals(string)) {
            viewHolder.info.setImageResource(R.drawable.ic_round_check_circle_24px);
            viewHolder.info.setVisibility(View.VISIBLE);
        } else
            viewHolder.info.setVisibility(View.INVISIBLE);
        // Return the completed view to render on screen
        return result;
    }

    public static String getStringFromIdiom(String idioma) {
        String defIdioma = "";
        switch (idioma) {
            case "es_ES":
                defIdioma = "Español";
                break;
            case "en_US":
                defIdioma = "Inglés";
                break;
            case "de_DE":
                defIdioma = "Alemán";
                break;
            case "fr_FR":
                defIdioma = "Francés";
                break;
            case "pt_PT":
                defIdioma = "Portugués";
                break;
            case "es_MX":
                defIdioma = "Español (MX)";
                break;
            default:
                break;
        }
        return defIdioma;
    }
}