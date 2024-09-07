# LectureRecognizer

## Description

**LectureRecognizer** is a web application built on Java Spring that enables automatic recognition of lecture audio files using the **Whisper API** and generates detailed summaries using **Llama 3.1 8b**. This is an ideal tool for students, educators, and researchers who want to quickly obtain a text version of a lecture and a concise summary for easier understanding.

## Key Features

- **Speech Recognition**: Automatic conversion of audio files (WAV format) to text using Whisper.
- **Text Segmentation**: Intelligent text segmentation into 1000-token chunks using TokenCounter for more efficient processing.
- **Text Summarization**: Generation of concise text summaries using the Llama 3.1 model.
- **Markdown Support**: The resulting text is displayed in a readable Markdown format.
- **Ease of Use**: Upload audio files and receive text and summarized versions of lectures through a simple web interface.

## Technologies

- **Java**
- **Spring Boot**
- **Thymeleaf** — a templating engine for data rendering.
- **Whisper** — for speech recognition.
- **OLlama** — for generating concise summaries.
- **Docker** — for application containerization.

## Installation and Usage

### 1. Clone the Repository

```bash
git clone https://github.com/unrlight/LectureRecognizer.git
cd LectureRecognizer
```

### 2. Start with Docker Compose

Ensure that Docker and Docker Compose are installed.

```bash
docker-compose up --build
```

This will start the application and all necessary services. The application will be available at `http://localhost:8080`.

### 3. Usage

1. Navigate to `http://localhost:8080`.
2. Upload an audio file in WAV format.
3. Obtain the text version of the lecture and a concise summary in Markdown format.