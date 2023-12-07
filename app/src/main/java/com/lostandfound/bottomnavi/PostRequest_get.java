package com.lostandfound.bottomnavi;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostRequest_get extends StringRequest {
    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://hs1288.dothome.co.kr/PostUpload_GET.php";
    private Map<String, String> map;

    public PostRequest_get(String userID, String category, String title, String Ddate, String firstLocation, String detailLocation, String storage, String detailContent, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("category", category);
        map.put("title", title);
        map.put("Ddate", Ddate);
        map.put("firstLocation", firstLocation);
        map.put("detailLocation", detailLocation);
        map.put("storage", storage);
        map.put("detailContent", detailContent);

    }
    @Override
    protected  Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
