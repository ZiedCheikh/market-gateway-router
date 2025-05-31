docker builder prune
docker build --no-cache -t market-discovery-service:latest .
docker build --no-cache -t market-gateway-router:latest .
docker build --no-cache -t market-orders-service:latest .
docker build --no-cache -t market-sales-service:latest .
docker build --no-cache -t startup-authorization-server:latest .
docker build --no-cache -t startup-token-generator:latest .
docker build --no-cache -t market-admin-portal:latest .

MARKET_DISCOVERY_HOST=http://marketdiscovery:8010;
AUTH_SERVER_ISSUER_URL=http://authorizationserver:9000/startup/authserver;
AUTH_SERVER_HOST_URL=http://myhost:9000;
MARKET_GATEWAY_HOST_URL=http://myhost:8082;
MARKET_ADMIN_PORTAL_HOST_URL=http://myhost:4200

MARKET_DISCOVERY_HOST=http://localhost:8010;
AUTH_SERVER_ISSUER_URL=http://localhost:9000/startup/authserver;
AUTH_SERVER_HOST_URL=http://myhost:9000;
MARKET_GATEWAY_HOST_URL=http://myhost:8082;
MARKET_ADMIN_PORTAL_HOST_URL=http://myhost:4200


market.discovery.host=http://marketdiscovery:8010
auth.server.issuer.url=http://authorizationserver:9000/startup/authserver
auth.server.host.url=http://myhost:9000
market.gateway.host.url=http://myhost:8082