package com.example.oa.repository;

import com.example.oa.model.Doctors;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctors, Integer>
{
    Doctors findByDoctorID(String doctorID);
}
