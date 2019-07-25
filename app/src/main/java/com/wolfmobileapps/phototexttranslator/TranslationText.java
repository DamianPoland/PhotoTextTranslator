package com.wolfmobileapps.phototexttranslator;


import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


// klasa do przetłumaczenia textu
public class TranslationText {

    private static final String TAG = "TranslationText";

    // construktor with context
    public TranslationText() {

    }

    // metoda do nawiązania połaczenia z serwerem i przetłumaczenia textu wywołana z MainActivity
    public void getTextTranslation(final Context context, String translationFrom, String translationTo, String textToTranslate) {

        //url który zwraca dostępne jezyki i tłumaczenia
        //url ="https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui=en&key=trnsl.1.1.20190718T064356Z.c4180cdafb765b7c.a377e14538bfd5e6cf9ebbbc48ab8f07e3908843";
        //przykłądowy url z tłumaczeniem hello word
        //url ="https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-pl&key=trnsl.1.1.20190718T064356Z.c4180cdafb765b7c.a377e14538bfd5e6cf9ebbbc48ab8f07e3908843&text=hello world";

        // utworzenie url do tłumaczenia
        String host = "https://translate.yandex.net"; //host - nie zmieniać
        String post = "/api/v1.5/tr.json/translate";  // post - nie zmieniać
        String questionMark = "?";  // rozpoczecie zapytania - nie zmieniać
        String language = "lang=" + translationFrom + "-" + translationTo;  // tłumaczenie jakiego języka na jaki - nie zmieniać
        String key = "&key=trnsl.1.1.20190718T064356Z.c4180cdafb765b7c.a377e14538bfd5e6cf9ebbbc48ab8f07e3908843"; // nie zmienaić
        //String addedHTTP = " HTTP/1.1";
        //key = "&key=trnsl.1.1.20190718T101604Z.83ee232f1710af34.3ae2e2a2f30afa7721edeece9c99b77e995f8ae1"; // drugi klucz ale ma nic nie daje bo zlicza razem z pierwszym zapytania
        try {
            textToTranslate = URLEncoder.encode(textToTranslate, "utf-8"); //dekodowanie textu - inaczej nie będzei działać w lollipopie
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = host + post + questionMark + language + key + "&text=" + textToTranslate; // końcowy url
        Log.d(TAG, "getTextTranslation: url: " + url);

        // utworzenie requst
        RequestQueue queue = Volley.newRequestQueue(context);
        //odpowiedż z serwera z Jsonem
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response: " + response);

                // wywołanie metody aby rozpakowała Jsona i zwróciła stringa z wynikiem lub z errorem
                String translationFinal = getJsonAndUnpack(response, context); //wynik wziety z Jsona
                Log.d(TAG, "onResponse: translationFinal: " + translationFinal);

                // przekazanie wyniku do MainActivity za pośrednictwem interfejsu
                InterfaceToShowSerwersResponses interfaceToShowSerwersResponses = (InterfaceToShowSerwersResponses) context;
                interfaceToShowSerwersResponses.getTranslationResult(translationFinal);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: error: " + error);
                String translationFinal = context.getResources().getString(R.string.Translator_not_respond);

                // przekazanie wyniku do MainActivity za pośrednictwem interfejsu
                InterfaceToShowSerwersResponses interfaceToShowSerwersResponses = (InterfaceToShowSerwersResponses) context;
                interfaceToShowSerwersResponses.getTranslationResult(translationFinal);
            }
        });

        queue.add(jsonObjectRequest); //wywołanie klasy
    }

    // wywołanie metody aby rozpakowała Jsona i zwróciła stringa z wynikiem lub z errorem
    private String getJsonAndUnpack(JSONObject response, Context context) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("text");
            String textFromJson = jsonArray.getString(0);
            return textFromJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return context.getResources().getString(R.string.Translator_Error);
    }
}
