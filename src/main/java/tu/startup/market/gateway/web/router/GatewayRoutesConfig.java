package tu.startup.market.gateway.web.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tu.startup.market.gateway.filter.AuthorizationHeaderFilter;
import tu.startup.market.gateway.web.router.routes.*;

@Configuration
public class GatewayRoutesConfig {

    private String marketAdminPortalUrl;

    private AuthorizationHeaderFilter authorizationHeaderFilter;

    public GatewayRoutesConfig(@Value("${market.admin.portal.url}") final String marketAdminPortalUrl,
                               final AuthorizationHeaderFilter authorizationHeaderFilter) {
        this.marketAdminPortalUrl = marketAdminPortalUrl;
        this.authorizationHeaderFilter = authorizationHeaderFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(final RouteLocatorBuilder builder) {
        //@formatter:off
        return builder.routes()
                .route(PortalRoute.marketPortalRoute(marketAdminPortalUrl))
                .route(DiscoveryRoute.marketDiscoveryRoute(authorizationHeaderFilter))
                .route(SaleRoute.salesByStatusRoute())
                .route(SaleRoute.saleSaveRoute())
                .route(OrderRoute.orderByStatusRoute())
                .route(OrderRoute.orderForCustomerRoute())
                .route(OrderRoute.orderByKeyRoute())
                .route(OrderRoute.orderSaveRoute())
                .build();
    }
    //@formatter:on
}
