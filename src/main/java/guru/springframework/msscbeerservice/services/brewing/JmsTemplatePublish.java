package guru.springframework.msscbeerservice.services.brewing;

import io.micrometer.observation.ObservationRegistry;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

@Slf4j
public class JmsTemplatePublish {
    private final JmsTemplate jmsTemplate;

    private final JmsMessagingTemplate jmsMessagingTemplate;

    public JmsTemplatePublish(ObservationRegistry observationRegistry, ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        log.info("--> INIT: JmsMessaging template");
        this.jmsTemplate = new JmsTemplate(connectionFactory);
        // configure the observation registry
        this.jmsTemplate.setObservationRegistry(observationRegistry);
        this.jmsTemplate.setMessageConverter(messageConverter);

        // For JmsMessagingTemplate, instantiate it with a JMS template that has a configured registry
        this.jmsMessagingTemplate = new JmsMessagingTemplate(this.jmsTemplate);
    }

    JmsTemplate getJmsTemplate() {

        return jmsTemplate;
    }
}
