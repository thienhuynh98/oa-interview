package com.example.oa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
@AllArgsConstructor
public class Appointments {
    @Id
    private String appointmentID;
    private String patientFirstName;
    private String patientLastName;
    private String patientType;
    private String appointmentDate;
    private String startTime;
}
