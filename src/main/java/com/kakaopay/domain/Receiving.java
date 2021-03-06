package com.kakaopay.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Receiving extends BaseEntity {

  // token
  private String token;

  // 받은 금액
  private Long amount;

  // 받은 사용자
  @Builder.Default private Integer userId = -1;

  // Lock 버전
  private Integer version;

  private Sprinkling sprinkling;

  public boolean isNotReceived() {
    return userId == -1;
  }
}
