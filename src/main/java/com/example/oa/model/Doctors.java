package com.example.oa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document (collection = "doctor")
public class Doctors {
    @Id
    private String doctorID;
    private String doctorFirstName;
    private String doctorLastName;
    private List<Appointments> doctorAppointment;
}
