--Drop tables
DROP TABLE ObjectField;
DROP TABLE PersistableObject;
DROP TABLE field;
DROP TABLE Clazz;
DROP TABLE sesion;
DROP TABLE FieldType;


--Create tables
CREATE TABLE sesion (
    id int generated by default as identity (start with 1) PRIMARY KEY,
    last_access numeric,
);

CREATE TABLE Clazz(
	id int generated by default as identity (start with 1) PRIMARY KEY,
	name varchar(200)
);

CREATE TABLE field (
    id int generated by default as identity (start with 1) PRIMARY KEY,
    clazz_Id int,
    type_id int,
    name varchar(200)
);

CREATE TABLE FieldType (
    id int generated by default as identity (start with 1) PRIMARY KEY,
    name varchar(200)
);

CREATE TABLE ObjectField (
	id int generated by default as identity (start with 1) PRIMARY KEY,
	object_Id int,
	field_Id int,
	nested_Object_Type varchar(200),
	nested_Object_Field_Id int,
	value_Object_Id int,
	valor varchar(200)
);

CREATE TABLE PersistableObject(
	id int generated by default as identity (start with 1) PRIMARY KEY,
	clazz_Id int,
	session_Id int
);

-- Foreign keys Field
ALTER TABLE field ADD FOREIGN KEY (clazz_Id) REFERENCES Clazz(id);
ALTER TABLE field ADD FOREIGN KEY (type_Id) REFERENCES FieldType(id);

-- Foreign keys ObjectField 
ALTER TABLE ObjectField ADD FOREIGN KEY (object_Id) REFERENCES PersistableObject(id);
ALTER TABLE ObjectField ADD FOREIGN KEY (field_Id) REFERENCES Field(id);
ALTER TABLE ObjectField ADD FOREIGN KEY (value_Object_Id) REFERENCES PersistableObject(id);
ALTER TABLE ObjectField ADD FOREIGN KEY (nested_Object_Field_Id) REFERENCES ObjectField(id);

-- Foreign keys PersistableObject 
ALTER TABLE PersistableObject ADD FOREIGN KEY (clazz_Id) REFERENCES Clazz(id);
ALTER TABLE PersistableObject ADD FOREIGN KEY (session_Id) REFERENCES Sesion(id);