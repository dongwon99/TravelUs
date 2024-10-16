package com.ssafy.soltravel.v2.util;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartupApplicationListener {

  @EventListener
  @Transactional
  public void onApplicationEvent(ApplicationReadyEvent event) {
  }

}
