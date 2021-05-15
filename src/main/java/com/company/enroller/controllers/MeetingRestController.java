package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;



@RestController
@RequestMapping("/meetings")
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
		if( meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity("Unable to create. A meeting with login " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @PathVariable("login") String login){
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.addParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id){
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting){
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		foundMeeting.setDescription(meeting.getDescription());
		foundMeeting.setDate(meeting.getDate());
		foundMeeting.setTitle(meeting.getTitle());
		meetingService.update(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> REMOVEParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login){
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.removeParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

}
