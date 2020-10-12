package com.kakaopay.controller;

import com.kakaopay.dto.ReadDto;
import com.kakaopay.dto.SprinklingDto.Request;
import com.kakaopay.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.kakaopay.dto.SprinklingDto.Response;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SprinklingController.class)
@ActiveProfiles("test")
class SprinklingControllerTest {

  @Autowired private WebTestClient webClient;
  @MockBean SprinklingService sprinklingService;

  @Test
  @DisplayName("뿌리기를 요청하고 token을 반환 받음")
  void sprinkleTest() {

    // Given
    Request request = Request.builder().amount(20000).people(3).build();

    when(sprinklingService.sprinkle(20000, 3, 900001, "TEST-ROOM")).thenReturn(Mono.just("tst"));

    // When
    WebTestClient.ResponseSpec responseSpec =
        webClient
            .post()
            .uri("/v1/sprinklings")
            .header("X-USER-ID", "900001")
            .header("X-ROOM-ID", "TEST-ROOM")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), Request.class)
            .exchange();

    // Then
    responseSpec.expectStatus().isCreated().expectBody(Response.class);
  }

  @Test
  @DisplayName("조회를 요청하고 뿌리기 상태를 반환 받음")
  void readTest() {

    // Given
    when(sprinklingService.read("tst", 900001))
        .thenReturn(Mono.just(ReadDto.SprinklingDto.builder().build()));

    // When
    WebTestClient.ResponseSpec responseSpec =
        webClient
            .get()
            .uri("/v1/sprinklings/{token}", "tst")
            .header("X-USER-ID", "900001")
            .exchange();

    // Then
    responseSpec.expectStatus().isOk().expectBody(ReadDto.SprinklingDto.class);
  }

  //  @Test
  //  @DisplayName("요청 Header 누락 테스트")
  //  void validateHeaderTest01() {
  //
  //    // Given
  //    Request request = Request.builder().amount(20000).people(3).build();
  //
  //    // When
  //    WebTestClient.ResponseSpec responseSpec =
  //        webClient
  //            .post()
  //            .uri("/v1/sprinklings")
  //            .contentType(MediaType.APPLICATION_JSON)
  //            .body(Mono.just(request), Request.class)
  //            .exchange();
  //
  //    // Then
  //    responseSpec.expectStatus().isBadRequest();
  //  }

  //  @Test
  //  @DisplayName("요청 Header 값 검증 테스트")
  //  void validateHeaderTest02() throws Exception {
  //
  //    // Given
  //    Request request = Request.builder().amount(20000).people(3).build();
  //
  //    // When
  //    final ResultActions actions =
  //        mockMvc.perform(
  //            post("/v1/sprinklings")
  //                .header("X-USER-ID", 0)
  //                .header("X-ROOM-ID", "")
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(objectMapper.writeValueAsString(request)));
  //
  //    // Then
  //    actions
  //        .andDo(print())
  //        .andExpect(status().isBadRequest())
  //        .andExpect(jsonPath("timestamp").exists())
  //        .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.name()))
  //        .andExpect(jsonPath("message").exists())
  //        .andExpect(jsonPath("debugMessage").exists())
  //        .andExpect(jsonPath("subErrors").isEmpty());
  //  }
  //
  //  @Test
  //  @DisplayName("요청 파라미터 값 검증 테스트")
  //  void validateParameterTest() throws Exception {
  //
  //    // Given
  //    Request request = Request.builder().amount(0).build();
  //
  //    // When
  //    final ResultActions actions =
  //        mockMvc.perform(
  //            post("/v1/sprinklings")
  //                .header("X-USER-ID", 900001)
  //                .header("X-ROOM-ID", "TEST-ROOM")
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(objectMapper.writeValueAsString(request)));
  //
  //    // Then
  //    actions
  //        .andDo(print())
  //        .andExpect(status().isBadRequest())
  //        .andExpect(jsonPath("timestamp").exists())
  //        .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.name()))
  //        .andExpect(jsonPath("message").exists())
  //        .andExpect(jsonPath("debugMessage").isEmpty())
  //        .andExpect(jsonPath("subErrors").exists());
  //  }
}
