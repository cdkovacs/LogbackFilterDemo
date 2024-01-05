package net.toph.logbackfilterdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public abstract class LogbackBaseTest {

    @Data
    @Builder
    protected static class SensitiveData {
        private String creditCardNumber;
        private String username;
        private String password;
        private String address;
        private String ssn;

        @SneakyThrows
        public String toString() {
            return new ObjectMapper().writeValueAsString(this);
        }
    }

        @Test
    public void testCreditCardFilter() {
        String creditCardNumber = "1234567890123456";
        testSensitiveDataIsNotLogged(
                creditCardNumber,
                SensitiveData.builder().creditCardNumber(creditCardNumber).build()
        );
    }

    @Test
    public void testSsnFilter() {
        String ssn = "111-22-3333";
        testSensitiveDataIsNotLogged(
                ssn,
                SensitiveData.builder().ssn(ssn).build()
        );
    }

    @Test
    public void testUsernameFilter() {
        String username = "robertjames";
        testSensitiveDataIsNotLogged(
                username,
                SensitiveData.builder().username(username).build()
        );
    }

    @Test
    public void testAddress() {
        String address = "121 Anderson";
        testSensitiveDataIsNotLogged(
                address,
                SensitiveData.builder().address(address).build()
        );
    }

    @Test
    public void testMultiple() {
        String address = "121 Anderson";
        String username = "robertjames";
        String creditCardNumber = "1234567890123456";
        String password = "SuperSecret";
        String ssn = "111-22-3333";
                SensitiveData sensitiveData = SensitiveData.builder()
                .address(address)
                .username(username)
                .build();
        testSensitiveDataIsNotLogged(
                address,
                sensitiveData
        );
        testSensitiveDataIsNotLogged(
                username,
                sensitiveData
        );
    }
    protected abstract void testSensitiveDataIsNotLogged(
        String dataToLookFor,
        SensitiveData loggedClass
    );
}
