package es.diaketroid;

import java.text.SimpleDateFormat;
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
import es.diaketroid.modelo.Socio;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CuotaActivity extends Activity {
	private EditText campoCuota;
	private EditText campoIntervalo;
	private Cuota cuota;
	private Context viewContext = this;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucuota);
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
	
	public void modificarCuota(View v){
		setContentView(R.layout.modificarcuota);
		
		campoCuota = (EditText) findViewById(R.id.campoCuota);
		campoIntervalo = (EditText) findViewById(R.id.campoIntervalo);
		
		ObtenerCuotaTask taskDatos = new ObtenerCuotaTask();
		taskDatos.execute();
	}
	
	public void guardarDatosCuota(View v){
		if(campoCuota.getText().toString()!="")
			cuota.setCantidad(Float.parseFloat(campoCuota.getText().toString()));
		if(campoCuota.getText().toString()!="")
			cuota.setIntervaloPagos(Integer.parseInt(campoIntervalo.getText().toString()));
		cuota.setFechaFin(new Date());
		
		GuardarDatosCuotasTask task = new GuardarDatosCuotasTask();
		task.execute();
	}
	
	public void habilitarCampos(View v){
		
		new AlertDialog.Builder(v.getContext())
		.setMessage(getString(R.string.mensajeCuota))
		.setCancelable(false)
		.setPositiveButton("Cerrar", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		}).show();
		
		findViewById(R.id.campoCuota).setFocusableInTouchMode(true);
		findViewById(R.id.campoIntervalo).setFocusableInTouchMode(true);
		findViewById(R.id.campoCuota).setFocusable(true);
		findViewById(R.id.campoIntervalo).setFocusable(true);
		Button btnguardar = (Button) findViewById(R.id.botonGuardarCuota);
		Button btnmod = (Button) findViewById(R.id.botonModificar);
		btnmod.setEnabled(false);
		btnguardar.setEnabled(true);
	}
	
	public void onBackPressed(){
		
		if(findViewById(R.id.layoutCuota)!=null)
			setContentView(R.layout.menucuota);
		else
			finish();
	}
	
	class ObtenerCuotaTask extends AsyncTask<Void,Void,String>{
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
	       		findViewById(R.id.botonModificar).setEnabled(false);
	       		findViewById(R.id.botonGuardarCuota).setEnabled(false);
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
	
	class GuardarDatosCuotasTask extends AsyncTask<Void,Void,String>{
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
					ObtenerCuotaTask task=new ObtenerCuotaTask();
					task.execute();
					Toast.makeText(getApplicationContext(), "Cuota modificada con éxito", Toast.LENGTH_LONG).show();
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
