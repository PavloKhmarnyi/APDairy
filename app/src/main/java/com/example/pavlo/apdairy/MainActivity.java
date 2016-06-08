package com.example.pavlo.apdairy;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pavlo.apdairy.com.example.pavlo.apdairy.classes.Constants;
import com.example.pavlo.apdairy.com.example.pavlo.apdairy.classes.MainPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private ViewPager pager;
    private MainPagerAdapter pagerAdapter;

    private LayoutInflater inflater;

    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        pagerAdapter = new MainPagerAdapter();

        inflater = getLayoutInflater();

        initPager();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        addView(createViewPagerItem(null));
    }

    public void initPager() {
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(onPageChangeListener);
    }

    private View createViewPagerItem (String date) {
        PageView pageView = new PageView(this, inflater, date);

        return pageView.getLayout();
    }

    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);

        pagerAdapter.notifyDataSetChanged();

        //pager.setCurrentItem(pageIndex, true);
    }

    public void addView(View newPage, int position) {
        pagerAdapter.addAView(newPage, position);
        pagerAdapter.notifyDataSetChanged();
    }

    public void removeView(View defuncPage) {
        int pageIndex = pagerAdapter.removeView(pager, defuncPage);

        if (pageIndex == pagerAdapter.getCount()) {
            pageIndex--;
        }

        pager.setCurrentItem(pageIndex);
        pagerAdapter.notifyDataSetChanged();
    }

    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        private boolean flag = true;

        private Calendar calendarLowDate = Calendar.getInstance();
        private Calendar calendarHighDate = Calendar.getInstance();

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if ((positionOffset == 0.0) && (flag)) {

                if (position == 0) {
                    calendarLowDate.add(Calendar.DATE, -1);
                    String lowDate = simpleDateFormat.format(calendarLowDate.getTime());
                    addView(createViewPagerItem(lowDate), 0);
                } else if ((position == pagerAdapter.getCount() - 1) ) {
                    calendarHighDate.add(Calendar.DATE, 1);
                    String highDate = simpleDateFormat.format(calendarHighDate.getTime());
                    addView(createViewPagerItem(highDate));
                }

                flag = false;
            }
            Log.d(Constants.LOG_TAG, pagerAdapter.getCount() + " items, " + position + " page, offset = " + positionOffset);
            Log.d(Constants.LOG_TAG, "lowDate = " + simpleDateFormat.format(calendarLowDate.getTime()) +
                    ", heighDate = " + simpleDateFormat.format(calendarHighDate.getTime()));
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                flag = true;
            }
            Log.d(Constants.LOG_TAG, "state = " + state);
        }
    };
}
