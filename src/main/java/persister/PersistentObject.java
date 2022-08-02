package persister;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import persister.core.domain.Clazz;
import persister.core.domain.ObjectField;
import persister.core.domain.PersistableObject;
import persister.core.domain.Session;
import persister.core.repository.*;
import persister.exception.StructureChangedException;

@Service
public class PersistentObject implements IPersistentObject{

	private SessionRepository sessionRepo;
	private ClazzRepository clazzRepo;
	private PersistableObjectRepository persistableObjectRepo;
	private FieldTypeRepository fieldTypeRepo;
	private ObjectFieldRepository objectFieldRepo;
	private FieldRepository fieldRepo;

	public PersistentObject(SessionRepository sessionRepo, ClazzRepository clazzRepo, PersistableObjectRepository persistableObjectRepo, 
			FieldTypeRepository fieldTypeRepo, ObjectFieldRepository objectFieldRepo, FieldRepository fieldRepo) {
		this.sessionRepo = sessionRepo;
		this.clazzRepo = clazzRepo;
		this.persistableObjectRepo = persistableObjectRepo;
		this.fieldTypeRepo = fieldTypeRepo;
		this.objectFieldRepo = objectFieldRepo;
		this.fieldRepo = fieldRepo;
	}

	private Predicate<PersistableObject> isOfClass(String clazzName){
		return obj -> obj.getClazzId().getName().equalsIgnoreCase(clazzName);
	}
	
	@Override
	public boolean store(long sId, Object o) {
		Session currentSession = getCurrentSession(sId);
		Clazz currentClazz = null;
		try {
			currentClazz = getCurrentClazz(o);
		} catch (StructureChangedException e) {
			e.printStackTrace();
			return false;
		}
		PersistableObject currentPersistableObject = getCurrentPersistableObject(currentClazz, currentSession);
		PersistableObject updatedPersistableObject = new ObjectParser(objectFieldRepo, fieldRepo).toPersistable(currentPersistableObject, o, currentClazz, currentSession);
		persistableObjectRepo.save(updatedPersistableObject);
		return true;
	}

	private PersistableObject getCurrentPersistableObject(Clazz clazz, Session session) {
		Optional<PersistableObject> storedObject = persistableObjectRepo.findByClazzId_IdAndSessionId_Id(clazz.getId(), session.getId());
		if (storedObject.isPresent()) {
			return storedObject.get();
		}
		PersistableObject obj = new PersistableObject();
		obj.setClazzId(clazz);
		obj.setSessionId(session);
		return obj;
	}

	private Session getCurrentSession(long sId) {
		Optional<Session> storedSession = sessionRepo.findById(sId);
		if (storedSession.isPresent()) {
			Session s = storedSession.get();
			Date date = new Date();
			s.setLast_access(date.getTime());
			return sessionRepo.save(s);
		}
		Session newSession = new Session();
		Date date = new Date();
		newSession.setLast_access(date.getTime());
		newSession.setId(sId);
		return sessionRepo.save(newSession);
	}

	private Clazz getCurrentClazz(Object obj) throws StructureChangedException {
		Clazz clazz = ClazzParser.getInstance(fieldTypeRepo).toClazzFromObject(obj);
		int exists = CompareClass.existsInDB(clazzRepo, clazz);
		if (exists == 0) {
			return clazzRepo.save(clazz);
		} else if (exists == 1) {
			return clazzRepo.findByName(clazz.getName());
		}else {
			Clazz existingClazz = clazzRepo.findByName(ClazzParser.getInstance().getName(clazz));
			List<PersistableObject> objectsToDelete = persistableObjectRepo.findByClazzId(existingClazz.getId());
			persistableObjectRepo.deleteAll(objectsToDelete); //Borro los objetos
			//remover la clase
			clazzRepo.deleteById(existingClazz.getId());
			//guardar la clase nueva
			return clazzRepo.save(clazz);
		}

	}

