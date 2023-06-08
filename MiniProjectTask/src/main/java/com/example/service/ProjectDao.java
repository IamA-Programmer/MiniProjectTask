package com.example.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.model.Admin;
import com.example.model.Customer;
import com.example.repository.AdminRepository;
import com.example.repository.CustomerRepository;

@Service
@Component
public class ProjectDao{

	@Autowired
	private CustomerRepository customerdao;
	
	@Autowired
	private AdminRepository admindao;
	
	public void registerSave(Customer customer) {
		customerdao.save(customer);
	}
	
	public List<Admin> getAll(){
		List<Admin> adminlist=admindao.findAll();
		return adminlist;
	}
	
	public Customer checkLogin(String email,String password) {
		Customer customer=customerdao.findByEmailAndPassword(email, password);
		return customer;
	}
	
	public void saveProject(Admin admin) {
		admindao.save(admin);
	}
	
	@SuppressWarnings("deprecation")
	public Admin getbyId(int id) {
		
		Admin admin=admindao.getById(id);
		return admin;
	}
	
	public void deletebyId(int id) {
		admindao.deleteById(id);
	}
	
	public List<Admin> filterRow(String keyword){
		List<Admin> list=admindao.filter(keyword);
		return list;
	}
	
	public List<Map<String,Date>>  getProject(String user) {
		List<Map<String,Date>> project=admindao.findName(user);
		return project;
	}
	
}
