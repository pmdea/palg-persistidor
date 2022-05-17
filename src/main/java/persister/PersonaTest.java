package persister;

import persister.annotations.NotPersistable;
import persister.annotations.Persistable;

public class PersonaTest {
    @Persistable
    private int dni;
    @NotPersistable
    private String nombre;

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


}
