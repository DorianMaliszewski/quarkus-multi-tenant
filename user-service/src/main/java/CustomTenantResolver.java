import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomTenantResolver implements TenantResolver {

    @Override
    public String resolve(RoutingContext routingContext) {
        String path = routingContext.request().path();
        String[] parts = path.split("/");
        if (parts.length == 0) {
            return null;
        }
        return parts[1];
    }
}
