package com.example.viacostafx.dao;

import com.example.viacostafx.Modelo.EmpleadosModel;
import jakarta.persistence.*;
import java.util.List;

public class EmpleadosDao {
    private EntityManagerFactory emf;

    public EmpleadosDao() {
        this.emf = Persistence.createEntityManagerFactory("viacostaFX");
    }

    // CREATE - Crear nuevo empleado
    public boolean crearEmpleado(EmpleadosModel empleado) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(empleado);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // READ - Obtener todos los empleados
    public List<EmpleadosModel> obtenerTodosEmpleados() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmpleadosModel> query = em.createQuery("SELECT e FROM EmpleadosModel e", EmpleadosModel.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Obtener empleado por ID
    public EmpleadosModel obtenerEmpleadoPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(EmpleadosModel.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Obtener empleado por DNI
    public EmpleadosModel obtenerEmpleadoPorDNI(int dni) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmpleadosModel> query = em.createQuery(
                    "SELECT e FROM EmpleadosModel e WHERE e.DNI = :dni",
                    EmpleadosModel.class
            );
            query.setParameter("dni", dni);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Obtener empleado por nombre de usuario
    public EmpleadosModel obtenerEmpleadoPorUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmpleadosModel> query = em.createQuery(
                    "SELECT e FROM EmpleadosModel e WHERE e.usuario = :username",
                    EmpleadosModel.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // UPDATE - Actualizar empleado
    public boolean actualizarEmpleado(EmpleadosModel empleado) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(empleado);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // DELETE - Eliminar empleado
    public boolean eliminarEmpleado(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            EmpleadosModel empleado = em.find(EmpleadosModel.class, id);
            if (empleado != null) {
                em.remove(empleado);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Búsqueda por criterios múltiples (nombre o apellido)
    public List<EmpleadosModel> buscarEmpleados(String criterio) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmpleadosModel> query = em.createQuery(
                    "SELECT e FROM EmpleadosModel e WHERE " +
                            "LOWER(e.nombre) LIKE LOWER(:criterio) OR " +
                            "LOWER(e.apellido) LIKE LOWER(:criterio) OR " +
                            "LOWER(e.usuario) LIKE LOWER(:criterio)",
                    EmpleadosModel.class
            );
            query.setParameter("criterio", "%" + criterio.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // Cerrar el EntityManagerFactory
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
