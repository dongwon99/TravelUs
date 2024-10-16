package com.goofy.tunabank.v1.controller;


import com.goofy.tunabank.v1.util.LogUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @PostMapping("/1")
  public ResponseEntity<?> test() {
    LogUtil.info("정상 요청");
    return ResponseEntity.ok("test~~");
  }
}
