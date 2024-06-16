package blog_spring.blog_spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {

    public static String UPLOAD_DIRECTORY = "D:\\File\\Code\\Lap Trinh Ung Dung Java\\Doan_Java\\Doan_Java\\frontend\\public\\images";

    @PostMapping("/api/v1/ckeditor/upload")
    public ResponseEntity<Map<String, Object>> uploadImageFromCkeditor(@RequestParam("upload") MultipartFile file) throws IOException {
        // Kiểm tra xem file có tồn tại không
        if (file.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", false);
            response.put("url", "");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Tạo tên ngẫu nhiên cho file
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            // Tạo đường dẫn cho file
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
            // Ghi file vào đường dẫn
            Files.write(filePath, file.getBytes());

            // Trả về đường dẫn file đã upload
            String fileUrl = "/images/" + fileName;
            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", true);
            response.put("url", fileUrl);
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            // Xử lý nếu có lỗi khi ghi file
            e.printStackTrace();

            // dạng object cần tr về của ckeditor
            // json: { uploaded: true/false, url = `path-to-your-file`}
            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", false);
            response.put("url", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/v1/upload")
    public ResponseEntity<String> uploadImage(@RequestParam(value = "image", required = false) MultipartFile file,
                                              @RequestParam(value = "images", required = false) List<MultipartFile> files) throws IOException {
        StringBuilder fileNames = new StringBuilder();

        if (file != null && !file.isEmpty()) {
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            fileNames.append(file.getOriginalFilename()).append(" ");
        }

        if (files != null && !files.isEmpty()) {
            for (var fileItem : files) {
                Path fileItemNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileItem.getOriginalFilename());
                Files.write(fileItemNameAndPath, fileItem.getBytes());
                fileNames.append(fileItem.getOriginalFilename()).append(" ");
            }
        }

        return ResponseEntity.ok("Successfully uploaded: " + fileNames.toString());
    }
}
