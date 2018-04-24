package com.example.abc.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaoming on 2015/9/21.
 */
public class LieBaoActivity extends Fragment {

    private List<PakageInf> list = new ArrayList<>();
    private ListView listView = null;
    private PackageManager pm;
    private PakageBaseActivity pakageBaseActivity;
    private static final String xiaoming = "xiaoming";
    private List<ResolveInfo> resolveInfos;


    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
    public static final int FILTER_LIEBAO_APP = 4; // 猎豹应用
    private TextView tv=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_liebao,container,false);
        listView = (ListView)view. findViewById(R.id.mylistviewframe);
        list = queryFilterAppInfo(FILTER_LIEBAO_APP);
        tv=(TextView)view.findViewById(R.id.titleTv);
        if (list != null) {
            pakageBaseActivity = new PakageBaseActivity(list, this.getActivity().getApplicationContext());
            listView.setAdapter(pakageBaseActivity);
            listView.setOnItemClickListener(new MyItemL());
        }
        tv.setText("猎豹应用");
        return view;
    }



    public class MyItemL implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.v(xiaoming,"itemselect");
            PakageInf inf=list.get(position);
            startActivity(inf.getIntent());
        }
    }


    // 根据查询条件，查询特定的ApplicationInfo
    private List<PakageInf> queryFilterAppInfo(int filter) {

        pm = this.getActivity().getApplication().getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.GET_UNINSTALLED_PACKAGES);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        List<PakageInf> appInfos = new ArrayList<PakageInf>(); // 保存过滤查到的AppInfo
        // 根据条件来过滤
        switch (filter) {
            case FILTER_LIEBAO_APP:
                Log.v(xiaoming,"liebaostart");
                appInfos.clear();
                for (ResolveInfo info : resolveInfos) {
                    String activityname = info.activityInfo.name;

                    String pkgname = info.activityInfo.packageName;

                    String applabel = (String) info.loadLabel(pm);
                    Log.v(xiaoming,"applabel:"+applabel);
                    Drawable icon = info.loadIcon(pm);
                    Intent lunchintent = new Intent();
                    lunchintent.setComponent(new ComponentName(pkgname, activityname));
                    if (pkgname.contains("com.cmair")) {
                        appInfos.add(Getpackageinfo(pkgname, icon, activityname, applabel, lunchintent));
                        Log.d(xiaoming, "找到的app：" + pkgname);
                    } else if (applabel.contains("猎豹")) {
                        Log.v(xiaoming,"abc");
                        appInfos.add(Getpackageinfo(pkgname,icon,activityname,applabel,lunchintent));
                        Log.d(xiaoming, "找到的app：" + applabel);
                    } else if (applabel.contains("金山")) {
                        appInfos.add(Getpackageinfo(pkgname,icon,activityname,applabel,lunchintent));
                        Log.d(xiaoming, "找到的app：" + applabel);
                    } else if (pkgname.contains(".cmcm")) {
                        appInfos.add(Getpackageinfo(pkgname,icon,activityname,applabel,lunchintent));
                        Log.d(xiaoming, "找到的app：" + applabel);
                    }else if(pkgname.contains("lock"))
                    {
                        appInfos.add(Getpackageinfo(pkgname,icon,activityname,applabel,lunchintent));
                        Log.d(xiaoming, "找到的app：" + applabel);
                    }
                }
                break;
            default:
                return null;
        }
        return appInfos;
    }




    private PakageInf Getpackageinfo(String pkgname,Drawable icon,String activityname,String applabel,Intent lunchintent) {
        PakageInf myinf=new PakageInf();
        myinf.setPakage_name(pkgname);
        myinf.setAppicon(icon);
        myinf.setActiviy(activityname);
        myinf.setMainactivity(applabel);
        myinf.setIntent(lunchintent);
        return myinf;
    }
}
