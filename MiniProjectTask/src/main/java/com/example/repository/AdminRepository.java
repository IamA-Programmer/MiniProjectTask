package com.example.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Admin;
import com.example.model.Customer;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer>{

	@Query(value = "select * from projectdetails where concat(projectname,description,teammembers) LIKE %?%;",nativeQuery = true)
	public List<Admin> filter(String keyword);
	
	 @Query(value = "select projectname,enddate from projectdetails where teammembers=?",nativeQuery = true)
	 public List<Map<String,Date>> findName(String teammember);
}
