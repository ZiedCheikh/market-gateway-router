package tu.startup.market.gateway.web.router.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import tu.startup.market.gateway.filter.AuthorizationHeaderFilter;

import java.util.function.Function;

import static org.springframework.http.HttpMethod.GET;
import static tu.startup.market.gateway.web.router.routes.Constant.APP_MARKET_DISCOVERY_BASE_URI;
import static tu.startup.market.gateway.web.router.routes.Constant.GATEWAY_MARKET_DISCOVERY_BASE_URI;

public class DiscoveryRoute {

    public static Function<PredicateSpec, Buildable<Route>> marketDiscoveryRoute(final AuthorizationHeaderFilter authorizationHeaderFilter) {
        return r -> r
                .path(GATEWAY_MARKET_DISCOVERY_BASE_URI + "/**")
                .and().method(GET)
                .filters(f -> f
                        .rewritePath(GATEWAY_MARKET_DISCOVERY_BASE_URI + "/(?<segment>.*)",
                                APP_MARKET_DISCOVERY_BASE_URI + "/${segment}")
                        .tokenRelay()
                        .filter(authorizationHeaderFilter.apply(config -> config.setAuthorities("eureka")))
                )
                .uri("http://localhost:8010");
    }
}