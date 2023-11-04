package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.AccessToken;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
