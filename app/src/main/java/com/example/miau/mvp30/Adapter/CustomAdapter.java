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
            case "af_AF":
                defIdioma = "Afrikaans";
                break;
            case "am_AM":
                defIdioma = "Amharic";
                break;
            case "hy_HY":
                defIdioma = "Armenio";
                break;
            case "az_AZ":
                defIdioma = "Azerbaiano";
                break;
            case "eu_EU":
                defIdioma = "Vasco";
                break;
            case "bn_BN":
                defIdioma = "Bangla";
                break;
            case "bg_BG":
                defIdioma = "Bulgaro";
                break;
            case "ca_CA":
                defIdioma = "Catalan";
                break;
            case "cs_CS":
                defIdioma = "Checo";
                break;
            case "da_DA":
                defIdioma = "Danés";
                break;
            case "nl_NL":
                defIdioma = "Belga";
                break;
            case "en_AU":
                defIdioma = "Inglés (AU)";
                break;
            case "en_CA":
                defIdioma = "Inglés (CA)";
                break;
            case "en_NZ":
                defIdioma = "Inglés (NZ)";
                break;
            case "en_GB":
                defIdioma = "Inglés (GB)";
                break;
            case "fi_FI":
                defIdioma = "Fines";
                break;
            case "gl_GL":
                defIdioma = "Gallego";
                break;
            case "ka_KA":
                defIdioma = "Georgiano";
                break;
            case "el_EL":
                defIdioma = "Griego";
                break;
            case "gu_GU":
                defIdioma = "Gujarati";
                break;
            case "he_HE":
                defIdioma = "Hebreo";
                break;
            case "hu_HU":
                defIdioma = "Hungaro";
                break;
            case "is_IS":
                defIdioma = "Islandés";
                break;
            case "it_IT":
                defIdioma = "Italiano";
                break;
            case "id_ID":
                defIdioma = "Indonesio";
                break;
            case "ja_JA":
                defIdioma = "Japonés";
                break;
            case "jv_JV":
                defIdioma = "Javanés";
                break;
            case "kn_KN":
                defIdioma = "Kannada";
                break;
            case "ko_KO":
                defIdioma = "Coreano";
                break;
            case "km_KM":
                defIdioma = "Khmer";
                break;
            case "lo_LO":
                defIdioma = "Lao";
                break;
            case "la_LA":
                defIdioma = "Latin";
                break;
            case "lv_LV":
                defIdioma = "Latvio";
                break;
            case "lt_LT":
                defIdioma = "Lituano";
                break;
            case "ms_MS":
                defIdioma = "Malayo";
                break;
            case "ml_ML":
                defIdioma = "Malabar";
                break;
            case "mr_MR":
                defIdioma = "Marathi";
                break;
            case "ne_NE":
                defIdioma = "Nepalí";
                break;
            case "no_NO":
                defIdioma = "Noruego";
                break;
            case "fa_FA":
                defIdioma = "Persa";
                break;
            case "pl_PL":
                defIdioma = "Polaco";
                break;
            case "ro_RO":
                defIdioma = "Rumano";
                break;
            case "ru_RU":
                defIdioma = "Ruso";
                break;
            case "sr_SR":
                defIdioma = "Serbio";
                break;
            case "si_SI":
                defIdioma = "Sinhala";
                break;
            case "sk_SK":
                defIdioma = "Esloveno";
                break;
            case "su_SU":
                defIdioma = "Suadanés";
                break;
            case "sw_SW":
                defIdioma = "Swahili";
                break;
            case "sv_SV":
                defIdioma = "Sueco";
                break;
            case "ta_TA":
                defIdioma = "Tamil";
                break;
            case "te_TE":
                defIdioma = "Telugu";
                break;
            case "tr_TR":
                defIdioma = "Turco";
                break;
            case "ur_UR":
                defIdioma = "Urdu";
                break;
            case "zu_ZU":
                defIdioma = "Zulu";
                break;
            case "vi_VI":
                defIdioma = "Vietnamita";
                break;
            default:
                break;
        }
        return defIdioma;
    }
}