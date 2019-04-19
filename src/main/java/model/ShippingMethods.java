package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethods {

    private int shippingMethodId;
    private String shippingMethod;

    private List<Orders> orders = new ArrayList<>();

}
