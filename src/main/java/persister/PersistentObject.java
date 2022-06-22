package persister;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import persister.core.domain.Clazz;
import persister.core.domain.ObjectField;
import persister.core.domain.PersistableObject;
import persister.core.domain.Session;
import persister.core.repository.ClazzRepository;
import persister.core.repository.SessionRepository;
import persister.exception.StructureChangedException;

@Service
public class PersistentObject implements IPersistentObject{

	private ObjectParser objectParser = new ObjectParser();
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	ClazzRepository clazzRepo;
	
	private Predicate<PersistableObject> isOfClass(String clazzName){
		return obj -> obj.getClazzId().getName().equalsIgnoreCase(clazzName);
	}
	
	@Override
	public boolean store(long sId, Object o) {
		// TODO Auto-generated method stub
		/*
		* 1. Existe sesion?
		* 	1-1-a. Actualizo timestamp
		* else
		* 	1-2-a. La creo con nuevo timestamp
		* 2. Obtener nombre de la clase de "o"
		* 3. Obtener ID de la clase
		* 4. Es una clase nueva? (no existe ID. Si no existe la clase, entonces el objeto tampoco)
		* 	4-1-a. Obtener Clazz de "o"
		* 	4-1-b. Storear la clase y obtener ID
		*  Preguntar al profe. Si existe clase hay que validarla como con load? Sino va a haber dos clases distintas en la base con mismo nombre
		* 		Dijo "actualizar clase, borrar todas las instancias y volverlas a guardar"
		* 		Con volverlas a guardar no se si se refiere a tener que volverlas a cargar por parte del usuario
		* 		o tenemos que "actualizarlas" nosotros, en este ultimo caso, que hacemos con las diferencias?
		* 5. Obtener PersistentObject de "o" <- la clase pasada ya tiene que estar validada y con los ids correspondientes
		* 6. Obtener objeto para esa clase y esa sesion
		* 7. Existe objeto?
		* 	7-1-a. Hacer update de ese objectId usando PersistentObject obtenido en 4
		* 	7-1-b. Devolver true
		* else
		* 	7-2-a. Crear nuevo PersistentObject obtenido en 4
		* 	7-2-b. Devolver false
		*/
		return false;
	}

	@Override
	public <T> T load(long sId, Class<T> clazz) throws StructureChangedException {
		// TODO Auto-generated method stub
		/*
			0. Obtengo nombre de clase
			1. Busco clase por nombre, existe?
				1-1-a. Obtengo Clazz de clazz y comparo para ver si son iguales, si son distintas tiro error StrutureChangedException
			else
				1-2-a. No se puede hacer load porque no existe clase, por ende tampoco objeto <-- Ver que hacer
			2. Existe objeto con esa clase y esa sesion?
				2-1-a. Parseo de PersistentObject al tipo dado
				2-1-b. Actualizo timestamp
				2-1-c. Retorno objeto
			else
				2-2-a. Objeto no encontrado
		 */
		String className = clazz.getName();
		Optional<Session> sessionOpt = sessionRepo.findById(sId);
		if(sessionOpt.isPresent()) {
			//Obtengo la sesion
			Session session = sessionOpt.get();
			List<PersistableObject> objectsFromSession = findObjectByClassName(session.getPersistableObject(), className); //Verifico que tenga un objeto con la class
			if(objectsFromSession.isEmpty())
				return null; //no tiene objeto, return null
			PersistableObject obj = objectsFromSession.get(0);
			if(hasStructureChanged(clazz, obj.getClazzId()))
				throw new StructureChangedException("Structure of class " + clazz.getName() + "has changed.");
			T object = rebuildObject(clazz, obj);
			session.setLast_access(Instant.now().getEpochSecond());
			return object;
		}
		
		
		return null;
	}
	
	private <T> T rebuildObject(Class<T> c, PersistableObject obj) {
		T recon = null;
		try {
			recon = c.newInstance();
			for(Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				ObjectField objField = obj.getObjectFields().stream().filter(field -> field.getFieldId().getName().equals(f.getName())).collect(Collectors.toList()).get(0);
				
			}
			
		} catch (InstantiationException e) {} catch (IllegalAccessException e) {}
		
		return recon;
	}
	
	private boolean hasStructureChanged(Class classJ, Clazz classDb) {
		
		return false;
	}
	
	private boolean isPrimitive(Object obj) {
		return obj.getClass().isPrimitive()
				|| obj instanceof String
                || obj instanceof Integer
                || obj instanceof Double
                || obj instanceof Boolean;
	}

	@Override
	public <T> boolean exists(long sId, Class<T> clazz) {
		
		Optional<Session> session = sessionRepo.findById(sId);
		if(session.isPresent()) {
			Session actual = session.get();
			actual.setLast_access(Instant.now().getEpochSecond());
			return !findObjectByClassName(actual
					.getPersistableObject(), clazz.getName())
					.isEmpty();
		}
		
		return false;
	}
	
	private List<PersistableObject> findObjectByClassName(List<PersistableObject> persistableList, String className){
		return persistableList.stream()
				.filter(isOfClass(className))
				.collect(Collectors.toList());
	}

	@Override
	public long elapsedTime(long sId) {
		Optional<Session> session = sessionRepo.findById(sId);
		if(session.isPresent())
			return Instant.now().getEpochSecond() - session.get().getLast_access();
		return -1; //Si no existe la sesion
	}

	@Override
	public <T> T delete(long sId, Class<T> clazz) {
		/*
			Llamar a load y catchear errores
			Si hay errores de clase corrupta o no existe objeto, que hacer?
			Si no hay error, buscar clase por nombre para obtener ID y eliminar el Persisted Object segun sId y classId
			Se puede hacer una funcion auxiliar que devuelva el objeto ya parseado y el classId, para reutilizar logica entre delete y store.
			Tambien puede tener un booleano si busca eliminar o leer
		 */
		T deletedObject = null;
		try {
			deletedObject = load(sId, clazz);
			if(sessionRepo.findById(sId).isPresent()) {
				Session session = sessionRepo.findById(sId).get();
				session.getPersistableObject().removeIf(isOfClass(clazz.getName()));
				session.setLast_access(Instant.now().getEpochSecond());
				sessionRepo.save(session);
			}
			return deletedObject;
			 
		} catch (StructureChangedException e) { }
		return deletedObject;
	}

}
