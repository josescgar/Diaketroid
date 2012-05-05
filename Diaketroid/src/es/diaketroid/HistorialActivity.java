/**
 *
 * Gestión de historial de pagos y colaboraciones del socio
 * @author Jose A. Escobar
 *
 */
package es.diaketroid;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import es.diaketroid.http.DriverHTTP;
import es.diaketroid.modelo.Movimiento;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;

public class HistorialActivity extends ListActivity {
	
	private int iniAño;
    private int iniMes;
    private int iniDia;
    private int finAño;
    private int finMes;
    private int finDia;
    private EditText campoFechaIni;
    private EditText campoFechaFin;
    private Context viewContext = this;
    
    private ArrayList<HashMap<String,String>> listado;
    private String[] from=new String[] {"Tipo", "Cantidad", "Fecha"};
    private int[] to=new int[] {R.id.filaTipo,R.id.filaCantidad,R.id.filaFecha};

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial);
        
        campoFechaIni = (EditText)findViewById(R.id.campoFechaIni);
        campoFechaFin = (EditText)findViewById(R.id.campoFechaFin);
        
        final Calendar c = Calendar.getInstance();
        iniAño = c.get(Calendar.YEAR)-1;
        finAño = c.get(Calendar.YEAR);
        iniMes = c.get(Calendar.MONTH);
        finMes = c.get(Calendar.MONTH);
        iniDia = c.get(Calendar.DAY_OF_MONTH);
        finDia = c.get(Calendar.DAY_OF_MONTH);
        
        actualizarFecha();

    }
	
	 private DatePickerDialog.OnDateSetListener listenerIni =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    iniAño = year;
                    iniMes = monthOfYear;
                    iniDia = dayOfMonth;     
                    actualizarFecha();
                }
            };
            
    private DatePickerDialog.OnDateSetListener listenerFin =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                	finAño = year;
                	finMes = monthOfYear;
                	finDia = dayOfMonth;     
                    actualizarFecha();
                }
            };
	
	public void mostrarFecha(View v){
		if(v.getId()==R.id.campoFechaIni)
			new DatePickerDialog(this, listenerIni, iniAño, iniMes, iniDia).show();
		else
			new DatePickerDialog(this, listenerFin, finAño, finMes, finDia).show();
	}
	
	public void actualizarFecha(){
		campoFechaIni.setText(new StringBuilder().append(iniDia+"-").append(iniMes+1+"-").append(iniAño));
        campoFechaFin.setText(new StringBuilder().append(finDia+"-").append(finMes+1+"-").append(finAño));
	}
	
	public void consultarHistorial(View v){
		HistorialTask task = new HistorialTask();
		task.execute();
	}
	
	class HistorialTask extends AsyncTask<Void,Void,String>{
    	
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
    		
    		Date auxDateIni=null;
    		Date auxDateFin=null;
			try {
				auxDateIni = new SimpleDateFormat("dd-MM-yyyy").parse(campoFechaIni.getText().toString());
				auxDateFin = new SimpleDateFormat("dd-MM-yyyy").parse(campoFechaFin.getText().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		
    		parametros.add(new BasicNameValuePair("fechaIni", new SimpleDateFormat("yyyy-MM-dd").format(auxDateIni)));
    		parametros.add(new BasicNameValuePair("fechaFin", new SimpleDateFormat("yyyy-MM-dd").format(auxDateFin)));
			String respuesta = DriverHTTP.doPost(DriverHTTP.HISTORIAL_URL, parametros, getApplicationContext());
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
					JSONArray datos = obj.getJSONArray("datos");
					
					listado = new ArrayList<HashMap<String,String>>();
					for(int i=0;i<datos.length();i++){
						Movimiento mov = new Movimiento(datos.getJSONObject(i));
						HashMap<String,String> mapa = new HashMap<String,String>();
						mapa.put("Tipo", mov.getTipo());
						mapa.put("Cantidad", ""+mov.getImporte());
						mapa.put("Fecha", new SimpleDateFormat("dd-MM-yyyy").format(mov.getFecha()));
						listado.add(mapa);
					}
					
					SimpleAdapter listadoAdapter = new SimpleAdapter(getApplicationContext(), listado, R.layout.fila, from, to);
					setListAdapter(listadoAdapter);
					
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
