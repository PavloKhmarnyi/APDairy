package com.example.pavlo.apdairy20.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavlo.apdairy20.R;

/**
 * Created by pavlo on 08.06.16.
 */
public class PageView extends View {

    TextView titleTextView;
    TextView explanationTextView;
    ImageView imageView;

    public PageView(Context context) {
        super(context);

        initComponents();
    }

    private void initComponents() {
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        explanationTextView = (TextView) findViewById(R.id.explanationTextView);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
}
