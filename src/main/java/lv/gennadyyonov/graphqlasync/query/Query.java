package lv.gennadyyonov.graphqlasync.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    private final QueryModel model;

    public Query(QueryModel model) {
        this.model = model;
    }

    public String ping() {
        return model.ping();
    }

    @SneakyThrows
    public String longRunningQuery() {
        return model.longRunningQuery();
    }

    @SneakyThrows
    public String sleep(Integer seconds) {
        return model.sleep(seconds);
    }
}
