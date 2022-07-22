package edu.unicauca.apliweb.persistences.entities;

import edu.unicauca.apliweb.persistences.entities.Estudiante;
import edu.unicauca.apliweb.persistences.entities.Parcial;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2022-07-19T17:50:53")
@StaticMetamodel(Nota.class)
public class Nota_ { 

    public static volatile SingularAttribute<Nota, Parcial> idParcialFk;
    public static volatile SingularAttribute<Nota, Estudiante> idEstudianteFk;
    public static volatile SingularAttribute<Nota, Integer> idNota;
    public static volatile SingularAttribute<Nota, Float> nota;

}