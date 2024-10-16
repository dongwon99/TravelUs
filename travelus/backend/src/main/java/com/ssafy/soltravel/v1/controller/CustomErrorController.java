package com.ssafy.soltravel.v1.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

  @RequestMapping("/v1/error")
  @ResponseBody
  public String handleError() {
    return "Custom error message";
  }
}
