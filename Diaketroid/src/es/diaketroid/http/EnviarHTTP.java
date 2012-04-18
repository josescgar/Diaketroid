package es.diaketroid.http;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.diaketroid.http.cookies.PersistentCookieStore;

import android.content.Context;

/**
 * @author spiotrowski
 *
 */
public class EnviarHTTP {

	final static String SERVERURL_SENDGEO = "http://130.149.223.110/backend/webpage/android/locationProcessing.php";
	final static String SERVERURL_SENDMEDIA = "http://130.149.223.110/backend/webpage/android/mediaProcessing.php";
	final static String SERVERURL_SENDINFO = "http://130.149.223.110/backend/webpage/android/tripProcessing.php";

	/**
	 * @param url URL of the server processing service
	 * @param c	JSON object to send over to the server
	 * @param appContext application context that runs the SyncService
	 * @return HTTPResponse response from the server
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse doPost(String url, JSONObject c, Context appContext) throws ClientProtocolException, IOException
	{
	
	    PersistentCookieStore ckApp = new PersistentCookieStore(appContext); 
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
	    httpclient.setCookieStore(ckApp);
	    HttpPost request = new HttpPost(url);
	    request.setHeader("User-Agent", "Android");
	    HttpEntity entity;
	    HttpResponse response = null;

	    if(c != null){
		    List<NameValuePair> postData = new ArrayList<NameValuePair>();
			postData.add(new BasicNameValuePair("data", c.toString()));
			UrlEncodedFormEntity s = new UrlEncodedFormEntity(postData);
			postData.clear();
			postData = null;
		    s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			request.setEntity(s);
	
			entity = s;
			request.setEntity(entity);
		    
		    response = httpclient.execute(request);
	    }
	    return response;
	}


}
