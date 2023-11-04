package fontys.group.greenpath.greenpaths.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserInfo {
    private String email;
    private String firstName;
    private String lastName;
}
