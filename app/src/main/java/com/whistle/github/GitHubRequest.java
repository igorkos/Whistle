package com.whistle.github;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
 GitHub  Volley request

 Extension of Volley JsonRequest to handle Issue and Comment objects parsing

 */
public class GitHubRequest<T extends GitHubObject> extends JsonRequest<ArrayList<T>> {

    private Class<T> cls;

    public GitHubRequest(int method, String url, Class<T> cls, Response.Listener<ArrayList<T>> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
        this.cls = cls;
    }

    @Override
    protected Response<ArrayList<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            //Response json data
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            JSONArray array = new JSONArray(jsonString);
            ArrayList<T> objects = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    //Construct object
                    T elem = cls.newInstance();
                    //Call parsing from json
                    elem.parse(object);
                    objects.add(elem);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            return Response.success(
                    objects, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
