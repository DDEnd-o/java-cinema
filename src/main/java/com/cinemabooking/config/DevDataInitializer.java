package com.cinemabooking.config;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.entity.MovieStatus;
import com.cinemabooking.entity.Role;
import com.cinemabooking.entity.Room;
import com.cinemabooking.entity.Seat;
import com.cinemabooking.entity.SeatType;
import com.cinemabooking.entity.Showtime;
import com.cinemabooking.entity.User;
import com.cinemabooking.repository.MovieRepository;
import com.cinemabooking.repository.RoomRepository;
import com.cinemabooking.repository.SeatRepository;
import com.cinemabooking.repository.ShowtimeRepository;
import com.cinemabooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataInitializer {

    @Bean
    public CommandLineRunner seedDevData(UserRepository userRepository,
                                         RoomRepository roomRepository,
                                         SeatRepository seatRepository,
                                         MovieRepository movieRepository,
                                         ShowtimeRepository showtimeRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("Quản trị viên");
            admin.setEmail("admin@cinema.local");
            admin.setPhone("0900000000");
            admin.setRole(Role.admin);
            userRepository.save(admin);

            User customer = new User();
            customer.setUsername("khach1");
            customer.setPasswordHash(passwordEncoder.encode("customer123"));
            customer.setFullName("Nguyễn Văn Khách");
            customer.setEmail("khach1@cinema.local");
            customer.setPhone("0900000001");
            customer.setRole(Role.customer);
            userRepository.save(customer);

            Room room1 = roomRepository.save(new Room("Phòng 1 (IMAX)", 40));
            Room room2 = roomRepository.save(new Room("Phòng 2 (Dolby 7.1)", 40));

            for (Room room : List.of(room1, room2)) {
                for (int r = 0; r < 5; r++) {
                    String rowLabel = String.valueOf((char) ('A' + r));
                    for (int n = 1; n <= 8; n++) {
                        Seat seat = new Seat();
                        seat.setRoom(room);
                        seat.setSeatRow(rowLabel);
                        seat.setSeatNumber(n);
                        seat.setSeatType(r == 4 ? SeatType.vip : SeatType.standard);
                        seatRepository.save(seat);
                    }
                }
            }

            Movie m1 = new Movie();
            m1.setTitle("Hành Trình Vũ Trụ: Kỷ Nguyên Mới");
            m1.setGenre("Hành động, Khoa học viễn tưởng");
            m1.setDurationMinutes(128);
            m1.setDescription("Một nhóm phi hành gia tinh nhuệ phải vượt qua lỗ đen vũ trụ và giới hạn của chính mình để giải cứu Trái Đất trước thảm họa thiên thạch diệt vong.");
            m1.setPosterUrl("https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=600&q=80");
            m1.setReleaseDate(LocalDate.of(2026, 10, 20));
            m1.setStatus(MovieStatus.showing);
            m1 = movieRepository.save(m1);

            Movie m2 = new Movie();
            m2.setTitle("Đêm Rực Rỡ Giữa Sài Gòn");
            m2.setGenre("Tình cảm, Hài hước");
            m2.setDurationMinutes(112);
            m2.setDescription("Câu chuyện hài hước nhưng đầy lắng đọng về cuộc hội ngộ định mệnh giữa hai tâm hồn cô đơn giữa nhịp sống sôi động về đêm của thành phố.");
            m2.setPosterUrl("https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?auto=format&fit=crop&w=600&q=80");
            m2.setReleaseDate(LocalDate.of(2026, 10, 28));
            m2.setStatus(MovieStatus.showing);
            m2 = movieRepository.save(m2);

            Movie m3 = new Movie();
            m3.setTitle("Mật Vụ Bóng Đêm: Truy Kích");
            m3.setGenre("Hành động, Hình sự, Giật gân");
            m3.setDurationMinutes(135);
            m3.setDescription("Đặc vụ ngầm xuất sắc nhất buộc phải đối đầu với tổ chức tội phạm xuyên quốc gia trong cuộc đua nghẹt thở kéo dài 48 giờ.");
            m3.setPosterUrl("https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80");
            m3.setReleaseDate(LocalDate.of(2026, 11, 2));
            m3.setStatus(MovieStatus.showing);
            m3 = movieRepository.save(m3);

            Movie m4 = new Movie();
            m4.setTitle("Huyền Thoại Rừng Thiêng");
            m4.setGenre("Phiêu lưu, Giả tưởng");
            m4.setDurationMinutes(118);
            m4.setDescription("Hành trình khám phá vùng đất cổ đại bị lãng quên với những sinh vật huyền bí và bí mật ngàn năm được cất giấu.");
            m4.setPosterUrl("https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=600&q=80");
            m4.setReleaseDate(LocalDate.of(2026, 11, 10));
            m4.setStatus(MovieStatus.showing);
            m4 = movieRepository.save(m4);

            Movie m5 = new Movie();
            m5.setTitle("Vương Quốc Bóng Tối");
            m5.setGenre("Kinh dị, Bí ẩn");
            m5.setDurationMinutes(115);
            m5.setDescription("Một thị trấn nhỏ vùng biên viễn bị cuốn vào lời nguyền cổ xưa khi ánh mặt trời cuối cùng dần tắt hẳn.");
            m5.setPosterUrl("https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&w=600&q=80");
            m5.setReleaseDate(LocalDate.of(2026, 12, 5));
            m5.setStatus(MovieStatus.upcoming);
            movieRepository.save(m5);

            Movie m6 = new Movie();
            m6.setTitle("Biệt Đội Nhí Phiêu Lưu Ký");
            m6.setGenre("Hoạt hình, Gia đình");
            m6.setDurationMinutes(98);
            m6.setDescription("Nhóm bạn nhỏ dũng cảm cùng nhau bước vào cánh cổng phép thuật phía sau thư viện cũ để giải cứu vương quốc đồ chơi.");
            m6.setPosterUrl("https://images.unsplash.com/photo-1536440136628-849c177e76a1?auto=format&fit=crop&w=600&q=80");
            m6.setReleaseDate(LocalDate.of(2026, 12, 20));
            m6.setStatus(MovieStatus.upcoming);
            movieRepository.save(m6);

            addShowtime(showtimeRepository, m1, room1, LocalDateTime.of(2026, 11, 18, 10, 0), LocalDateTime.of(2026, 11, 18, 12, 8), 85000);
            addShowtime(showtimeRepository, m1, room1, LocalDateTime.of(2026, 11, 18, 19, 30), LocalDateTime.of(2026, 11, 18, 21, 38), 95000);
            addShowtime(showtimeRepository, m1, room2, LocalDateTime.of(2026, 11, 19, 14, 15), LocalDateTime.of(2026, 11, 19, 16, 23), 85000);

            addShowtime(showtimeRepository, m2, room2, LocalDateTime.of(2026, 11, 18, 18, 0), LocalDateTime.of(2026, 11, 18, 19, 52), 80000);
            addShowtime(showtimeRepository, m2, room1, LocalDateTime.of(2026, 11, 19, 20, 15), LocalDateTime.of(2026, 11, 19, 22, 7), 90000);

            addShowtime(showtimeRepository, m3, room1, LocalDateTime.of(2026, 11, 18, 15, 0), LocalDateTime.of(2026, 11, 18, 17, 15), 90000);
            addShowtime(showtimeRepository, m3, room2, LocalDateTime.of(2026, 11, 18, 21, 30), LocalDateTime.of(2026, 11, 18, 23, 45), 95000);

            addShowtime(showtimeRepository, m4, room2, LocalDateTime.of(2026, 11, 18, 11, 30), LocalDateTime.of(2026, 11, 18, 13, 28), 75000);
            addShowtime(showtimeRepository, m4, room1, LocalDateTime.of(2026, 11, 19, 17, 30), LocalDateTime.of(2026, 11, 19, 19, 28), 85000);
        };
    }

    private void addShowtime(ShowtimeRepository repo, Movie movie, Room room,
                             LocalDateTime start, LocalDateTime end, int price) {
        Showtime st = new Showtime();
        st.setMovie(movie);
        st.setRoom(room);
        st.setStartTime(start);
        st.setEndTime(end);
        st.setPrice(BigDecimal.valueOf(price));
        repo.save(st);
    }
}
