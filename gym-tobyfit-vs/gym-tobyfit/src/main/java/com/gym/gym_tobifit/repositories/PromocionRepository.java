package com.gym.gym_tobifit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gym.gym_tobifit.models.Promocion;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
}

