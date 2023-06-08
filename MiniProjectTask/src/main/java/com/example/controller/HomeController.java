package com.example.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Admin;
import com.example.model.Customer;
import com.example.service.ProjectDao;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private ProjectDao servicedao;
	
	//opens in login page
	@RequestMapping("/")
	public String loginpage(Model model) {
		
		return "Login";
	}

	//goes to register Page
	@RequestMapping("/register")
	public String register() {
		return "Register";
	}
	
	@RequestMapping("/loginForm")
	public String redirectLogin() {
		
		return "redirect:/";
	}
	
	//save register data
	@PostMapping("/save")
	public String saveUser(Customer customer,Model model) {
		servicedao.registerSave(customer);
		model.addAttribute("success", "Registration Successful");
	return "redirect:/";
		
	}
	
	//after login
	@GetMapping("/processing")
	public String afterLogin(@RequestParam("email") String email,@RequestParam("password") String password,HttpSession session,Model model) {
		
		Customer customer=servicedao.checkLogin(email, password);
		
		
		if(customer == null) {
			model.addAttribute("failed", "Invalid Username and Password");
			return "redirect:/";
		}
		String customername=customer.getCustomername();
		String customerrole=customer.getRole();
		
		if(customerrole.equals("admin")) {
			session.setAttribute("adminname", customername);
			model.addAttribute("loginsuccessful", "Login Successful");
			return "AdminPortal";
		}
		else {
			session.setAttribute("username", customername);
			
			 Object user=  session.getAttribute("username");

             List<Map<String,Date>> getproject=servicedao.getProject(user.toString());
             
             if(getproject.isEmpty()) {
            	 model.addAttribute("error", "No Projects has been assigned to you "+customername+" Enjoy!!!");
            	 return "UserPortal";
             }
            int size=getproject.size();
             model.addAttribute("welcome", "Hii "+customername+" welcome back!!");
             model.addAttribute("project", getproject);
             model.addAttribute("size", size);
			return "UserPortal";
		}
		
	}
	
	//goes to assign project page with current date and future dates to ignore the admin to assign it in past dates
	@RequestMapping("/createProject")
	public String createProject(HttpSession session,Model model) {
		session.getAttribute("adminname");
		
		LocalDate date=LocalDate.now();
		
		model.addAttribute("today", date);
		
		return "AssignProject";
	}
	
	// gets all datas from the project details table 
	@GetMapping("/existProject")
	public String existProject(HttpSession session,Model model) {
		session.getAttribute("adminname");
		
		List<Admin> listadmin=servicedao.getAll();
		
		if(listadmin.isEmpty()) {
			model.addAttribute("empty", "No projects has been assigned by you!!");
			return "ViewProjects";
		}
		
		model.addAttribute("view", listadmin);
		model.addAttribute("projectsaved", "Project added Successfully");
		return "ViewProjects";
	}
	
	//adds a new project to the database
	@PostMapping("/saveproject")
	public String saveProject(@ModelAttribute("admin")Admin admin,Model model) {
		servicedao.saveProject(admin);
		model.addAttribute("projectsaved", "Project added Successfully");
		
		return "redirect:/existProject";
	}
	
	//redirects to the createProject page to add more projects or members after adding a data
	@RequestMapping("/add")
	public String goBack() {
		return "redirect:/createProject";
	}
	
	//edits the project or we can also add members to that existing project
	@GetMapping("/edit/{id}")
	public String editProject(@PathVariable("id") int id,HttpSession session,Model model) {
		Admin admin=servicedao.getbyId(id);
		session.getAttribute("adminname");
		model.addAttribute("admin", admin);
		return "EditProject";
	}
	
	//deletes the unwanted projects
	@RequestMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") int id,HttpSession session,Model model) {
		servicedao.deletebyId(id);
		return "redirect:/existProject";
		
	}
	
	// filters the datas by using project name and team members name
	@GetMapping("/search")
	public String searchResult(@RequestParam("filtertable") String keyword,HttpSession session,Model model) {
		List<Admin> searched=servicedao.filterRow(keyword);
		session.getAttribute("adminname");
		model.addAttribute("searched", searched);
		return "Search";
		
	}
	
	//logout the admin portal
	@RequestMapping("/adminlogout")
	public String adminLogout(HttpSession session) {
		session.removeAttribute("adminname");
		return "redirect:/";
	}
	
	//logout the user portal
	@RequestMapping("/logoutuser")
	public String userLogout(HttpSession session) {
		session.removeAttribute("username");
		return "redirect:/";
	}
	
	
}
