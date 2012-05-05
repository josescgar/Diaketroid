/**
 *
 * Gestión de los datos referentes al socio
 * @author Jose A. Escobar
 *
 */
package es.diaketroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.diaketroid.http.DriverHTTP;
import es.diaketroid.modelo.Socio;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class CuentaActivity extends Activity{
	
	private Socio socio;
	private Boolean habilitar;
	
	private Context viewContext = this;
	private int dia;
	private int mes;
	private int año;
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
	private EditText campoContraseña;
	private EditText campoTlfFijo;
	private EditText campoTlfMovil;
	private EditText campoNacionalidad;
	private Button guardar;
	private Button modificarDatos;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucuenta);
        habilitar=true;
        
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
        campoContraseña =(EditText) findViewById(R.id.campoPassword);
        modificarDatos = (Button) findViewById(R.id.botonModificarDatos);
        guardar = (Button) findViewById(R.id.botonGuardarDatos);
        campoTlfFijo = (EditText) findViewById(R.id.campoFijo);
        campoTlfMovil = (EditText) findViewById(R.id.campoMovil);
        campoNacionalidad = (EditText) findViewById(R.id.campoNacionalidad);
        
		ConsultarDatosCuentaTask tareaConsultar = new ConsultarDatosCuentaTask();
		tareaConsultar.execute();
    }
	
	public void modificarDatos(View v){
		
		campoNombre.setFocusableInTouchMode(habilitar);
		campoNombre.setFocusable(habilitar);
		campoTlfFijo.setFocusableInTouchMode(habilitar);
		campoTlfFijo.setFocusable(habilitar);
		campoTlfMovil.setFocusableInTouchMode(habilitar);
		campoTlfMovil.setFocusable(habilitar);
		campoNacionalidad.setFocusableInTouchMode(habilitar);
		campoNacionalidad.setFocusable(habilitar);
		campoApellidos.setFocusableInTouchMode(habilitar);
		campoApellidos.setFocusable(habilitar);
		campoMasculino.setEnabled(habilitar);
		campoFemenino.setEnabled(habilitar);
		campoDireccion.setFocusableInTouchMode(habilitar);
		campoDireccion.setFocusable(habilitar);
		campoCP.setFocusableInTouchMode(habilitar);
		campoCP.setFocusable(habilitar);		
		campoLocalidad.setFocusableInTouchMode(habilitar);
		campoLocalidad.setFocusable(habilitar);		
		campoProvincia.setFocusableInTouchMode(habilitar);
		campoProvincia.setFocusable(habilitar);		
		campoEmail.setFocusableInTouchMode(habilitar);
		campoEmail.setFocusable(habilitar);		
		campoUsuario.setFocusableInTouchMode(habilitar);
		campoUsuario.setFocusable(habilitar);
		campoContraseña.setFocusableInTouchMode(habilitar);
		campoContraseña.setFocusable(habilitar);
		
		modificarDatos.setEnabled(false);
		guardar.setEnabled(true);
		
		if(habilitar)
			campoFechaNacimiento.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					new DatePickerDialog(v.getContext(), listenerFecha, año, mes, dia).show();
					
				}
			});
		else
			campoFechaNacimiento.setOnClickListener(null);
		
		habilitar=true;
	}
	
	public void guardar(View v){
		if(!campoNombre.getText().toString().equals(socio.getNombre()) && !campoNombre.getText().toString().equals(""))
			socio.setNombre(campoNombre.getText().toString());
		
		if(!campoApellidos.getText().toString().equals(socio.getApellidos()) && !campoApellidos.getText().toString().equals(""))
			socio.setApellidos(campoApellidos.getText().toString());
		
		Date auxfecha=null;
		try {
			auxfecha = new SimpleDateFormat("dd-MM-yyyy").parse(campoFechaNacimiento.getText().toString());
			if(!auxfecha.equals(socio.getFechaNacimiento()))
				socio.setFechaNacimiento(auxfecha);
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Error de formato de fecha: dd/mm/aaaa",Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		if(campoMasculino.isChecked())
			socio.setSexo("M");
		else
			socio.setSexo("F");
		
		if(!campoDireccion.getText().toString().equals(socio.getDireccion()) && !campoDireccion.getText().toString().equals(""))
			socio.setDireccion(campoDireccion.getText().toString());
		
		if(!campoCP.getText().toString().equals(socio.getCodigoPostal()) && !campoCP.getText().toString().equals(""))
			socio.setCodigoPostal(campoCP.getText().toString());
		
		if(!campoLocalidad.getText().toString().equals(socio.getLocalidad()) && !campoLocalidad.getText().toString().equals(""))
			socio.setLocalidad(campoLocalidad.getText().toString());
		
		if(!campoTlfFijo.getText().toString().equals(socio.getTelefonoFijo()) && !campoTlfFijo.getText().toString().equals(""))
			socio.setTelefonoFijo(campoTlfFijo.getText().toString());
		
		if(!campoTlfMovil.getText().toString().equals(socio.getTelefonoMovil()) && !campoTlfMovil.getText().toString().equals(""))
			socio.setTelefonoMovil(campoTlfMovil.getText().toString());
		
		if(!campoNacionalidad.getText().toString().equals(socio.getNacionalidad()) && !campoNacionalidad.getText().toString().equals(""))
			socio.setNacionalidad(campoNacionalidad.getText().toString());
		
		
		if(!campoProvincia.getText().toString().equals(socio.getProvincia()) && !campoProvincia.getText().toString().equals(""))
			socio.setProvincia(campoProvincia.getText().toString());
		
		if(!campoEmail.getText().toString().equals(socio.getEmail()) && !campoEmail.getText().toString().equals(""))
			socio.setEmail(campoEmail.getText().toString());
		
		if(!campoUsuario.getText().toString().equals(socio.getUsuario()) && !campoUsuario.getText().toString().equals(""))
			socio.setUsuario(campoUsuario.getText().toString());
		
		if(!campoLocalidad.getText().toString().equals(socio.getLocalidad()) && !campoLocalidad.getText().toString().equals(""))
			socio.setLocalidad(campoLocalidad.getText().toString());
		
		if(!campoContraseña.getText().toString().equals(""))
			socio.setPassword(md5(campoContraseña.getText().toString()));
		
		GuardarDatosCuentaTask taskGuardar = new GuardarDatosCuentaTask();
		taskGuardar.execute();
		
	}
	
	private DatePickerDialog.OnDateSetListener listenerFecha =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                	año = year;
                	mes = monthOfYear;
                	dia = dayOfMonth;     
                    campoFechaNacimiento.setText(new StringBuilder().append(dia+"-").append(mes+1+"-").append(año));
                }
            };
	
	class ConsultarDatosCuentaTask extends AsyncTask<Void,Void,String>{
    	
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
					socio = new Socio(obj);
					actualizarCampos();
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
    	
    	private void actualizarCampos() {

			campoNombre.setText(socio.getNombre());
			campoApellidos.setText(socio.getApellidos());
			SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
			campoFechaNacimiento.setText(fecha.format(socio.getFechaNacimiento()));
			dia = fecha.getCalendar().get(Calendar.DAY_OF_MONTH);
			mes = fecha.getCalendar().get(Calendar.MONTH);
			año = fecha.getCalendar().get(Calendar.YEAR);
			
			
			if(socio.getSexo().equals("M"))
				campoMasculino.setChecked(true);
			else
				campoFemenino.setChecked(true);
			
			campoDireccion.setText(socio.getDireccion());
			campoTlfFijo.setText(socio.getTelefonoFijo());
			campoTlfMovil.setText(socio.getTelefonoMovil());
			campoNacionalidad.setText(socio.getNacionalidad());
			campoCP.setText(socio.getCodigoPostal());
			campoLocalidad.setText(socio.getLocalidad());
			campoProvincia.setText(socio.getProvincia());
			campoEmail.setText(socio.getEmail());
			campoUsuario.setText(socio.getUsuario());
		}
	}
	
	class GuardarDatosCuentaTask extends AsyncTask<Void,Void,String>{
    	
    	private ProgressDialog cargando;
    	
    	 @Override
        protected void onPreExecute() {
           cargando = new ProgressDialog(viewContext);
           cargando.setMessage("Guardando datos. Espere por favor...");
           cargando.setCancelable(false);
           cargando.show();
        }
    	
    	@Override
		protected String doInBackground(Void... params) {
    		ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
    		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    		String datos = gson.toJson(socio, Socio.class);
    		parametros.add(new BasicNameValuePair("tarea", "modificar"));
    		parametros.add(new BasicNameValuePair("datos", datos));
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
					Toast.makeText(viewContext, "Datos modificados correctamente", Toast.LENGTH_LONG).show();
					habilitar=false;
					modificarDatos(null);
					guardar.setEnabled(false);
					modificarDatos.setEnabled(true);
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
