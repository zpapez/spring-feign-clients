package cz.zpapez.springfeignclients.testutil;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;

public class WireMockExtension
        implements AfterAllCallback, AfterEachCallback, ParameterResolver {

    private WireMockServer wiremockServer;

    @Override
    public void afterAll(ExtensionContext context) {
        wiremockServer.shutdown();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        wiremockServer.resetAll();
    }

    @Override
    public WireMockServer resolveParameter(ParameterContext parameterContext, ExtensionContext arg1) throws ParameterResolutionException {
        wiremockServer = new WireMockServer(options()
                .notifier(new ConsoleNotifier(true))
                .dynamicPort());
        wiremockServer.start();
        return wiremockServer;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        return
                parameterContext.getParameter().getType().equals(WireMockServer.class)
                && parameterContext.isAnnotated(WireMockInstance.class);
    }
}
