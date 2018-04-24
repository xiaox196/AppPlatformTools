package com.example.abc.app;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.myapplication.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by D444Q52 on 2015/10/15.
 */
public class UpdateFrame extends Fragment {
    private  String myUrl="http://10.20.216.206/zentao/devices/find_phone.php";
    private  boolean isstop=false;
    private String debug="debug=1";
    private TextView showFindPhone=null;
    private  static  final  int updateui=1;
    private static  final  int stopui=2;
    private static  final  int showdialog=3;
    private static  final  int showprocess=4;
    private static  final  int hideprocess=5;
    private static  final  int newversion=6;
    private Button stopbutton=null;
    private String IMEI=null;
    private NotificationManager notificationManager;
    private  Notification.Builder builder;
    private UpdateInfo myinfo=null;
    private  int count=1000;
    private  boolean  isNeedUpdate;
    private  String url_update="http://10.20.216.206/zentao/devices/update/updateinfo.ini";
    private String download_url="http://172.18.193.16/update/test.apk";
    private ProgressDialog pBar;
    private String fileName="testtool.apk";
    private String filePath="app_tool";
    private  TextView showversion=null;
    private  Button update=null;
    private  Button help=null;
    private TextView tv=null;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==updateui)
            {
                showFindPhone.setText("服务端查找手机，请联系测试组");
            }else if(msg.what==stopui)
            {
                showFindPhone.setText("");
            }else  if(msg.what==showdialog)
            {
                showUpdateDialog(myinfo);
            }else  if(msg.what==showprocess)
            {
                pBar.show();
            }else  if(msg.what==hideprocess)
            {
                pBar.cancel();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InstallAPP();
            }else  if(msg.what==newversion)
            {
                showNew();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        tv = (TextView) view.findViewById(R.id.titleTv);
        showversion = (TextView) view.findViewById(R.id.showversion);
        update=(Button)view.findViewById(R.id.checkupdate);
        help=(Button)view.findViewById(R.id.help);
        FileUtils.ui=this;
        pBar = new ProgressDialog(this.getActivity());    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        tv.setText("应用信息");
        String versiontext = getVersion();
        showversion.setText("Version: " + versiontext);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"请发送邮件给： xiaoming@conew.com",Toast.LENGTH_LONG).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkupdate();
            }
        });
        return view;
    }


    private void showUpdateDialog(final  UpdateInfo updateInfo ) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //   builder.setIcon(R.drawable.icon);
        builder.setTitle("请升级APP版本");

        builder.setMessage(updateInfo.getDescription());
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = showprocess;
                        handler.sendMessage(message);
                        HttpDownloader httpDownloader = new HttpDownloader();
                        httpDownloader.downFile(updateInfo.getUrl(), filePath, fileName);
                    }
                }).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getActivity().getPackageName(), 0);
            Log.v("xiaoming", "当前版本：" + packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public  void showNotification(int mcount)
    {
        builder=new Notification.Builder(getActivity());
        notificationManager=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(getActivity(),MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("查找测试机");
        builder.setContentText("赶紧联系测试人员");
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("查找手机");
        Notification notification=builder.build();
        notificationManager.notify(1000, notification);
    }

    public void checkupdate()
    { new Thread(new Runnable() {
        @Override
        public void run() {
            String string = sendGet2(url_update, "utf-8");
            myinfo = UpdataAPP.parseURL(string);
            isNeedUpdate = isNeedUpdate(myinfo);
            Log.v("xiaoming", string);
            if (isNeedUpdate) {
                Message message=new Message();
                message.what=showdialog;
                handler.sendMessage(message);
            }else
            {
                Message message=new Message();
                message.what=newversion;
                handler.sendMessage(message);
            }
        }
    }).start();
    }

    public static String sendGet2(String url_str,String encode) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url_str);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            // 建立实际的连接
            connection.connect();
            if(connection.getResponseCode()==200)
            {
                // 定义 BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),encode));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public void updatePrecess(int max,int position)
    {
        pBar.setMax(max);
        pBar.setProgress(position);
        pBar.setCancelable(false);
        pBar.setCanceledOnTouchOutside(false);
    }

    public void showNew()
    { Toast.makeText(getActivity(), "已经是最新版本", Toast.LENGTH_LONG).show();}

    public void down()
    {
        Message message=new Message();
        message.what=hideprocess;
        handler.sendMessage(message);
    }




    private String getMyIMEI(){
        TelephonyManager mTelephonyMgr = (TelephonyManager)getActivity(). getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        Log.i("xiaoming", "imei=" + imei);
        return  imei;
    }

    private  boolean isNeedUpdate(UpdateInfo updateInfo)
    {
        String v=updateInfo.getVersion();
        Log.v("xiaoming", "服务器版本:" + v);
        if(v!=null)
        {
            if(v.equals(getVersion()))
            {
                return false;
            }
        }
        return  true;
    }




    public void InstallAPP()
    {
        Intent intent_InstallAPP=new Intent(Intent.ACTION_VIEW);
        File file=new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator+filePath,fileName);
        intent_InstallAPP.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent_InstallAPP);
    }
}