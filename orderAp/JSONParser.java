package com.application.orderAp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.util.Log;
 
public class JSONParser extends Activity{
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    boolean exeptionThrown = false;
 
    // constructor
   public JSONParser() {
 
    }
 
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
            List<NameValuePair> params){
 
    	
    	
        // Making HTTP request
        try {
 
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
 
            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
 
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }          
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("Exep", "UnsupportedEncodingException");
            exeptionThrown = true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.d("Exep", "ClientProtocolException");
            exeptionThrown = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Exep", "IOException");
            exeptionThrown = true;
        } catch (Exception e){
        	exeptionThrown = true;
        	Log.d("Exep", "Exception");
            e.printStackTrace();
        }
 if(exeptionThrown == false){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            
            Log.d("JSONParser/makeHttpRequest/json(data from BufferedReader)", json.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("JSONParser/makeHttpRequest/jObj", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            
            
        	}
 		}else{
 			jObj = null;
 		}
        // return JSON String
        return jObj;
    }
    
}