package com.example.oa.repository;

import com.example.oa.model.Appointments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointments, Integer> {
}