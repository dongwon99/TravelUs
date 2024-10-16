package com.ssafy.soltravel.v2.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NCPUtil {

  private static String SPACE = " ";
  private static String NEW_LINE = "\n";
  private final Map<String, String> apiKeys;

  public String makeSignature(String method, String url)
      throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    String timestamp = now.toString();
    String accessKey = apiKeys.get("NCP_ACCESS_KEY");
    String secretKey = apiKeys.get("NCP_SECRET_KEY");

    String message = new StringBuilder()
        .append(method)
        .append(SPACE)
        .append(url)
        .append(NEW_LINE)
        .append(timestamp)
        .append(NEW_LINE)
        .append(accessKey)
        .toString();

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);

    byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
    String encodeBase64String = Base64.encodeBase64String(rawHmac);
    return encodeBase64String;
  }
}
