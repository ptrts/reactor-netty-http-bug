package app;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;

public class Main {

    public static void main(String[] args) {

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.err.println();
        System.err.println("--- reactor-netty-http logging:");
        System.err.println();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DisposableServer disposableHttpServer = HttpServer
                .create()
                .host("localhost")
                .port(8080)
                .handle((request, response) -> {
                    request.withConnection(DisposableChannel::dispose);
                    return Mono.never();
                })
                .bindNow();

        try {
            HttpClient
                    .create()
                    .baseUrl("http://localhost:8080")
                    .get()
                    .response()
                    .block();
        } catch (Exception e) {

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            System.err.println();
            System.err.println("--- app.Main logging:");
            System.err.println();
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.err.println("Caught exception " + e.getClass().getName() + ": " + e.getMessage());
            System.err.println("The cause was " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
        }

        disposableHttpServer.disposeNow();
    }
}
