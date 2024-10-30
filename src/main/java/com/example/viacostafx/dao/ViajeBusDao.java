package com.example.viacostafx.dao;

import com.example.viacostafx.Modelo.ViajeBusModel;
import jakarta.persistence.*;

import java.util.List;

public class ViajeBusDao {
    private final EntityManagerFactory emf;

    public ViajeBusDao() {
        emf = Persistence.createEntityManagerFactory("viacostaFX");
    }

    public void guardar(ViajeBusModel viajeBus) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(viajeBus);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizar(ViajeBusModel viajeBus) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(viajeBus);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminar(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ViajeBusModel viajeBus = em.find(ViajeBusModel.class, id);
            if (viajeBus != null) {
                em.remove(viajeBus);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public ViajeBusModel obtenerPorId(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ViajeBusModel.class, id);
        } finally {
            em.close();
        }
    }

    public List<ViajeBusModel> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ViajeBusModel> query = em.createQuery("SELECT vb FROM ViajeBusModel vb", ViajeBusModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Integer obtenerBusIdPorViajeId(int viajeId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT vb.bus.id FROM ViajeBusModel vb WHERE vb.viaje.id = :viajeId", Integer.class);
            query.setParameter("viajeId", viajeId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }


    public List<ViajeBusModel> obtenerPorBusId(Integer busId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ViajeBusModel> query = em.createQuery(
                    "SELECT vb FROM ViajeBusModel vb WHERE vb.bus.id = :busId", ViajeBusModel.class);
            query.setParameter("busId", busId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
