package com.example.oa.controller;

import com.example.oa.model.Appointments;
import com.example.oa.model.Doctors;
import com.example.oa.repository.AppointmentRepository;
import com.example.oa.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("doctor")
    public List<Doctors> findAllDoctor()
    {
        return doctorRepository.findAll();
    }

    @GetMapping("{doctorID}/appointment")
    public List<Appointments> findAllAppointment(@PathVariable String doctorID)
    {
        Doctors temp = doctorRepository.findByDoctorID(doctorID);
        if(temp == null) {
            return new ArrayList<>();
        }
        else
        {
            return temp.getDoctorAppointment();
        }
    }

    @PostMapping("addDoctor")
    public String addDoctor(@RequestBody Doctors doctors)
    {
        String fullName = doctors.getDoctorFirstName() + doctors.getDoctorLastName();
        List<Doctors> list_doctor = doctorRepository.findAll();
        boolean checkName = false;
        for(Doctors doctor : list_doctor)
        {
            if((doctor.getDoctorFirstName() + doctor.getDoctorLastName()).equals(fullName))
            {
                checkName = true;
            }
        }
        if(checkName)
        {
            return "This doctor has been exist";
        }
        else{
            doctorRepository.save(doctors);
            return "This doctor has been added successfully";
        }
    }
    @PostMapping("{doctorID}/appointmentAdd")
    public String addAppointment(@RequestBody Appointments appointments, @PathVariable String doctorID)
    {
        Doctors temp_doctor = doctorRepository.findByDoctorID(doctorID);
        if(temp_doctor == null)
        {
            return "This doctor does not exist";
        }
        HashMap<String, Integer> schedule = countAppointment(doctorID);
        String time = appointments.getStartTime();
        if(schedule.containsKey(time) && schedule.get(time) == 3)
        {
            return "This doctor have enough appointments for this time slot";
        }
        else{
            String[] timeSplit = time.split(":");
            int minutes = Integer.valueOf(timeSplit[1].substring(0, 2));

            if(minutes % 15 != 0)
            {
                return "Cannot add this appointment";
            }
            else {
                List<Appointments> temp_list = temp_doctor.getDoctorAppointment();
                temp_list.add(appointments);
                temp_doctor.setDoctorAppointment(temp_list);
                if(schedule.containsKey(time))
                {
                    schedule.put(appointments.getStartTime(), schedule.get(appointments.getStartTime()) + 1);
                }
                else{
                    schedule.put(appointments.getStartTime(), 1);
                }
                System.out.println(temp_doctor.getDoctorID());
                doctorRepository.save(temp_doctor);
                return "This appointment has been added successfully";
            }
        }
    }
    @DeleteMapping("delete/{doctorID}/{appointmentID}")
    public String deleteAppointment(@PathVariable String doctorID, @PathVariable String appointmentID)
    {
        int removeIndex = -1;
        String time_slot = "";
        Doctors temp_doctor = doctorRepository.findByDoctorID(doctorID);
        if(temp_doctor == null)
        {
            return "This doctor does not exist";
        }
        if(temp_doctor != null && temp_doctor.getDoctorAppointment().size() != 0)
        {
            HashMap<String, Integer> schedule = countAppointment(doctorID);
            List<Appointments> list_appointment = temp_doctor.getDoctorAppointment();
            for(Appointments apt : list_appointment)
            {
                if(apt.getAppointmentID() == appointmentID)
                {
                    removeIndex = temp_doctor.getDoctorAppointment().indexOf(apt);
                    time_slot = apt.getStartTime();
                }
            }
            if(removeIndex == -1)
            {
                return "There is nothing to remove";
            }
            else{
                list_appointment.remove(removeIndex);
                schedule.put(time_slot, schedule.get(time_slot) - 1);
                doctorRepository.save(temp_doctor);
                return "Appointment is removed successfully";
            }
        }
        else {
            return "This doctor does not have any appointment to remove";
        }
    }

    /*
    The below function is used to count the number of appointments for each time slot;
     */
    public HashMap<String, Integer> countAppointment(String doctorID)
    {
        Doctors temp = doctorRepository.findByDoctorID(doctorID);
        List<Appointments> temp_appointment = temp.getDoctorAppointment();
        HashMap<String, Integer> schedule = new HashMap<>();
        for(Appointments apt : temp_appointment)
        {
            if(!schedule.containsKey(apt.getStartTime()))
            {
                schedule.put(apt.getStartTime(), 1);
            }
            else
            {
                schedule.put(apt.getStartTime(), schedule.get(apt.getStartTime()) + 1);
            }
        }
        return schedule;
    }
}
