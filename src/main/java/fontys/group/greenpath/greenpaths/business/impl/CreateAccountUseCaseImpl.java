package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.CreateAccountUseCase;
import fontys.group.greenpath.greenpaths.business.exceptions.UsernameExistsException;
import fontys.group.greenpath.greenpaths.domain.requests.CreateAccountRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateAccountResponse;
import fontys.group.greenpath.greenpaths.persistence.UserInfoRepository;
import fontys.group.greenpath.greenpaths.persistence.UserRepository;
import fontys.group.greenpath.greenpaths.persistence.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CreateAccountUseCaseImpl implements CreateAccountUseCase {
    private UserRepository userRepository;
    private UserInfoRepository userInfoRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        if (userRepository.existsByUsername(request.getUsername())){
            throw new UsernameExistsException();
        }

        UserEntity userEntity = saveNewAccount(request);
        return CreateAccountResponse.builder()
                .id(userEntity.getId())
                .build();
    }

    private UserEntity saveNewAccount(CreateAccountRequest request){
        UserInfoEntity userInfo = UserInfoEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userInfoEntity(userInfoRepository.save(userInfo))
                .build();

        userEntity.setUserRoles(Set.of(
                UserRoleEntity.builder()
                        .user(userEntity)
                        .role(RoleEnum.USER)
                        .build()));

        return userRepository.save(userEntity);
    }
}
