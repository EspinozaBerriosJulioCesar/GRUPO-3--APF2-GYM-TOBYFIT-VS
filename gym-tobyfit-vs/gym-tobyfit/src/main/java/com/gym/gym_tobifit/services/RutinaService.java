package com.gym.gym_tobifit.services;

import com.gym.gym_tobifit.models.Rutina;
import com.gym.gym_tobifit.repositories.RutinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RutinaService {

    private final RutinaRepository repository;

    public RutinaService(RutinaRepository repository) {
        this.repository = repository;
    }

    public List<Rutina> listar() {
        return repository.findAll();
    }

    public Optional<Rutina> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Rutina guardar(Rutina rutina) {
        return repository.save(rutina);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
