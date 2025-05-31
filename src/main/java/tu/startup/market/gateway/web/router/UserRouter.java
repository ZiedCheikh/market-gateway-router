package tu.startup.market.gateway.web.router;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import tu.startup.market.gateway.web.handler.UserHandler;

@Component
public class UserRouter {

	private UserHandler userHandler;

	public UserRouter(final UserHandler userHandler) {
		this.userHandler = userHandler;
	}

	@Bean
	public WebProperties.Resources resources() {
		return new WebProperties.Resources();
	}

	@Bean
	public RouterFunction<ServerResponse> publicRoutes() {
		return RouterFunctions
				.route(RequestPredicates.GET("/sso/user"),
						userHandler::getUserInfo);
	}
}
