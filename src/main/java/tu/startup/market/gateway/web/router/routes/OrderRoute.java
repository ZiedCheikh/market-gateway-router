package tu.startup.market.gateway.web.router.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import java.util.function.Function;

import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static tu.startup.market.gateway.web.router.routes.Constant.*;

public class OrderRoute {

    public static Function<PredicateSpec, Buildable<Route>> orderByStatusRoute() {
        return r -> r
                .path(GATEWAY_MARKET_ORDERS_BASE_URI + "/status/{status}")
                .and().method(GET)
                .filters(f -> f
                        .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_ORDERS_BASE_URI + "/status/(?<status>.*)",
                                API_MARKET_ORDERS_BASE_URI + "/status/${status}")
                        .tokenRelay())
                .uri(LB_MARKET_ORDERS_URI);
    }

    public static Function<PredicateSpec, Buildable<Route>> orderForCustomerRoute() {
        return r -> r
                .path(GATEWAY_MARKET_ORDERS_BASE_URI + "/customer/{customer}")
                .and().method(GET)
                .filters(f -> f
                        .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_ORDERS_BASE_URI + "/customer/(?<customer>.*)",
                                API_MARKET_ORDERS_BASE_URI + "/customer/${customer}")
                        .tokenRelay())
                .uri(LB_MARKET_ORDERS_URI);
    }

    public static Function<PredicateSpec, Buildable<Route>> orderByKeyRoute() {
        return r -> r
                .path(GATEWAY_MARKET_ORDERS_BASE_URI + "/{id}/{number}")
                .and().method(GET)
                .filters(f -> f
                        .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_ORDERS_BASE_URI + "/(?<id>.*)/(?<number>.*)",
                                API_MARKET_ORDERS_BASE_URI + "/${id}/${number}")
                        .tokenRelay())
                .uri(LB_MARKET_ORDERS_URI);
    }

    public static Function<PredicateSpec, Buildable<Route>> orderSaveRoute() {
        return r -> r
                .path(GATEWAY_MARKET_ORDERS_BASE_URI)
                .and().method(POST)
                .filters(f -> f
                        .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_ORDERS_BASE_URI, API_MARKET_ORDERS_BASE_URI)
                        .tokenRelay())
                .uri(LB_MARKET_ORDERS_URI);
    }
}