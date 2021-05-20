package app.services;


import app.model.Courier;
import app.model.DeliveryRequest;
import app.repositories.DeliveryRequestRepository;
import org.springframework.stereotype.Service;
import app.repositories.CourierRepository;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@Service("courierService")
public class CourierService
{

    private CourierRepository couriers;
    private DeliveryRequestRepository deliveries;

    public CourierService(CourierRepository couriers, DeliveryRequestRepository deliveries)
    {
        this.couriers = couriers;
        this.deliveries = deliveries;
    }



    public void completeDeliveryRequest(DeliveryRequest request){
    }

    public Courier pickCourier()
    {
        Courier picked = null;
        int currentOrders = Integer.MAX_VALUE;

        StreamSupport.stream(couriers.findAll().spliterator(), false)
                .filter(Courier::isAvailable)
                .forEach(c -> System.out.println(c.getName() + ", " + c.getRequests().size()));

        return StreamSupport.stream(couriers.findAll().spliterator(), false)
                .filter(Courier::isAvailable)
                .min(Comparator.comparingInt(c -> c.getRequests().size())).get();
    }
}
