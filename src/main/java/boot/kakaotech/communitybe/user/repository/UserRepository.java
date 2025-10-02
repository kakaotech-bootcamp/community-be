package boot.kakaotech.communitybe.user.repository;

import boot.kakaotech.communitybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
