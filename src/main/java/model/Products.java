package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    private int productId;
    private String productName;
    private double unitPrice;
    private char inStock;

    private List<OrderDetails> orderDetails = new ArrayList<>();

}
