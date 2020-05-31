package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {

        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meetingService.findById(meeting.getId()), HttpStatus.CREATED);

    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/participant/{login}", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {

        Meeting meeting = meetingService.findById(id);
        Participant participant = participantService.findByLogin(login);

        if (meeting == null) {
            return new ResponseEntity<>("Meeting not found.", HttpStatus.NOT_FOUND);
        }
        if (meetingService.getParticipants(meeting).contains(participant)) {

            return new ResponseEntity<>("Participant already added.", HttpStatus.CONFLICT);

        }
        if (participant == null) {
            return new ResponseEntity<>("Participant not found.", HttpStatus.NOT_FOUND);
        }

        meetingService.addParticipant(meeting, participant);

        return new ResponseEntity<Collection<Participant>>(meetingService.getParticipants(meeting), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>("Meeting not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Participant>>(meetingService.getParticipants(meeting), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/participant/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findById(id);
        Participant participant = participantService.findByLogin(login);

        if (meeting == null) {
            return new ResponseEntity<>("Meeting not found.", HttpStatus.NOT_FOUND);
        }
        if (participant == null) {
            return new ResponseEntity<>("Participant not found.", HttpStatus.NOT_FOUND);
        }
        if (!meetingService.getParticipants(meeting).contains(participant)) {
            return new ResponseEntity<>("Participant not added.", HttpStatus.CONFLICT);
        }

        meetingService.deleteParticipant(meeting, participant);

        return new ResponseEntity<Collection<Participant>>(meetingService.getParticipants(meeting), HttpStatus.OK);

    }
}