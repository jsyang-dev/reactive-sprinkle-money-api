package com.kakaopay.controller;

import com.kakaopay.dto.ReceivingDto;
import com.kakaopay.service.ReceivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/receivings")
@Validated
@RequiredArgsConstructor
public class ReceivingController {

  private final ReceivingService receivingService;

  @PutMapping("/{token}")
  public Mono<ResponseEntity<ReceivingDto>> receive(
      @PathVariable String token,
      @RequestHeader("X-USER-ID") @Positive int userId,
      @RequestHeader("X-ROOM-ID") @NotBlank String roomID) {

    return receivingService
        .receive(token, userId, roomID)
        .map(receivedAmount -> ReceivingDto.builder().amount(receivedAmount).build())
        .flatMap(receivingDto -> Mono.just(new ResponseEntity<>(receivingDto, HttpStatus.OK)));
  }
}
