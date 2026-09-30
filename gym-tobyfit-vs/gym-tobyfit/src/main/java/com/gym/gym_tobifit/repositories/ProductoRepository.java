package com.gym.gym_tobifit.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.gym_tobifit.models.Producto;
public interface ProductoRepository extends JpaRepository<Producto, Long> {}
