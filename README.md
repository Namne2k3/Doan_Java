# Doan_Java


Cách Khởi Chạy Dự Án
1. Thiết Lập Lưu Trữ Ảnh
Thay Đổi Đường Dẫn Lưu Trữ Ảnh
Mở file UploadController (Đây là nơi để lưu trữ ảnh).
Thay đổi đường dẫn chứa thư mục ảnh để trỏ tới thư mục public/images/ trong thư mục frontend.
java
Copy code
// Ví dụ thay đổi trong UploadController
String imageDirectory = "/path/to/frontend/public/images/";
// Hãy thay đổi "/path/to/frontend/" thành đường dẫn thực tế của bạn.
2. (Optional) Test Chức Năng Thanh Toán Quốc Tế VISA
Tải Stripe CLI
Tải xuống Stripe CLI theo đường dẫn: Stripe CLI v1.20.0
Chọn phiên bản phù hợp với hệ điều hành của bạn và giải nén:
Windows: stripe_1.20.0_windows_x86_64.zip
MacOS: stripe_1.20.0_mac-os_x86_64.tar.gz
Thiết Lập Biến Môi Trường cho Stripe
Sau khi giải nén, thiết lập biến môi trường cho Stripe bằng cách thêm đường dẫn tới file stripe.exe (hoặc file thực thi tương ứng trên MacOS) vào PATH của bạn.

Windows:

Mở "System Properties" (Windows + Pause/Break).
Chọn "Environment Variables".
Thêm đường dẫn của stripe.exe vào "Path" trong phần "User variables" hoặc "System variables".
Ví dụ: C:\path\to\stripe\directory
MacOS:

Mở file .bash_profile hoặc .zshrc trong terminal.
Thêm dòng: export PATH="$PATH:/path/to/stripe/directory"
Lưu lại và chạy lệnh source ~/.bash_profile hoặc source ~/.zshrc.
Chạy Stripe CLI
Mở terminal và chạy lệnh sau để bắt đầu lắng nghe các webhook từ Stripe và chuyển tiếp chúng tới địa chỉ localhost:8080/webhook:

bash
Copy code
stripe listen --forward-to localhost:8080/webhook
3. Khởi Chạy Ứng Dụng
Khởi Chạy Phía Frontend
Mở terminal tại thư mục frontend.

Chạy lệnh:

bash
Copy code
npm start
Khởi Chạy Phía Backend
Mở thư mục backend bằng IntelliJ IDEA.
Chạy ứng dụng Spring Boot bằng cách ấn RunApplication.
Lưu ý: Hãy chắc chắn rằng bạn đã cài đặt đầy đủ các phụ thuộc cần thiết cho cả backend và frontend trước khi khởi chạy dự án. Nếu có bất kỳ vấn đề gì trong quá trình thiết lập và khởi chạy, vui lòng kiểm tra lại các bước hoặc liên hệ với nhóm phát triển để được hỗ trợ.
