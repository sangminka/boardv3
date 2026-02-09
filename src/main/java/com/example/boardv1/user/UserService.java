package com.example.boardv1.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.boardv1._core.errors.ex.Exception400;
import com.example.boardv1._core.errors.ex.Exception401;
import com.example.boardv1.user.UserRequset.JoinDTO;
import com.example.boardv1.user.UserRequset.LoginDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(JoinDTO joinDTO) {
        // username 중복 체크
        String getUsername = joinDTO.getUsername();
        Optional<User> optUser = userRepository.findByUsername(getUsername);
        if (optUser.isPresent()) {
            throw new Exception400("중복입니다.");

        }

        // 영속성 등록
        User user = new User();
        user.setUsername(joinDTO.getUsername());
        user.setPassword(joinDTO.getPassword());
        user.setEmail(joinDTO.getEmail());

        userRepository.save(user);
    }

    public User 로그인(LoginDTO reqDto) {
        User findUser = userRepository.findByUsername(reqDto.getUsername())
                .orElseThrow(() -> new Exception401("username을 찾을 수 없어요"));

        if (!findUser.getPassword().equals(reqDto.getPassword())) {
            throw new Exception401("패스워드가 일치하지 않아요");
        }
        return findUser;
    }

}
