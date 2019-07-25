package com.wolfmobileapps.phototexttranslator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import static com.wolfmobileapps.phototexttranslator.MainActivity.SHAR_PREF_KEY_TEXT_ORIGIN;
import static com.wolfmobileapps.phototexttranslator.MainActivity.SHAR_PREF_KEY_TEXT_TRANLATED;
import static com.wolfmobileapps.phototexttranslator.MainActivity.SHAR_PREF_NAME_TEXT_CONVERTER;

public class ActivitySettings extends AppCompatActivity {

    private static final String TAG = "ActivitySettings";

    //view
    private TextView textViewLanguageOrigin;
    private TextView textViewLanguageTranslated;
    private Spinner spinnerLanguageOrigin;
    private Spinner spinnerLanguageTranslated;

    //do shared pref
    private SharedPreferences shar;
    private SharedPreferences.Editor editor;

    // języki 24 sztuki po angielsku
    public static final String Arabic = "Arabic";
    public static final String Bulgarian = "Bulgarian";
    public static final String ChineseSimplified = "Chinese(Simplified)";
    public static final String ChineseTraditional = "Chinese(Traditional)";
    public static final String Croatian = "Croatian";
    public static final String Czech = "Czech";
    public static final String Danish = "Danish";
    public static final String Dutch = "Dutch";
    public static final String English = "English";
    public static final String Finnish = "Finnish";
    public static final String French = "French";
    public static final String German = "German";
    public static final String Greek = "Greek";
    public static final String Hungarian = "Hungarian";
    public static final String Korean = "Korean";
    public static final String Italian = "Italian";
    public static final String Japanese = "Japanese";
    public static final String Polish = "Polish";
    public static final String Portuguese = "Portuguese";
    public static final String Russian = "Russian";
    public static final String Slovenian = "Slovenian";
    public static final String Spanish = "Spanish";
    public static final String Swedish = "Swedish";
    public static final String Turkish = "Turkish";

    // języki 24 sztuki po polsku
    public static final String Arabski = "Arabski";
    public static final String Bulgarski = "Bułgarski";
    public static final String ChinskiUproszczony = "Chiński(Uproszczony)";
    public static final String ChinskiTradycyjny = "Chiński(Tradycyjny)";
    public static final String Chorwacki = "Chorwacki";
    public static final String Czeski = "Czeski";
    public static final String Dunski = "Duński";
    public static final String Holenderski = "Holenderski";
    public static final String Angielski = "Angielski";
    public static final String Finski = "Fiński";
    public static final String Francuski = "Francuski";
    public static final String Niemiecki = "Niemiecki";
    public static final String Grecki = "Grecki";
    public static final String Wegierski = "Węgierski";
    public static final String Koreanski = "Koreański";
    public static final String Wloski = "Włoski";
    public static final String Japonski = "Japoński";
    public static final String Polski = "Polski";
    public static final String Portugalski = "Portugalski";
    public static final String Rosyjski = "Rosyjski";
    public static final String Slowenski = "Słoweński";
    public static final String Hiszpanski = "Hiszpański";
    public static final String Szwedzki = "Szwedzki";
    public static final String Turecki = "Turecki";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //views
        textViewLanguageOrigin = findViewById(R.id.textViewLanguageOrigin);
        textViewLanguageTranslated = findViewById(R.id.textViewLanguageTranslated);
        spinnerLanguageOrigin = findViewById(R.id.spinnerLanguageOrigin);
        spinnerLanguageTranslated = findViewById(R.id.spinnerLanguageTranslated);

        //ustawienie górnej nazwy i strzałki do powrotu
        getSupportActionBar().setTitle(getResources().getString(R.string.settings)); //ustawia nazwę na górze
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ustawia strzałkę

        // instancja shared pref
        shar = getSharedPreferences(SHAR_PREF_NAME_TEXT_CONVERTER, MODE_PRIVATE);

        // odpalenie spinera z językiem origin
        spinnerOrigin();

