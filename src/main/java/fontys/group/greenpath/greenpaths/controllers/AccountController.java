package fontys.group.greenpath.greenpaths.controllers;

import fontys.group.greenpath.greenpaths.business.CreateAccountUseCase;
import fontys.group.greenpath.greenpaths.business.GetAccountUseCase;
import fontys.group.greenpath.greenpaths.domain.requests.CreateAccountRequest;
import fontys.group.greenpath.greenpaths.domain.requests.GetAccountRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateAccountResponse;
import fontys.group.greenpath.greenpaths.domain.responses.GetAccountResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountUseCase getAccountUseCase;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        CreateAccountResponse response = createAccountUseCase.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/get")
    public ResponseEntity<GetAccountResponse> getAccount(@RequestBody @Valid String request) {
        GetAccountResponse response = getAccountUseCase.getAccount(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
