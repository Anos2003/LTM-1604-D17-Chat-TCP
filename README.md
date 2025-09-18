<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
  Hệ thống Chat Nhóm bằng RMI
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>
1. 📖 Giới thiệu hệ thống

Hệ thống Chat Nhóm bằng RMI là một ứng dụng mạng phân tán hỗ trợ người dùng:

💬 Tham gia các nhóm chat để trao đổi tin nhắn thời gian thực.

👤 Quản lý tài khoản cá nhân (đăng ký, đăng nhập).

📂 Lưu trữ và truy vấn lịch sử chat.

Đối với quản trị viên, hệ thống cung cấp công cụ để:

👥 Quản lý người dùng.

🛠️ Quản lý nhóm chat (tạo, chỉnh sửa, xoá).

📊 Theo dõi hoạt động & log hệ thống.

2. 🛠️ Ngôn ngữ & Công nghệ chính

☕ Java (JDK 11+)

🔗 Java RMI (Remote Method Invocation)

🎨 JavaFX (FXML) – giao diện người dùng

🗄️ MySQL / SQLite – lưu trữ dữ liệu (user, group, message)

📦 Maven / Gradle – quản lý build

💻 IDE: Eclipse / IntelliJ IDEA

3. 🖼️ Hình ảnh các chức năng

🚪 Màn hình đăng nhập / đăng ký — image

🏠 Trang sảnh chờ (Lobby) — image

💬 Giao diện chat nhóm — image

🛠️ Giao diện quản trị (Admin panel) — image

📜 Xem lịch sử chat — image

4. 📚 Một số project demo trên platform

🎯 Demo cơ bản: Chat nhóm sử dụng RMI Registry + nhiều Client.

🔐 Demo nâng cao: Tích hợp xác thực, lưu trữ tin nhắn vào DB.

🚀 Demo mở rộng: Bổ sung TLS/SSL cho kênh RMI, phân quyền Admin/User.

5. ⚙️ Các bước cài đặt & triển khai
🔧 Chuẩn bị môi trường

Cài đặt JDK 11+ → Kiểm tra bằng java -version.

Cài đặt Maven/Gradle → Kiểm tra bằng mvn -v.

(Tuỳ chọn) Cài đặt MySQL để lưu dữ liệu.