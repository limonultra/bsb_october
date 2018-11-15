package com.example.miau.mvp30;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class IdiomaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioma);

        final SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        String idiomaActual = getStringFromIdiom(preferences.getString("idioma", "es_ES"));
        final TextView idiomaSeleccionado = findViewById(R.id.textView5);
        idiomaSeleccionado.setText(idiomaActual);

        final String[] idiomas = getResources().getStringArray(R.array.idiomas);
        String[] formatedIdiomas = new String[idiomas.length];
        for (int i = 0; i < idiomas.length; i++) {
            formatedIdiomas[i] = getStringFromIdiom(idiomas[i]);
        }


        ArrayAdapter<String> idiomasAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, formatedIdiomas);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(idiomasAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = idiomas[position];
                String formatSelected = getStringFromIdiom(idiomas[position]);
                preferences.edit().putString("idioma", selected).apply();
                idiomaSeleccionado.setText(formatSelected);
            }
        });

    }


    private String getStringFromIdiom(String idioma) {
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
