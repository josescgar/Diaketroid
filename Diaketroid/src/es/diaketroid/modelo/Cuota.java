/**
 *
 * Representación de las cuotas en el modelo
 * @author Jose A. Escobar
 *
 */
package es.diaketroid.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;


public class Cuota {
	private float cantidad;
	private int intervaloPagos;
	private Date fechaInicio;
	private Date fechaFin;
	private Date ultimoPago;
	private int OID;
	
	public Cuota(JSONObject objeto){
		try {
			this.cantidad=Float.parseFloat(objeto.getString("Cantidad"));
			this.intervaloPagos=objeto.getInt("IntervalosPagos");
			this.OID=objeto.getInt("OID");
			try {
				this.fechaFin=new SimpleDateFormat("yyyy-MM-dd").parse(objeto.getString("FechaFin"));
				this.fechaInicio=new SimpleDateFormat("yyyy-MM-dd").parse(objeto.getString("FechaInicio"));
				this.ultimoPago=new SimpleDateFormat("yyyy-MM-dd").parse(objeto.getString("FechaUltimoPago"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public int getOID() {
		return OID;
	}



	public void setOID(int oID) {
		OID = oID;
	}



	public float getCantidad() {
		return cantidad;
	}
	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}
	public int getIntervaloPagos() {
		return intervaloPagos;
	}
	public void setIntervaloPagos(int intervaloPagos) {
		this.intervaloPagos = intervaloPagos;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Date getUltimoPago() {
		return ultimoPago;
	}
	public void setUltimoPago(Date ultimoPago) {
		this.ultimoPago = ultimoPago;
	}
	
}
