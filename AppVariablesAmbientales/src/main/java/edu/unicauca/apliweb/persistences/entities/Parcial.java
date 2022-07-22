/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unicauca.apliweb.persistences.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jefer
 */
@Entity
@Table(name = "parcial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parcial.findAll", query = "SELECT p FROM Parcial p"),
    @NamedQuery(name = "Parcial.findByIdParcial", query = "SELECT p FROM Parcial p WHERE p.idParcial = :idParcial"),
    @NamedQuery(name = "Parcial.findByTitulo", query = "SELECT p FROM Parcial p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Parcial.findByIdNotaFk", query = "SELECT p FROM Parcial p WHERE p.idNotaFk = :idNotaFk"),
    @NamedQuery(name = "Parcial.findByIdVariableFk", query = "SELECT p FROM Parcial p WHERE p.idVariableFk = :idVariableFk")})
public class Parcial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_parcial")
    private Integer idParcial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_nota_fk")
    private int idNotaFk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_variable_fk")
    private int idVariableFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idParcialFk")
    private List<VariablesAmbientales> variablesAmbientalesList;
    @JoinColumn(name = "id_curso_fk", referencedColumnName = "id_curso")
    @ManyToOne(optional = false)
    private Curso idCursoFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idParcialFk")
    private List<Nota> notaList;

    public Parcial() {
    }

    public Parcial(Integer idParcial) {
        this.idParcial = idParcial;
    }

    public Parcial(Integer idParcial, String titulo, int idNotaFk, int idVariableFk) {
        this.idParcial = idParcial;
        this.titulo = titulo;
        this.idNotaFk = idNotaFk;
        this.idVariableFk = idVariableFk;
    }

    public Integer getIdParcial() {
        return idParcial;
    }

    public void setIdParcial(Integer idParcial) {
        this.idParcial = idParcial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdNotaFk() {
        return idNotaFk;
    }

    public void setIdNotaFk(int idNotaFk) {
        this.idNotaFk = idNotaFk;
    }

    public int getIdVariableFk() {
        return idVariableFk;
    }

    public void setIdVariableFk(int idVariableFk) {
        this.idVariableFk = idVariableFk;
    }

    @XmlTransient
    public List<VariablesAmbientales> getVariablesAmbientalesList() {
        return variablesAmbientalesList;
    }

    public void setVariablesAmbientalesList(List<VariablesAmbientales> variablesAmbientalesList) {
        this.variablesAmbientalesList = variablesAmbientalesList;
    }

    public Curso getIdCursoFk() {
        return idCursoFk;
    }

    public void setIdCursoFk(Curso idCursoFk) {
        this.idCursoFk = idCursoFk;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParcial != null ? idParcial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parcial)) {
            return false;
        }
        Parcial other = (Parcial) object;
        if ((this.idParcial == null && other.idParcial != null) || (this.idParcial != null && !this.idParcial.equals(other.idParcial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unicauca.apliweb.persistences.entities.Parcial[ idParcial=" + idParcial + " ]";
    }
    
}
