package com.kakaopay.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.kakaopay.constant.SprinklingConstant.EXPIRE_READ_SECONDS;
import static com.kakaopay.constant.SprinklingConstant.EXPIRE_RECEIVING_SECONDS;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sprinkling extends BaseEntity {

  // token
  private String token;

  // 뿌린 금액
  private Long amount;

  // 뿌린 인원
  private Integer people;

  // 뿌린 사용자
  private Integer userId;

  // 대화방
  private String roomId;

  //  @OneToMany(mappedBy = "sprinkling", cascade = CascadeType.PERSIST)
  //  @Builder.Default List<Receiving> receivings = new ArrayList<>();

  //  public void addReceiving(Receiving receiving) {
  //    receivings.add(receiving);
  //    receiving.setSprinkling(this);
  //  }

  public boolean isSprinklingUserDuplicated(int userId) {
    return this.userId == userId;
  }

  public boolean isDifferentRoom(String roomId) {
    return !roomId.equals(this.roomId);
  }

  public boolean isReceivingExpired() {
    long secondsGap = Duration.between(super.getCreateDate(), LocalDateTime.now()).getSeconds();
    return secondsGap > EXPIRE_RECEIVING_SECONDS;
  }

  public boolean isPermissionDenied(int userId) {
    return this.userId != userId;
  }

  public boolean isReadExpired() {
    long secondsGap = Duration.between(super.getCreateDate(), LocalDateTime.now()).getSeconds();
    return secondsGap > EXPIRE_READ_SECONDS;
  }
}
