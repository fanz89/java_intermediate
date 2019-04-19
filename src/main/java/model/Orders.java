package model;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    private int orderId;
    private Date orderDate;
    private String puchaseOrderNumber;
    private Date shipDate;
    private int freightCharge;
    private int taxes;
    private char paymentRecevied;
    private String comment;

    private Customers customer;
    private Employees employee;
    private ShippingMethods shippingMethod;

    private List<OrderDetails> orderDetailsList = new ArrayList<>();

}
