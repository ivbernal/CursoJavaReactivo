package com.example.tallerfinal.config;

import com.example.tallerfinal.exceptions.Exception400;
import com.example.tallerfinal.services.interfaces.IPaymentRestClient;
import com.example.tallerfinal.services.interfaces.ITransactionHistoryRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient buildWebClient(WebClient.Builder webClient){
        return webClient
                .baseUrl("http://localhost:8091")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new Exception400(errorBody)));
                })
                // .defaultStatusHandler(HttpStatusCode::is5xxServerError, clientResponse -> {})
                .build();
    }

    @Bean
    public IPaymentRestClient paymentRestClientBuild(WebClient client){
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client)).build();
        return  factory.createClient(IPaymentRestClient.class);
    }

    @Bean
    public ITransactionHistoryRestClient transactionHistoryRestClientBuild(WebClient client){
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client)).build();
        return  factory.createClient(ITransactionHistoryRestClient.class);
    }
}
