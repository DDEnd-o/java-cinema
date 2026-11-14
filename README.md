# CinemaBooking

Website đặt vé xem phim trực tuyến — đồ án môn học, xây dựng bằng Spring Boot (MVC + Thymeleaf), Spring Data JPA, Spring Security và MySQL.

## Công nghệ

- Java 17, Spring Boot 3.2.5
- Spring MVC + Thymeleaf
- Spring Data JPA (Hibernate)
- Spring Security (đăng nhập bằng form, mã hoá BCrypt, phân quyền theo Role)
- MySQL 8

## Chạy project

1. Tạo database MySQL khớp với các Entity trong `src/main/java/com/cinemabooking/entity` (ứng dụng dùng `spring.jpa.hibernate.ddl-auto=validate` nên không tự tạo bảng, phải có sẵn schema trước).
2. Cập nhật `src/main/resources/application.properties`: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` theo MySQL thật của bạn.
3. Chạy bằng Maven Wrapper (không cần cài Maven riêng):
   ```bash
   ./mvnw spring-boot:run
   ```
4. Mở http://localhost:8080

## Cấu trúc thư mục

- `entity/` — JPA entity khớp schema CSDL (bao gồm `BookingSeats` với ràng buộc `UNIQUE(ShowtimeID, SeatID)` để chống đặt trùng ghế)
- `repository/` — Spring Data JPA repository
- `service/` — xử lý nghiệp vụ (đặt vé, thanh toán, chống trùng ghế qua `@Transactional` + ràng buộc UNIQUE ở DB)
- `controller/` — MVC controller (khách hàng + `admin/`)
- `security/` — Spring Security `UserDetailsService` tự viết, cấu hình phân quyền theo Role
- `templates/` — giao diện Thymeleaf
