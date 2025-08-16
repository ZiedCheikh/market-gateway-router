package tu.startup.market.gateway.web.router.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import java.util.function.Function;

import static org.springframework.http.HttpMethod.*;
import static tu.startup.market.gateway.web.router.routes.Constant.*;

public class SaleRoute {

    public static Function<PredicateSpec, Buildable<Route>> salesByStatusRoute(final String salesServiceUri) {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI + "/status/{status}")
                .and().method(GET)
                .filters(f -> f
                        // .removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_SALES_BASE_URI + "/status/(?<status>.*)",
                                API_MARKET_SALES_BASE_URI + "/status/${status}")
                        .tokenRelay())
                .uri(salesServiceUri);
    }

    public static Function<PredicateSpec, Buildable<Route>> saleSaveRoute(final String salesServiceUri) {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI)
                .and().method(POST)
                .filters(f -> f
                        //.removeRequestHeader(COOKIE)
                        .rewritePath(GATEWAY_MARKET_SALES_BASE_URI, API_MARKET_SALES_BASE_URI)
                        .tokenRelay())
                .uri(salesServiceUri);
    }

    public static Function<PredicateSpec, Buildable<Route>> saleUpdateRoute(final String salesServiceUri) {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI + "/{identify}")
                .and().method(PUT)
                .filters(f -> f
                        .rewritePath(
                                GATEWAY_MARKET_SALES_BASE_URI + "/(?<identify>[^/]+)",
                                API_MARKET_SALES_BASE_URI + "/${identify}"
                        )
                        .tokenRelay())
                .uri(salesServiceUri);
    }

    public static Function<PredicateSpec, Buildable<Route>> saleByIdRoute(final String salesServiceUri) {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI + "/{identify}")
                .and().method(GET)
                .filters(f -> f
                        .rewritePath(
                                GATEWAY_MARKET_SALES_BASE_URI + "/(?<identify>[^/]+)",
                                API_MARKET_SALES_BASE_URI + "/${identify}"
                        )
                        .tokenRelay())
                .uri(salesServiceUri);
    }

    public static Function<PredicateSpec, Buildable<Route>> saleUploadPosterRoute(final String salesServiceUri) {
        return r -> r
                .path(GATEWAY_MARKET_SALES_BASE_URI + "/{identify}/poster")
                .and().method(POST)
                .filters(f -> f
                        // rewritePath uses REGEX on the left side
                        .rewritePath(
                                GATEWAY_MARKET_SALES_BASE_URI + "/(?<identify>[^/]+)/poster",
                                API_MARKET_SALES_BASE_URI + "/${identify}/poster"
                        )
                        .tokenRelay())
                .uri(salesServiceUri);
    }
}
