package com.nekochan.uploadfile.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FirebaseStorageFileService {
    String uploadFile(File file, String fileName) throws IOException;
    File convertToFile(MultipartFile multipartFile, String fileName);
    String getExtension(String fileName);
}
