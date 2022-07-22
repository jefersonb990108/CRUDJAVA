package edu.unicauca.apliweb.persistences.entities;

import edu.unicauca.apliweb.persistences.entities.Estudiante;
import edu.unicauca.apliweb.persistences.entities.Parcial;
import edu.unicauca.apliweb.persistences.entities.Profesor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2022-07-19T17:50:53")
@StaticMetamodel(Curso.class)
public class Curso_ { 

    public static volatile ListAttribute<Curso, Estudiante> estudianteList;
    public static volatile SingularAttribute<Curso, Integer> idCurso;
    public static volatile ListAttribute<Curso, Parcial> parcialList;
    public static volatile SingularAttribute<Curso, Integer> creditos;
    public static volatile ListAttribute<Curso, Profesor> profesorList;
    public static volatile SingularAttribute<Curso, String> nombre;

}