package com.wolfmobileapps.phototexttranslator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Angielski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Arabic;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Arabski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Bulgarian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Bulgarski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.ChineseSimplified;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.ChineseTraditional;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.ChinskiTradycyjny;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.ChinskiUproszczony;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Chorwacki;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Croatian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Czech;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Czeski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Danish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Dunski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Dutch;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.English;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Finnish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Finski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Francuski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.French;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.German;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Grecki;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Greek;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Hiszpanski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Holenderski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Hungarian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Italian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Japanese;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Japonski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Korean;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Koreanski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Niemiecki;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Polish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Polski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Portugalski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Portuguese;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Rosyjski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Russian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Slovenian;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Slowenski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Spanish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Swedish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Szwedzki;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Turecki;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Turkish;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Wegierski;
import static com.wolfmobileapps.phototexttranslator.ActivitySettings.Wloski;

public class MainActivity extends AppCompatActivity implements InterfaceToShowSerwersResponses {

    private static final String TAG = "MainActivity";

    //views
    private TextView textViewResult;
    private TextView textViewResultTranslated;
    private TextView textViewResultLanguage;
    private TextView textViewResultTranslatedLanguage;
    private ImageView imageViewPhoto;
    private ImageView imageViewCopyAndSaveOrigin;
    private LinearLayout linearLayoutImageAndPoweredYandex;
    private ProgressDialog mProgressDialog;
    private TextView textViewLanguageOnPhotoTakeFromShar;
    private TextView textViewLanguageToTranslateTakeFromShar;

    // do permissions
    private String[] permisionas;
    private static final int PERMISSION_ALL = 101; //stała do permission
    private static final int RC_PHOTO_PICKER = 102; //stała do permission
    private static final int REQUEST_IMAGE_CAPTURE = 103; //stała do permission

    // do pobierania zdjęc z kamery i z dysku
    private String mCameraFileName = null; // ścieżka zdjęcia zapisanego z kamery jeszcze przed kompresją - potem będzie zapisana jeszcze raz jako docelowa
    private Uri imageUri = null;  //zwraca zdjęcie jako ścieszkę uri ( z aparatu lub z dysku) - przed kompresją i finalnym zapisaniem
    private Uri imageUriFinal = null; // ścieżka Uri zdjęcia po skompresowaniu, zapisaniu nowego zdjęcia na dysku i przygotowaniu do wysłąnia do firebase
    private String fileName = null;
    private Bitmap imageBitmap = null; //zdjęcie zapisane tu te pobrane z aparatu lub dysku i skompresowane jeśli trzeba
    private int imageCompressQuality = 100;

    //do firebase
    private StorageReference mStorageReference;

    //do shared pref
    private SharedPreferences shar;
    private SharedPreferences.Editor editor;
    public static final String SHAR_PREF_NAME_TEXT_CONVERTER = "shar pref name text converter";
    public static final String SHAR_PREF_KEY_PERSONAL_ID = "shar pref personal ID";
    public static final String SHAR_PREF_KEY_TEXT_ORIGIN = "shar pref text origin";
    public static final String SHAR_PREF_KEY_TEXT_TRANLATED = "shar pref text translated";

    // stałe do onSaveInstanceState gdyby był obrócony telefon to zachowa dane - NIE do shar pref
    public static final String KEY_M_CAMERA_FILE_NAME = "key_mCameraFileName";
    public static final String KEY_IMAGE_URI = "key_imageUri";
    public static final String KEY_IMAGE_URI_FINAL = "key_imageUriFinal";
    public static final String KEY_FILE_NAME = "key_FileName";
    public static final String KEY_TEXT_VIEW_RESULT = "key_textViewResult";
    public static final String KEY_TEXT_VIEW_RESULT_LANGUAGE = "key_textViewResultLanguage";
    public static final String KEY_TEXT_VIEW_RESULT_TRANSLATED = "key_textViewResultTranslated";
    public static final String KEY_TEXT_VIEW_RESULT_TRANSLATED_LANGUAGE = "key_textViewResultTranslatedLanguage";

