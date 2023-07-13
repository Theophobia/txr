package me.theophobia.shtipsbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

	private final Flux<String> intervalFlux;

	public ReactiveWebSocketHandler() {
		this.intervalFlux = Flux.interval(Duration.ofSeconds(1))
			.map(sequence -> "Message " + sequence)
			.log();
	}

	@Bean
	public HandlerMapping webSocketHandlerMapping(WebSocketHandler webSocketHandler) {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/event-emitter", webSocketHandler);

		SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
		handlerMapping.setOrder(1);
		handlerMapping.setUrlMap(map);
		return handlerMapping;
	}

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		return webSocketSession.send(intervalFlux
				.map(webSocketSession::textMessage))
				.and(webSocketSession.receive()
				.map(WebSocketMessage::getPayloadAsText)
				.log());
	}
}
