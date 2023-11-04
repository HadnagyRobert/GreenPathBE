package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.exceptions.InvalidAccessTokenException;
import fontys.group.greenpath.greenpaths.domain.AccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccessTokenEncoderDecoderImplTest {

    private AccessTokenEncoderDecoderImpl accessTokenEncoderDecoder;

    private static final String SECRET_KEY = "E91E158E4C6656F68B1B5D1C316766DE98D2AD6EF3BFB44F78E9CFCDF5";

    @BeforeEach
    public void setUp() {
        this.accessTokenEncoderDecoder = new AccessTokenEncoderDecoderImpl(SECRET_KEY);
    }

    @Test
    void givenAccessToken_whenEncode_thenSuccess() {
        AccessToken accessToken = AccessToken.builder()
                .subject("subject")
                .userId(1)
                .roles(Arrays.asList("ADMIN", "USER"))
                .build();
        String encoded = accessTokenEncoderDecoder.encode(accessToken);
        assertNotNull(encoded);
    }

    @Test
    void givenEncodedAccessToken_whenDecode_thenSuccess() {
        AccessToken accessToken = AccessToken.builder()
                .subject("subject")
                .userId(1)
                .roles(Arrays.asList("ADMIN", "USER"))
                .build();
        String encoded = accessTokenEncoderDecoder.encode(accessToken);
        AccessToken decoded = accessTokenEncoderDecoder.decode(encoded);
        assertNotNull(decoded);
        assertEquals(accessToken.getSubject(), decoded.getSubject());
        assertEquals(accessToken.getUserId(), decoded.getUserId());
        assertEquals(accessToken.getRoles(), decoded.getRoles());
    }

    @Test
    void givenNullAccessToken_whenEncode_thenThrowException() {
        assertThrows(NullPointerException.class, () -> accessTokenEncoderDecoder.encode(null));
    }

    @Test
    void givenEmptyAccessToken_whenEncode_thenSuccess() {
        AccessToken accessToken = AccessToken.builder().build();
        String encoded = accessTokenEncoderDecoder.encode(accessToken);
        assertNotNull(encoded);
    }

    @Test
    void givenNullEncodedAccessToken_whenDecode_thenThrowInvalidAccessTokenException() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenEncoderDecoder.decode(null));
    }

    @Test
    void givenEmptyEncodedAccessToken_whenDecode_thenThrowInvalidAccessTokenException() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenEncoderDecoder.decode(""));
    }

    @Test
    void givenInvalidEncodedAccessToken_whenDecode_thenThrowInvalidAccessTokenException() {
        assertThrows(InvalidAccessTokenException.class, () -> accessTokenEncoderDecoder.decode("InvalidToken"));
    }
}