package com.bangqu.eshow.demo.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangqu.eshow.demo.R;
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

        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final View loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
