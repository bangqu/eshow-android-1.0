package cn.org.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.guide.OpenCalligraphyFactory;
import cn.org.eshow.demo.guide.ParallaxFragment;
import com.prolificinteractive.parallaxpager.ParallaxContextWrapper;

/**
 * 引导页面
 * Created by daikting on 16/2/24.
 */
public class GuideActivity  extends CommonActivity {
    boolean isShowButton = true;
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

        isShowButton = getIntent().getBooleanExtra(ParallaxFragment.INTENT_ISSHOWBUTTON,true);
        if (savedInstanceState == null) {
            ParallaxFragment parallaxFragment = new ParallaxFragment();
            parallaxFragment.setIsShowButton(isShowButton);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, parallaxFragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
