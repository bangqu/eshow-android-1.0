package com.bangqu.eshow.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.fragment.ESFragment;

/**
 * 导航分页
 * Created by daikting on 16/2/19.
 */
public class NaviFragment extends ESFragment implements View.OnClickListener {
    private Context mContext;
    final int radioIds[] = {R.id.radio0, R.id.radio1, R.id.radio2};
    RadioButton radios[] = new RadioButton[radioIds.length];
    Button buttonExit;
    private NaviCallbacks naviCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_navi, null);

        for (int i = 0; i < radioIds.length; ++i) {
            radios[i] = (RadioButton) view.findViewById(radioIds[i]);
            radios[i].setOnClickListener(this);
        }
        radios[0].setChecked(true);

        buttonExit = (Button) view.findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExit) {
            if (naviCallbacks != null) {
                naviCallbacks.onNaviItemSelected(3);
            }
            return;
        }
        for (int i = 0; i < radioIds.length; i++) {
            if (v.getId() == radioIds[i]) {
                selectItem(i);
            } else {
                radios[i].setChecked(false);
            }
        }
    }

    private void selectItem(int position) {
        if (naviCallbacks != null) {
            naviCallbacks.onNaviItemSelected(position);
        }
    }

    /**
     * 侧边栏的回调
     *
     * @param naviCallbacks
     */
    public void setNaviCallbacks(NaviCallbacks naviCallbacks) {
        this.naviCallbacks = naviCallbacks;
    }

    public interface NaviCallbacks {
        void onNaviItemSelected(int position);
    }

}
