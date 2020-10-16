package com.kakaopay.service;

import com.kakaopay.domain.Receiving;
import com.kakaopay.domain.Sprinkling;
import com.kakaopay.dto.ReadDto;
import com.kakaopay.exception.InsufficientAmountException;
import com.kakaopay.exception.PermissionDeniedException;
import com.kakaopay.exception.ReadExpiredException;
import com.kakaopay.exception.SprinklingNotFoundException;
import com.kakaopay.mapper.SprinklingMapper;
import com.kakaopay.repository.ReceivingRepository;
import com.kakaopay.repository.SprinklingRepository;
import com.kakaopay.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinklingServiceImpl implements SprinklingService {

  private final SprinklingRepository sprinklingRepository;
  private final ReceivingRepository receivingRepository;
  private final SprinklingMapper sprinklingMapper;

  @Override
  @Transactional
  public Mono<String> sprinkle(long amount, int people, int userId, String roomId) {

    if (amount < people) {
      throw new InsufficientAmountException(amount, people);
    }

    String token = RandomUtils.generateToken();
    Sprinkling sprinkling =
        Sprinkling.builder()
            .token(token)
            .amount(amount)
            .people(people)
            .roomId(roomId)
            .userId(userId)
            .build();

    receivingRepository.saveAll(makeReceivings(token, amount, people));

    return Mono.just(sprinklingRepository.save(sprinkling).getToken());
  }

  @Override
  public Mono<ReadDto.SprinklingDto> read(String token, int userId) {

    Sprinkling sprinkling =
        sprinklingRepository
            .findByToken(token)
            .orElseThrow(() -> new SprinklingNotFoundException(token));

    validateReading(sprinkling, token, userId);

    List<Receiving> receivings = receivingRepository.findAllByToken(token);

    return Mono.just(sprinklingMapper.toDto(sprinkling, receivings));
  }

  private List<Receiving> makeReceivings(String token, long amount, int people) {

    List<Receiving> receivings = new ArrayList<>();

    long remainAmount = amount;
    for (int remainPeople = people; remainPeople > 0; remainPeople--) {

      long sprinklingAmount;
      if (remainPeople == 1) {
        sprinklingAmount = remainAmount;
      } else {
        sprinklingAmount = RandomUtils.generateRandomMoney(remainAmount, remainPeople);
      }
      remainAmount -= sprinklingAmount;

      receivings.add(Receiving.builder().token(token).amount(sprinklingAmount).build());
    }

    return receivings;
  }

  private void validateReading(Sprinkling sprinkling, String token, int userId) {

    if (sprinkling.isPermissionDenied(userId)) {
      throw new PermissionDeniedException(token);
    }
    if (sprinkling.isReadExpired()) {
      throw new ReadExpiredException(sprinkling.getCreateDate());
    }
  }
}
