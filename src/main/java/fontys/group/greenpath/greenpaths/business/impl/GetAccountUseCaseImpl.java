package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.GetAccountUseCase;
import fontys.group.greenpath.greenpaths.domain.responses.GetAccountResponse;
import fontys.group.greenpath.greenpaths.persistence.UserRepository;
import fontys.group.greenpath.greenpaths.persistence.entity.UserEntity;
import fontys.group.greenpath.greenpaths.persistence.entity.UserInfoEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class GetAccountUseCaseImpl implements GetAccountUseCase {
    private UserRepository userRepository;
    private AccessTokenEncoderDecoderImpl accessTokenEncoderDecoder;

    @Override
    @Transactional
    public GetAccountResponse getAccount(String accessToken) {
        int userId = accessTokenEncoderDecoder.decode(accessToken).getUserId();
        UserEntity user = userRepository.getById(userId);
        UserInfoEntity userInfo = user.getUserInfoEntity();

        return GetAccountResponse.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userInfo.getEmail())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .build();
    }
}