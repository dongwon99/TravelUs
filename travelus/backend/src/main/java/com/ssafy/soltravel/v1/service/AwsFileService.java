package com.ssafy.soltravel.v1.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.soltravel.v1.exception.FileConvertException;
import com.ssafy.soltravel.v1.util.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsFileService {

  private final AmazonS3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final static String TEST_DIR = "test";
  private final static String REC_DIR = "receipt";
  private final static String PROFILE_DIR = "profile";


  /*
  * 영수증 저장
  */
  public String saveReciept(MultipartFile file, Long userId) throws IOException {
    File uploadFile = convert(file).orElseThrow(
        () -> new FileConvertException("파일 변환 에러")
    );
    return uploadWithUserId(uploadFile, REC_DIR, userId);
  }

  /*
  * 프로필 이미지 저장
  */
  public String saveProfileImage(MultipartFile file) throws IOException {
    File uploadFile = convert(file).orElseThrow(
        () -> new FileConvertException("파일 변환 에러")
    );
    return upload(uploadFile, PROFILE_DIR);
  }

  // 로컬에 잠시 저장
  private Optional<File> convert(MultipartFile file) throws IOException {

    Path convertFilePath = Paths.get(System.getProperty("user.home"), TEST_DIR, file.getOriginalFilename());
    Files.write(convertFilePath, file.getBytes());
    return Optional.of(convertFilePath.toFile());
  }

  // S3 업로드 함수 호출 + 로컬에 저장한 파일 삭제
  // 형식 : dirName/UUID.jpg
  private String upload(File file, String dirName) {
    String fileName = String.format("%s/%s", dirName, UUID.randomUUID());
    String uploadUrl = putS3(file, fileName);
    removeNewFile(file);
    return uploadUrl;
  }

  // S3 업로드 함수 호출 + 로컬에 저장한 파일 삭제
  // 형식 : dirName/userId/UUID+fileName.jpg
  private String uploadWithUserId(File file, String dirName, Long userId) {
    String fileName = String.format("%s/%d/%s%s", dirName, userId, UUID.randomUUID(), file.getName());
    String uploadUrl = putS3(file, fileName);
    removeNewFile(file);
    return uploadUrl;
  }

  // S3로 파일 업로드
  private String putS3(File file, String fileName) {
    s3Client.putObject(
        new PutObjectRequest(bucket, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead)
    );
    return s3Client.getUrl(bucket, fileName).toString();
  }

  // 로컬에 저장한 파일 삭제
  private void removeNewFile(File file) {
    if (file.delete()) {
      LogUtil.info("File delete success");
      return;
    }
    LogUtil.error("File delete fail");
  }

  // 디렉토리 생성?
  public void createDir(String bucketName, String folderName) {
    s3Client.putObject(
        bucketName,
        folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata()
    );
  }

}
