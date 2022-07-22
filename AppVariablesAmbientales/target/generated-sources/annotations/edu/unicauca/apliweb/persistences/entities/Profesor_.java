package edu.unicauca.apliweb.persistences.entities;

import edu.unicauca.apliweb.persistences.entities.Curso;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2022-07-19T17:50:53")
@StaticMetamodel(Profesor.class)
public class Profesor_ { 

    public static volatile SingularAttribute<Profesor, String> apellido;
    public static volatile SingularAttribute<Profesor, Integer> idDocente;
    public static volatile SingularAttribute<Profesor, String> nombre;
    public static volatile ListAttribute<Profesor, Curso> cursoList;

}