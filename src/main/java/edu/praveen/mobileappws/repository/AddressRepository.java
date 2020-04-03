package edu.praveen.mobileappws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.praveen.mobileappws.io.entity.AddressEntity;
import edu.praveen.mobileappws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	
	
	
	List<AddressEntity> findAllByUserDetails(UserEntity user);
	
	AddressEntity findByAddressId(String addressId);

}
