package com.gym.gym_tobifit.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gym.gym_tobifit.models.Promocion;
import com.gym.gym_tobifit.repositories.PromocionRepository;

@Service
public class PromocionService {

    private final PromocionRepository repository;

    public PromocionService(PromocionRepository repository) {
        this.repository = repository;
    }

    public List<Promocion> listar() {
        return repository.findAll();
    }

    public Optional<Promocion> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Promocion guardar(Promocion promocion) {
        return repository.save(promocion);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}

