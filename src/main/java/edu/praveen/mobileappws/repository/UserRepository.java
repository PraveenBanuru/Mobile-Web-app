package edu.praveen.mobileappws.repository;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.praveen.mobileappws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findUserByEmail(String email);
	
	UserEntity findByUserId(String userId);

}
