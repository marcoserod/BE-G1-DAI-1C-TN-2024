package com.dai.dai.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    String upload(String imageUrl) throws IOException;

    Map delete(String id) throws IOException;

    File convert(MultipartFile multipartFile) throws IOException;
}
