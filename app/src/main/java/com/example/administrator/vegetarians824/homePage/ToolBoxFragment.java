package com.example.administrator.vegetarians824.homePage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.MFGT;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolBoxFragment extends Fragment {


    public ToolBoxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tool_box, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tool_passport, R.id.tool_invitation, R.id.tool_nutriData, R.id.contribute_temp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_nutriData:
                MFGT.gotoNutriData(getContext());
                break;
            case R.id.tool_passport:
                MFGT.gotoCheckLanguage(getContext());
                break;
            case R.id.tool_invitation:
                break;
            case R.id.contribute_temp:
                MFGT.gotoMyContribute(getContext());
                break;
        }
    }
}
