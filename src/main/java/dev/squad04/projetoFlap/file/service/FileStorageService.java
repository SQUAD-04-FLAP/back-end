package dev.squad04.projetoFlap.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload storage", e);
        }
    }

    public String salvarArquivo(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Empty file.");
            }

            String nomeOriginal = file.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf('.'));
            }

            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Files.copy(file.getInputStream(),this.rootLocation.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);

            return nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    public Path carregarArquivo(String filename) {
        return rootLocation.resolve(filename);
    }

    public void deletarArquivo(String filename) {
        try {
            Files.deleteIfExists(rootLocation.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }
}
