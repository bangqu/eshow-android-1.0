package cn.org.eshow.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.parallaxpager.ParallaxContainer;

import cn.org.eshow.R;
import cn.org.eshow.activity.InputTelActivity_;
import cn.org.eshow.activity.LoginActivity_;
import cn.org.eshow.activity.MainActivity_;
import cn.org.eshow.bean.Enum_CodeType;
import cn.org.eshow_structure.util.ESViewUtil;

/**
 * 引导页面
 */
public class ParallaxFragment extends Fragment implements ViewPager.OnPageChangeListener {
    public static final String INTENT_ISSHOWBUTTON = "IsShowButton";
    IndicatorView mIndicatorView;

    LinearLayout layoutBottom;
    TextView tvPass;
    boolean isShowButton = true;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parallax, container, false);
        ESViewUtil.scaleContentView((RelativeLayout) view.findViewById(R.id.rlParent));
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

        tvPass = (TextView) view.findViewById(R.id.tvPass);
        tvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity_.intent(ParallaxFragment.this).start();
            }
        });
        layoutBottom = (LinearLayout) view.findViewById(R.id.layoutBottom);
        if(isShowButton){
            layoutBottom.setVisibility(View.VISIBLE);

        }else{
            layoutBottom.setVisibility(View.GONE);

        }
        final Button registButton = (Button) view.findViewById(R.id.register_button);
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTelActivity_.intent(ParallaxFragment.this).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.REGISTER).start();
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
    public void setIsShowButton(boolean isShow){
        this.isShowButton = isShow;
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
