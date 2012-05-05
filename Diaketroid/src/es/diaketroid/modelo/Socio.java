/**
 *
 * Representacion de los socios en el modelo
 * @author Jose A. Escobar
 *
 */
package es.diaketroid.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Socio {
	private String usuario;
	private int OID;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String codigoPostal;
	private String localidad;
	private String provincia;
	private String email;
	private String telefonoFijo;
	private String telefonoMovil;
	private String nacionalidad;
	private Date fechaNacimiento;
	private String sexo;
	private String password;
	
	public Socio(){}
	
	public Socio(JSONObject objeto){
		try {
			this.nombre=objeto.getString("Nombre");
			this.apellidos=objeto.getString("Apellidos");
			try {
				this.fechaNacimiento=new SimpleDateFormat("yyyy-MM-dd").parse(objeto.getString("FechaNacimiento"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.sexo=objeto.getString("Sexo");
			this.telefonoFijo=objeto.getString("TelefonoFijo");
			this.telefonoMovil=objeto.getString("TelefonoMovil");
			this.nacionalidad=objeto.getString("Nacionalidad");
			this.direccion=objeto.getString("Direccion");
			this.codigoPostal=objeto.getString("CP");
			this.localidad=objeto.getString("Localidad");
			this.provincia=objeto.getString("Provincia");
			this.email=objeto.getString("Email");
			this.usuario=objeto.getString("Usuario");
			this.password=objeto.getString("Contraseña");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public String getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public int getOID() {
		return OID;
	}
	public void setOID(int oID) {
		OID = oID;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public void setPassword(String md5) {
		this.password=md5;
		
	}

	public String getPassword() {
		return password;
	}
	

}
