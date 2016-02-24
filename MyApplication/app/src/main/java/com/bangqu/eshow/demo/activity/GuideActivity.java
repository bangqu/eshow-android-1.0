package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.guide.ParallaxFragment;
import com.bangqu.eshow.demo.guide.OpenCalligraphyFactory;
import com.prolificinteractive.parallaxpager.ParallaxContextWrapper;

/**
 * 引导页面
 * Created by daikting on 16/2/24.
 */
public class GuideActivity  extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        //ParallaxPager and Calligraphy don't seem to play nicely together
        //The solution was to add a listener for view creation events so that we can hook up
        // Calligraphy to our view creation calls instead.
        super.attachBaseContext(
                new ParallaxContextWrapper(newBase, new OpenCalligraphyFactory())
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new ParallaxFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
