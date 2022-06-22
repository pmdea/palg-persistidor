package persister.core.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.aspectj.apache.bcel.classfile.ClassParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import persister.ClazzParser;
import persister.CompareClass;
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
		private SessionRepository sessionRepository;

		@RequestMapping("/test2")
		public Object generarTest2() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, StructureChangedException {
			PersonaTest objTest = new PersonaTest();
			objTest.setDni(123123123);
			objTest.setNombre("test");

			Clazz clazz = getClazz(clazzRepo, objTest);


			return "Estamos validando clases";
		}

		private Clazz getClazz(ClazzRepository clazzRepo, Object obj) throws StructureChangedException {
			Clazz clazz = ClazzParser.toClazzFromObject(obj);
			int exists = CompareClass.existsInDB(clazzRepo, ClazzParser.toClazzFromObject(obj));
			if (exists == 0) {
				return clazzRepo.save(clazz);
			} else if (exists == 1) {
				String className = clazz.getName();
				return clazzRepo.findByName(className);
			} else {
				throw new StructureChangedException(PersonaTest.class.getName() + " is different from the one in DB");
			}
		}
		
		@RequestMapping("/test")
		public Object generarTest() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException
		{
			//Ejemplo de como me imagino que se pueden ir populando los campos del objeto utilizando los repositories
			Session session = new Session();
			//Utilizando clase PersonaTest como ejemplo:
			PersonaTest objTest = new PersonaTest();
			objTest.setDni(123123123);
			objTest.setNombre("test");
			List<Field> fieldsPersonaTest = new ArrayList<Field>();
			
			Class personaClass = objTest.getClass(); //Obtengo los fields de PersonaTest
			for (java.lang.reflect.Field field : personaClass.getDeclaredFields()) {
				Field f = new Field();
				f.setName(field.getName());
				FieldType fType = new FieldType();
				fType.setName(field.getType().getName());
				f.setType(fType);
				fieldsPersonaTest.add(f);
			}
			
			Clazz newClass = new Clazz();
			newClass.setName(personaClass.getName());
			newClass.setFieldsParents(fieldsPersonaTest);
			clazzRepo.save(newClass); //Con esto, con el arbol de objetos populado correctamente, ya guardo todo correctamente.
			//Con la clase y sus campos guardados, paso a popular el objeto e impactarlo en la base.
			Clazz clazzGuardada = clazzRepo.findByName("persister.PersonaTest"); //Obtengo la clase guardada de la base
			PersistableObject pObject = new PersistableObject();
			pObject.setSessionId(session);
			pObject.setClazzId(clazzGuardada);
			List<Method> methodList = Arrays.asList(personaClass.getMethods()).stream().filter(method -> method.getName().contains("get")).collect(Collectors.toList());
			//Genero los objectFields
			List<ObjectField> objectFields = new ArrayList<ObjectField>();
			for(Method method: methodList) {
				ObjectField oField = new ObjectField();
				if(method.getName().contains("Class"))
					break;
				oField.setFieldId(fieldRepo.findByName(method.getName().replace("get", "").toLowerCase()));
				Object valueObj = method.invoke(objTest, null);
				oField.setValue(valueObj.toString());
				objectFields.add(oField);
			}
			pObject.setObjectFieldsParents(objectFields);

			ArrayList<PersistableObject> objects = new ArrayList<>();
			objects.add(pObject);
			session.setPersistableObject(objects);
			session.setLast_access(Instant.now().getEpochSecond()); //Timestamp como long en segundos
			sessionRepository.save(session);
			return session;
		}
	}
}
