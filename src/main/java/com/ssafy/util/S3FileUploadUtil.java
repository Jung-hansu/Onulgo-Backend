package com.ssafy.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUploadUtil {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile uploadFile) throws IOException {
        log.debug("uploadFile file: {}", uploadFile.getOriginalFilename());
        String origName = uploadFile.getOriginalFilename();
        String url;

        try {
            final String ext = origName.substring(origName.lastIndexOf('.')); // 확장자를 찾기 위한 코드
            final String saveFileName = getUuid() + ext; // 파일이름 암호화

            // System.getProperty => 시스템 환경에 관한 정보를 얻을 수 있다. (user.dir = 현재 작업 디렉토리를 의미함)
            File file = new File(System.getProperty("user.dir") + saveFileName); // 파일 객체 생성
            uploadFile.transferTo(file);     // 파일 변환
            uploadOnS3(saveFileName, file);  // S3 파일 업로드
            url = defaultUrl + saveFileName; // 주소 할당
            file.delete(); // 파일 삭제
        } catch (StringIndexOutOfBoundsException e) {
            url = null;
        }
        return url;
    }

    public void deleteFile(String fileName) throws IOException {
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (SdkClientException e){
            throw new IOException("Error deleting file from S3", e);
        }
    }

    private static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(String findName, File file) {
        final TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build(); // AWS S3 전송 객체 생성

        try {
            transferManager
                    .upload(new PutObjectRequest(bucket, findName, file))
                    .waitForCompletion();
        } catch (AmazonClientException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }

}
