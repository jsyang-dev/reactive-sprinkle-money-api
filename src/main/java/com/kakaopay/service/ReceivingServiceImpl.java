package com.kakaopay.service;

import com.kakaopay.domain.Receiving;
import com.kakaopay.domain.Sprinkling;
import com.kakaopay.exception.DifferentRoomException;
import com.kakaopay.exception.DuplicateReceivingUserException;
import com.kakaopay.exception.DuplicateSprinklingUserException;
import com.kakaopay.exception.ReceivingCompletedException;
import com.kakaopay.exception.ReceivingExpiredException;
import com.kakaopay.exception.SprinklingNotFoundException;
import com.kakaopay.repository.ReceivingRepository;
import com.kakaopay.repository.SprinklingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceivingServiceImpl implements ReceivingService {

  private final SprinklingRepository sprinklingRepository;
  private final ReceivingRepository receivingRepository;

  @Override
  @Transactional
  public Mono<Long> receive(String token, int userId, String roomId) {

    Sprinkling sprinkling =
        sprinklingRepository
            .findByToken(token)
            .orElseThrow(() -> new SprinklingNotFoundException(token));

    validateReceiving(sprinkling, userId, roomId);

    List<Receiving> receivings = receivingRepository.findAllByToken(token);

    if (receivings.stream().anyMatch(receiving -> receiving.getUserId() == userId)) {
      throw new DuplicateReceivingUserException(userId);
    }

    Receiving remainReceiving =
        receivings.stream()
            .filter(Receiving::isNotReceived)
            .findFirst()
            .orElseThrow(() -> new ReceivingCompletedException(token));

    remainReceiving.setUserId(userId);
    return Mono.just(remainReceiving.getAmount());
  }

  private void validateReceiving(Sprinkling sprinkling, int userId, String roomId) {

    if (sprinkling.isSprinklingUserDuplicated(userId)) {
      throw new DuplicateSprinklingUserException(userId);
    }
    if (sprinkling.isDifferentRoom(roomId)) {
      throw new DifferentRoomException(roomId);
    }
    if (sprinkling.isReceivingExpired()) {
      throw new ReceivingExpiredException(sprinkling.getCreateDate());
    }
  }
}
