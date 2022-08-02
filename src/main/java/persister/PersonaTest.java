package persister;

import persister.annotations.NotPersistable;
import persister.annotations.Persistable;

import java.util.ArrayList;
import java.util.List;

public class PersonaTest {
    @Persistable
    private int dni;
    @NotPersistable
    private String nombre;
    @Persistable
    private String apellido;
    
    @Persistable
    private List<Direccion> direcciones;
    
    @Persistable
    private List<String> amigos;
    
    

    public PersonaTest() {
        this.amigos = new ArrayList<>();
    }
    
    

    public String getApellido() {
		return apellido;
	}
    
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public List<Direccion> getDirecciones() {
		return direcciones;
	}
	
	public void setDirecciones(List<Direccion> direcciones) {
		this.direcciones = direcciones;
	}



	public int getDni()
    {
        return dni;
    }
    public void setDni(int dni)
    {
        this.dni=dni;
    }
    public String getNombre()
    {
        return nombre;
    }
    public void setNombre(String nombre)
    {
        this.nombre=nombre;
    }


    public List<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<String> amigos) {
        this.amigos = amigos;
    }
}
