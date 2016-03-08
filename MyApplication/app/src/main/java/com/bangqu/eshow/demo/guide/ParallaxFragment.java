package com.bangqu.eshow.demo.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.activity.InputTelActivity_;
import com.bangqu.eshow.demo.activity.LoginActivity_;
import com.bangqu.eshow.demo.bean.Enum_InputTel;
import com.prolificinteractive.parallaxpager.ParallaxContainer;

/**
 * 引导页面
 */
public class ParallaxFragment extends Fragment implements ViewPager.OnPageChangeListener {

    IndicatorView mIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parallax, container, false);
        mIndicatorView = (IndicatorView) view.findViewById(R.id.indicatorView);

        ParallaxContainer parallaxContainer =
                (ParallaxContainer) view.findViewById(R.id.parallax_container);

        parallaxContainer.setLooping(false);

        parallaxContainer.setupChildren(inflater,
                R.layout.parallax_view_1,
                R.layout.parallax_view_2,
                R.layout.parallax_view_3
        );

        parallaxContainer.setOnPageChangeListener(this);

        final Button registButton = (Button) view.findViewById(R.id.register_button);
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTelActivity_.intent(ParallaxFragment.this).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_InputTel.REGISTER).start();
            }
        });

        final Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity_.intent(ParallaxFragment.this).start();

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {
//        Log.d("", String.format("off %d, %f, %d", position, offset, offsetPixels));
        if (offset > 0.5) {
            mIndicatorView.setSelect(position + 1);
        } else {
            mIndicatorView.setSelect(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
