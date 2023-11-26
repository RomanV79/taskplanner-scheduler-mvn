package ru.vlasov.taskplannerschedulermvn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;
import ru.vlasov.taskplannerschedulermvn.dto.MessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerRabbitService {

    private final AmqpTemplate amqpTemplate;
    private final Queue queue;

    public void send(MessageDto messageDto) {
        amqpTemplate.convertAndSend(queue.getName(), messageDto);
        log.info("Send message with -> {}", messageDto);

    }
}
