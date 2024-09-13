package com.example.LectureRecognizer.model;

import org.springframework.web.multipart.MultipartFile;

public class TranscriptionRequest {
    private MultipartFile file;

    public TranscriptionRequest(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

}
