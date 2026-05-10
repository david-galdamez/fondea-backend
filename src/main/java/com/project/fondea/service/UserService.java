package com.project.fondea.service;

import com.project.fondea.repository.CreatorProfileRepository;
import com.project.fondea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CreatorProfileRepository creatorProfileRepository;
}
