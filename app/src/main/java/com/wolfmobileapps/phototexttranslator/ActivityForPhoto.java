package com.wolfmobileapps.phototexttranslator;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityForPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_photo);

        //ustawienie górnej nazwy i strzałki do powrotu
        getSupportActionBar().setTitle(getResources().getString(R.string.Image)); //ustawia nazwę na górze
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ustawia strzałkę

        Intent intentforPhoto = getIntent();
        Uri imageUri = (Uri) intentforPhoto.getExtras().get("photoUri");

        TouchImageView img = new TouchImageView(this);
        img.setImageURI(imageUri); // może być Uri, bitmapa, drawable i inne tylko trzebawywoąłć inną metodę
        img.setMaxZoom(4f);
        setContentView(img);
    }

    //po to zeby jak wraca to nie czyścił tego co jest w activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
