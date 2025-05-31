package tu.startup.market.gateway.web.router.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import java.util.function.Function;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static tu.startup.market.gateway.web.router.routes.Constant.*;

public class SaleRoutes {

    public static Function<PredicateSpec, Buildable<Route>> salesByStatusRoute() {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI + "/status/{status}")
                .and().method(GET)
                .filters(f -> f
                       // .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_SALES_BASE_URI + "/status/(?<status>.*)",
                                API_MARKET_SALES_BASE_URI + "/status/${status}")
                        .tokenRelay())
                .uri(LB_MARKET_SALES_URI);
    }

    public static Function<PredicateSpec, Buildable<Route>> saleSaveRoute() {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI)
                .and().method(POST)
                .filters(f -> f
                        //.removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_SALES_BASE_URI, API_MARKET_SALES_BASE_URI)
                        .tokenRelay())
                .uri(LB_MARKET_SALES_URI);
    }
}
