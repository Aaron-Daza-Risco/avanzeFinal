package com.example.viacostafx.dao;

import com.example.viacostafx.Modelo.BusModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class BusDao {
    private EntityManagerFactory emf;

    public BusDao() {
        emf = Persistence.createEntityManagerFactory("viacostaFX"); // Cambia el nombre según tu configuración
    }

    public BusModel obtenerBusPorId(int busId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(BusModel.class, busId);
        } finally {
            em.close();
        }
    }
    public List<BusModel> obtenerTodosLosBuses() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("from BusModel", BusModel.class).getResultList();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // Cierra el EntityManagerFactory
    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
