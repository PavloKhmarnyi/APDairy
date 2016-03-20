package com.example.pavlo.apdairy;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlo.apdairy.com.example.pavlo.apdairy.classes.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pavlo on 17.03.16.
 */
public class PageView extends View{

    TextView titleTextView;
    TextView explanationTextView;
    ImageView imageView;

    private Context context;

    private RelativeLayout layout;

    private JSONObject json;

    private String urlString;

    private String title;
    private String explanation;
    private String imageUrl;

    public PageView(Context context, LayoutInflater inflater, String date) {
        super(context);
        this.context = context;

        layout = (RelativeLayout) inflater.inflate(R.layout.page_view, null);
        initViewComponents();

        urlString = UrlBuilder(date);
        new LongOperation().execute();
    }

    private void initViewComponents() {
        titleTextView = (TextView) layout.findViewById(R.id.titleTextView);
        explanationTextView = (TextView) layout.findViewById(R.id.explanationTextView);
        imageView = (ImageView) layout.findViewById(R.id.imageView);
        imageView.setOnClickListener(imageViewClickListener);
    }

    OnClickListener imageViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(context)
                    .setTitle("Set as wallpaper")
                    .setMessage("Would you like to set this image as wallpaper?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                                imageView.buildDrawingCache();
                                Bitmap bitmap = imageView.getDrawingCache();
                                wallpaperManager.setBitmap(bitmap);
                                Toast.makeText(context, "Wallpaper set successfull!", Toast.LENGTH_SHORT).show();
                                Log.d(Constants.LOG_TAG, "Wallpaper set successfull");
                            } catch (IOException e) {
                                Toast.makeText(context, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
                                Log.d(Constants.LOG_TAG, "Error setting wallpaper " + e.getMessage());
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Action has canceled!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    };

    public RelativeLayout getLayout() {
        return layout;
    }

    public String UrlBuilder(String date) {
        StringBuilder urlString = new StringBuilder();
        urlString.append(Constants.NASA_APOD_URL);
        urlString.append("?api_key=");
        urlString.append(Constants.API_KEY);

        if (date != null) {
            urlString.append("&date=" + date);
        }

        return urlString.toString();
    }

    private class LongOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void...params) {
            try {
                URL url = new URL(urlString);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);

                InputStream stream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String inputLine;
                StringBuffer stringBuffer = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }

                json = new JSONObject(stringBuffer.toString());

                title = json.getString("title");
                explanation = json.getString("explanation");
                imageUrl = json.getString("url");

                if (reader != null) {
                    reader.close();
                }

                Log.d(Constants.LOG_TAG, "title = " + title);
                Log.d(Constants.LOG_TAG, "image url = " + imageUrl);
                Log.d(Constants.LOG_TAG, "explanation = " + explanation);

                Log.d(Constants.LOG_TAG, "Get data from NASA");

            } catch (MalformedURLException e) {
                Log.d(Constants.LOG_TAG, "Exception " +  e.getMessage());
            } catch (IOException e) {
                Log.d(Constants.LOG_TAG, "Exception " +  e.getMessage());
            } catch (JSONException e) {
                Log.d(Constants.LOG_TAG, "Exception " +  e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        titleTextView.setText(title);
        explanationTextView.setText(explanation);

        Picasso.with(getContext()).load(imageUrl).placeholder(R.mipmap.ic_launcher).
                error(R.drawable.error_img).into(imageView);
        }
    }
}
