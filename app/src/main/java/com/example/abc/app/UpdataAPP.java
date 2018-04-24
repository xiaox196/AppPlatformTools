package com.example.abc.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by D444Q52 on 2015/10/12.
 */
public class UpdataAPP {


//{"updata":{"version":"1.5","description":"www.baidu.com","url":"www.baidu.com"}}

    public static  UpdateInfo parseURL(String string)
    {
        UpdateInfo updateInfo=new UpdateInfo();
        try {
            JSONObject jsonObject=new JSONObject(string).getJSONObject("update");
            String url=jsonObject.getString("url");
            updateInfo.setUrl(url);
            String description=jsonObject.getString("description");
            updateInfo.setDescription(description);
            String version=jsonObject.getString("version");
            updateInfo.setVersion(version);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  updateInfo;
    }




}
