package com.example.abc.app;

import android.app.ActivityManager;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

public class CallPhoneActivity extends Fragment {
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
    private Timer timer;
    private UpdateInfo myinfo=null;
    private  int count=1000;
    private  boolean  isNeedUpdate;
    private  String url_update="http://10.20.216.206/zentao/devices/update/updateinfo.ini";
    private String download_url="http://172.18.193.16/update/test.apk";
    private ProgressDialog pBar;
    private String fileName="testtool.apk";
    private String filePath="app_tool";
    private  TextView versiontev=null;
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
        View view=inflater.inflate(R.layout.fragment_findphone,container,false);
        showFindPhone=(TextView)view.findViewById(R.id.showfindphone);
        stopbutton=(Button)view.findViewById(R.id.stopbutton);
        tv=(TextView)view.findViewById(R.id.titleTv);

        tv.setText("查找手机");

        //  getMyUUID();
        IMEI="imei="+getMyIMEI();
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isstop = true;
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchPhoneService.class);
                intent.putExtra("isstop", isstop);
                intent.putExtra("stop", false);
                getActivity().startService(intent);
            }
        });



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    String data2=sendGet(myUrl, IMEI, "utf-8");
                    Log.v("xiaoming","data2"+data2);
                    try {
                        JSONObject jsonObject=new JSONObject(data2);
                        String result=jsonObject.getString("findstate");
                        Log.v("xiaoming","findstate::"+result);
                        if(result.equals("1"))
                        {
                            if(getActivity()!=null)
                            {
                                isstop=false;
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), SearchPhoneService.class);
                                intent.putExtra("isstop", isstop);
                                intent.putExtra("stop", true);
                                getActivity().startService(intent);
                                count++;
                                showNotification(count);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message=new Message();
                                        message.what=updateui;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                            }

                        }else {
                            if(getActivity()!=null)
                            {
                                isstop=true;
                                Intent intent=new Intent();
                                intent.setClass(getActivity(),SearchPhoneService.class);
                                intent.putExtra("isstop", isstop);
                                intent.putExtra("stop",false);
                                getActivity().startService(intent);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message=new Message();
                                        message.what=stopui;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.currentThread().sleep(30000);
                        Log.v("xiaoming", "当前时间："+System.currentTimeMillis()+" ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return view;
    }



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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("xiaoming", "destroy");
//        Intent intent=new Intent();
//        intent.setClass(getActivity(),SearchPhoneService.class);
//        getActivity().stopService(intent);
    }

    private String getMyUUID(){
        final TelephonyManager tm = (TelephonyManager) getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getActivity().getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.v("xiaoming", "uuid=" + uniqueId);
        return uniqueId;
    }


    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        Log.v("xiaoming", "httpUrl=" +httpUrl);

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.connect();
            if(connection.getResponseCode()==200)
            {
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                }
                reader.close();
                result = sbf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("xiaoming", "获取的数据" + "  " + result);
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url_str, String param_str,String encode) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url_str + "?" + param_str;
            Log.v("xiaoming", "urlNameString=" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
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

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the

        ActivityManager activityManager = (ActivityManager) getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getActivity().getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    private String getMyIMEI(){
        TelephonyManager mTelephonyMgr = (TelephonyManager)getActivity(). getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        Log.i("xiaoming", "imei=" + imei);
        return  imei;
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_update) {
         //   checkupdate();
        }
        return super.onOptionsItemSelected(item);
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

    public void updatePrecess(int max,int position)
    {
        pBar.setMax(max);
        pBar.setProgress(position);
        pBar.setCancelable(false);
        pBar.setCanceledOnTouchOutside(false);
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

    public void showNew()
    { Toast.makeText(getActivity(),"已经是最新版本",Toast.LENGTH_LONG).show();}

    public void down()
    {
        Message message=new Message();
        message.what=hideprocess;
        handler.sendMessage(message);
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



    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getActivity().getPackageName(), 0);
            Log.v("xiaoming","当前版本："+packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
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
