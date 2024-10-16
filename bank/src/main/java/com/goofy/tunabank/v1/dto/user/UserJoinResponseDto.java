package com.goofy.tunabank.v1.dto.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinResponseDto {
  String userId;
  String userName;
  String userKey;
  LocalDateTime created;
  LocalDateTime modified;
}
