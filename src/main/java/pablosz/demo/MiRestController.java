package pablosz.demo;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiRestController
{
	@Autowired
	private EntityManager em;
	
	@RequestMapping("/nombre/{id}")
	public String obtenerNombre(@PathVariable int id)
	{
		Perfil p = em.find(Perfil.class,id);
		return p.getNombre();
	}
	
	@RequestMapping("/mails/{nom}")
	public List<String> obtenerNombre(@PathVariable String nom)
	{
		String hql="";
		hql+="SELECT p.email ";
		hql+="FROM Perfil p ";
		hql+="WHERE p.nombre =:x ";
		
		Query q = em.createQuery(hql);
		q.setParameter("x",nom);
		
		List<String> lst = q.getResultList();
		return lst;
	}

	
	@RequestMapping("/test")
	public String test()
	{
		return "Hola, "+new Date();
	}
	
	@RequestMapping("/persona")
	public Persona obtenerPersona()
	{
		Persona p = new Persona();
		p.setDni(123);
		p.setNombre("Pablo");
		return p;
	}	
}
