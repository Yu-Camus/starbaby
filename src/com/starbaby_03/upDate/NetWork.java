package com.starbaby_03.upDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * 获取网络地址内容 
 * @author  umauser 
 */
public class NetWork
{
    public static String getContent(String url) throws ClientProtocolException, IOException
    {
        StringBuilder sb=new StringBuilder();
        HttpClient client=new DefaultHttpClient();
        HttpParams httpParams=client.getParams();
       //设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpResponse response = client.execute(new HttpGet(url));
        HttpEntity entity=response.getEntity();
        if(entity!=null)
        {
            BufferedReader reader=new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"),8192);
            String line=null;
            while((line=reader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            reader.close();
            Log.e("info","4");
        }
        return sb.toString();
    }
}
