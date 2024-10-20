package com.asdc.dalexchange.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Utility class for handling image uploads to Cloudinary.
 */
@Component
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    /**
     * Constructor for CloudinaryUtil.
     *
     * @param cloudinary the Cloudinary instance.
     */
    @Autowired
    public CloudinaryUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Uploads an image file to Cloudinary and returns the URL of the uploaded image.
     *
     * @param imageFile the image file to be uploaded.
     * @return the URL of the uploaded image.
     * @throws RuntimeException if an error occurs during the upload.
     */
    public String uploadImage(MultipartFile imageFile) {
        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }
}
