package com.example.viacostafx.dao;

import com.example.viacostafx.Modelo.PasajeroModel;
import jakarta.persistence.*;

import java.util.List;

public class PasajeroDao {
    private EntityManagerFactory emf;

    public PasajeroDao() {
        this.emf = Persistence.createEntityManagerFactory("viacostaFX");
    }

    // Método para guardar un pasajero
    public void guardarPasajero(PasajeroModel pasajero) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pasajero);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    // Método para actualizar un pasajero
    public void actualizarPasajero(PasajeroModel pasajero) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pasajero);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    // Método para eliminar un pasajero
    public void eliminarPasajero(PasajeroModel pasajero) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            PasajeroModel pasajeroToRemove = em.find(PasajeroModel.class, pasajero.getId());
            if (pasajeroToRemove != null) {
                em.remove(pasajeroToRemove);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los pasajeros
    public List<PasajeroModel> obtenerTodosLosPasajeros() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<PasajeroModel> query = em.createQuery("SELECT p FROM PasajeroModel p", PasajeroModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public List<PasajeroModel> buscarPasajerosPorDni(String dni) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<PasajeroModel> query = em.createQuery("SELECT p FROM PasajeroModel p WHERE p.dni = :dni", PasajeroModel.class);
            query.setParameter("dni", dni);
            return query.getResultList(); // Retorna una lista de resultados
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retorna null si hay otro error
        } finally {
            em.close();
        }
    }


    // Método para cerrar el EntityManagerFactory
    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
