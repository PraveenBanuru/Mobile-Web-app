package edu.praveen.mobileappws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.praveen.mobileappws.io.entity.AddressEntity;
import edu.praveen.mobileappws.io.entity.UserEntity;
import edu.praveen.mobileappws.repository.AddressRepository;
import edu.praveen.mobileappws.repository.UserRepository;
import edu.praveen.mobileappws.service.AddressService;
import edu.praveen.mobileappws.ui.model.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	AddressRepository addressRepo;

	@Override
	public List<AddressDto> getAddresses(String userId) {

		List<AddressDto> returnValue = new ArrayList<AddressDto>();

		ModelMapper modelMapper = new ModelMapper();

		UserEntity user = userRepo.findByUserId(userId);

		if (user == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepo.findAllByUserDetails(user);

		/*for (AddressEntity address : addresses) {

			returnValue.add(modelMapper.map(address, AddressDto.class));
		}*/

		addresses.forEach((address)-> returnValue.add(modelMapper.map(address, AddressDto.class)));
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		// TODO Auto-generated method stub

		AddressEntity addressEntity = addressRepo.findByAddressId(addressId);

		ModelMapper map = new ModelMapper();

		return map.map(addressEntity, AddressDto.class);
	}

}
