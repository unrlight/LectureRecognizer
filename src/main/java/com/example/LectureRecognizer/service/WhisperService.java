package com.example.LectureRecognizer.service;

import com.example.LectureRecognizer.model.TranscriptionRequest;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import io.github.givimad.whisperjni.WhisperContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class WhisperService {

    public WhisperService() {}

    public String transcribe(TranscriptionRequest request) throws UnsupportedAudioFileException, IOException {
        final String PATH_TO_LIB = "whisper.dll";
        final String PATH_TO_MODEL = "ggml-medium.bin";

        try {
            var loadOptions = new WhisperJNI.LoadOptions();
            loadOptions.logger = System.out::println;
            loadOptions.whisperLib = Paths.get(PATH_TO_LIB);

            WhisperJNI.loadLibrary(loadOptions);
            //WhisperJNI.setLibraryLogger(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Whisper JNI library", e);
        }

        var whisper = new WhisperJNI();

        float[] samples = loadAudioSamples(request.getFile());

        WhisperContext ctx;
        try {
            ctx = whisper.init(Paths.get(PATH_TO_MODEL));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Whisper context", e);
        }

        var params = new WhisperFullParams();
        int result = whisper.full(ctx, params, samples, samples.length);

        if (result != 0) {
            throw new RuntimeException("Transcription failed with code " + result);
        }

        String text = whisper.fullGetSegmentText(ctx, 0);

        ctx.close();
        System.out.println(text);
        return text;
    }

    private float[] loadAudioSamples(MultipartFile audioFile) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = new BufferedInputStream(audioFile.getInputStream());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        AudioFormat originalFormat = audioInputStream.getFormat();

        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                16000,
                16,
                1,
                2,
                16000,
                false
        );

        if (!AudioSystem.isConversionSupported(targetFormat, originalFormat)) {
            throw new UnsupportedAudioFileException("Cant convert audio file to PCM_SIGNED");
        }

        AudioInputStream pcmInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);

        byte[] audioBytes = pcmInputStream.readAllBytes();
        float[] audioSamples = new float[audioBytes.length / 2];

        for (int i = 0, j = 0; i < audioBytes.length; i += 2, j++) {
            int sample = (audioBytes[i + 1] << 8) | (audioBytes[i] & 0xff);
            audioSamples[j] = sample / 32768f;
        }

        return audioSamples;
    }

}