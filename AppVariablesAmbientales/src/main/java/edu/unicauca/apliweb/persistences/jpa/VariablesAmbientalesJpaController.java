/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unicauca.apliweb.persistences.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.unicauca.apliweb.persistences.entities.Parcial;
import edu.unicauca.apliweb.persistences.entities.VariablesAmbientales;
import edu.unicauca.apliweb.persistences.jpa.exceptions.NonexistentEntityException;
import edu.unicauca.apliweb.persistences.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jefer
 */
public class VariablesAmbientalesJpaController implements Serializable {

    public VariablesAmbientalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariablesAmbientales variablesAmbientales) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parcial idParcialFk = variablesAmbientales.getIdParcialFk();
            if (idParcialFk != null) {
                idParcialFk = em.getReference(idParcialFk.getClass(), idParcialFk.getIdParcial());
                variablesAmbientales.setIdParcialFk(idParcialFk);
            }
            em.persist(variablesAmbientales);
            if (idParcialFk != null) {
                idParcialFk.getVariablesAmbientalesList().add(variablesAmbientales);
                idParcialFk = em.merge(idParcialFk);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVariablesAmbientales(variablesAmbientales.getIdMedida()) != null) {
                throw new PreexistingEntityException("VariablesAmbientales " + variablesAmbientales + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariablesAmbientales variablesAmbientales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariablesAmbientales persistentVariablesAmbientales = em.find(VariablesAmbientales.class, variablesAmbientales.getIdMedida());
            Parcial idParcialFkOld = persistentVariablesAmbientales.getIdParcialFk();
            Parcial idParcialFkNew = variablesAmbientales.getIdParcialFk();
            if (idParcialFkNew != null) {
                idParcialFkNew = em.getReference(idParcialFkNew.getClass(), idParcialFkNew.getIdParcial());
                variablesAmbientales.setIdParcialFk(idParcialFkNew);
            }
            variablesAmbientales = em.merge(variablesAmbientales);
            if (idParcialFkOld != null && !idParcialFkOld.equals(idParcialFkNew)) {
                idParcialFkOld.getVariablesAmbientalesList().remove(variablesAmbientales);
                idParcialFkOld = em.merge(idParcialFkOld);
            }
            if (idParcialFkNew != null && !idParcialFkNew.equals(idParcialFkOld)) {
                idParcialFkNew.getVariablesAmbientalesList().add(variablesAmbientales);
                idParcialFkNew = em.merge(idParcialFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variablesAmbientales.getIdMedida();
                if (findVariablesAmbientales(id) == null) {
                    throw new NonexistentEntityException("The variablesAmbientales with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariablesAmbientales variablesAmbientales;
            try {
                variablesAmbientales = em.getReference(VariablesAmbientales.class, id);
                variablesAmbientales.getIdMedida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variablesAmbientales with id " + id + " no longer exists.", enfe);
            }
            Parcial idParcialFk = variablesAmbientales.getIdParcialFk();
            if (idParcialFk != null) {
                idParcialFk.getVariablesAmbientalesList().remove(variablesAmbientales);
                idParcialFk = em.merge(idParcialFk);
            }
            em.remove(variablesAmbientales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariablesAmbientales> findVariablesAmbientalesEntities() {
        return findVariablesAmbientalesEntities(true, -1, -1);
    }

    public List<VariablesAmbientales> findVariablesAmbientalesEntities(int maxResults, int firstResult) {
        return findVariablesAmbientalesEntities(false, maxResults, firstResult);
    }

    private List<VariablesAmbientales> findVariablesAmbientalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariablesAmbientales.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public VariablesAmbientales findVariablesAmbientales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariablesAmbientales.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariablesAmbientalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariablesAmbientales> rt = cq.from(VariablesAmbientales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
