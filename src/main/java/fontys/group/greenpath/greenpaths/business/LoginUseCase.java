package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.requests.LoginRequest;
import fontys.group.greenpath.greenpaths.domain.responses.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
