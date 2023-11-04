package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.domain.AccessToken;
import fontys.group.greenpath.greenpaths.domain.responses.GetAccountResponse;
import fontys.group.greenpath.greenpaths.persistence.UserRepository;
import fontys.group.greenpath.greenpaths.persistence.entity.UserEntity;
import fontys.group.greenpath.greenpaths.persistence.entity.UserInfoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAccountUseCaseImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AccessTokenEncoderDecoderImpl accessTokenEncoderDecoder;

    @InjectMocks
    GetAccountUseCaseImpl getAccountUseCase;

    @Test
    public void getAccountTest() {
        // Prepare data
        String accessToken = "fakeAccessToken";
        AccessToken decodedToken = AccessToken.builder()
                .userId(1)
                .build();
        when(accessTokenEncoderDecoder.decode(accessToken)).thenReturn(decodedToken);

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .username("TestUser")
                .password("TestPassword")
                .build();

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .id(1)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@example.com")
                .build();
        userEntity.setUserInfoEntity(userInfoEntity);

        when(userRepository.getById(1)).thenReturn(userEntity);

        // Execute
        GetAccountResponse response = getAccountUseCase.getAccount(accessToken);

        // Verify
        assertEquals("TestUser", response.getUsername());
        assertEquals("TestPassword", response.getPassword());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("FirstName", response.getFirstName());
        assertEquals("LastName", response.getLastName());

        verify(accessTokenEncoderDecoder, times(1)).decode(accessToken);
        verify(userRepository, times(1)).getById(1);
    }
}