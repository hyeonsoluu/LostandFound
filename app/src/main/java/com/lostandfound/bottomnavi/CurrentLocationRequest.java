package com.lostandfound.bottomnavi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CurrentLocationRequest extends StringRequest {
    final static private String URL = "http://hs1288.dothome.co.kr/currentLocationUpload.php";
    private Map<String, String> map;

    public CurrentLocationRequest(String userID, String markerTitle, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("userID", userID);
        map.put("markerTitle", markerTitle);

    }
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