        // odpalenie spinnera z jeżykiem tłumaczonym
        spinnerTranslated();
    }

    //po to zeby jak wraca to nie czyścił tego co jest w activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // odpalenie spinnera z jeżykiem tłumaczonym
    private void spinnerOrigin() {
        ArrayAdapter<CharSequence> adapterOrigin = ArrayAdapter.createFromResource(this,
                R.array.language_chose, android.R.layout.simple_list_item_1);
        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguageOrigin.setAdapter(adapterOrigin);
        spinnerLanguageOrigin.setSelection(adapterOrigin.getPosition(shar.getString(SHAR_PREF_KEY_TEXT_ORIGIN, English))); //ustawienie pozycji która wcześniej zostałą wybrana i zapisana w shared pref
        spinnerLanguageOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                switch (text) {         //ifem też można
                    case Arabic:
                        caseSpinnerOrigin(Arabic);
                        break;
                    case Bulgarian:
                        caseSpinnerOrigin(Bulgarian);
                        break;
                    case ChineseSimplified:
                        caseSpinnerOrigin(ChineseSimplified);
                        break;
                    case ChineseTraditional:
                        caseSpinnerOrigin(ChineseTraditional);
                        break;
                    case Croatian:
                        caseSpinnerOrigin(Croatian);
                        break;
                    case Czech:
                        caseSpinnerOrigin(Czech);
                        break;
                    case Danish:
                        caseSpinnerOrigin(Danish);
                        break;
                    case Dutch:
                        caseSpinnerOrigin(Dutch);
                        break;
                    case English:
                        caseSpinnerOrigin(English);
                        break;
                    case Finnish:
                        caseSpinnerOrigin(Finnish);
                        break;
                    case French:
                        caseSpinnerOrigin(French);
                        break;
                    case German:
                        caseSpinnerOrigin(German);
                        break;
                    case Greek:
                        caseSpinnerOrigin(Greek);
                        break;
                    case Hungarian:
                        caseSpinnerOrigin(Hungarian);
                        break;
                    case Korean:
                        caseSpinnerOrigin(Korean);
                        break;
                    case Italian:
                        caseSpinnerOrigin(Italian);
                        break;
                    case Japanese:
                        caseSpinnerOrigin(Japanese);
                        break;
                    case Polish:
                        caseSpinnerOrigin(Polish);
                        break;
                    case Portuguese:
                        caseSpinnerOrigin(Portuguese);
                        break;
                    case Russian:
                        caseSpinnerOrigin(Russian);
                        break;
                    case Slovenian:
                        caseSpinnerOrigin(Slovenian);
                        break;
                    case Spanish:
                        caseSpinnerOrigin(Spanish);
                        break;
                    case Swedish:
                        caseSpinnerOrigin(Swedish);
                        break;
                    case Turkish:
                        caseSpinnerOrigin(Turkish);
                        break;

                        // tłumaczone na polski
                    case Arabski:
                        caseSpinnerOrigin(Arabski);
                        break;
                    case Bulgarski:
                        caseSpinnerOrigin(Bulgarski);
                        break;
                    case ChinskiUproszczony:
                        caseSpinnerOrigin(ChinskiUproszczony);
                        break;
                    case ChinskiTradycyjny:
                        caseSpinnerOrigin(ChinskiTradycyjny);
                        break;
                    case Chorwacki:
                        caseSpinnerOrigin(Chorwacki);
                        break;
                    case Czeski:
                        caseSpinnerOrigin(Czeski);
                        break;
                    case Dunski:
                        caseSpinnerOrigin(Dunski);
                        break;
                    case Holenderski:
                        caseSpinnerOrigin(Holenderski);
                        break;
                    case Angielski:
                        caseSpinnerOrigin(Angielski);
                        break;
                    case Finski:
                        caseSpinnerOrigin(Finski);
                        break;
                    case Francuski:
                        caseSpinnerOrigin(Francuski);
                        break;
                    case Niemiecki:
                        caseSpinnerOrigin(Niemiecki);
                        break;
                    case Grecki:
                        caseSpinnerOrigin(Grecki);
                        break;
                    case Wegierski:
                        caseSpinnerOrigin(Wegierski);
                        break;
                    case Koreanski:
                        caseSpinnerOrigin(Koreanski);
                        break;
                    case Wloski:
                        caseSpinnerOrigin(Wloski);
                        break;
                    case Japonski:
                        caseSpinnerOrigin(Japonski);
                        break;
                    case Polski:
                        caseSpinnerOrigin(Polski);
                        break;
                    case Portugalski:
                        caseSpinnerOrigin(Portugalski);
                        break;
                    case Rosyjski:
                        caseSpinnerOrigin(Rosyjski);
                        break;
                    case Slowenski:
                        caseSpinnerOrigin(Slowenski);
                        break;
                    case Hiszpanski:
                        caseSpinnerOrigin(Hiszpanski);
                        break;
                    case Szwedzki:
                        caseSpinnerOrigin(Szwedzki);
                        break;
                    case Turecki:
                        caseSpinnerOrigin(Turecki);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nic nie musi być
            }
        });
    }

    // odpalenie spinera z językiem origin
    private void spinnerTranslated() {
        ArrayAdapter<CharSequence> adapterTranslated = ArrayAdapter.createFromResource(this,
                R.array.language_chose, android.R.layout.simple_list_item_1);
        adapterTranslated.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguageTranslated.setAdapter(adapterTranslated);
        spinnerLanguageTranslated.setSelection(adapterTranslated.getPosition(shar.getString(SHAR_PREF_KEY_TEXT_TRANLATED, English))); //ustawienie pozycji która wcześniej zostałą wybrana i zapisana w shared pref
        spinnerLanguageTranslated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                switch (text) {         //ifem też można
                    case Arabic:
                        caseSpinnerTranslated(Arabic);
                        break;
                    case Bulgarian:
                        caseSpinnerTranslated(Bulgarian);
                        break;
                    case ChineseSimplified:
                        caseSpinnerTranslated(ChineseSimplified);
                        break;
                    case ChineseTraditional:
                        caseSpinnerTranslated(ChineseTraditional);
                        break;
                    case Croatian:
                        caseSpinnerTranslated(Croatian);
                        break;
                    case Czech:
                        caseSpinnerTranslated(Czech);
                        break;
                    case Danish:
                        caseSpinnerTranslated(Danish);
                        break;
                    case Dutch:
                        caseSpinnerTranslated(Dutch);
                        break;
                    case English:
                        caseSpinnerTranslated(English);
                        break;
                    case Finnish:
                        caseSpinnerTranslated(Finnish);
                        break;
                    case French:
                        caseSpinnerTranslated(French);
                        break;
                    case German:
                        caseSpinnerTranslated(German);
                        break;
                    case Greek:
                        caseSpinnerTranslated(Greek);
                        break;
                    case Hungarian:
                        caseSpinnerTranslated(Hungarian);
                        break;
                    case Korean:
                        caseSpinnerTranslated(Korean);
                        break;
                    case Italian:
                        caseSpinnerTranslated(Italian);
                        break;
                    case Japanese:
                        caseSpinnerTranslated(Japanese);
                        break;
                    case Polish:
                        caseSpinnerTranslated(Polish);
                        break;
                    case Portuguese:
                        caseSpinnerTranslated(Portuguese);
                        break;
                    case Russian:
                        caseSpinnerTranslated(Russian);
                        break;
                    case Slovenian:
                        caseSpinnerTranslated(Slovenian);
                        break;
                    case Spanish:
                        caseSpinnerTranslated(Spanish);
                        break;
                    case Swedish:
                        caseSpinnerTranslated(Swedish);
                        break;
                    case Turkish:
                        caseSpinnerTranslated(Turkish);
                        break;

                    // tłumaczone na polski
                    case Arabski:
                        caseSpinnerTranslated(Arabski);
                        break;
                    case Bulgarski:
                        caseSpinnerTranslated(Bulgarski);
                        break;
                    case ChinskiUproszczony:
                        caseSpinnerTranslated(ChinskiUproszczony);
                        break;
                    case ChinskiTradycyjny:
                        caseSpinnerTranslated(ChinskiTradycyjny);
                        break;
                    case Chorwacki:
                        caseSpinnerTranslated(Chorwacki);
                        break;
                    case Czeski:
                        caseSpinnerTranslated(Czeski);
                        break;
                    case Dunski:
                        caseSpinnerTranslated(Dunski);
                        break;
                    case Holenderski:
                        caseSpinnerTranslated(Holenderski);
                        break;
                    case Angielski:
                        caseSpinnerTranslated(Angielski);
                        break;
                    case Finski:
                        caseSpinnerTranslated(Finski);
                        break;
                    case Francuski:
                        caseSpinnerTranslated(Francuski);
                        break;
                    case Niemiecki:
                        caseSpinnerTranslated(Niemiecki);
                        break;
                    case Grecki:
                        caseSpinnerTranslated(Grecki);
                        break;
                    case Wegierski:
                        caseSpinnerTranslated(Wegierski);
                        break;
                    case Koreanski:
                        caseSpinnerTranslated(Koreanski);
                        break;
                    case Wloski:
                        caseSpinnerTranslated(Wloski);
                        break;
                    case Japonski:
                        caseSpinnerTranslated(Japonski);
                        break;
                    case Polski:
                        caseSpinnerTranslated(Polski);
                        break;
                    case Portugalski:
                        caseSpinnerTranslated(Portugalski);
                        break;
                    case Rosyjski:
                        caseSpinnerTranslated(Rosyjski);
                        break;
                    case Slowenski:
                        caseSpinnerTranslated(Slowenski);
                        break;
                    case Hiszpanski:
                        caseSpinnerTranslated(Hiszpanski);
                        break;
                    case Szwedzki:
                        caseSpinnerTranslated(Szwedzki);
                        break;
                    case Turecki:
                        caseSpinnerTranslated(Turecki);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nic nie musi być
            }
        });
    }


    // metoda wywołana ze spinnera origin w zależności który case zostanie wybrany
    private void caseSpinnerOrigin(String item) {
        //Toast.makeText(ActivitySettings.this, item, Toast.LENGTH_SHORT).show();
        editor = shar.edit();
        editor.putString(SHAR_PREF_KEY_TEXT_ORIGIN, item);
        editor.apply();
    }

    // metoda wywołana ze spinnera origin w zależności który case zostanie wybrany
    private void caseSpinnerTranslated(String item) {
        //Toast.makeText(ActivitySettings.this, item, Toast.LENGTH_SHORT).show();
        editor = shar.edit();
        editor.putString(SHAR_PREF_KEY_TEXT_TRANLATED, item);
        editor.apply();
    }
}

