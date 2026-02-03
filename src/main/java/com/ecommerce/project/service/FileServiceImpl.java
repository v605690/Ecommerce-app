package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the file names of the current / original file
        String originalFilename = file.getOriginalFilename();
        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // image.jpg --> 123 --> 123.jpg
        assert originalFilename != null;
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        // Check if a path exists and create
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // return file name
        return fileName;
    }
}
