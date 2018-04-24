package com.example.abc.app;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.abc.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by D444Q52 on 2015/9/21.
 */
public class AllTabActivity extends Activity{
    private List<PakageInf> list = new ArrayList<>();
    private ListView listView = null;
    private PackageManager pm;
    private PakageBaseActivity pakageBaseActivity;
    private  static final  String xiaoming="xiaoming";

    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
    public static final int FILTER_LIEBAO_APP = 4; // 猎豹应用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.mylistview);
        list = queryFilterAppInfo(FILTER_ALL_APP);
        if (list!=null) {
            pakageBaseActivity = new PakageBaseActivity(list, getApplicationContext());
            listView.setAdapter(pakageBaseActivity);
        }
    }

    // 根据查询条件，查询特定的ApplicationInfo
    private List<PakageInf> queryFilterAppInfo(int filter) {
        pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));// 排序
        List<PakageInf> appInfos = new ArrayList<PakageInf>(); // 保存过滤查到的AppInfo
        // 根据条件来过滤
        switch (filter) {
            case FILTER_ALL_APP: // 所有应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    appInfos.add(getAppInfo(app));
                }
                return appInfos;
            case FILTER_SYSTEM_APP: // 系统程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appInfos.add(getAppInfo(app));
                    }
                }
                return appInfos;
            case FILTER_THIRD_APP: // 第三方应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    //非系统程序
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        appInfos.add(getAppInfo(app));
                    }
                    //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                    else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        appInfos.add(getAppInfo(app));
                    }
                }
                break;
            case FILTER_SDCARD_APP: // 安装在SDCard的应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                        appInfos.add(getAppInfo(app));
                    }
                }
                return appInfos;
            case  FILTER_LIEBAO_APP:
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        Log.d(xiaoming, app.packageName);
                        if(app.packageName.contains("com.cmair"))
                        {
                            appInfos.add(getAppInfo(app));
                            Log.d(xiaoming, "找到的app："+app.packageName);
                        }else  if(((String) app.loadLabel(pm)).contains("猎豹"))
                        {
                            appInfos.add(getAppInfo(app));
                            Log.d(xiaoming, "找到的app："+app.packageName);
                        }
                    }
                }
                break;
            default:
                return null;
        }
        return appInfos;
    }

    // 构造一个AppInfo对象 ，并赋值
    private PakageInf getAppInfo(ApplicationInfo app) {
        PakageInf appInfo = new PakageInf();
        appInfo.setMainactivity((String) app.loadLabel(pm));
        appInfo.setAppicon(app.loadIcon(pm));
        appInfo.setPakage_name(app.packageName);
        return appInfo;
    }
}
