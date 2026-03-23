package com.project.ecommerce.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    
    @Override
    public void deleteFile(String path) {

        try {
            Path filePath = Paths.get(path);
            Files.deleteIfExists(filePath);
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    };

    @Override
    public void deleteFiles(List<String> paths) {
            paths.forEach(this::deleteFile);
    }

}
