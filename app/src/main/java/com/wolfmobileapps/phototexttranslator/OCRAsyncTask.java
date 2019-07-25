package com.wolfmobileapps.phototexttranslator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


//klasa pobiera obrazek i przesyła go na serwer, zczytuje text i odsyła text w postaci Jsona
public class OCRAsyncTask extends AsyncTask {

    private static final String TAG = "OCRAsyncTask";

    private final String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
    private final String mApiKey = "5c522d6f9988957"; // API key wziety z rejestracji na stronie wilczynskid@o2.pl - nie zmieniać
    private boolean isOverlayRequired = false; // if true to podaje wjakich miejscach był text dany
    private String mImageUrl; // "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg"; // Image url przykładowe z OCR API
    private String mLanguage;
    private Activity mActivity;
    private InterfaceToShowSerwersResponses mIOCRCallBack;


    public OCRAsyncTask(Activity activity, String imageUrl, String language, InterfaceToShowSerwersResponses iOCRCallBack) {
        this.mActivity = activity;
        this.mImageUrl = imageUrl;
        this.mLanguage = language;
        this.mIOCRCallBack = iOCRCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object[] params) {

        try {
            return sendPost(mApiKey, isOverlayRequired, mImageUrl, mLanguage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String sendPost(String apiKey, boolean isOverlayRequired, String imageUrl, String language) throws Exception {

        URL obj = new URL(url); // OCR API Endpoints
        Log.d(TAG, "sendPost: " + obj);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


        JSONObject postDataParams = new JSONObject();


        postDataParams.put("apikey", apiKey);
        postDataParams.put("isOverlayRequired", isOverlayRequired);
        postDataParams.put("url", imageUrl);
        postDataParams.put("language", language);
        //postDataParams.put("filetype", "jpg");

        Log.d(TAG, "sendPost: postDataParams: " + postDataParams);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(getPostDataString(postDataParams));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        return String.valueOf(response);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        String response = (String) result;
        mIOCRCallBack.getOCRkResult(response); // zapisanie wyniku do interfacu
        Log.d(TAG, response);
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}