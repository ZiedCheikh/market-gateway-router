package tu.startup.market.gateway.web.router.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import java.util.function.Function;

import static tu.startup.market.gateway.web.router.routes.Constant.APP_MARKET_PORTAL_BASE_URI;
import static tu.startup.market.gateway.web.router.routes.Constant.GATEWAY_MARKET_PORTAL_BASE_URI;

public class PortalRoute {

    public static Function<PredicateSpec, Buildable<Route>> marketPortalRoute(final String marketAdminPortalUrl) {
        return r -> r
                .path(GATEWAY_MARKET_PORTAL_BASE_URI + "/**")
                .filters(f -> f
                        .rewritePath(GATEWAY_MARKET_PORTAL_BASE_URI + "/(?<segment>.*)",
                                APP_MARKET_PORTAL_BASE_URI + "/${segment}"))
                .uri(marketAdminPortalUrl);
    }
}
