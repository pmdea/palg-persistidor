--Drop tables
DROP TABLE ObjectField;
DROP TABLE PersistableObject;
DROP TABLE field;
DROP TABLE Clazz;
DROP TABLE sesion;
DROP TABLE FieldType;


--Create tables
CREATE TABLE sesion (
    id int PRIMARY KEY,
    last_access numeric,
);

CREATE TABLE Clazz(
	id int PRIMARY KEY,
	name varchar
);

CREATE TABLE field (
    id int PRIMARY KEY,
    clazzId int,
    typeid int,
    name varchar
);

CREATE TABLE FieldType (
    id int PRIMARY KEY,
    name varchar
);

CREATE TABLE ObjectField (
	id int PRIMARY KEY,
	objectId int,
	fieldId int,
	nestedObjectFieldId int,
	valueObjectId int,
	"value" varchar
);

CREATE TABLE PersistableObject(
	id int PRIMARY KEY,
	clazzId int,
	sessionId int
);

-- Foreign keys Field
ALTER TABLE field ADD FOREIGN KEY (clazzId) REFERENCES Clazz(id);
ALTER TABLE field ADD FOREIGN KEY (typeId) REFERENCES FieldType(id);

-- Foreign keys ObjectField 
ALTER TABLE ObjectField ADD FOREIGN KEY (objectId) REFERENCES PersistableObject(id);
ALTER TABLE ObjectField ADD FOREIGN KEY (fieldId) REFERENCES Field(id);
ALTER TABLE ObjectField ADD FOREIGN KEY (valueObjectId) REFERENCES PersistableObject(id);

-- Foreign keys PersistableObject 
ALTER TABLE PersistableObject ADD FOREIGN KEY (clazzId) REFERENCES Clazz(id);
ALTER TABLE PersistableObject ADD FOREIGN KEY (sessionId) REFERENCES Sesion(id);