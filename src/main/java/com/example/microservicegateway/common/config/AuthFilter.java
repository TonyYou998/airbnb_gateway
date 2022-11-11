package com.example.microservicegateway.common.config;

import com.example.microservicegateway.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final WebClient.Builder webClientBuilder;
    private final Logger LOGGER= LoggerFactory.getLogger(AuthFilter.class);
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);

        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {


            return (exchange,chain)->{

                if( !exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    throw new RuntimeException("Missing authorization information");
                    return exchange.getResponse().setComplete();

                }

                String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                LOGGER.info(authHeader);
                String [] parts=authHeader.split(" ");
                if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                    throw new RuntimeException("Incorrect authorization structure");
                }
                return webClientBuilder.build()
                        .post()
                        .uri("http://USER-SERVICE/api/v1/user/validate?token="+parts[1])
                        .retrieve().bodyToMono(UserDto.class).map(userDto -> {
                            exchange.getRequest()
                                    .mutate()
                                    .header("X-auth-user-id", String.valueOf(userDto.getId()));
                            exchange.getRequest().mutate().header("Role",userDto.getRole());
                            exchange.getRequest().mutate().header("UUID",userDto.getId().toString());
                            return exchange;
                        }).flatMap(chain::filter);

            };



    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}
