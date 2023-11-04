package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.AccessToken;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
