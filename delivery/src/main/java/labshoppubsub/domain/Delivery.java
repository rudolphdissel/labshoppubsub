package labshoppubsub.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labshoppubsub.DeliveryApplication;
import labshoppubsub.domain.DeliverAdded;
import lombok.Data;

@Entity
@Table(name = "Delivery_table")
@Data
//<<< DDD / Aggregate Root
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;

    private String customerId;

    private String status;

    @PostPersist
    public void onPostPersist() {
        DeliverAdded deliverAdded = new DeliverAdded(this);
        deliverAdded.publishAfterCommit();
    }

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }
    public static void addDelivery(OrderPlaced orderPlaced) {
        //implement business logic here:

        // Example 1:  new item 
        Delivery delivery = new Delivery();
        delivery.setCustomerId(orderPlaced.getCustomerId());
        delivery.setAddress(orderPlaced.getAddress());
        delivery.setStatus("READY");
        repository().save(delivery);

    }
}
//>>> DDD / Aggregate Root
