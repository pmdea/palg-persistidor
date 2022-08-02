package persister.core.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import persister.Direccion;
import persister.PersistentObject;
import persister.PersonaTest;
import persister.core.repository.ClazzRepository;
import persister.core.repository.FieldRepository;
import persister.core.repository.FieldTypeRepository;
import persister.core.repository.ObjectFieldRepository;
import persister.core.repository.PersistableObjectRepository;
import persister.core.repository.SessionRepository;
import persister.exception.StructureChangedException;

@SpringBootApplication
@ComponentScan(basePackages = {"persister.core.*"})
@EnableJpaRepositories("persister.core.repository")
@EntityScan("persister.core.domain")
public class DemoEntities {

	public static void main(String[] args) {
		
		SpringApplication.run(DemoEntities.class, args);
	}
	
	
	@RestController
	public class DemoEntitiesController{
		@Autowired
		private EntityManager em;
		@Autowired
		private ClazzRepository clazzRepo;
		@Autowired
		private FieldRepository fieldRepo;
		@Autowired
		private FieldTypeRepository fieldTypeRepo;
		@Autowired
		private ObjectFieldRepository objectFieldRepo;
		@Autowired
		private PersistableObjectRepository persistableObjectRepo;
		@Autowired
		private SessionRepository sessionRepo;

		@RequestMapping("/teststore") // Prueba el store
		public Object testStore1() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, StructureChangedException {
			PersonaTest objTest = new PersonaTest();
			objTest.setDni(123123123);
			objTest.setNombre("test");
			List<String> amigos = new ArrayList<>();
			amigos.add("jorge");
			objTest.setAmigos(amigos);
			List<Direccion> direcciones = new ArrayList<Direccion>();
			direcciones.add(new Direccion("rivadavia", 1));
			direcciones.add(new Direccion(null, 23));
			objTest.setDirecciones(direcciones);
			objTest.setApellido("Garcia");
			PersistentObject persistentObject = new PersistentObject(sessionRepo, clazzRepo, persistableObjectRepo, fieldTypeRepo, objectFieldRepo, fieldRepo);
			return persistentObject.store(1, objTest);
		}
		
		@RequestMapping("testload")
		public Object testLoad() throws StructureChangedException {
			PersistentObject persistentObject = new PersistentObject(sessionRepo, clazzRepo, persistableObjectRepo, fieldTypeRepo, objectFieldRepo, fieldRepo);

			PersonaTest recuperado = persistentObject.load(1, PersonaTest.class);
			return recuperado;
		}
		
		@RequestMapping("testExists")
		public Object testExists() {
			PersistentObject persistentObject = new PersistentObject(sessionRepo, clazzRepo, persistableObjectRepo, fieldTypeRepo, objectFieldRepo, fieldRepo);
			return persistentObject.exists(1, PersonaTest.class);
		}
		
		@RequestMapping("testelapsedtime")
		public Object testElapsedTime() {
			PersistentObject persistentObject = new PersistentObject(sessionRepo, clazzRepo, persistableObjectRepo, fieldTypeRepo, objectFieldRepo, fieldRepo);
			return persistentObject.elapsedTime(1);
		}
		
		@RequestMapping("delete")
		public Object testDelete() {
			PersistentObject persistentObject = new PersistentObject(sessionRepo, clazzRepo, persistableObjectRepo, fieldTypeRepo, objectFieldRepo, fieldRepo);
			return persistentObject.delete(1, PersonaTest.class);
		}
		
	}
}