	@Override
	public <T> T load(long sId, Class<T> clazz) throws StructureChangedException {
		String className = clazz.getName();
		Optional<Session> sessionOpt = sessionRepo.findById(sId);
		if(sessionOpt.isPresent()) {
			//Obtengo la sesion
			Session session = sessionOpt.get();
			List<PersistableObject> objectsFromSession = findObjectByClassName(session.getPersistableObject(), className); //Verifico que tenga un objeto con la class
			if(objectsFromSession.isEmpty())
				return null; //no tiene objeto, return null
			PersistableObject obj = objectsFromSession.get(0);
			T object;
			try {
				//Obtengo el HEADER - fila que indica la cabeza del objeto
				ObjectField header = obj.getObjectFields().get(0);
				object = rebuildObject(clazz, header);
			} catch (Exception e) {
				throw new StructureChangedException("Structure of class " + clazz.getName() + "has changed.");
			}
			session.setLast_access(Instant.now().getEpochSecond());
			sessionRepo.save(session);
			return object;
		}
		return null;
	}
	
	private <T> T rebuildObject(Class<T> c, ObjectField obj) throws Exception {
		//Obtengo la clase
		String className = c.getName();
		//Obtengo los atributos
		Field[] atributos = c.getDeclaredFields();
		//Hago una instancia de la clase
		T objeto = (T)Class.forName(className).newInstance();
		//Busco si tiene hijos
		List<ObjectField> childFields = objectFieldRepo.findByParentId(obj.getId());
		
		for(Field prop : atributos) {
			prop.setAccessible(true);
			String propName = prop.getName();
			Optional<ObjectField> optAtr = childFields.stream().filter(atributo -> atributo.getFieldId().getName().equals(propName)).findFirst();
			if(!optAtr.isPresent())
				continue;
			ObjectField atr = optAtr.get();
			if(Types.getInstance().isPrimitive(atr.getNombre())){ // no tiene hijos
				setObject(prop, atr.getValue(), objeto);
			}
			else {

				List<ObjectField> elementos = objectFieldRepo.findByParentId(atr.getId());

				Class clase = prop.getType();
				if (Types.getInstance().isObject(prop)){ // El hijo es un objeto
					prop.set(objeto, rebuildObject(clase, atr));
				}
				else if(Types.getInstance().isListObject(prop)) { //El hijo es lista de objetos
					List<T> listaObjetos = new ArrayList<T>();
					ParameterizedType listType = (ParameterizedType)prop.getGenericType();
			        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
					for(ObjectField e : elementos) {
						listaObjetos.add((T) rebuildObject(listClass, e));
					}
					prop.set(objeto, listaObjetos);
				}
				else if(Types.getInstance().isListPrimitive(prop)) { //El hijo es lista de primitivos
					List<T> listaObjetos = new ArrayList<T>();
					for(ObjectField e : elementos) {
						T type = (T) e.getValue();
						listaObjetos.add(type);
					}
					prop.set(objeto, listaObjetos);
				}
			}
		}
		
		return objeto;
	}
	
	private <T> void setObject(Field prop, String valueparam, T objeto) throws NumberFormatException, IllegalArgumentException, IllegalAccessException{
		switch (prop.getType().toString()) {
		case "int":
			prop.setInt(objeto, Integer.parseInt(valueparam));
			break;
		case "double":
			prop.setDouble(objeto, Double.parseDouble(valueparam));
			break;
		case "boolean":
			prop.setBoolean(objeto, Boolean.parseBoolean(valueparam));
			
			break;
		case "char":
			prop.setChar(objeto, valueparam.charAt(0));
			
			break;
		case "float":
			prop.setFloat(objeto, Float.parseFloat(valueparam));
			
			break;
		case "long":
			prop.setLong(objeto, Long.parseLong(valueparam));
			break;

		default:
			prop.set(objeto, valueparam);
			break;
		}
	}

	@Override
	public <T> boolean exists(long sId, Class<T> clazz) {
		
		Optional<Session> session = sessionRepo.findById(sId);
		if(session.isPresent()) {
			Session actual = session.get();
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
		return -1;
	}

	@Override
	public <T> T delete(long sId, Class<T> clazz) {
		T deletedObject = null;
		try {
			deletedObject = load(sId, clazz);
			if(sessionRepo.findById(sId).isPresent()) {
				Session session = sessionRepo.findById(sId).get();
				List<PersistableObject> objectsFromSession = findObjectByClassName(session.getPersistableObject(), clazz.getName()); //Verifico que tenga un objeto con la class
				persistableObjectRepo.delete(objectsFromSession.get(0));
				session.getPersistableObject().removeIf(isOfClass(clazz.getName()));
				session.setLast_access(Instant.now().getEpochSecond());
				sessionRepo.save(session);
			}
			return deletedObject;
			 
		} catch (StructureChangedException e) { }
		return deletedObject;
	}

}
