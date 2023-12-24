# Logback Filter demo
This is a demo project to show how to use logback filter to filter out sensitive information in log files.

## Setup
### Gradle
Add the following dependencies to your `build.gradle` file:
```groovy
    // This supplies the actual logging configuration and are required
    implementation 'ch.qos.logback:logback-classic:1.4.12'
    implementation 'ch.qos.logback.contrib:logback-json-classic:0.1.5'
    implementation 'ch.qos.logback.contrib:logback-jackson:0.1.5'
    implementation 'net.logstash.logback:logstash-logback-encoder:7.4'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.14.0-rc1'
    // This is only required for testing and not for implementation
    implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2'

```

### Logback.xml
Add the following to your `logback.xml` file:
```xml
<configuration>
    <appender name="json" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <jsonGeneratorDecorator class="net.logstash.logback.mask.MaskingJsonGeneratorDecorator">
                <!-- JSON -->
                <valueMask>
                    <value>(\"(?:creditCardNumber|ssn|username|address)\":\").*(\")</value>
                    <mask>$1*REDACTED*$2</mask>
                </valueMask>
                <!-- XML -->
                <valueMask>
                    <value>(&lt;(?:creditCardNumber|ssn|username|address)>).*(&lt;)</value>
                    <mask>$1*REDACTED*$2</mask>
                </valueMask>
                <!-- Default ToString -->
                <valueMask>
                    <value>((?:creditCardNumber|ssn|username|address)=).*([,\)]+)</value>
                    <mask>$1*REDACTED*$2</mask>
                </valueMask>
            </jsonGeneratorDecorator>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="json"/>
    </root>
</configuration>
```

The `net.logstash.logback.encoder.LogstashEncoder` provides a convenient way to format a log message for consumption
by Elasticsearch and Kibana, while the `net.logstash.logback.mask.MaskingJsonGeneratorDecorator` is a convenient
way to use regex to mask sensitive data.

## Filtering JSON data
The following XML snippet (see [the logback config](src/main/resources/logback.xml)) replaces values of JSON fields 
named `creditCardNumber`, `ssn`, `username`, and `address` with `*REDACTED*`:
```xml
<valueMask>
    <value>(\"(?:creditCardNumber|ssn|username|address)\":\").*(\")</value>
    <mask>$1*REDACTED*$2</mask>
</valueMask>

```

We can use [LogbackConfigJsonMessageTest class](src/test/java/net/toph/logbackfilterdemo/config/LogbackConfigJsonMessageTest.java)
to test this.

## Filtering XML data
The following XML snippet (see [the logback config](src/main/resources/logback.xml)) replaces values of XML elements 
named `creditCardNumber`, `ssn`, `username`, and `address` with `*REDACTED*`:
```xml
<valueMask>
    <value>(&lt;(?:creditCardNumber|ssn|username|address)>).*(&lt;)</value>
    <mask>$1*REDACTED*$2</mask>
</valueMask>
```

We can use [LogbackConfigXmlMessageTest class](src/test/java/net/toph/logbackfilterdemo/config/LogbackConfigXmlMessageTest.java)
to test this.

## Filtering default toString data
The following XML snippet (see [the logback config](src/main/resources/logback.xml)) replaces values rendered with the 
Lombok `@ToString` annotation named `creditCardNumber`, `ssn`, `username`, and `address` with `*REDACTED*`:
```xml
<valueMask>
    <value>((?:creditCardNumber|ssn|username|address)=).*([,\)]+)</value>
    <mask>$1*REDACTED*$2</mask>
</valueMask>
```

We can use [LogbackConfigToStringMessageTest class](src/test/java/net/toph/logbackfilterdemo/config/LogbackConfigToStringMessageTest.java)
to test this.