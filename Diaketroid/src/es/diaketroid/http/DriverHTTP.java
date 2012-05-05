/**
 *
 * Driver para envio y recepcion de peticiones HTTP al servidor
 * @author Jose A. Escobar
 *
 */
package es.diaketroid.http;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import es.diaketroid.http.cookies.PersistentCookieStore;
import android.content.Context;
import android.util.Log;


public class DriverHTTP {

	public static final String LOGIN_URL = "http://diaketas.byethost13.com/ControladorLogin.php";
	public static final String LOGOUT_URL = "http://diaketas.byethost13.com/ControladorLogout.php";
	public static final String CUOTAS_URL = "http://diaketas.byethost13.com/ControladorCuota.php";
	public static final String SOCIO_URL = "http://diaketas.byethost13.com/ControladorSocio.php";
	public static final String HISTORIAL_URL = "http://diaketas.byethost13.com/ControladorHistorial.php";
	
	public static String doPost(String url, ArrayList<NameValuePair> parametros, Context appContext)
	{
	
	    PersistentCookieStore ckApp = new PersistentCookieStore(appContext); 
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    httpclient.setCookieStore(ckApp);
	    
	    HttpPost request = new HttpPost(url);
	    request.setHeader("User-Agent", "Android");
	    HttpResponse respuesta = null;
	    
	    if(parametros != null){
			UrlEncodedFormEntity s=null;
			try {
				s = new UrlEncodedFormEntity(parametros);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "Error en la codificación de los parámetros";
			}
		    s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			request.setEntity(s);
	    }
	    
	    String contenido=null;
	    try {
			respuesta = httpclient.execute(request);
			
			ResponseHandler<String> rh=new BasicResponseHandler();
			contenido = rh.handleResponse(respuesta);
			Log.d("DIAK",contenido);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error de conexión";
		}
	    
	    return contenido;
	}


}
