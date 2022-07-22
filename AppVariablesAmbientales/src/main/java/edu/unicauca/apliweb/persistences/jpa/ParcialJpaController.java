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
import edu.unicauca.apliweb.persistences.entities.Curso;
import edu.unicauca.apliweb.persistences.entities.VariablesAmbientales;
import java.util.ArrayList;
import java.util.List;
import edu.unicauca.apliweb.persistences.entities.Nota;
import edu.unicauca.apliweb.persistences.entities.Parcial;
import edu.unicauca.apliweb.persistences.jpa.exceptions.IllegalOrphanException;
import edu.unicauca.apliweb.persistences.jpa.exceptions.NonexistentEntityException;
import edu.unicauca.apliweb.persistences.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jefer
 */
public class ParcialJpaController implements Serializable {

    public ParcialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parcial parcial) throws PreexistingEntityException, Exception {
        if (parcial.getVariablesAmbientalesList() == null) {
            parcial.setVariablesAmbientalesList(new ArrayList<VariablesAmbientales>());
        }
        if (parcial.getNotaList() == null) {
            parcial.setNotaList(new ArrayList<Nota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso idCursoFk = parcial.getIdCursoFk();
            if (idCursoFk != null) {
                idCursoFk = em.getReference(idCursoFk.getClass(), idCursoFk.getIdCurso());
                parcial.setIdCursoFk(idCursoFk);
            }
            List<VariablesAmbientales> attachedVariablesAmbientalesList = new ArrayList<VariablesAmbientales>();
            for (VariablesAmbientales variablesAmbientalesListVariablesAmbientalesToAttach : parcial.getVariablesAmbientalesList()) {
                variablesAmbientalesListVariablesAmbientalesToAttach = em.getReference(variablesAmbientalesListVariablesAmbientalesToAttach.getClass(), variablesAmbientalesListVariablesAmbientalesToAttach.getIdMedida());
                attachedVariablesAmbientalesList.add(variablesAmbientalesListVariablesAmbientalesToAttach);
            }
            parcial.setVariablesAmbientalesList(attachedVariablesAmbientalesList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : parcial.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            parcial.setNotaList(attachedNotaList);
            em.persist(parcial);
            if (idCursoFk != null) {
                idCursoFk.getParcialList().add(parcial);
                idCursoFk = em.merge(idCursoFk);
            }
            for (VariablesAmbientales variablesAmbientalesListVariablesAmbientales : parcial.getVariablesAmbientalesList()) {
                Parcial oldIdParcialFkOfVariablesAmbientalesListVariablesAmbientales = variablesAmbientalesListVariablesAmbientales.getIdParcialFk();
                variablesAmbientalesListVariablesAmbientales.setIdParcialFk(parcial);
                variablesAmbientalesListVariablesAmbientales = em.merge(variablesAmbientalesListVariablesAmbientales);
                if (oldIdParcialFkOfVariablesAmbientalesListVariablesAmbientales != null) {
                    oldIdParcialFkOfVariablesAmbientalesListVariablesAmbientales.getVariablesAmbientalesList().remove(variablesAmbientalesListVariablesAmbientales);
                    oldIdParcialFkOfVariablesAmbientalesListVariablesAmbientales = em.merge(oldIdParcialFkOfVariablesAmbientalesListVariablesAmbientales);
                }
            }
            for (Nota notaListNota : parcial.getNotaList()) {
                Parcial oldIdParcialFkOfNotaListNota = notaListNota.getIdParcialFk();
                notaListNota.setIdParcialFk(parcial);
                notaListNota = em.merge(notaListNota);
                if (oldIdParcialFkOfNotaListNota != null) {
                    oldIdParcialFkOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdParcialFkOfNotaListNota = em.merge(oldIdParcialFkOfNotaListNota);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParcial(parcial.getIdParcial()) != null) {
                throw new PreexistingEntityException("Parcial " + parcial + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parcial parcial) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parcial persistentParcial = em.find(Parcial.class, parcial.getIdParcial());
            Curso idCursoFkOld = persistentParcial.getIdCursoFk();
            Curso idCursoFkNew = parcial.getIdCursoFk();
            List<VariablesAmbientales> variablesAmbientalesListOld = persistentParcial.getVariablesAmbientalesList();
            List<VariablesAmbientales> variablesAmbientalesListNew = parcial.getVariablesAmbientalesList();
            List<Nota> notaListOld = persistentParcial.getNotaList();
            List<Nota> notaListNew = parcial.getNotaList();
            List<String> illegalOrphanMessages = null;
            for (VariablesAmbientales variablesAmbientalesListOldVariablesAmbientales : variablesAmbientalesListOld) {
                if (!variablesAmbientalesListNew.contains(variablesAmbientalesListOldVariablesAmbientales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariablesAmbientales " + variablesAmbientalesListOldVariablesAmbientales + " since its idParcialFk field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idParcialFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCursoFkNew != null) {
                idCursoFkNew = em.getReference(idCursoFkNew.getClass(), idCursoFkNew.getIdCurso());
                parcial.setIdCursoFk(idCursoFkNew);
            }
            List<VariablesAmbientales> attachedVariablesAmbientalesListNew = new ArrayList<VariablesAmbientales>();
            for (VariablesAmbientales variablesAmbientalesListNewVariablesAmbientalesToAttach : variablesAmbientalesListNew) {
                variablesAmbientalesListNewVariablesAmbientalesToAttach = em.getReference(variablesAmbientalesListNewVariablesAmbientalesToAttach.getClass(), variablesAmbientalesListNewVariablesAmbientalesToAttach.getIdMedida());
                attachedVariablesAmbientalesListNew.add(variablesAmbientalesListNewVariablesAmbientalesToAttach);
            }
            variablesAmbientalesListNew = attachedVariablesAmbientalesListNew;
            parcial.setVariablesAmbientalesList(variablesAmbientalesListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            parcial.setNotaList(notaListNew);
            parcial = em.merge(parcial);
            if (idCursoFkOld != null && !idCursoFkOld.equals(idCursoFkNew)) {
                idCursoFkOld.getParcialList().remove(parcial);
                idCursoFkOld = em.merge(idCursoFkOld);
            }
            if (idCursoFkNew != null && !idCursoFkNew.equals(idCursoFkOld)) {
                idCursoFkNew.getParcialList().add(parcial);
                idCursoFkNew = em.merge(idCursoFkNew);
            }
            for (VariablesAmbientales variablesAmbientalesListNewVariablesAmbientales : variablesAmbientalesListNew) {
                if (!variablesAmbientalesListOld.contains(variablesAmbientalesListNewVariablesAmbientales)) {
                    Parcial oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales = variablesAmbientalesListNewVariablesAmbientales.getIdParcialFk();
                    variablesAmbientalesListNewVariablesAmbientales.setIdParcialFk(parcial);
                    variablesAmbientalesListNewVariablesAmbientales = em.merge(variablesAmbientalesListNewVariablesAmbientales);
                    if (oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales != null && !oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales.equals(parcial)) {
                        oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales.getVariablesAmbientalesList().remove(variablesAmbientalesListNewVariablesAmbientales);
                        oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales = em.merge(oldIdParcialFkOfVariablesAmbientalesListNewVariablesAmbientales);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Parcial oldIdParcialFkOfNotaListNewNota = notaListNewNota.getIdParcialFk();
                    notaListNewNota.setIdParcialFk(parcial);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdParcialFkOfNotaListNewNota != null && !oldIdParcialFkOfNotaListNewNota.equals(parcial)) {
                        oldIdParcialFkOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdParcialFkOfNotaListNewNota = em.merge(oldIdParcialFkOfNotaListNewNota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parcial.getIdParcial();
                if (findParcial(id) == null) {
                    throw new NonexistentEntityException("The parcial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parcial parcial;
            try {
                parcial = em.getReference(Parcial.class, id);
                parcial.getIdParcial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parcial with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VariablesAmbientales> variablesAmbientalesListOrphanCheck = parcial.getVariablesAmbientalesList();
            for (VariablesAmbientales variablesAmbientalesListOrphanCheckVariablesAmbientales : variablesAmbientalesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Parcial (" + parcial + ") cannot be destroyed since the VariablesAmbientales " + variablesAmbientalesListOrphanCheckVariablesAmbientales + " in its variablesAmbientalesList field has a non-nullable idParcialFk field.");
            }
            List<Nota> notaListOrphanCheck = parcial.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Parcial (" + parcial + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idParcialFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso idCursoFk = parcial.getIdCursoFk();
            if (idCursoFk != null) {
                idCursoFk.getParcialList().remove(parcial);
                idCursoFk = em.merge(idCursoFk);
            }
            em.remove(parcial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parcial> findParcialEntities() {
        return findParcialEntities(true, -1, -1);
    }

    public List<Parcial> findParcialEntities(int maxResults, int firstResult) {
        return findParcialEntities(false, maxResults, firstResult);
    }

    private List<Parcial> findParcialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parcial.class));
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

    public Parcial findParcial(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parcial.class, id);
        } finally {
            em.close();
        }
    }

    public int getParcialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parcial> rt = cq.from(Parcial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