    // stałe do tłumaczenia textu
    private String languagePhoto; //jeżyk do tłumaczenia z obrazka na text
    private String languageTranslationFrom; //jeżyk z którego bedzie tłumaczenie czyli ten sam co languagePhoto ale inaczej oznaczony bo inne API
    private String languageTranslationTo; // jezyk na który będzie tłumaczony z languageTranslationFrom

    // do reklam
    public static final String APP_ID = "ca-app-pub-1490567689734833~8586941039"; //nie zmieniać
    public static final String REKLAMA_PELNOEKRANOWA_ID = "ca-app-pub-1490567689734833/5577634311"; // mój  ca-app-pub-1490567689734833/5577634311 (przykładowy do testów ca-app-pub-3940256099942544/1033173712)
    public static final String BANNER_ID = "ca-app-pub-1490567689734833/5193016154"; // przed wysłaniem do Google Play zmienić w XML w portrait i land na to ( testowy to ca-app-pub-3940256099942544/6300978111)
    private InterstitialAd mInterstitialAd;
    private boolean shouldLoadAds; // żeby reklamy nie pokazywały się po wyłaczeniu aplikacji - tylko do intestitialAds - NIE URZYTE JEST JESZCZE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        ImageView buttonTakePicture = findViewById(R.id.imageViewTakePicture);
        ImageView buttonOpenCamera = findViewById(R.id.imageViewOpenCamera);
        ImageView button_call_api = findViewById(R.id.image_view_call_api);
        ImageView imageViewRotate = findViewById(R.id.imageViewRotate);
        imageViewCopyAndSaveOrigin = findViewById(R.id.imageViewCopyAndSaveOrigin);
        ImageView imageViewCopyAndSaveTranslated = findViewById(R.id.imageViewCopyAndSaveTranslated);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        textViewResult = findViewById(R.id.textViewResult);
        textViewResultLanguage = findViewById(R.id.textViewResultLanguage);
        textViewResultTranslated = findViewById(R.id.textViewResultTranslated);
        textViewResultTranslatedLanguage = findViewById(R.id.textViewResultTranslatedLanguage);
        LinearLayout lineraLayoutLangugageInfo = findViewById(R.id.lineraLayoutLangugageInfo);
        linearLayoutImageAndPoweredYandex = findViewById(R.id.linearLayoutImageAndPoweredYandex);
        textViewLanguageOnPhotoTakeFromShar = findViewById(R.id.textViewLanguageOnPhotoTakeFromShar);
        textViewLanguageToTranslateTakeFromShar = findViewById(R.id.textViewLanguageToTranslateTakeFromShar);
        TextView poweredYandex = findViewById(R.id.textViewPoweredYandex);

