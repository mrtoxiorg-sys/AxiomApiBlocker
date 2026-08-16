package dev.axiomapiblocker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AxiomApiBlockerClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("axiomapiblocker");
    private static final String AXIOM_HOST = "axiom.moulberry.com";

    @Override
    public void onInitializeClient() {
        ProxySelector previous = ProxySelector.getDefault();
        Proxy unavailable = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("127.0.0.1", 1));

        ProxySelector.setDefault(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                if (AXIOM_HOST.equalsIgnoreCase(uri.getHost())) {
                    int blocked = ChatLogger.registerBlock();
                    LOGGER.warn("Intercepted Axiom API request: {}", uri);
                    ChatLogger.warn("AXIOM попытался подключиться к API и был заблокирован: " + uri + " (всего заблокировано: " + blocked + ")");
                    return List.of(unavailable);
                }

                return previous == null ? List.of(Proxy.NO_PROXY) : previous.select(uri);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress address, IOException failure) {
                if (AXIOM_HOST.equalsIgnoreCase(uri.getHost())) {
                    LOGGER.warn("Forced Axiom API failure for {} through {}: {}", uri, address, failure.toString());
                    ChatLogger.error("Прокси для AXIOM API принудительно упал: " + uri + " -> " + failure);
                    return;
                }

                if (previous != null && !AXIOM_HOST.equalsIgnoreCase(uri.getHost())) {
                    previous.connectFailed(uri, address, failure);
                }
            }
        });

        LOGGER.warn("Test mode active: requests to {} are forced to fail locally", AXIOM_HOST);
        ChatLogger.log("Test mode active: запросы AXIOM к " + AXIOM_HOST + " блокируются");
    }
}