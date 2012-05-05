/**
 *
 * Gestión de pestañas de la interfaz y funcionaldiad de 
 * desidentificacion del socio
 * @author Jose A. Escobar
 *
 */
package es.diaketroid;

import org.json.JSONException;
import org.json.JSONObject;
import es.diaketroid.http.DriverHTTP;
import es.diaketroid.http.cookies.PersistentCookieStore;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class PrincipalActivity extends TabActivity {
	
	private static final int DESCONECTAR = 0;
	private Context viewContext = this;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menutabs);
		
		TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    Resources res = getResources();

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, CuotaActivity.class);
	    spec = tabHost.newTabSpec("cuota").setIndicator("Cuota",res.getDrawable(R.drawable.ic_menu_month)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, HistorialActivity.class);
	    spec = tabHost.newTabSpec("historial").setIndicator("Historial",res.getDrawable(R.drawable.ic_menu_archive)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, CuentaActivity.class);
	    spec = tabHost.newTabSpec("cuenta").setIndicator("Cuenta",res.getDrawable(R.drawable.ic_menu_contact)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, DESCONECTAR, 0, "Desconectar").setIcon(R.drawable.ic_menu_logout);
	    return true;
	}
	
	public boolean onOptionsItemSelected (MenuItem item){

		switch (item.getItemId()){
			case DESCONECTAR:
				LogoutTask task = new LogoutTask();
				task.execute();
				return true;
		}
		return false;
	}
	
	class LogoutTask extends AsyncTask<Void,Void,String>{
		private ProgressDialog cargando;
		
		 @Override
        protected void onPreExecute() {
           cargando = new ProgressDialog(viewContext);
           cargando.setMessage("Cargando. Espere por favor...");
           cargando.show();
        }
		
		@Override
		protected String doInBackground(Void... arg0) {
			return DriverHTTP.doPost(DriverHTTP.LOGOUT_URL, null, getApplicationContext());
		}
		
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
						PersistentCookieStore ck = new PersistentCookieStore(getApplicationContext());
						ck.clear();
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
	}
}
