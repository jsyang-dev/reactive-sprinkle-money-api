package com.kakaopay.repository;

import com.kakaopay.domain.Sprinkling;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SprinklingRepository extends MongoRepository<Sprinkling, Long> {

  Optional<Sprinkling> findByToken(String token);
}
