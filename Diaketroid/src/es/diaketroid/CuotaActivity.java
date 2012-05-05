/**
 *
 * Gestión de cuotas del socio
 * @author Jose A. Escobar
 *
 */
package es.diaketroid;

import java.util.ArrayList;
import java.util.Date;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.diaketroid.http.DriverHTTP;
import es.diaketroid.modelo.Cuota;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CuotaActivity extends Activity {
	private EditText campoCuota;
	private EditText campoIntervalo;
	private Cuota cuota;
	private Context viewContext = this;
	private Button btnguardar;
	private Button btnmod;
	private Button btncancelar;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarcuota);
        
        campoCuota = (EditText) findViewById(R.id.campoCuota);
		campoIntervalo = (EditText) findViewById(R.id.campoIntervalo);
		btnguardar = (Button) findViewById(R.id.botonGuardarCuota);
		btnmod = (Button) findViewById(R.id.botonModificar);
		btncancelar = (Button) findViewById(R.id.botonCancelarCuota);
		
		ConsultarDatosCuotaTask taskDatos = new ConsultarDatosCuotaTask();
		taskDatos.execute();
    }
	
	
	public void cancelarCuota(View v){
		new AlertDialog.Builder(v.getContext())
		.setMessage("¿Está seguro que desea cancelar su cuota?")
		.setNegativeButton("No", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {

			} })
		.setPositiveButton("Si", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				CancelarCuotaTask task = new CancelarCuotaTask();
				task.execute();
			}
		}).show();
	}
	
	
	public void guardarCuota(View v){
		if(campoCuota.getText().toString()!="")
			cuota.setCantidad(Float.parseFloat(campoCuota.getText().toString()));
		if(campoCuota.getText().toString()!="")
			cuota.setIntervaloPagos(Integer.parseInt(campoIntervalo.getText().toString()));
		cuota.setFechaFin(new Date());
		
		GuardarDatosCuotaTask task = new GuardarDatosCuotaTask();
		task.execute();
	}
	
	public void modificarCuota(View v){
		
		new AlertDialog.Builder(v.getContext())
		.setMessage(getString(R.string.mensajeCuota))
		.setCancelable(false)
		.setPositiveButton("Cerrar", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		}).show();
		
		campoCuota.setFocusableInTouchMode(true);
		campoIntervalo.setFocusableInTouchMode(true);
		campoCuota.setFocusable(true);
		campoIntervalo.setFocusable(true);
		
		btnmod.setEnabled(false);
		btnguardar.setEnabled(true);
	}
	
	
	class ConsultarDatosCuotaTask extends AsyncTask<Void,Void,String>{
		private ProgressDialog cargando;
    	
   	 @Override
       protected void onPreExecute() {
          cargando = new ProgressDialog(viewContext);
          cargando.setMessage("Cargando. Espere por favor...");
          cargando.setCancelable(false);
          cargando.show();
       }
   	
   	@Override
		protected String doInBackground(Void... params) {
   		ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
   		parametros.add(new BasicNameValuePair("tarea", "consultar"));
			String respuesta = DriverHTTP.doPost(DriverHTTP.CUOTAS_URL, parametros, getApplicationContext());
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
					cuota = new Cuota(obj);
					actualizarCampos();
				} else if (obj!=null && obj.getString("estado").equals("error")) {
					error=obj.getString("msg");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	       	
	       	
	       	if(error!=null){
	       		btnmod.setEnabled(false);
	       		btnguardar.setEnabled(false);
	       		btncancelar.setEnabled(false);
	       		new AlertDialog.Builder(viewContext)
				.setMessage(error)
				.setPositiveButton("Cerrar", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).show();
	       	}
       }

		private void actualizarCampos() {
			campoCuota.setText(Float.toString(cuota.getCantidad()));
			campoIntervalo.setText(Integer.toString(cuota.getIntervaloPagos()));
		}
	}
	
	class GuardarDatosCuotaTask extends AsyncTask<Void,Void,String>{
		private ProgressDialog cargando;
    	
   	 @Override
       protected void onPreExecute() {
          cargando = new ProgressDialog(viewContext);
          cargando.setMessage("Cargando. Espere por favor...");
          cargando.setCancelable(false);
          cargando.show();
       }
   	
   	@Override
		protected String doInBackground(Void... params) {
	   		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String datos = gson.toJson(cuota, Cuota.class);
	   		ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
	   		parametros.add(new BasicNameValuePair("tarea", "modificar"));
	   		parametros.add(new BasicNameValuePair("datos", datos));
			String respuesta = DriverHTTP.doPost(DriverHTTP.CUOTAS_URL, parametros, getApplicationContext());
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
					ConsultarDatosCuotaTask task=new ConsultarDatosCuotaTask();
					task.execute();
					Toast.makeText(getApplicationContext(), "Cuota modificada con éxito", Toast.LENGTH_LONG).show();
					btnguardar.setEnabled(false);
					btnmod.setEnabled(true);
					campoCuota.setFocusableInTouchMode(false);
					campoCuota.setFocusable(false);
					campoIntervalo.setFocusableInTouchMode(false);
					campoIntervalo.setFocusable(false);
					
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
	
	class CancelarCuotaTask extends AsyncTask<Void,Void,String>{
		private ProgressDialog cargando;
    	
   	 @Override
       protected void onPreExecute() {
          cargando = new ProgressDialog(viewContext);
          cargando.setMessage("Cargando. Espere por favor...");
          cargando.setCancelable(false);
          cargando.show();
       }
   	
   	@Override
		protected String doInBackground(Void... params) {
	   		ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
	   		parametros.add(new BasicNameValuePair("tarea", "cancelar"));
			String respuesta = DriverHTTP.doPost(DriverHTTP.CUOTAS_URL, parametros, getApplicationContext());
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
					Toast.makeText(getApplicationContext(), "Cuota cancelada con éxito", Toast.LENGTH_LONG).show();
					campoCuota.setText(null);
					campoIntervalo.setText(null);
					btnmod.setEnabled(false);
					btnguardar.setEnabled(false);
					btncancelar.setEnabled(false);
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
