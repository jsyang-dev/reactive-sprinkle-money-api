package com.kakaopay.controller;

import com.kakaopay.dto.ReceivingDto;
import com.kakaopay.service.ReceivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/receivings")
@Validated
@RequiredArgsConstructor
public class ReceivingController {

  private final ReceivingService receivingService;

  @PutMapping("/{token}")
  public ResponseEntity<ReceivingDto> receive(
      @PathVariable String token,
      @RequestHeader("X-USER-ID") @Positive int userId,
      @RequestHeader("X-ROOM-ID") @NotBlank String roomID) {

    long receivedAmount = receivingService.receive(token, userId, roomID);

    ReceivingDto receivingResDto = ReceivingDto.builder().amount(receivedAmount).build();

    return ResponseEntity.ok(receivingResDto);
  }
}
