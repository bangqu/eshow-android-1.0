package com.bangqu.eshow.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.util.ESViewUtil;

/**
 * 个人信息修改分页
 * Created by daikting on 16/2/19.
 */
public class PersonFragment extends Fragment implements View.OnClickListener {
    private Context mContext;

    TextView tvNotify;
    RelativeLayout rlTel;
    TextView tvTel;
    RelativeLayout rlEmail;
    TextView tvEmail;
    RelativeLayout rlBirthday;
    TextView tvBirthday;
    RelativeLayout rlCity;
    TextView tvCity;
    RelativeLayout rlSignature;
    TextView tvSignature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_person, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        tvNotify = (TextView) view.findViewById(R.id.tvNotify);
        tvNotify.setVisibility(View.GONE);

        rlTel = (RelativeLayout) view.findViewById(R.id.rlTel);
        rlTel.setOnClickListener(this);
        tvTel = (TextView) view.findViewById(R.id.tvTel);
        rlEmail = (RelativeLayout) view.findViewById(R.id.rlEmail);
        rlEmail.setOnClickListener(this);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        rlBirthday = (RelativeLayout) view.findViewById(R.id.rlBirthday);
        rlBirthday.setOnClickListener(this);
        tvBirthday = (TextView) view.findViewById(R.id.tvBirthday);

        rlCity = (RelativeLayout) view.findViewById(R.id.rlCity);
        rlCity.setOnClickListener(this);
        tvCity = (TextView) view.findViewById(R.id.tvCity);

        rlSignature = (RelativeLayout) view.findViewById(R.id.rlSignature);
        rlSignature.setOnClickListener(this);
        tvSignature = (TextView) view.findViewById(R.id.tvSignature);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
