package com.dai.dai.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dai.dai.service.CloudinaryService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;
    @Value("${cloudinary.cloud.name}")
    String cloudName;
    @Value("${cloudinary.apiKey}")
    String apiKey;
    @Value("${cloudinary.apiSecret}")
    String apiSecret;

    @PostConstruct
    public void init() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", cloudName);
        valuesMap.put("api_key", apiKey);
        valuesMap.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(valuesMap);
    }

    public String upload(String imageUrl) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(imageUrl, new HashMap<>());
        return (String) uploadResult.get("secure_url");
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }

    @Override
    public Map delete(String id) throws IOException {
        return null;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