        // do persmissions
        permisionas = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(this, permisionas)) {
            ActivityCompat.requestPermissions(this, permisionas, PERMISSION_ALL);
        }

        // do onSaveInstanceState gdyby był obrócony telefon to wczyta dane
        if (savedInstanceState != null) {
            mCameraFileName = savedInstanceState.getString(KEY_M_CAMERA_FILE_NAME);
            if (savedInstanceState.getString(KEY_IMAGE_URI) != null) {
                imageUri = Uri.parse(savedInstanceState.getString(KEY_IMAGE_URI));
            }
            if (savedInstanceState.getString(KEY_IMAGE_URI_FINAL) != null) {
                imageUriFinal = Uri.parse(savedInstanceState.getString(KEY_IMAGE_URI_FINAL));
            }
            fileName = savedInstanceState.getString(KEY_FILE_NAME);
            textViewResult.setText(savedInstanceState.getString(KEY_TEXT_VIEW_RESULT));
            textViewResultLanguage.setText(savedInstanceState.getString(KEY_TEXT_VIEW_RESULT_LANGUAGE));
            textViewResultTranslated.setText(savedInstanceState.getString(KEY_TEXT_VIEW_RESULT_TRANSLATED));
            textViewResultTranslatedLanguage.setText(savedInstanceState.getString(KEY_TEXT_VIEW_RESULT_TRANSLATED_LANGUAGE));
            if (imageUriFinal != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUriFinal);
                    //pokazanie zapisanego zdjęcia w imageViev
                    imageViewPhoto.setImageBitmap(imageBitmap);
                    //ustawienie widoczności ikonek do kopiowania
                    imageViewCopyAndSaveOrigin.setVisibility(View.VISIBLE);
                    linearLayoutImageAndPoweredYandex.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // instancja shared pref
        shar = getSharedPreferences(SHAR_PREF_NAME_TEXT_CONVERTER, MODE_PRIVATE);

        // zapisanie do shar pref unikalnego id zeby potem móc tworzyć w firebase nazwę obrazka zawsze taką samą
        if (shar.getString(SHAR_PREF_KEY_PERSONAL_ID, "example").equals("example")) {
            String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID); //zwraca id urządzenia danego - dla każdego inne
            if (android_id == null || android_id.equals("")) { // jeśli coś pojdzie nie tak to weżmie czas jako nazwe aktualną
                android_id = "" + System.currentTimeMillis();
            }
            editor = shar.edit();
            editor.putString(SHAR_PREF_KEY_PERSONAL_ID, android_id);
            editor.apply();
        }
        Log.d(TAG, "onCreate: android_id: " + shar.getString(SHAR_PREF_KEY_PERSONAL_ID, "not created!"));

        // jeśli jest pierwsze uruchomienie to zapisze jezyk domyślny obrazka jako angoelski
        if (shar.getString(SHAR_PREF_KEY_TEXT_ORIGIN, "firstOpen").equals("firstOpen")) {
            editor = shar.edit();
            editor.putString(SHAR_PREF_KEY_TEXT_ORIGIN, English);
            editor.apply();
        }
        // jeśli jest pierwsze uruchomienie to zapisze jezyk domyślny do tłumaczenia jako angoelski
        if (shar.getString(SHAR_PREF_KEY_TEXT_TRANLATED, "firstOpen").equals("firstOpen")) {
            editor = shar.edit();
            editor.putString(SHAR_PREF_KEY_TEXT_TRANLATED, English);
            editor.apply();
        }

        //do firebase
        mStorageReference = FirebaseStorage.getInstance().getReference("myPhotos");

        //do reklamy pełnoekranowej
        MobileAds.initialize(this); //inicjalizacja reklam potrzebna tylko raz na całą aplikację
        mInterstitialAd = new InterstitialAd(this); // instancja danej reklamy
        mInterstitialAd.setAdUnitId(REKLAMA_PELNOEKRANOWA_ID); //wpisać ID danej reklamy czyli identyfikator jednostki reklamowej wzięty z AdMOB (do testów korzystać tylko z przykładowego czyli: ca-app-pub-3940256099942544/1033173712)
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // ładuje reklamę to chwile potrwa więc od razu może nie pokazać bo nie będzie załadowana dlatego trzeba dodać listenera jak niżej

        //do reklamy banner
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // ustawienie textu z językami jakko klikalnego żeby otworzyć setings
        lineraLayoutLangugageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivitySettings.class));
            }
        });

        //pobiera zdjęcie z dysku
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sprawdzenie permissions
                if (!hasPermissions(MainActivity.this, permisionas)) {
                    ActivityCompat.requestPermissions(MainActivity.this, permisionas, PERMISSION_ALL);
                    return;
                }

                //tworzy folder na dysku telefonu - żeby utworzył to muszą być dane permissions
                Boolean checkDirectory = makeDirectoryOnHardDrive();
                if (!checkDirectory) {
                    return;
                }

                //metoda
                takePhotoFromStorage();
            }
        });

        //pobiera zdjęcie z kamery
        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sprawdzenie permissions
                if (!hasPermissions(MainActivity.this, permisionas)) {
                    ActivityCompat.requestPermissions(MainActivity.this, permisionas, PERMISSION_ALL);
                    return;
                }

                //tworzy folder na dysku telefonu - żeby utworzył to muszą być dane permissions
                Boolean checkDirectory = makeDirectoryOnHardDrive();
                if (!checkDirectory) {
                    return;
                }

                //metoda
                dispatchTakePictureIntent();
            }
        });

        // wykonanie zczytania textu i tłumaczenia
        button_call_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imageUriFinal: " + imageUriFinal);
                if (imageUriFinal != null) {
                    putPhotoToFireBaseAndTranslate(imageUriFinal);
                } else {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.First_take_photo), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //obraca zdjęcie i zapisuje na dysku
        imageViewRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageBitmap != null) { // aby sie nie wywaliło gdy nie ma jeszcze zdjęcia zaczytanego
                    // pbrócenie o 90 stopni zdjęcia
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true);
                    imageBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                    if (fileName != null) {
                        writeFile(fileName, 100);
                    }
                } else {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.First_take_photo), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // kopiowanie tekstu zczytanego ze zdjęcia
        imageViewCopyAndSaveOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFromTextView = textViewResult.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), textFromTextView);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.Text_saved), Toast.LENGTH_SHORT).show();
            }
        });

        // kopiowanie przetłumaczonego tekstu
        imageViewCopyAndSaveTranslated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFromTextView = textViewResultTranslated.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), textFromTextView);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.Text_saved), Toast.LENGTH_SHORT).show();
            }
        });

        //powiększanie zdjęciaw oddzielnym activity
        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForPhoto = new Intent(MainActivity.this, ActivityForPhoto.class);
                intentForPhoto.putExtra("photoUri", imageUriFinal);
                startActivity(intentForPhoto);
            }
        });

        //do otwierania strony z Yandex translatorem
        linearLayoutImageAndPoweredYandex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://translate.yandex.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    //pokazuje reklamę
    private void showAddFullScrean() {
        if (mInterstitialAd.isLoaded()) { // potrzeba bo reklama może nie zdążyć się załadować przed pokazaniem
            mInterstitialAd.show(); //pokazuje reklamę
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // ładuje reklamę to chwile potrwa więc od razu może nie pokazać bo nie będzie załadowana dlatego trzeba dodać listenera jak niżej
    }

    //tworzy folder na dysku telefonu - żeby utworzył to muszą być dane permissions
    private boolean makeDirectoryOnHardDrive() {
        if (isExternalStorageWritable()) {
            File myDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MainActivity.this.getResources().getString(R.string.app_name));
            myDirectory.mkdir();
            return true;
        } else {
            Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.External_storage_not_available), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // ustwia text w textView z informacją o językach
    @Override
    protected void onResume() {
        super.onResume();

        // ustwia text w textView z informacją o językach - musi być tu a nie w onCreate bo jak się zmienia w ustawieniach i wraca do MainActivity to nie zapisuje ustawień przez onCreate
        textViewLanguageOnPhotoTakeFromShar.setText(" " + shar.getString(SHAR_PREF_KEY_TEXT_ORIGIN, "none"));
        textViewLanguageToTranslateTakeFromShar.setText(" " + shar.getString(SHAR_PREF_KEY_TEXT_TRANLATED, "none"));

        // dane do opisania jezyków textu - musi być tu a nie w onCreate bo jak się zmienia w ustawieniach i wraca do MainActivity to nie zapisuje ustawień przez onCreate
        languagePhoto = getLanguageFreeOCRApiFromSharPref(shar.getString(SHAR_PREF_KEY_TEXT_ORIGIN, English));          //Language, pol to polski, eng
        languageTranslationFrom = getLanguageYandexFromSharPref(shar.getString(SHAR_PREF_KEY_TEXT_ORIGIN, English));    //Language, pl to polski, en
        languageTranslationTo = getLanguageYandexFromSharPref(shar.getString(SHAR_PREF_KEY_TEXT_TRANLATED, English));   //Language, pl to polski, en
        Log.d(TAG, "onCreate:languages: language photo: " + languagePhoto + " languageTranslationFrom: " + languageTranslationFrom + " languageTranslationTo: " + languageTranslationTo);
    }

    //wyłączenie dzialogu jeśli się obróci telefon
    @Override
    protected void onPause() {
        super.onPause();

        //wyłączenie dzialogu jeśli się obróci telefon
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    // metoda zwraca kod języka 2 literowy
    private String getLanguageYandexFromSharPref(String textLanguage) {

        switch (textLanguage) {
            case Arabic:
                return "ar";
            case Bulgarian:
                return "bg";
            case ChineseSimplified:
                return "zh";
            case ChineseTraditional:
                return "zh";
            case Croatian:
                return "hr";
            case Czech:
                return "cs";
            case Danish:
                return "da";
            case Dutch:
                return "nl";
            case English:
                return "en";
            case Finnish:
                return "fi";
            case French:
                return "fr";
            case German:
                return "de";
            case Greek:
                return "el";
            case Hungarian:
                return "hu";
            case Korean:
                return "ko";
            case Italian:
                return "it";
            case Japanese:
                return "ja";
            case Polish:
                return "pl";
            case Portuguese:
                return "pt";
            case Russian:
                return "ru";
            case Slovenian:
                return "sl";
            case Spanish:
                return "es";
            case Swedish:
                return "sv";
            case Turkish:
                return "tr";

            // tłumaczone na polski
            case Arabski:
                return "ar";
            case Bulgarski:
                return "bg";
            case ChinskiUproszczony:
                return "zh";
            case ChinskiTradycyjny:
                return "zh";
            case Chorwacki:
                return "hr";
            case Czeski:
                return "cs";
            case Dunski:
                return "da";
            case Holenderski:
                return "nl";
            case Angielski:
                return "en";
            case Finski:
                return "fi";
            case Francuski:
                return "fr";
            case Niemiecki:
                return "de";
            case Grecki:
                return "el";
            case Wegierski:
                return "hu";
            case Koreanski:
                return "ko";
            case Wloski:
                return "it";
            case Japonski:
                return "ja";
            case Polski:
                return "pl";
            case Portugalski:
                return "pt";
            case Rosyjski:
                return "ru";
            case Slowenski:
                return "sl";
            case Hiszpanski:
                return "es";
            case Szwedzki:
                return "sv";
            case Turecki:
                return "tr";

        }
        return "en";
    }

    // metoda zwraca kod języka 3 literowy
    private String getLanguageFreeOCRApiFromSharPref(String textLanguage) {
        switch (textLanguage) {
            case Arabic:
                return "ara";
            case Bulgarian:
                return "bul";
            case ChineseSimplified:
                return "chs";
            case ChineseTraditional:
                return "cht";
            case Croatian:
                return "hrv";
            case Czech:
                return "cze";
            case Danish:
                return "dan";
            case Dutch:
                return "dut";
            case English:
                return "eng";
            case Finnish:
                return "fin";
            case French:
                return "fre";
            case German:
                return "ger";
            case Greek:
                return "gre";
            case Hungarian:
                return "hun";
            case Korean:
                return "kor";
            case Italian:
                return "ita";
            case Japanese:
                return "jpn";
            case Polish:
                return "pol";
            case Portuguese:
                return "por";
            case Russian:
                return "rus";
            case Slovenian:
                return "slv";
            case Spanish:
                return "spa";
            case Swedish:
                return "swe";
            case Turkish:
                return "tur";

            //tłumaczenie na polski
            case Arabski:
                return "ara";
            case Bulgarski:
                return "bul";
            case ChinskiUproszczony:
                return "chs";
            case ChinskiTradycyjny:
                return "cht";
            case Chorwacki:
                return "hrv";
            case Czeski:
                return "cze";
            case Dunski:
                return "dan";
            case Holenderski:
                return "dut";
            case Angielski:
                return "eng";
            case Finski:
                return "fin";
            case Francuski:
                return "fre";
            case Niemiecki:
                return "ger";
            case Grecki:
                return "gre";
            case Wegierski:
                return "hun";
            case Koreanski:
                return "kor";
            case Wloski:
                return "ita";
            case Japonski:
                return "jpn";
            case Polski:
                return "pol";
            case Portugalski:
                return "por";
            case Rosyjski:
                return "rus";
            case Slowenski:
                return "slv";
            case Hiszpanski:
                return "spa";
            case Szwedzki:
                return "swe";
            case Turecki:
                return "tur";
        }
        return "eng";
    }

    // rezultat pobrania zdjęcia z kamery lub z dysku, zapisanie stałej imageUri i dodanie do stałej imageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        showAddFullScrean(); //ładuje reklamę pełnoekranową

        if (resultCode != RESULT_OK) { //po to aby jak sie cofnie do aplikacji bez pobrania zdjęcia się nie wywalało
            return;
        }

        // zdjęcie z aparatu
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri = Uri.fromFile(new File(mCameraFileName)); //zwraca zdjęcie z aparatu jako ścieszkę uri
            Log.d(TAG, "onActivityResult: imageUri z aparatu: " + imageUri);

            // zdjęcie z dysku
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            imageUri = data.getData(); //zwraca zdjęcie z dysku jako ścieszkę Uri
            Log.d(TAG, "onActivityResult: imageUri z dysku: " + imageUri);
        }

        // zapisuje zdjęcie do imageBitmap na podstawie imageUriFinal zapisanego na dysku lub wziętwgo z aparatu i potem dalej to będzie przetwarzane w metodzie write file
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            Log.d(TAG, "onActivityResult: blok try do zapisania imageBitmap");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //metoda do zmniejszenia zdjęcia poniżej 1MB ( darmowe API tylko do 1MB) i zapisania na dysku
        Date currentDate = new Date(System.currentTimeMillis()); //System.currentTimeMillis()-long
        fileName = new SimpleDateFormat("yyyyMMdd_HH.mm.ss").format(currentDate) + ".jpg";
        writeFile(fileName, imageCompressQuality); //metoda do zapisania pliku
    }

    //metoda do zmniejszenia zdjęcia poniżej 1MB ( darmowe API tylko do 1MB), zapisania na dysku i ustawienia do imageViewPhoto
    private void writeFile(String fileNameGotFromCall, int quality) {//fileName to nazwa pliku zdjęcia, np: "zdjecie.jpg"

        // sprawdzenie czy mozna zapisywac na dysku
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, MainActivity.this.getResources().getString(R.string.External_storage_not_available), Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "writeFile: po if");

        //tworzy w katalogu Pictures katalog z nazwą aplikacji i ścieżkę do pliku na dysku do pliku
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/" + MainActivity.this.getResources().getString(R.string.app_name), fileNameGotFromCall);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //zapisuje bitmape na dysku
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
            //Toast.makeText(this, MainActivity.this.getResources().getString(R.string.Photo_saved), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //zmniejsza jakość zdjęcia o 50% jeśli zdjęcie jest wieksze od 1MB - bo darmowe API nie przyjmuje zdjęć większych niż 1MB
        long imageSize = file.length() / 1024; // wielkośc w kB
        if (imageSize > 1000) {

            //zwiększa kompresję o 10% za każdym razem jak wpadnie do ifa
            imageCompressQuality = imageCompressQuality - 10;
            if (imageCompressQuality == 0) { //zabezpieczenie żeby nie było = 0
                imageCompressQuality = 1;
            }
            Log.d(TAG, "onActivityResult: image size: " + imageSize + "  ,imageCompressQuality = " + imageCompressQuality);
            writeFile(fileNameGotFromCall, imageCompressQuality);
        }
        imageCompressQuality = 100; // zmienia kompresję na domyślną
        Log.d(TAG, "onActivityResult: image size: " + imageSize);

        imageUriFinal = Uri.parse("file://" + file.toString());
        Log.d(TAG, "writeFile: imageUriFinal: " + imageUriFinal);

        //pokazanie zapisanego zdjęcia w imageViev
        imageViewPhoto.setImageBitmap(imageBitmap);

        //wyczyszczenie pól tłumaczeń
        textViewResult.setText("");
        textViewResultTranslated.setText("");
    }

    //wrzucenie zdjęcia do firebase i zczytanie textu
    private void putPhotoToFireBaseAndTranslate(Uri imageUri) {

        //utworzenie dialogu podczas wczytywania danych
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(MainActivity.this.getResources().getString(R.string.Wait_while_decoding));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mProgressDialog.show();

        mStorageReference.child(shar.getString(SHAR_PREF_KEY_PERSONAL_ID, "personal ID not created") + ".jpg").putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl(); //potrzebne do zdobycia uri zdjęcia
                while (!uri.isComplete()) ; //potrzebne do zdobycia uri zdjęcia
                String mImageUrl = "" + uri.getResult(); //potrzebne do zdobycia uri zdjęcia
                Log.d(TAG, "onSuccess: url: " + mImageUrl);

                // wywołanie metody do przekształecenia na text
                OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(MainActivity.this, mImageUrl, languagePhoto, MainActivity.this);
                oCRAsyncTask.execute();

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.Can_not_save_data_on_server), Toast.LENGTH_SHORT).show();
                //wyłaczenie dialogu
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    // do implementacji intrfejsu - wynik zczytania ze zdjęcia textu
    @Override
    public void getOCRkResult(String response) {

        Log.d(TAG, "getImageTextAndConvert: String result: " + response);
        try {

            //ustawia textów na textViews
            textViewResultLanguage.setText(getResources().getString(R.string.text_on_photo));
            textViewResultTranslatedLanguage.setText(getResources().getString(R.string.text_translated));

            //ustawienie widoczności ikonki do kopiowania tekstu oryginalnego
            imageViewCopyAndSaveOrigin.setVisibility(View.VISIBLE);

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("ParsedResults");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String parsedText = jsonObject1.getString("ParsedText");

            if (!parsedText.equals("")) {             // jeśli jest przetłumaczony text to będzie coś zawierało
                Log.d(TAG, "getImageTextAndConvert: z ifa po wyjeciu parsed text: " + parsedText);
                textViewResult.setText("");
                textViewResult.setText(parsedText);

                translationText(parsedText);     // metoda do wywołania klasy TranslationText i zwraca przetłumaczony text przez interfase
                textViewResultTranslated.setText(MainActivity.this.getResources().getString(R.string.Translating));

            } else {
                textViewResult.setText("");
                textViewResult.setText(MainActivity.this.getResources().getString(R.string.Text_not_found));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //wyłaczenie dialogu
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    // metoda do wywołania klasy TranslationText i zwraca przetłumaczony text przez interfase
    public void translationText(String textToTranslate) {
        TranslationText translationText = new TranslationText();
        translationText.getTextTranslation(MainActivity.this, languageTranslationFrom, languageTranslationTo, textToTranslate);
    }

    // do implementacji intrfejsu - wynik tłumaczenia textu
    @Override
    public void getTranslationResult(String response) {
        textViewResultTranslated.setText("");
        textViewResultTranslated.setText(response);

        //ustawienie widoczności ikonki do kopiowania i tekstu powered by yandex
        linearLayoutImageAndPoweredYandex.setVisibility(View.VISIBLE);
    }

    // sprawdzenie czy da sie zapisywać zdjęcia na dysku
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // metoda do użycia aparatu
    private void dispatchTakePictureIntent() {  // z onClick buttonOpenCamera
        if (isExternalStorageWritable()) {

            // nie wiem co to i po co ale musi być bo inaczej sie wywala
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            //stworzenie nazwy pliku dla nowego zdjęcia, bedzie nadpisywana za każdym razem
            File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + MainActivity.this.getResources().getString(R.string.app_name), "example.jpg");

            //zapisanie ścieżki do nowego zdjęcia z aparatu
            mCameraFileName = outFile.toString();
            Uri outUri = Uri.fromFile(outFile);
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
            Log.d(TAG, "dispatchTakePictureIntent: mCameraFileName: " + mCameraFileName);
        } else {
            Toast.makeText(this, MainActivity.this.getResources().getString(R.string.External_storage_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    //metoda do użycia zdjęcia z dysku
    private void takePhotoFromStorage() {  // z onClick buttonTakePicture
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    // do permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //do zachowywania wyników przy obrocie ekranu
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); //zawsze musi być

        outState.putString(KEY_M_CAMERA_FILE_NAME, mCameraFileName);
        if (imageUri != null) {
            outState.putString(KEY_IMAGE_URI, imageUri.toString());
        }
        if (imageUriFinal != null) {
            outState.putString(KEY_IMAGE_URI_FINAL, imageUriFinal.toString());
        }
        outState.putString(KEY_FILE_NAME, fileName);
        outState.putString(KEY_TEXT_VIEW_RESULT, textViewResult.getText().toString());
        outState.putString(KEY_TEXT_VIEW_RESULT_LANGUAGE, textViewResultLanguage.getText().toString());
        outState.putString(KEY_TEXT_VIEW_RESULT_TRANSLATED, textViewResultTranslated.getText().toString());
        outState.putString(KEY_TEXT_VIEW_RESULT_TRANSLATED_LANGUAGE, textViewResultTranslatedLanguage.getText().toString());
    }

    // górne menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //górne menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this, ActivitySettings.class));
                break;
            case R.id.menu_info:
                startActivity(new Intent(MainActivity.this, ActivityInfo.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

