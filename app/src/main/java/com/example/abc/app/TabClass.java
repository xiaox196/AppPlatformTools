package com.example.abc.app;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.abc.myapplication.R;

/**
 * Created by D444Q52 on 2015/9/21.
 */
public class TabClass extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout);
        AddPhoneInf();
        LiebaoTab();
        ThreeTab();
        AddCallPhone();
      //  AllTab();
    }

    private void ThreeTab()
    {
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, MainActivity.class);
        //生成一个TabSpec对象，这个对象代表了一个页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("第三方应用");
        Resources res = getResources();
        //设置该页的indicator
        remoteSpec.setIndicator("第三方应用", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
    }

    private void LiebaoTab()
    {
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, LieBaoActivity.class);
        //生成一个TabSpec对象，这个对象代表了一个页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("猎豹应用");
        Resources res = getResources();
        //设置该页的indicator
        remoteSpec.setIndicator("猎豹应用", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
    }

    private void AllTab()
    {
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, AllTabActivity.class);
        //生成一个TabSpec对象，这个对象代表了一个页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("所有应用");
        Resources res = getResources();
        //设置该页的indicator
        remoteSpec.setIndicator("所有应用", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
    }

    private void AddPhoneInf()
    {
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, PhoneInfo.class);
        //生成一个TabSpec对象，这个对象代表了一个页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("手机信息");
        Resources res = getResources();
        //设置该页的indicator
        remoteSpec.setIndicator("手机信息", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
    }

    private void AddCallPhone()
    {
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, CallPhoneActivity.class);
        //生成一个TabSpec对象，这个对象代表了一个页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("查找手机");
        Resources res = getResources();
        //设置该页的indicator
        remoteSpec.setIndicator("查找手机", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
    }

}
