package com.qdm.cs.usermanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qdm.cs.usermanagement.constants.ResponseConstants;
import com.qdm.cs.usermanagement.dto.FormDataDTO;
import com.qdm.cs.usermanagement.entity.CareGiver;
import com.qdm.cs.usermanagement.entity.Category;
import com.qdm.cs.usermanagement.enums.Status;
import com.qdm.cs.usermanagement.response.ResponseInfo;
import com.qdm.cs.usermanagement.response.ResponseType;
import com.qdm.cs.usermanagement.service.CareGiverService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/careGiver" })
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
@Slf4j
public class CareGiverController {

	@Autowired
	CareGiverService careGiverService;

	@PostMapping(value = "/addCareGiver", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> addCareGiver(FormDataDTO formDataDTO) {
		ResponseEntity response = null;
		try {
			CareGiver careGiver = careGiverService.addCareGiver(formDataDTO);
			log.info("Care Giver Created Successfully With CareGiver_Id : " + careGiver.getCareGiverId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At AddCareGiver : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getCareGiver", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCareGiver() {
		ResponseEntity response = null;
		try {
			List<CareGiver> careGiverList = careGiverService.getCareGiver();
			List<Object> careGiverRecords = new ArrayList<>();
			for (CareGiver careGiver : careGiverList) {
				Map<String, Object> map = new HashMap<>();
				map.put("Care_Giver_ID", careGiver.getCareGiverId());
				map.put("Care_Giver_Name", careGiver.getCareGiverName());
				map.put("Availability", careGiver.getActiveStatus());
				map.put("Clients_Count", careGiver.getClientsCount());
				map.put("Profile_Picture", careGiver.getUploadPhoto().getData());
				careGiverRecords.add(map);
			}
			log.info("Care Givers List " + careGiverRecords);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", careGiverRecords), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getCareGiver : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getCareGiverQuickSummary/{careGiverId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCareGiverById(@PathVariable("careGiverId") int careGiverId) {
		ResponseEntity response = null;
		try {
			Optional<CareGiver> careGiver = careGiverService.getCareGiverById(careGiverId);
			Map<String, Object> careGiverRecords = new HashMap<>();
			if (careGiver.isPresent()) {
				CareGiver careGiverList = careGiver.get();
				careGiverRecords.put("Care_Giver_ID", careGiverList.getCareGiverId());
				careGiverRecords.put("Care_Giver_Name", careGiverList.getCareGiverName());
				careGiverRecords.put("Availability", careGiverList.getActiveStatus());
				careGiverRecords.put("Mobile_No", careGiverList.getMobileNo());
				careGiverRecords.put("Email_ID", careGiverList.getEmailId());
				careGiverRecords.put("Address", careGiverList.getAddress());
				careGiverRecords.put("Skills", careGiverList.getSkills());
				careGiverRecords.put("Clients_Count", careGiverList.getClientsCount());
				careGiverRecords.put("Profile_Picture", careGiverList.getUploadPhoto().getData());
				log.info("Get CareGiver Record By CareGiverId : " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", careGiverRecords), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", careGiverRecords), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At getCareGiverQuickSummary : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/editCareGiverDetails/{careGiverId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> editCareGiverDetails(@PathVariable("careGiverId") int careGiverId) {
		ResponseEntity response = null;
		try {
			Optional<CareGiver> careGiver = careGiverService.getCareGiverById(careGiverId);
			Map<String, Object> careGiverRecord = new HashMap<>();
			if (careGiver.isPresent()) {
				CareGiver careGiverList = careGiver.get();
				List<Category> category = careGiverService.getCategoryListById(careGiverList.getCategory());

				careGiverRecord.put("Care_Giver_Id", careGiverList.getCareGiverId());
				careGiverRecord.put("Care_Giver_Name", careGiverList.getCareGiverName());
				careGiverRecord.put("Category_List", category);
				careGiverRecord.put("Mobile_No", careGiverList.getMobileNo());
				careGiverRecord.put("Email_ID", careGiverList.getEmailId());
				careGiverRecord.put("Address", careGiverList.getAddress());
				careGiverRecord.put("Skills", careGiverList.getSkills());
				careGiverRecord.put("Profile_Picture", careGiverList.getUploadPhoto().getData());
				log.info("Get CareGiver Records By CareGiverId " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", careGiverRecord), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareGiver Found with Id : " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", careGiverRecord), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At editCareGiverDetails : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping(value = "/updateCareGiver", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> updateCareGiver(FormDataDTO formDataDTO) throws IOException {
		ResponseEntity response = null;

		if (formDataDTO.getCareGiverId() == 0) {
			log.error(ResponseConstants.Care_Giver_Id);
			response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
					ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
			return response;
		}

		try {
			CareGiver careGiver = careGiverService.updateCareGiver(formDataDTO);
			if (careGiver != null) {
				log.info("CareGiver Updated Successfully with Id " + careGiver.getCareGiverId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareGiver Found with Id :  " + formDataDTO.getCareGiverId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
						ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At UpdateCareGiver : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

	}

	@PutMapping(value = "/updateClientsCount/{careGiverId}/{clientsCount}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateClientsCount(@PathVariable("careGiverId") long careGiverId,
			@PathVariable("clientsCount") int clientsCount) {
		ResponseEntity response = null;
		try {
			CareGiver careGiver = careGiverService.updateClientsCount(careGiverId, clientsCount);
			if (careGiver != null) {
				log.info("CareGiver Clients Count Successfully ");
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id :  " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", null), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At updateClientsCount : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping(value = "/updateCareGiverAvailabilityStatus/{careGiverId}/{activeStatus}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateClientsActiveStatus(@PathVariable("careGiverId") long careGiverId,
			@PathVariable("activeStatus") Status activeStatus) {
		ResponseEntity response = null;
		try {
			CareGiver careGiver = careGiverService.updateClientsActiveStatus(careGiverId, activeStatus);
			if (careGiver != null) {
				log.info("Updated Availability Status Successfully ");
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careGiverId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
						ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At updateClientsActiveStatus : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}
}
