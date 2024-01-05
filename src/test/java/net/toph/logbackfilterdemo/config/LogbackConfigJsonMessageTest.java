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
public class LogbackConfigJsonMessageTest extends LogbackBaseTest {

    protected void testSensitiveDataIsNotLogged(String dataToLookFor, SensitiveData loggedClass) {
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

}
