package net.toph.logbackfilterdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public class LogbackConfigJsonMessageTest {
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

    private void testSensitiveDataIsNotLogged(String dataToLookFor, SensitiveData loggedClass) {
        PrintStream console = System.out;
        ByteArrayOutputStream sensitiveBytes = new ByteArrayOutputStream();
        PrintStream sensitiveConsole = new PrintStream(sensitiveBytes);
        System.setOut(sensitiveConsole);
        log.info("{}", loggedClass);
        System.setOut(console);
        log.info("{}", loggedClass);
        String actualLog = sensitiveBytes.toString();
        assertThat("Log is empty", actualLog, not(is(emptyString())));
        assertThat("Sensitive data found in log", actualLog, not(containsString(dataToLookFor)));
    }

    @Data
    @Builder
    static class SensitiveData {
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


}
