package com.example.boardv1.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;

@Import(UserRepository.class)
@DataJpaTest // EntityManager가 ioc에 등록됨
public class UserRepositoryTest {

    @Autowired // 어노테이션 DI 기법.
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void save_fail_test() {
        // given
        User user = new User();
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@naver.com");
        // when
        User user2 = userRepository.save(user);

        // eye
        System.out.println(user2);
    }

    @Test
    public void save_test() {
        // given
        User user = new User();
        user.setUsername("love");
        user.setPassword("1234");
        user.setEmail("love@naver.com");
        // when
        User user2 = userRepository.save(user);

        // eye
        System.out.println(user2);
    }

    @Test
    public void findByUsername_test() {
        // given
        String username = "ssar";
        // when
        User findUser = userRepository.findByUsername(username);
        // eye
        System.out.println(findUser);
    }
}
