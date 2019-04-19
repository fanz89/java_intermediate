package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employees {

    private int employeeId;
    private String firstname;
    private String lastname;
    private String title;
    private String workPhone;

    private List<Employees> employees = new ArrayList<>();

}
