package com.kakaopay.service;

import reactor.core.publisher.Mono;

public interface ReceivingService {

  /**
   * 머니 받기
   *
   * @param token 뿌리기 token
   * @param userId 받은 사용자 ID
   * @param roomId 대화방 ID
   * @return 받은 금액
   */
  Mono<Long> receive(String token, int userId, String roomId);
}
