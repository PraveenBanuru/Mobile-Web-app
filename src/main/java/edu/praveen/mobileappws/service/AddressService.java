package edu.praveen.mobileappws.service;

import java.util.List;

import edu.praveen.mobileappws.ui.model.shared.dto.AddressDto;

public interface AddressService {
	
	List<AddressDto> getAddresses(String uderId);

	AddressDto getAddress(String ddressId);
}
