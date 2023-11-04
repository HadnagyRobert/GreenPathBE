package fontys.group.greenpath.greenpaths.persistence;

import fontys.group.greenpath.greenpaths.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

//    @Autowired
//    private EntityManager entityManager;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Test
//    void save_shouldSaveAccountWithAllFields() {
//        AccountEntity account = AccountEntity.builder()
//                .id(1)
//                .username("bob")
//                .email("bob@gmail.com")
//                .build();
//
//        AccountEntity savedAccount = accountRepository.save(account);
//        assertNotNull(savedAccount.getId());
//
//        savedAccount = entityManager.find(AccountEntity.class, savedAccount.getId());
//        AccountEntity expectedAccount = AccountEntity.builder()
//                .id(savedAccount.getId())
//                .username("bob")
//                .email("bob@gmail.com")
//                .build();
//        assertEquals(expectedAccount, savedAccount);
//    }
}
