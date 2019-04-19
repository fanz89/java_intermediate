package dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalOrderDto {

    private int orderId;
    private Date orderDate;
    private int taxes;
    private int freightCharge;
    private double extendedCharge;

}
