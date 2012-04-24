package es.diaketroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import es.diaketroid.http.DriverHTTP;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CuentaActivity extends Activity{
	
	private Context viewContext = this;
	private EditText campoNombre;
	private EditText campoApellidos;
	private RadioButton campoMasculino;
	private RadioButton campoFemenino;
	private EditText campoLocalidad;
	private EditText campoProvincia;
	private EditText campoEmail;
	private EditText campoUsuario;
	private EditText campoCP;
	private EditText campoDireccion;
	private EditText campoFechaNacimiento;
	private EditText campoPassword;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucuenta);
        
        campoNombre =(EditText) findViewById(R.id.campoNombre);
        campoApellidos = (EditText) findViewById(R.id.campoApellidos);
        campoFechaNacimiento = (EditText) findViewById(R.id.campoFechaNacimiento);
        campoMasculino = (RadioButton) findViewById(R.id.campoMasculino);
        campoFemenino = (RadioButton) findViewById(R.id.campoFemenino);
        campoDireccion = (EditText) findViewById(R.id.campoDireccion);
        campoCP = (EditText) findViewById(R.id.campoCP);
        campoLocalidad = (EditText) findViewById(R.id.campoLocalidad);
        campoProvincia = (EditText) findViewById(R.id.campoProvincia);
        campoEmail = (EditText) findViewById(R.id.campoEmail);
        campoUsuario =(EditText) findViewById(R.id.campoUsuario);
        campoPassword =(EditText) findViewById(R.id.campoPassword);
        
		ConsultarDatosTask tareaConsultar = new ConsultarDatosTask();
		tareaConsultar.execute();
    }
	
	public void habilitarCampos(View v){
		
		
		campoNombre.setFocusableInTouchMode(true);
		campoApellidos.setFocusableInTouchMode(true);
		campoFechaNacimiento.setFocusableInTouchMode(true);
		campoMasculino.setEnabled(true);
		campoFemenino.setEnabled(true);
		campoDireccion.setFocusableInTouchMode(true);
		campoCP.setFocusableInTouchMode(true);
		campoLocalidad.setFocusableInTouchMode(true);
		campoProvincia.setFocusableInTouchMode(true);
		campoEmail.setFocusableInTouchMode(true);
		campoUsuario.setFocusableInTouchMode(true);
		campoPassword.setFocusableInTouchMode(true);
		
		Button btnguardar = (Button) findViewById(R.id.botonGuardarDatos);
		Button btnmod = (Button) findViewById(R.id.botonModificarDatos);
		btnmod.setEnabled(false);
		btnguardar.setEnabled(true);
	}
	
class ConsultarDatosTask extends AsyncTask<Void,Void,String>{
    	
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
			String respuesta = DriverHTTP.doPost(DriverHTTP.SOCIO_URL, parametros, getApplicationContext());
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
					actualizarCampos(obj);
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
    	
    	private void actualizarCampos(JSONObject datos) {

			try {
				campoNombre.setText(datos.getString("Nombre"));
				campoApellidos.setText(datos.getString("Apellidos"));
				campoFechaNacimiento.setText(datos.getString("FechaNacimiento"));
				
				if(datos.getString("Sexo").equals("M"))
					campoMasculino.setChecked(true);
				else
					campoFemenino.setChecked(true);
				
				campoDireccion.setText(datos.getString("Direccion"));
				campoCP.setText(datos.getString("CP"));
				campoLocalidad.setText(datos.getString("Localidad"));
				campoProvincia.setText(datos.getString("Provincia"));
				campoEmail.setText(datos.getString("Email"));
				campoUsuario.setText(datos.getString("Usuario"));
				
			} catch (JSONException e) {
				e.printStackTrace();
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
