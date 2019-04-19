package model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    private int orderDetailId;
    private int quantity;
    private int unitPrice;
    private int discount;

    private Products product;
    private Orders order;

}
