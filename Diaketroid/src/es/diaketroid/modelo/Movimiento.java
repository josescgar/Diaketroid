/**
 *
 * Representación de los movimientos en el modelo
 * @author Jose A. Escobar
 *
 */
package es.diaketroid.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class Movimiento {
	
	private String tipo;
	private Date fecha;
	private float importe;
	private String concepto;
	
	public Movimiento(JSONObject obj) {
		try {
			this.fecha=new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("Fecha"));
			this.tipo=obj.getString("Type");
			this.concepto=obj.getString("Concepto");
			this.importe=Float.parseFloat(obj.getString("Cantidad"));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public float getImporte() {
		return importe;
	}
	public void setImporte(float importe) {
		this.importe = importe;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	
}
