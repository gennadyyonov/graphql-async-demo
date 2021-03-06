package lv.gennadyyonov.graphqlasync.config;

import lv.gennadyyonov.graphqlasync.service.AuthenticationService;
import lv.gennadyyonov.graphqlasync.web.HttpRequestLoggingFilter;
import lv.gennadyyonov.graphqlasync.web.UserLoggingFilter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import javax.validation.constraints.NotNull;

import static lv.gennadyyonov.graphqlasync.config.HttpFilterOrder.COMMONS_REQUEST_LOGGING_ORDER;
import static lv.gennadyyonov.graphqlasync.config.HttpFilterOrder.REQUEST_LOGGING_ORDER;
import static lv.gennadyyonov.graphqlasync.config.HttpFilterOrder.USER_LOGGING_ORDER;

@Configuration
public class FilterConfig {

    private static final String ALL_URL_PATTERN = "/*";
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    private final SecurityProperties securityProperties;
    private final AuthenticationService authenticationService;

    public FilterConfig(SecurityProperties securityProperties, AuthenticationService authenticationService) {
        this.securityProperties = securityProperties;
        this.authenticationService = authenticationService;
    }

    @Bean
    public FilterRegistrationBean<UserLoggingFilter> userLoggingFilter(AutowireCapableBeanFactory beanFactory) {
        FilterRegistrationBean<UserLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, new UserLoggingFilter(authenticationService)
        );
        bean.setOrder(getFilterOrder(USER_LOGGING_ORDER));
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HttpRequestLoggingFilter> loggingFilter(AutowireCapableBeanFactory beanFactory) {
        FilterRegistrationBean<HttpRequestLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, new HttpRequestLoggingFilter()
        );
        bean.setOrder(getFilterOrder(REQUEST_LOGGING_ORDER));
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter(AutowireCapableBeanFactory beanFactory) {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(MAX_PAYLOAD_LENGTH);
        filter.setIncludeHeaders(true);
        FilterRegistrationBean<CommonsRequestLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, filter
        );
        bean.setOrder(getFilterOrder(COMMONS_REQUEST_LOGGING_ORDER));
        return bean;
    }

    @NotNull
    private <T extends Filter> FilterRegistrationBean<T> makeFilterRegistrationBean(AutowireCapableBeanFactory beanFactory,
                                                                                    String urlPatterns, T filter) {
        FilterRegistrationBean<T> filterRegistrationBean = new FilterRegistrationBean<>();

        beanFactory.autowireBean(filter);
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }

    private int getFilterOrder(int order) {
        int securityFilterChainOrder = securityProperties.getFilter().getOrder();
        return securityFilterChainOrder + order;
    }
}
