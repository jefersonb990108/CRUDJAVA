package edu.unicauca.apliweb.persistences.entities;

import edu.unicauca.apliweb.persistences.entities.Curso;
import edu.unicauca.apliweb.persistences.entities.Nota;
import edu.unicauca.apliweb.persistences.entities.VariablesAmbientales;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2022-07-19T17:50:53")
@StaticMetamodel(Parcial.class)
public class Parcial_ { 

    public static volatile ListAttribute<Parcial, VariablesAmbientales> variablesAmbientalesList;
    public static volatile SingularAttribute<Parcial, Integer> idVariableFk;
    public static volatile SingularAttribute<Parcial, Integer> idParcial;
    public static volatile SingularAttribute<Parcial, Integer> idNotaFk;
    public static volatile SingularAttribute<Parcial, String> titulo;
    public static volatile SingularAttribute<Parcial, Curso> idCursoFk;
    public static volatile ListAttribute<Parcial, Nota> notaList;

}