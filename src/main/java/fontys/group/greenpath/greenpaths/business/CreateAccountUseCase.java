package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.requests.CreateAccountRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateAccountResponse;

public interface CreateAccountUseCase {
    CreateAccountResponse createAccount(CreateAccountRequest request);
}
