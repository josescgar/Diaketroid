/**
 *
 * Gestión de identificación del socio
 * @author Jose A. Escobar
 *
 */
package es.diaketroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import es.diaketroid.http.DriverHTTP;
import es.diaketroid.http.cookies.PersistentCookieStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Context viewContext=null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        PersistentCookieStore ck = new PersistentCookieStore(this);
		ck.clearExpired(new Date(System.currentTimeMillis()));
		
		for (Cookie cookie : ck.getCookies()) {
			if (cookie.getName().equals("hash")) {
				Intent intent = new Intent(this, PrincipalActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
    }
    
    public void identificarse(View v){
    	String username = ((EditText)findViewById(R.id.campoNombre)).getText().toString();
    	String password = ((EditText)findViewById(R.id.campoPassword)).getText().toString();
    	
    	if(!username.equals("") && !password.equals("")){
			viewContext = v.getContext();
	    	LoginTask task = new LoginTask();
	    	task.execute(username,password);
    	} else {
    		Toast.makeText(getApplicationContext(), "Debe especificar un nombre de usuario y una contraseña", Toast.LENGTH_SHORT).show();
    	}
    }
    
    class LoginTask extends AsyncTask<String,Void,String>{
    	
    	private ProgressDialog cargando;
    	
    	 @Override
        protected void onPreExecute() {
           cargando = new ProgressDialog(viewContext);
           cargando.setMessage("Cargando. Espere por favor...");
           cargando.show();
        }
    	
    	@Override
		protected String doInBackground(String... params) {
    		ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
    		parametros.add(new BasicNameValuePair("username", params[0]));
    		parametros.add(new BasicNameValuePair("password", md5(params[1])));
			String respuesta = DriverHTTP.doPost(DriverHTTP.LOGIN_URL, parametros, getApplicationContext());
			return respuesta;

		}
    	
    	@Override
        protected void onPostExecute(String result) {
    		cargando.dismiss();
    		String error=null;
    		
    		
    		JSONObject obj=null;
	       	try {
	       		obj = new JSONObject(result);
			} catch (JSONException e) {
				error=result;
			}
	       	
	       	
	       	
	       	try {
				if(obj!=null && obj.getString("estado").equals("OK")){
					Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
					startActivity(intent);
					finish();
				} else if (obj!=null && obj.getString("estado").equals("error")) {
					error=obj.getString("msg");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	       	
	       	if(error!=null){
	       		new AlertDialog.Builder(viewContext)
				.setMessage(error)
				.setPositiveButton("Cerrar", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).show();
	       	}
        }
    	
    	public String md5(String s) {
    		try {
    	        // Create MD5 Hash
    	        MessageDigest digest = java.security.MessageDigest
    	                .getInstance("MD5");
    	        digest.update(s.getBytes());
    	        byte messageDigest[] = digest.digest();
    	 
    	        // Create Hex String
    	        StringBuffer hexString = new StringBuffer();
    	        for (int i = 0; i < messageDigest.length; i++) {
    	            String h = Integer.toHexString(0xFF & messageDigest[i]);
    	            while (h.length() < 2)
    	                h = "0" + h;
    	            hexString.append(h);
    	        }
    	        return hexString.toString();
    	 
    	    } catch (NoSuchAlgorithmException e) {
    	        e.printStackTrace();
    	    }
    	    return "";
    	}
    	
    }
}