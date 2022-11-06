package com.nekochan.uploadfile.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.nekochan.uploadfile.service.FirebaseStorageFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class FirebaseStorageFileServiceImpl implements FirebaseStorageFileService {

    @Value("${application.config.firebase.private_key}")
    private String firebasePrivateKey;

    @Value("${application.config.firebase.bucket}")
    private String firebaseBucket;

    @Value("${application.config.firebase.download}")
    private String downloadUrl;

    @Override
    public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(firebaseBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media").build();
        Credentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource(this.firebasePrivateKey).getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(downloadUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));


    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) {
        File temporaryFile = new File(fileName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (FileNotFoundException exception) {
            throw new RuntimeException(exception);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return temporaryFile;
    }

    @Override
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
