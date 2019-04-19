package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customers {

    private int customerId;
    private String companyName;
    private String firstname;
    private String lastname;
    private String billingAddress;
    private String city;
    private String stateOrProvince;
    private String zipCode;
    private String email;
    private String companyWebsite;
    private String phoneNumber;
    private String faxNumber;
    private String shipAddress;
    private String shipCity;
    private String shipStateOrProvince;
    private String shipZipCode;
    private String shipPhoneNumber;

    private List<Orders> orders = new ArrayList<>();

}
