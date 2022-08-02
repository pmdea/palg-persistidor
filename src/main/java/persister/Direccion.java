package persister;

import persister.annotations.Persistable;

public class Direccion {

	@Persistable
	String calle;
	@Persistable
	int numero;
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Direccion(String calle, int numero) {
		super();
		this.calle = calle;
		this.numero = numero;
	}
	public Direccion() {}
	
}
