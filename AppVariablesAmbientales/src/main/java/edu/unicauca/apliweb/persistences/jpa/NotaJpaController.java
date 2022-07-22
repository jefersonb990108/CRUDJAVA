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
import edu.unicauca.apliweb.persistences.entities.Estudiante;
import edu.unicauca.apliweb.persistences.entities.Nota;
import edu.unicauca.apliweb.persistences.jpa.exceptions.NonexistentEntityException;
import edu.unicauca.apliweb.persistences.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jefer
 */
public class NotaJpaController implements Serializable {

    public NotaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nota nota) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parcial idParcialFk = nota.getIdParcialFk();
            if (idParcialFk != null) {
                idParcialFk = em.getReference(idParcialFk.getClass(), idParcialFk.getIdParcial());
                nota.setIdParcialFk(idParcialFk);
            }
            Estudiante idEstudianteFk = nota.getIdEstudianteFk();
            if (idEstudianteFk != null) {
                idEstudianteFk = em.getReference(idEstudianteFk.getClass(), idEstudianteFk.getIdEstudiante());
                nota.setIdEstudianteFk(idEstudianteFk);
            }
            em.persist(nota);
            if (idParcialFk != null) {
                idParcialFk.getNotaList().add(nota);
                idParcialFk = em.merge(idParcialFk);
            }
            if (idEstudianteFk != null) {
                idEstudianteFk.getNotaList().add(nota);
                idEstudianteFk = em.merge(idEstudianteFk);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNota(nota.getIdNota()) != null) {
                throw new PreexistingEntityException("Nota " + nota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nota nota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nota persistentNota = em.find(Nota.class, nota.getIdNota());
            Parcial idParcialFkOld = persistentNota.getIdParcialFk();
            Parcial idParcialFkNew = nota.getIdParcialFk();
            Estudiante idEstudianteFkOld = persistentNota.getIdEstudianteFk();
            Estudiante idEstudianteFkNew = nota.getIdEstudianteFk();
            if (idParcialFkNew != null) {
                idParcialFkNew = em.getReference(idParcialFkNew.getClass(), idParcialFkNew.getIdParcial());
                nota.setIdParcialFk(idParcialFkNew);
            }
            if (idEstudianteFkNew != null) {
                idEstudianteFkNew = em.getReference(idEstudianteFkNew.getClass(), idEstudianteFkNew.getIdEstudiante());
                nota.setIdEstudianteFk(idEstudianteFkNew);
            }
            nota = em.merge(nota);
            if (idParcialFkOld != null && !idParcialFkOld.equals(idParcialFkNew)) {
                idParcialFkOld.getNotaList().remove(nota);
                idParcialFkOld = em.merge(idParcialFkOld);
            }
            if (idParcialFkNew != null && !idParcialFkNew.equals(idParcialFkOld)) {
                idParcialFkNew.getNotaList().add(nota);
                idParcialFkNew = em.merge(idParcialFkNew);
            }
            if (idEstudianteFkOld != null && !idEstudianteFkOld.equals(idEstudianteFkNew)) {
                idEstudianteFkOld.getNotaList().remove(nota);
                idEstudianteFkOld = em.merge(idEstudianteFkOld);
            }
            if (idEstudianteFkNew != null && !idEstudianteFkNew.equals(idEstudianteFkOld)) {
                idEstudianteFkNew.getNotaList().add(nota);
                idEstudianteFkNew = em.merge(idEstudianteFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nota.getIdNota();
                if (findNota(id) == null) {
                    throw new NonexistentEntityException("The nota with id " + id + " no longer exists.");
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
            Nota nota;
            try {
                nota = em.getReference(Nota.class, id);
                nota.getIdNota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nota with id " + id + " no longer exists.", enfe);
            }
            Parcial idParcialFk = nota.getIdParcialFk();
            if (idParcialFk != null) {
                idParcialFk.getNotaList().remove(nota);
                idParcialFk = em.merge(idParcialFk);
            }
            Estudiante idEstudianteFk = nota.getIdEstudianteFk();
            if (idEstudianteFk != null) {
                idEstudianteFk.getNotaList().remove(nota);
                idEstudianteFk = em.merge(idEstudianteFk);
            }
            em.remove(nota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nota> findNotaEntities() {
        return findNotaEntities(true, -1, -1);
    }

    public List<Nota> findNotaEntities(int maxResults, int firstResult) {
        return findNotaEntities(false, maxResults, firstResult);
    }

    private List<Nota> findNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nota.class));
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

    public Nota findNota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nota.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nota> rt = cq.from(Nota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
