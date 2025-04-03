package com.devices.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/SkincareBookingSystem/";

    public void deleteFile(String path) {
        CompletableFuture.runAsync(() -> {
            try {
                Path filePath = Paths.get(ROOT_PATH, path);

                if (path.equals("assets/images/base/admin/base_user.png")) {
                    System.out.println("Không thể xóa ảnh mặc định: {}"+ path);
                    return;
                }

                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    System.out.println("File tại {} đã được xóa thành công."+ filePath);
                } else {
                    System.out.println("File tại {} không tồn tại."+ filePath);
                }
            } catch (IOException e) {
                System.out.println("Có lỗi xảy ra khi xóa file: {}"+ e.getMessage());
            }
        });
    }

    public String uploadFile(MultipartFile file, String path) {
        try {
            // Danh sách các định dạng hình ảnh cho phép
            String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};
            // Lấy phần mở rộng của file
            String extension = getFileExtension(file.getOriginalFilename());

            // Kiểm tra xem phần mở rộng có hợp lệ không
            if (!isAllowedExtension(extension, allowedExtensions)) {
                logger.info("Chỉ cho phép tải lên hình ảnh (.jpg, .jpeg, .png, .gif).");
                return "Chỉ cho phép tải lên hình ảnh (.jpg, .jpeg, .png, .gif).";
            }

            // Tạo tên file duy nhất bằng UUID
            String fileName = UUID.randomUUID() + extension;
            // Thư mục lưu file
            File uploadDir = new File(ROOT_PATH + "/" + path);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lưu file vào thư mục
            File savedFile = new File(uploadDir, fileName);
            try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                fos.write(file.getBytes());
            }

            // Tạo URL của file đã tải lên
            String fileUrl = path + "/" + fileName;
            logger.info("Tải file thành công: {}", fileUrl);
            return fileUrl; // Trả về URL của file đã tải lên

        } catch (IOException e) {
            // Log lỗi và trả về thông báo lỗi
            logger.error("Upload hình thất bại: {}", e.getMessage());
            return "Upload hình thất bại: " + e.getMessage();
        }
    }


    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    private boolean isAllowedExtension(String extension, String[] allowedExtensions) {
        for (String ext : allowedExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
