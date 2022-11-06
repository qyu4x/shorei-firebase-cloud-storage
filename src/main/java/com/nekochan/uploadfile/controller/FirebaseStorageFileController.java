package com.nekochan.uploadfile.controller;

import com.nekochan.uploadfile.model.response.FirebaseStorageReponse;
import com.nekochan.uploadfile.model.response.WebResponse;
import com.nekochan.uploadfile.service.FirebaseStorageFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FirebaseStorageFileController {

    private final FirebaseStorageFileService  firebaseStorageFileService;

    @Autowired
    public FirebaseStorageFileController(FirebaseStorageFileService firebaseStorageFileService) {
        this.firebaseStorageFileService = firebaseStorageFileService;
    }

    @PostMapping("/upload/picture")
    public ResponseEntity<WebResponse<FirebaseStorageReponse>> upload(@RequestParam("multipartFile") MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        try {
            String uniqueFileName = UUID.randomUUID().toString()
                    .concat(firebaseStorageFileService.getExtension(originalFilename));

            File file = firebaseStorageFileService.convertToFile(multipartFile, uniqueFileName);
            String url = firebaseStorageFileService.uploadFile(file, uniqueFileName);
            file.delete();
            return new ResponseEntity<>(
                    new WebResponse<FirebaseStorageReponse>(
                            HttpStatus.OK.value(),
                            HttpStatus.OK.getReasonPhrase(),
                            new FirebaseStorageReponse(
                                    url
                            )
                    ),
                    HttpStatus.OK
            );
        }catch (IOException exception) {
            log.error("error {} ", exception.getMessage());
            return new ResponseEntity<>(
                    new WebResponse<FirebaseStorageReponse>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            null
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
