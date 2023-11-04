package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.responses.GetAccountResponse;

public interface GetAccountUseCase {
    GetAccountResponse getAccount(String accessToken);
}
