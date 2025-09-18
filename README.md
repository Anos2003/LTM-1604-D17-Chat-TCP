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

Trong bối cảnh 🌐 công nghệ thông tin phát triển nhanh chóng, nhu cầu giao tiếp, trao đổi và làm việc từ xa trở nên ngày càng cần thiết.
Ứng dụng 💬 Chat Nhóm bằng RMI ra đời nhằm hỗ trợ:

👥 Người dùng có thể đăng nhập, tham gia nhóm chat và trò chuyện theo thời gian thực.

🏷️ Quản lý các nhóm chat (tạo mới, tham gia, thoát nhóm).

✉️ Gửi và nhận tin nhắn ngay lập tức nhờ cơ chế Remote Method Invocation (RMI).

📜 Xem lại lịch sử trò chuyện đã lưu trong cơ sở dữ liệu.

Đối với quản trị viên (Admin):

👤 Quản lý tài khoản người dùng.

🛠️ Quản lý nhóm chat, phân quyền.

📊 Theo dõi log hệ thống và hoạt động của người dùng.

2. 🎯 Mục tiêu hệ thống
🔹 Người dùng:

📝 Đăng ký / Đăng nhập.

💬 Tham gia vào một hoặc nhiều nhóm chat.

✍️ Gửi và nhận tin nhắn thời gian thực.

📜 Xem lại lịch sử tin nhắn.

🔹 Quản trị viên:

🧑‍💻 Quản lý người dùng (thêm, xóa, khóa tài khoản).

🗂️ Quản lý nhóm chat (tạo, sửa, xóa).

📈 Thống kê (số lượng người dùng online, số tin nhắn đã gửi).

3. 🛠️ Ngôn ngữ & Công nghệ sử dụng

☕ Java (JDK 11+)

🔗 Java RMI – Remote Method Invocation

🎨 JavaFX (FXML) – thiết kế giao diện

🗄️ MySQL / SQLite – cơ sở dữ liệu

📦 Maven/Gradle – công cụ quản lý build

💻 IDE: Eclipse / IntelliJ IDEA

🌍 GitHub – quản lý mã nguồn và version

4. 📂 Cấu trúc hệ thống
ChatRMI/
├─ 📁 common/        # Chứa model & interface dùng chung
│   ├─ model/        # Lớp User, Message, Group
│   └─ interfaces/   # ChatServerInterface.java
│
├─ 📁 server/        # Chứa logic của server
│   ├─ ChatServerImpl.java
│   ├─ ChatServerMain.java
│   └─ utils/
│
├─ 📁 client/        # Ứng dụng JavaFX
│   ├─ ClientApp.java
│   ├─ controller/
│   └─ fxml/
│
├─ 📁 db/            # Schema SQL (schema_chat.sql)
└─ 📄 pom.xml        # Cấu hình Maven

5. 🗄️ Cơ sở dữ liệu
Bảng chính:

👤 users

id, username, password_hash, role, created_at

🏷️ groups

id, name, created_by, created_at

💬 messages

id, group_id, sender_id, content, sent_at

6. 🏗️ Sơ đồ kiến trúc hệ thống
         👤 Client A (JavaFX)
               |
         👤 Client B (JavaFX)
               |
         👤 Client C (JavaFX)
               |
        -----------------------
        |     🌐 RMI Server    |
        |  (ChatServerImpl)   |
        -----------------------
          |           |
   📂 Database    📜 RMI Registry
   (MySQL/SQLite)     (Port 1099)


📌 Luồng hoạt động:

Người dùng 👤 thao tác trên Client (giao diện JavaFX).

Client gọi hàm trên ChatServerInterface thông qua 📜 RMI Registry.

🌐 Server xử lý request, lưu vào 📂 Database nếu cần.

Server gửi phản hồi hoặc broadcast message đến các client khác trong nhóm.

7. ⚙️ Các bước cài đặt & chạy
🔧 Chuẩn bị môi trường

☕ Cài đặt JDK 11+.

📦 Cài đặt Maven.

🗄️ Cài đặt MySQL.

▶️ Cài đặt & chạy

⬇️ Clone project:

git clone https://github.com/your-username/chat-rmi.git


🗄️ Import cơ sở dữ liệu:

mysql -u root -p < db/schema_chat.sql


🚀 Khởi động RMI Registry:

rmiregistry 1099


🖥️ Chạy server:

java server.ChatServerMain


💻 Mở client (JavaFX):

java client.ClientApp

8. 🔒 Bảo mật & An toàn

🛡️ Hash mật khẩu bằng bcrypt/argon2.

🔐 Mã hóa RMI bằng SSL/TLS hoặc triển khai trong VPN.

🔥 Cấu hình tường lửa để giới hạn cổng 1099.

📝 Ghi log đăng nhập & tin nhắn.

👥 Phân quyền rõ ràng User/Admin.

9. 🌟 Hướng phát triển trong tương lai

📱 Hỗ trợ client trên mobile (Android/iOS).

📡 Tích hợp WebSocket/gRPC để tăng hiệu suất.

📁 Cho phép gửi file, hình ảnh, emoji.

🔭 Phân quyền nâng cao (moderator, super admin).

📊 Dashboard giám sát real-time cho admin.

10. ✅ Checklist trước khi bàn giao

 🔑 Hash mật khẩu trong DB

 📦 Import database thành công

 🏃 Server + Client hoạt động ổn định

 🎨 Giao diện JavaFX đầy đủ (login, register, lobby, chat, admin)

 📜 README + hướng dẫn triển khai rõ ràng

11. 🏁 Kết luận

Ứng dụng 💬 Chat Nhóm bằng RMI là một dự án mang tính học thuật giúp sinh viên:

Hiểu rõ cách triển khai hệ thống phân tán bằng Java.

Rèn luyện kỹ năng lập trình giao diện (JavaFX).

Thực hành kết nối cơ sở dữ liệu & quản lý người dùng.

Tích hợp bảo mật cơ bản cho ứng dụng mạng.