package com.ssafy.soltravel.v2.exception.participant;

import com.ssafy.soltravel.v2.exception.CustomException;

public class ParticipantNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Participant not found for participant ID.";
  private static final String DEFAULT_CODE = "PARTICIPANT_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public ParticipantNotFoundException(Long participantId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %d", DEFAULT_MESSAGE, participantId)
    );
  }
}
