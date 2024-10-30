package com.example.viacostafx.dao;

import com.example.viacostafx.Modelo.AgenciaModel;
import com.example.viacostafx.Modelo.JPAUtils;
import com.example.viacostafx.Modelo.UbigeoModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgenciaDao {

    // Nuevo método para obtener provincias
    public static List<String> obtenerProvincias() {
        EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
        List<String> provincias = null;

        try {
            String jpql = "SELECT DISTINCT u.provincia FROM UbigeoModel u ORDER BY u.provincia";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            provincias = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return provincias;
    }

    // Método para obtener ubigeos por provincia
    public static List<UbigeoModel> obtenerUbigeosPorProvincia(String provincia) {
        EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
        List<UbigeoModel> ubigeos = null;

        try {
            String jpql = "SELECT u FROM UbigeoModel u WHERE u.provincia = :provincia";
            TypedQuery<UbigeoModel> query = em.createQuery(jpql, UbigeoModel.class);
            query.setParameter("provincia", provincia);
            ubigeos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return ubigeos;
    }

    public static List<String> obtenerDistritosConAgencias() {
        EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
        List<String> distritos = null;

        try {
            String jpql = "SELECT DISTINCT a.ubigeo.distrito FROM AgenciaModel a WHERE a.isActive = true";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            distritos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return distritos;
    }

    // Método para crear agencia con validación
    public AgenciaModel crearAgencia(String nombre, UbigeoModel ubigeo) {
        EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
        AgenciaModel nuevaAgencia = null;

        try {
            em.getTransaction().begin();

            // Verificar si ya existe una agencia con el mismo nombre en el mismo ubigeo
            String jpql = "SELECT COUNT(a) FROM AgenciaModel a WHERE a.nombre = :nombre AND a.ubigeo.id = :ubigeoId";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("nombre", nombre)
                    .setParameter("ubigeoId", ubigeo.getId())
                    .getSingleResult();

            if (count == 0) {
                nuevaAgencia = new AgenciaModel();
                nuevaAgencia.setNombre(nombre);
                nuevaAgencia.setUbigeo(ubigeo);
                nuevaAgencia.setIsActive(true);

                em.persist(nuevaAgencia);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        return nuevaAgencia;
    }

    // Método para obtener agencias activas por ubigeo
    public static List<AgenciaModel> obtenerAgenciasPorUbigeo(Integer ubigeoId) {
        EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
        List<AgenciaModel> agencias = null;

        try {
            String jpql = "SELECT a FROM AgenciaModel a WHERE a.ubigeo.id = :ubigeoId AND a.isActive = true";
            TypedQuery<AgenciaModel> query = em.createQuery(jpql, AgenciaModel.class);
            query.setParameter("ubigeoId", ubigeoId);
            agencias = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return agencias;
    }
}