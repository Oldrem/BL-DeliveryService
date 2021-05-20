package app.jms;

import app.repositories.DeliveryRequestRepository;
import app.services.CourierService;
import app.model.DeliveryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

@Component
@EnableJms
public class DeliveryRequestConsumer {


    private final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private CourierService courierService;

    @Autowired
    private DeliveryRequestRepository deliveries;

    @JmsListener(destination = "test-queue")
    public void listener(Message message) throws JMSException {
        logger.info("Message received {} ", message);
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                DeliveryRequest request = objectMessage.getBody(DeliveryRequest.class);
                if (request.getOrder().getMethodOfDelivery().equals("courier"))
                {
                    request.setAssignedCourier(courierService.pickCourier());
                }
                deliveries.save(request);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
