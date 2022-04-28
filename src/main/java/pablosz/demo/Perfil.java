package pablosz.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="perfil")
public class Perfil
{
	@Id
	@Column(name="id_perfil")
	private int idPerfil;
	
	@Column(name="nombre")
	private String nombre;

	@Column(name="email")
	private String email;

	
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public int getIdPerfil()
	{
		return idPerfil;
	}
	public void setIdPerfil(int idPerfil)
	{
		this.idPerfil=idPerfil;
	}
	public String getNombre()
	{
		return nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	
}
