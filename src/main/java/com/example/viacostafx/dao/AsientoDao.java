package com.example.viacostafx.dao;
import com.example.viacostafx.Modelo.AsientoModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AsientoDao {
    private EntityManagerFactory emf;

    public AsientoDao() {
        this.emf = Persistence.createEntityManagerFactory("viacostaFX"); // Cambia al nombre de tu unidad de persistencia
    }

    // Crear un nuevo asiento
    public void crearAsiento(AsientoModel asiento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(asiento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Obtener un asiento por su ID
    public AsientoModel obtenerAsientoPorId(int asientoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(AsientoModel.class, asientoId);
        } finally {
            em.close();
        }
    }

    // Obtener todos los asientos
    public List<AsientoModel> obtenerTodosLosAsientos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AsientoModel> query = em.createQuery("SELECT a FROM AsientoModel a", AsientoModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Obtener asientos por bus
    public List<AsientoModel> obtenerAsientosPorBus(int busId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AsientoModel> query = em.createQuery("SELECT a FROM AsientoModel a WHERE a.bus.id = :busId", AsientoModel.class);
            query.setParameter("busId", busId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Actualizar un asiento existente
    public boolean actualizarAsiento(AsientoModel asientoActualizado) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            AsientoModel asiento = em.find(AsientoModel.class, asientoActualizado.getId());
            if (asiento != null) {
                asiento.setNumero(asientoActualizado.getNumero());
                asiento.setEstado(asientoActualizado.getEstado());
                asiento.setBus(asientoActualizado.getBus());
                asiento.setBoletos(asientoActualizado.getBoletos());
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Eliminar un asiento por su ID
    public boolean eliminarAsiento(int asientoId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            AsientoModel asiento = em.find(AsientoModel.class, asientoId);
            if (asiento != null) {
                em.remove(asiento);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Cerrar el EntityManagerFactory
    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
