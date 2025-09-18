<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
  Hệ thống Chat Nhóm bằng RMI +JavaFX
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


## 📌 Giới thiệu
`ChatRMI` là một ứng dụng **chat nhóm thời gian thực** được xây dựng bằng:
- **Java RMI**: hỗ trợ giao tiếp client-server và callback 2 chiều.
- **JavaFX**: xây dựng giao diện đồ họa.
- **JSON (Gson)**: lưu trữ thông tin user và nhóm.

Ứng dụng cho phép nhiều người dùng đăng nhập, tham gia nhóm chat và gửi/nhận tin nhắn theo thời gian thực.

---

## 🏗️ Kiến trúc hệ thống

Hệ thống sử dụng mô hình **Client - Server**:
- **Server**: quản lý danh sách nhóm, user, phân phối tin nhắn đến tất cả client trong cùng nhóm.
- **Client**: gửi tin nhắn đến server và lắng nghe callback để nhận tin nhắn mới.

### Luồng hoạt động:
1. User đăng nhập và tham gia vào một nhóm chat.
2. Client đăng ký callback với server.
3. Khi có user gửi tin nhắn:
   - Server nhận tin nhắn → phát lại (broadcast) đến tất cả client trong nhóm.
   - Client nhận callback → hiển thị tin nhắn trên giao diện.

---

## 📂 Cấu trúc dự án
<pre>
src/
 ├─ client/
 │   ├─ ClientApp.java        <-- chạy client (JavaFX App)
 │   ├─ LoginWindow.java      <-- giao diện đăng nhập & đăng ký
 │   ├─ LobbyWindow.java      <-- giao diện sảnh chờ, chứa 3 nhóm chat mặc định
 │   └─ ChatWindow.java       <-- giao diện chat nhóm
 │
 ├─ common/
 │   ├─ interfaces/
 │   │   ├─ ChatClientInterface.java
 │   │   └─ ChatServerInterface.java
 │   │
 │   ├─ model/
 │   │   ├─ ChatGroup.java
 │   │   ├─ Message.java
 │   │   └─ User.java
 │
 ├─ server/
 │   ├─ ChatServerImpl.java
 │   └─ ChatServerMain.java   <-- chạy server
 │
 ├─ lib/
 │   └─ gson-2.10.1.jar

</pre>
---

## ⚙️ Yêu cầu hệ thống
- **Java JDK 11+**
- **JavaFX SDK**
- **Gson library** (đã kèm trong thư mục `lib/`)
- IDE: Eclipse / IntelliJ IDEA / VS Code

---

## ▶️ Cách chạy ứng dụng

### 1. Clone dự án
```bash
git clone https://github.com/<your-username>/ChatRMI.git
cd ChatRMI
2. Compile

Thêm gson-2.10.1.jar vào classpath khi build.

3. Chạy server
cd src
rmiregistry 1099 &
java server.ChatServerMain
4. Chạy client

Mỗi client chạy ở một cửa sổ khác:
java client.ClientApp
🏗️ Kiến trúc hệ thống

Hệ thống Chat Nhóm bằng Java RMI được thiết kế theo mô hình Client – Server – Database, với thành phần chính như sau:
💡 Tính năng chính
<pre>
         👤 Client A (JavaFX)
               |
         👤 Client B (JavaFX)
               |
         👤 Client C (JavaFX)
               |
        -------------------------
        |      🌐 RMI Server     |
        |   (ChatServerImpl)    |
        -------------------------
          |               |
   📂 Database         📜 RMI Registry
 (MySQL/SQLite)         (Port 1099)

</pre>

# 📌 Giao diện hệ thống Chat RMI

## 1. 🔑 Giao diện Đăng nhập/Đăng ký
<img src="./docs/login.png" alt="Login/Register" width="300"/>

## 2. 🏠 Giao diện Sảnh chờ
<img src="./docs/lobby.png" alt="Lobby" width="300"/>

## 3. 💬 Giao diện Phòng chat
<img src="./docs/chat.png" alt="Chat Window" width="300"/>

🔮 Định hướng phát triển

Thêm tính năng emoji, gửi ảnh trong tin nhắn.

Quản lý nhóm: tạo, xóa, mời user.

Tích hợp cơ sở dữ liệu (MySQL, MongoDB) thay cho JSON.

Mã hóa tin nhắn để tăng bảo mật.

👨‍💻 Tác giả

Dự án được xây dựng phục vụ học tập và nghiên cứu về:

Lập trình mạng (Java RMI)

Ứng dụng desktop với JavaFX

Mô hình Client - Server