package org.example.gatewayservice;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class CachedBodyServerHttpResponse extends ServerHttpResponseDecorator {
    private final CachedBodyOutputMessage cachedBodyOutputMessage;

    public CachedBodyServerHttpResponse(ServerHttpResponse delegate, CachedBodyOutputMessage cachedBodyOutputMessage) {
        super(delegate);
        this.cachedBodyOutputMessage = cachedBodyOutputMessage;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        // Utilisez writeWith de CachedBodyOutputMessage pour intercepter et gérer le corps de la réponse
        return super.writeWith(Flux.from(body)
                .doOnNext(dataBuffer -> cachedBodyOutputMessage.writeWith(Flux.just(dataBuffer))));
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return super.writeAndFlushWith(Flux.from(body).map(p -> Flux.from(p)
                .doOnNext(dataBuffer -> cachedBodyOutputMessage.writeWith(Flux.just(dataBuffer)))));
    }
}