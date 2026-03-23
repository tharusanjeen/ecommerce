package com.project.ecommerce.service;

import java.util.List;

public interface FileStorageService {

    /**
     * Delete a file
     * 
     * @param path the file path to delete
     */
    public void deleteFile(String path);

    /**
     * Delete multiple files
     * 
     * @param paths the file paths to delete
     */
    public void deleteFiles(List<String> paths);
    
}
