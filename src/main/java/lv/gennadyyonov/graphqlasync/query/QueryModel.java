package lv.gennadyyonov.graphqlasync.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.graphqlasync.logging.ParameterLogging;
import lv.gennadyyonov.graphqlasync.service.AuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class QueryModel implements ParameterLogging {

    private static final long MILLIS = 1000L;

    private final AuthenticationService authenticationService;

    public QueryModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public String ping() {
        return "pong";
    }

    @SneakyThrows
    public String longRunningQuery() {
        return sleep(5);
    }

    @SneakyThrows
    public String sleep(Integer seconds) {
        Thread.sleep(seconds * MILLIS);
        String userId = authenticationService.getUserId();
        return "Hello, " + userId + "! I was sleeping for " + seconds + " seconds...";
    }
}
