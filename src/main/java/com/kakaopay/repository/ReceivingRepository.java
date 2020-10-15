package com.kakaopay.repository;

import com.kakaopay.domain.Receiving;
import com.kakaopay.domain.Sprinkling;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReceivingRepository extends MongoRepository<Receiving, Long> {

  List<Receiving> findAllByToken(String token);
}
