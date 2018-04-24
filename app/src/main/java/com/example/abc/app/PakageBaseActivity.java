package com.example.abc.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoming on 2015/9/17.
 */
class PakageBaseActivity extends BaseAdapter {
    private List<PakageInf> pakageinf = null;
    private Map<Integer, View> myView = new HashMap<Integer, View>();
    private Context context;

    public PakageBaseActivity(List<PakageInf> pakageinf, Context context) {
        this.pakageinf = pakageinf;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pakageinf.size();
    }

    @Override
    public Object getItem(int position) {
        return pakageinf.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View showview = myView.get(position);
        if (showview == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            showview = inflater.inflate(R.layout.listviewlayout, null);
            TextView pakage_nmae = (TextView) showview.findViewById(R.id.pkgname);
            TextView mainactivity = (TextView) showview.findViewById(R.id.mainactivity);
            ImageView imageView = (ImageView) showview.findViewById(R.id.pkgimgeview);
            final Button uninstall = (Button) showview.findViewById(R.id.uninstallapp);
            Button resetbutton = (Button) showview.findViewById(R.id.resetapp);

            final PakageInf inf = pakageinf.get(position);
            final String pakstring=inf.getPakage_name();
            pakage_nmae.setText(inf.getPakage_name());
            mainactivity.setText(inf.getMainactivity());
            imageView.setImageDrawable(inf.getAppicon());
            myView.put(position,showview);
            resetbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final  String string = inf.getPakage_name();
                            Log.d("HELLO", "test " + string);
                            Intent intent=new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + string));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }).start();
                }
            });

            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final  String string = inf.getPakage_name();
                    Log.d("HELLO", "test " + string);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + string));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }).start();
                }
            });
        }
        return showview;
    }

    public String exceadb(String str) {
        StringBuffer sb = new StringBuffer();
        Process process;
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec(str);
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {

                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


}

