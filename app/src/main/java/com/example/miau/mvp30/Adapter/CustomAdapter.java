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
            case "es_CO":
                defIdioma = "Español (CO)";
                break;
            case "es_AR":
                defIdioma = "Español (AR)";
                break;
            case "af-AF":
                defIdioma = "Afrikaans";
                break;
            case "am-AM":
                defIdioma = "Amharic";
                break;
            case "hy-HY":
                defIdioma = "Armenio";
                break;
            case "az-AZ":
                defIdioma = "Azerbaiano";
                break;
            case "eu-EU":
                defIdioma = "Vasco";
                break;
            case "bn-BN":
                defIdioma = "Bangla";
                break;
            case "bg-BG":
                defIdioma = "Bulgaro";
                break;
            case "ca-CA":
                defIdioma = "Catalan";
                break;
            case "cs-CS":
                defIdioma = "Checo";
                break;
            case "da-DA":
                defIdioma = "Danés";
                break;
            case "nl-NL":
                defIdioma = "Belga";
                break;
            case "en-AU":
                defIdioma = "Inglés (AU)";
                break;
            case "en-CA":
                defIdioma = "Inglés (CA)";
                break;
            case "en-NZ":
                defIdioma = "Inglés (NZ)";
                break;
            case "en-GB":
                defIdioma = "Inglés (GB)";
                break;
            case "fi-FI":
                defIdioma = "Fines";
                break;
            case "gl-GL":
                defIdioma = "Gallego";
                break;
            case "ka-KA":
                defIdioma = "Georgiano";
                break;
            case "el-EL":
                defIdioma = "Griego";
                break;
            case "gu-GU":
                defIdioma = "Gujarati";
                break;
            case "he-HE":
                defIdioma = "Hebreo";
                break;
            case "hu-HU":
                defIdioma = "Hungaro";
                break;
            case "is-IS":
                defIdioma = "Islandés";
                break;
            case "it-IT":
                defIdioma = "Italiano";
                break;
            case "id-ID":
                defIdioma = "Indonesio";
                break;
            case "ja-JA":
                defIdioma = "Japonés";
                break;
            case "jv-JV":
                defIdioma = "Javanés";
                break;
            case "kn-KN":
                defIdioma = "Kannada";
                break;
            case "ko-KO":
                defIdioma = "Coreano";
                break;
            case "km-KM":
                defIdioma = "Khmer";
                break;
            case "lo-LO":
                defIdioma = "Lao";
                break;
            case "la-LA":
                defIdioma = "Latin";
                break;
            case "lv-LV":
                defIdioma = "Latvio";
                break;
            case "lt-LT":
                defIdioma = "Lituano";
                break;
            case "ms-MS":
                defIdioma = "Malayo";
                break;
            case "ml-ML":
                defIdioma = "Malabar";
                break;
            case "mr-MR":
                defIdioma = "Marathi";
                break;
            case "ne-NE":
                defIdioma = "Nepalí";
                break;
            case "no-NO":
                defIdioma = "Noruego";
                break;
            case "fa-FA":
                defIdioma = "Persa";
                break;
            case "pl-PL":
                defIdioma = "Polaco";
                break;
            case "ro-RO":
                defIdioma = "Rumano";
                break;
            case "ru-RU":
                defIdioma = "Ruso";
                break;
            case "sr-SR":
                defIdioma = "Serbio";
                break;
            case "si-SI":
                defIdioma = "Sinhala";
                break;
            case "sk-SK":
                defIdioma = "Esloveno";
                break;
            case "su-SU":
                defIdioma = "Suadanés";
                break;
            case "sw-SW":
                defIdioma = "Swahili";
                break;
            case "sv-SV":
                defIdioma = "Sueco";
                break;
            case "ta-TA":
                defIdioma = "Tamil";
                break;
            case "te-TE":
                defIdioma = "Telugu";
                break;
            case "tr-TR":
                defIdioma = "Turco";
                break;
            case "ur-UR":
                defIdioma = "Urdu";
                break;
            case "zu-ZU":
                defIdioma = "Zulu";
                break;
            case "vi-VI":
                defIdioma = "Vietnamita";
                break;
            default:
                break;
        }
        return defIdioma;
    }
}