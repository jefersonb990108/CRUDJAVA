/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unicauca.apliweb.persistences.jpa;

import edu.unicauca.apliweb.persistences.entities.Curso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.unicauca.apliweb.persistences.entities.Profesor;
import java.util.ArrayList;
import java.util.List;
import edu.unicauca.apliweb.persistences.entities.Estudiante;
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
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) throws PreexistingEntityException, Exception {
        if (curso.getProfesorList() == null) {
            curso.setProfesorList(new ArrayList<Profesor>());
        }
        if (curso.getEstudianteList() == null) {
            curso.setEstudianteList(new ArrayList<Estudiante>());
        }
        if (curso.getParcialList() == null) {
            curso.setParcialList(new ArrayList<Parcial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Profesor> attachedProfesorList = new ArrayList<Profesor>();
            for (Profesor profesorListProfesorToAttach : curso.getProfesorList()) {
                profesorListProfesorToAttach = em.getReference(profesorListProfesorToAttach.getClass(), profesorListProfesorToAttach.getIdDocente());
                attachedProfesorList.add(profesorListProfesorToAttach);
            }
            curso.setProfesorList(attachedProfesorList);
            List<Estudiante> attachedEstudianteList = new ArrayList<Estudiante>();
            for (Estudiante estudianteListEstudianteToAttach : curso.getEstudianteList()) {
                estudianteListEstudianteToAttach = em.getReference(estudianteListEstudianteToAttach.getClass(), estudianteListEstudianteToAttach.getIdEstudiante());
                attachedEstudianteList.add(estudianteListEstudianteToAttach);
            }
            curso.setEstudianteList(attachedEstudianteList);
            List<Parcial> attachedParcialList = new ArrayList<Parcial>();
            for (Parcial parcialListParcialToAttach : curso.getParcialList()) {
                parcialListParcialToAttach = em.getReference(parcialListParcialToAttach.getClass(), parcialListParcialToAttach.getIdParcial());
                attachedParcialList.add(parcialListParcialToAttach);
            }
            curso.setParcialList(attachedParcialList);
            em.persist(curso);
            for (Profesor profesorListProfesor : curso.getProfesorList()) {
                profesorListProfesor.getCursoList().add(curso);
                profesorListProfesor = em.merge(profesorListProfesor);
            }
            for (Estudiante estudianteListEstudiante : curso.getEstudianteList()) {
                estudianteListEstudiante.getCursoList().add(curso);
                estudianteListEstudiante = em.merge(estudianteListEstudiante);
            }
            for (Parcial parcialListParcial : curso.getParcialList()) {
                Curso oldIdCursoFkOfParcialListParcial = parcialListParcial.getIdCursoFk();
                parcialListParcial.setIdCursoFk(curso);
                parcialListParcial = em.merge(parcialListParcial);
                if (oldIdCursoFkOfParcialListParcial != null) {
                    oldIdCursoFkOfParcialListParcial.getParcialList().remove(parcialListParcial);
                    oldIdCursoFkOfParcialListParcial = em.merge(oldIdCursoFkOfParcialListParcial);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCurso(curso.getIdCurso()) != null) {
                throw new PreexistingEntityException("Curso " + curso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getIdCurso());
            List<Profesor> profesorListOld = persistentCurso.getProfesorList();
            List<Profesor> profesorListNew = curso.getProfesorList();
            List<Estudiante> estudianteListOld = persistentCurso.getEstudianteList();
            List<Estudiante> estudianteListNew = curso.getEstudianteList();
            List<Parcial> parcialListOld = persistentCurso.getParcialList();
            List<Parcial> parcialListNew = curso.getParcialList();
            List<String> illegalOrphanMessages = null;
            for (Parcial parcialListOldParcial : parcialListOld) {
                if (!parcialListNew.contains(parcialListOldParcial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Parcial " + parcialListOldParcial + " since its idCursoFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Profesor> attachedProfesorListNew = new ArrayList<Profesor>();
            for (Profesor profesorListNewProfesorToAttach : profesorListNew) {
                profesorListNewProfesorToAttach = em.getReference(profesorListNewProfesorToAttach.getClass(), profesorListNewProfesorToAttach.getIdDocente());
                attachedProfesorListNew.add(profesorListNewProfesorToAttach);
            }
            profesorListNew = attachedProfesorListNew;
            curso.setProfesorList(profesorListNew);
            List<Estudiante> attachedEstudianteListNew = new ArrayList<Estudiante>();
            for (Estudiante estudianteListNewEstudianteToAttach : estudianteListNew) {
                estudianteListNewEstudianteToAttach = em.getReference(estudianteListNewEstudianteToAttach.getClass(), estudianteListNewEstudianteToAttach.getIdEstudiante());
                attachedEstudianteListNew.add(estudianteListNewEstudianteToAttach);
            }
            estudianteListNew = attachedEstudianteListNew;
            curso.setEstudianteList(estudianteListNew);
            List<Parcial> attachedParcialListNew = new ArrayList<Parcial>();
            for (Parcial parcialListNewParcialToAttach : parcialListNew) {
                parcialListNewParcialToAttach = em.getReference(parcialListNewParcialToAttach.getClass(), parcialListNewParcialToAttach.getIdParcial());
                attachedParcialListNew.add(parcialListNewParcialToAttach);
            }
            parcialListNew = attachedParcialListNew;
            curso.setParcialList(parcialListNew);
            curso = em.merge(curso);
            for (Profesor profesorListOldProfesor : profesorListOld) {
                if (!profesorListNew.contains(profesorListOldProfesor)) {
                    profesorListOldProfesor.getCursoList().remove(curso);
                    profesorListOldProfesor = em.merge(profesorListOldProfesor);
                }
            }
            for (Profesor profesorListNewProfesor : profesorListNew) {
                if (!profesorListOld.contains(profesorListNewProfesor)) {
                    profesorListNewProfesor.getCursoList().add(curso);
                    profesorListNewProfesor = em.merge(profesorListNewProfesor);
                }
            }
            for (Estudiante estudianteListOldEstudiante : estudianteListOld) {
                if (!estudianteListNew.contains(estudianteListOldEstudiante)) {
                    estudianteListOldEstudiante.getCursoList().remove(curso);
                    estudianteListOldEstudiante = em.merge(estudianteListOldEstudiante);
                }
            }
            for (Estudiante estudianteListNewEstudiante : estudianteListNew) {
                if (!estudianteListOld.contains(estudianteListNewEstudiante)) {
                    estudianteListNewEstudiante.getCursoList().add(curso);
                    estudianteListNewEstudiante = em.merge(estudianteListNewEstudiante);
                }
            }
            for (Parcial parcialListNewParcial : parcialListNew) {
                if (!parcialListOld.contains(parcialListNewParcial)) {
                    Curso oldIdCursoFkOfParcialListNewParcial = parcialListNewParcial.getIdCursoFk();
                    parcialListNewParcial.setIdCursoFk(curso);
                    parcialListNewParcial = em.merge(parcialListNewParcial);
                    if (oldIdCursoFkOfParcialListNewParcial != null && !oldIdCursoFkOfParcialListNewParcial.equals(curso)) {
                        oldIdCursoFkOfParcialListNewParcial.getParcialList().remove(parcialListNewParcial);
                        oldIdCursoFkOfParcialListNewParcial = em.merge(oldIdCursoFkOfParcialListNewParcial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getIdCurso();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getIdCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Parcial> parcialListOrphanCheck = curso.getParcialList();
            for (Parcial parcialListOrphanCheckParcial : parcialListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Parcial " + parcialListOrphanCheckParcial + " in its parcialList field has a non-nullable idCursoFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Profesor> profesorList = curso.getProfesorList();
            for (Profesor profesorListProfesor : profesorList) {
                profesorListProfesor.getCursoList().remove(curso);
                profesorListProfesor = em.merge(profesorListProfesor);
            }
            List<Estudiante> estudianteList = curso.getEstudianteList();
            for (Estudiante estudianteListEstudiante : estudianteList) {
                estudianteListEstudiante.getCursoList().remove(curso);
                estudianteListEstudiante = em.merge(estudianteListEstudiante);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
