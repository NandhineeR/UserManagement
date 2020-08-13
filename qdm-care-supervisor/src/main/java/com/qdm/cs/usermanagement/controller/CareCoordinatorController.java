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
import com.qdm.cs.usermanagement.entity.CareCoordinator;
import com.qdm.cs.usermanagement.entity.Category;
import com.qdm.cs.usermanagement.enums.Status;
import com.qdm.cs.usermanagement.response.ResponseInfo;
import com.qdm.cs.usermanagement.response.ResponseType;
import com.qdm.cs.usermanagement.service.CareCoordinatorService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/careCoordinator" })
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
@Slf4j
public class CareCoordinatorController {

	@Autowired
	CareCoordinatorService careCoordinatorService;

	@PostMapping(value = "/addCareCoordinator", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> addCareCoordinator(FormDataDTO formDataDTO) throws IOException {
		ResponseEntity response = null;
		try {
			CareCoordinator careCoordinator = careCoordinatorService.addCareCoordinator(formDataDTO);
			log.info("Care Giver Created Successfully With CareGiver_Id : " + careCoordinator.getCareCoordinatorId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At addCareCoordinator : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getCareCoordinator", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCareCoordinator() {
		ResponseEntity response = null;
		try {
			List<CareCoordinator> careCoordinatorList = careCoordinatorService.getCareCoordinator();
			List<Object> coordinator = new ArrayList<>();
			for (CareCoordinator careCoordinatorData : careCoordinatorList) {
				Map<String, Object> map = new HashMap<>();
				map.put("Care_Coordinator_ID", careCoordinatorData.getCareCoordinatorId());
				map.put("Care_Coordinator_Name", careCoordinatorData.getCareCoordinatorName());
				map.put("Availability", careCoordinatorData.getActiveStatus());
				map.put("Clients_Count", careCoordinatorData.getClientsCount());
				map.put("Care_Givers_Count", careCoordinatorData.getCareGiversCount());
				map.put("Profile_Picture", careCoordinatorData.getUploadPhoto().getData());
				coordinator.add(map);
			}
			log.info("Get All CareCoordinator Records - Total Count : " + coordinator.size());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", coordinator), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getCareCoordinator : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getCareCoordinatorQuickSummary/{careCoordinatorId}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getCareCoordinatorById(@PathVariable("careCoordinatorId") long careCoordinatorId) {
		ResponseEntity<Object> response = null;
		try {
			Optional<CareCoordinator> careCoordinator = careCoordinatorService
					.getCareCoordinatorById(careCoordinatorId);
			Map<String, Object> careCoordinatorRecords = new HashMap<>();
			if (careCoordinator.isPresent()) {
				CareCoordinator careCoordinatorList = careCoordinator.get();

				careCoordinatorRecords.put("Care_Coordinator_ID", careCoordinatorList.getCareCoordinatorId());
				careCoordinatorRecords.put("Care_Coordinator_Name", careCoordinatorList.getCareCoordinatorName());
				careCoordinatorRecords.put("Availability", careCoordinatorList.getActiveStatus());
				careCoordinatorRecords.put("Mobile_No", careCoordinatorList.getMobileNo());
				careCoordinatorRecords.put("Email_ID", careCoordinatorList.getEmailId());
				careCoordinatorRecords.put("Address", careCoordinatorList.getAddress());
				careCoordinatorRecords.put("Skills", careCoordinatorList.getSkills());
				careCoordinatorRecords.put("Clients_Count", careCoordinatorList.getCareGiversCount());
				careCoordinatorRecords.put("Profile_Picture", careCoordinatorList.getUploadPhoto().getData());
				log.info("Get CareCoordinator Records By CareCoordinatorId " + careCoordinatorId);
				response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", careCoordinatorRecords), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careCoordinatorId);
				response = new ResponseEntity<Object>(
						new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
								ResponseType.NOT_FOUND.getResponseCode(), "", careCoordinatorRecords),
						HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At getCareCoordinatorById : " + e.getMessage());
			response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/editCareCoordinatorDetails/{careCoordinatorId}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> editCareGiverDetails(@PathVariable("careCoordinatorId") int careCoordinatorId) {
		ResponseEntity response = null;
		try {

			Optional<CareCoordinator> careCoordinator = careCoordinatorService
					.getCareCoordinatorById(careCoordinatorId);
			Map<String, Object> careCoordinatorRecord = new HashMap<>();
			if (careCoordinator.isPresent()) {
				CareCoordinator careCoordinatorData = careCoordinator.get();
				List<Category> category = careCoordinatorService.getCategoryListById(careCoordinatorData.getCategory());

				careCoordinatorRecord.put("Care_Coordinator_Id", careCoordinatorData.getCareCoordinatorId());
				careCoordinatorRecord.put("Care_Coordinator_Name", careCoordinatorData.getCareCoordinatorName());
				careCoordinatorRecord.put("Category_List", category);
				careCoordinatorRecord.put("Mobile_No", careCoordinatorData.getMobileNo());
				careCoordinatorRecord.put("Email_ID", careCoordinatorData.getEmailId());
				careCoordinatorRecord.put("Address", careCoordinatorData.getAddress());
				careCoordinatorRecord.put("Skills", careCoordinatorData.getSkills());
				careCoordinatorRecord.put("Profile_Picture", careCoordinatorData.getUploadPhoto().getData());

				log.info("Get CareGiver Records By CareGiverId " + careCoordinatorId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", careCoordinatorRecord), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareGiver Found with Id : " + careCoordinatorId);
				response = new ResponseEntity(
						new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
								ResponseType.NOT_FOUND.getResponseCode(), "", careCoordinatorRecord),
						HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At editCareGiverDetails : " + e.getMessage());

			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping(value = "/updateCareCoordinator", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> updateCareCoordinator(FormDataDTO formDataDTO) throws IOException {
		ResponseEntity response = null;
		if (formDataDTO.getCareCoordinatorId() == 0) {
			log.info(ResponseConstants.Care_Giver_Id);
			response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
					ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
			return response;
		}
		try {
			CareCoordinator careCoordinator = careCoordinatorService.updateCareCoordinator(formDataDTO);
			if (careCoordinator != null) {
				log.info("Updated Care Coordinator Successfully with Id : " + careCoordinator.getCareCoordinatorId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + formDataDTO.getCareCoordinatorId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
						ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At updateCareCoordinator : " + e.getMessage());

			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping("/updateClientsCount/{careCoordinatorId}/{clientsCount}")
	public ResponseEntity<?> updateClientsCount(@PathVariable("careCoordinatorId") long careCoordinatorId,
			@PathVariable("clientsCount") int clientsCount) {
		ResponseEntity response = null;

		try {
			CareCoordinator careCoordinator = careCoordinatorService.updateClientsCount(careCoordinatorId,
					clientsCount);
			if (careCoordinator != null) {
				log.info("Updated Clients Count Successfully with Id : " + careCoordinatorId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careCoordinatorId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
						ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At updateClientsCount : " + e.getMessage());

			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping("/updateCareGiversCount/{careCoordinatorId}/{careGiversCount}")
	public ResponseEntity<?> updateCareGiversCount(@PathVariable("careCoordinatorId") long careCoordinatorId,
			@PathVariable("careGiversCount") int careGiversCount) {
		ResponseEntity response = null;
		try {
			CareCoordinator careCoordinator = careCoordinatorService.updateCareGiversCount(careCoordinatorId,
					careGiversCount);
			if (careCoordinator != null) {
				log.info("Updated CareGivers Count Successfully with Id : " + careCoordinatorId);

				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careCoordinatorId);

				response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
						ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At updateCareGiversCount : " + e.getMessage());

			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping("/updateClientsActiveStatus/{careCoordinatorId}/{activeStatus}")
	public ResponseEntity<?> updateClientsActiveStatus(@PathVariable("careCoordinatorId") long careCoordinatorId,
			@PathVariable("activeStatus") Status activeStatus) {
		ResponseEntity response = null;
		try {
			CareCoordinator careCoordinator = careCoordinatorService.updateClientsActiveStatus(careCoordinatorId,
					activeStatus);
			if (careCoordinator != null) {
				log.info("Updated Availability Status Successfully with Id : " + careCoordinatorId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No CareCoordinator Found with Id : " + careCoordinatorId);
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
