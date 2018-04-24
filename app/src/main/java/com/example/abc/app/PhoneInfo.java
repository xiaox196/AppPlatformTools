package com.example.abc.app;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.abc.myapplication.R;

/**
 * Created by D444Q52 on 2015/10/9.
 */
public class PhoneInfo extends android.support.v4.app.Fragment {

    private TextView phoneinf=null;
    private  TextView tv=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.phoneinfo,container,false);
        phoneinf= (TextView) view. findViewById(R.id.text);
        tv=(TextView)view.findViewById(R.id.titleTv);
        String string="SDK版本:  "+ Build.VERSION.RELEASE+"\n"+"手机型号： "+Build.MODEL+"\n"+"手机厂家:  "
                +Build.MANUFACTURER+"\n"+"CPU型号:  "+Build.CPU_ABI+"\n";

        WindowManager windowManager = this.getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth  = display.getWidth();
        int screenHeight =  display.getHeight();
        string=string+"手机分辨率:  "+screenHeight+"*"+screenWidth;
        phoneinf.setText(string);
        tv.setText("手机信息");
        return view;
    }
}
