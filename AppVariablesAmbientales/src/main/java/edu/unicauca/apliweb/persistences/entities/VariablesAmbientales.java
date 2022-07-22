/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unicauca.apliweb.persistences.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jefer
 */
@Entity
@Table(name = "variables_ambientales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariablesAmbientales.findAll", query = "SELECT v FROM VariablesAmbientales v"),
    @NamedQuery(name = "VariablesAmbientales.findByIdMedida", query = "SELECT v FROM VariablesAmbientales v WHERE v.idMedida = :idMedida"),
    @NamedQuery(name = "VariablesAmbientales.findByHumedad", query = "SELECT v FROM VariablesAmbientales v WHERE v.humedad = :humedad"),
    @NamedQuery(name = "VariablesAmbientales.findByTemperatura", query = "SELECT v FROM VariablesAmbientales v WHERE v.temperatura = :temperatura"),
    @NamedQuery(name = "VariablesAmbientales.findByLuminosidad", query = "SELECT v FROM VariablesAmbientales v WHERE v.luminosidad = :luminosidad")})
public class VariablesAmbientales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_medida")
    private Integer idMedida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "humedad")
    private float humedad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "temperatura")
    private float temperatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "luminosidad")
    private float luminosidad;
    @JoinColumn(name = "id_parcial_fk", referencedColumnName = "id_parcial")
    @ManyToOne(optional = false)
    private Parcial idParcialFk;

    public VariablesAmbientales() {
    }

    public VariablesAmbientales(Integer idMedida) {
        this.idMedida = idMedida;
    }

    public VariablesAmbientales(Integer idMedida, float humedad, float temperatura, float luminosidad) {
        this.idMedida = idMedida;
        this.humedad = humedad;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
    }

    public Integer getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(Integer idMedida) {
        this.idMedida = idMedida;
    }

    public float getHumedad() {
        return humedad;
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getLuminosidad() {
        return luminosidad;
    }

    public void setLuminosidad(float luminosidad) {
        this.luminosidad = luminosidad;
    }

    public Parcial getIdParcialFk() {
        return idParcialFk;
    }

    public void setIdParcialFk(Parcial idParcialFk) {
        this.idParcialFk = idParcialFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedida != null ? idMedida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariablesAmbientales)) {
            return false;
        }
        VariablesAmbientales other = (VariablesAmbientales) object;
        if ((this.idMedida == null && other.idMedida != null) || (this.idMedida != null && !this.idMedida.equals(other.idMedida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unicauca.apliweb.persistences.entities.VariablesAmbientales[ idMedida=" + idMedida + " ]";
    }
    
}
