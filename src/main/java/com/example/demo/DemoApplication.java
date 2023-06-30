package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

@SpringBootApplication
@RestController
public class DemoApplication {

	@Value("${game.file.v1.path}")
	private String gameJsonV1Path;
	@Value("${game.file.v2.path}")
	private String gameJsonV2Path;

	@GetMapping("/")
	String home() {
		return "Spring is here! I want to test Auto Devops!";
	}

	@GetMapping("/v1/json")
	String getGameV1Json() {
		return getTargetFileJson(gameJsonV1Path);
	}

	@GetMapping("/v2/json")
	String getGameV2Json() {
		return getTargetFileJson(gameJsonV2Path);
	}

	private String getTargetFileJson(String folderPath) {
		File file = new File(folderPath);

		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles((dir, name) -> name.endsWith(".json"));
			assert files != null;
			File targetFile = Arrays.stream(files).max(Comparator.comparing(File::lastModified)).orElse(null);
			if (targetFile != null && targetFile.exists() && targetFile.isFile()) {
				try (Reader reader = new InputStreamReader(Files.newInputStream(targetFile.toPath()), StandardCharsets.UTF_8)) {
					return FileCopyUtils.copyToString(reader);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

		}
		return "Spring is here! I want to test Auto Devops!";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}