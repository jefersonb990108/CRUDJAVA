package edu.unicauca.apliweb.persistences.entities;

import edu.unicauca.apliweb.persistences.entities.Parcial;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2022-07-19T17:50:53")
@StaticMetamodel(VariablesAmbientales.class)
public class VariablesAmbientales_ { 

    public static volatile SingularAttribute<VariablesAmbientales, Parcial> idParcialFk;
    public static volatile SingularAttribute<VariablesAmbientales, Integer> idMedida;
    public static volatile SingularAttribute<VariablesAmbientales, Float> humedad;
    public static volatile SingularAttribute<VariablesAmbientales, Float> temperatura;
    public static volatile SingularAttribute<VariablesAmbientales, Float> luminosidad;

}