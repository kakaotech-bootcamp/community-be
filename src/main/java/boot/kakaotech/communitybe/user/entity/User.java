package boot.kakaotech.communitybe.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 80, nullable = false)
    private String password;

    @Column(length = 12, nullable = false, unique = true)
    private String nickname;

    @Column
    private String profileImageUrl;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime deletedAt;

}
