package com.example.LectureRecognizer.viewmodel;

import com.example.LectureRecognizer.model.TranscriptionRequest;
import com.example.LectureRecognizer.model.TranscriptionResult;
import com.example.LectureRecognizer.service.WhisperService;
import com.example.LectureRecognizer.service.LlamaService;
import com.example.LectureRecognizer.service.TokenCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

@Component
public class TranscriptionViewModel {

    @Autowired
    private WhisperService whisperService;

    @Autowired
    private LlamaService llamaService;

    @Autowired
    private TokenCounterService tokenCounterService;

    public TranscriptionResult processAudio(TranscriptionRequest request) throws UnsupportedAudioFileException, IOException {
        String SummarizedText;
        // I. Recognize
        String transcribedText = whisperService.transcribe(request);

        // II. Text to chunks
        List<String> chunks = tokenCounterService.splitTextIntoChunks(transcribedText);

        // III. Input chunks
        for (String chunk : chunks) {
            llamaService.InputText(chunk);
        }

        // IV. Summarize
        SummarizedText = llamaService.summarize();

        return new TranscriptionResult(SummarizedText.trim());
    }
}
