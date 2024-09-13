package com.example.LectureRecognizer.controller;

import com.example.LectureRecognizer.model.TranscriptionRequest;
import com.example.LectureRecognizer.model.TranscriptionResult;
import com.example.LectureRecognizer.viewmodel.TranscriptionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Controller
@RequestMapping("/audio")
public class AudioController {

    @Autowired
    private TranscriptionViewModel transcriptionViewModel;

    @PostMapping("/upload")
    public String uploadAudio(@RequestParam("file") MultipartFile file, Model model) throws UnsupportedAudioFileException, IOException {
        TranscriptionRequest request = new TranscriptionRequest(file);
        TranscriptionResult result = transcriptionViewModel.processAudio(request);
        model.addAttribute("result", result.getSummary());
        return "index";
    }
}
