package com.example.boardv1.user;

import org.springframework.stereotype.Service;

import com.example.boardv1.user.UserRequset.JoinDTO;

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
        User findUser = userRepository.findByUsername(getUsername);
        if (findUser != null) {
            throw new RuntimeException("중복입니다.");

        }

        // 영속성 등록
        User user = new User();
        user.setUsername(joinDTO.getUsername());
        user.setPassword(joinDTO.getPassword());
        user.setEmail(joinDTO.getEmail());

        userRepository.save(user);
    }

}
