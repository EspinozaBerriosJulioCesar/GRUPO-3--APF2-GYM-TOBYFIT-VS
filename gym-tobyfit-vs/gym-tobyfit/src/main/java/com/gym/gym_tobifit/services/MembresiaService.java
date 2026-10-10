package com.gym.gym_tobifit.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gym.gym_tobifit.models.Membresia;
import com.gym.gym_tobifit.repositories.MembresiaRepository;

@Service
public class MembresiaService {

    private final MembresiaRepository repository;

    public MembresiaService(MembresiaRepository repository) {
        this.repository = repository;
    }

    public List<Membresia> listar() {
        return repository.findAll();
    }

    public Optional<Membresia> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Membresia guardar(Membresia membresia) {
        return repository.save(membresia);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
