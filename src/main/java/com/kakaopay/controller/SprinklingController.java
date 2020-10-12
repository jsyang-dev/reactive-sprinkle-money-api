package com.kakaopay.controller;

import com.kakaopay.dto.ReadDto;
import com.kakaopay.service.SprinklingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import static com.kakaopay.dto.SprinklingDto.Request;
import static com.kakaopay.dto.SprinklingDto.Response;
import static org.springframework.web.reactive.function.server.EntityResponse.fromObject;

@RestController
@RequestMapping("/v1/sprinklings")
@Validated
@RequiredArgsConstructor
public class SprinklingController {

  private final SprinklingService sprinklingService;

  @PostMapping
  public Mono<ResponseEntity<Response>> sprinkle(
      @RequestHeader("X-USER-ID") @Positive int userId,
      @RequestHeader("X-ROOM-ID") @NotBlank String roomID,
      @RequestBody @Valid Request request) {

    return sprinklingService
        .sprinkle(request.getAmount(), request.getPeople(), userId, roomID)
        .map(token -> Response.builder().token(token).build())
        .flatMap(response -> Mono.just(new ResponseEntity<>(response, HttpStatus.CREATED)));
  }

  @GetMapping("/{token}")
  public Mono<ResponseEntity<ReadDto.SprinklingDto>> read(
      @PathVariable String token, @RequestHeader("X-USER-ID") @Positive int userId) {

    return sprinklingService
        .read(token, userId)
        .flatMap(sprinklingDto -> Mono.just(new ResponseEntity<>(sprinklingDto, HttpStatus.OK)));
  }
}
