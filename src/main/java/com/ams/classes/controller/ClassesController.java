package com.ams.classes.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ams.classes.model.dto.ClassDto;
import com.ams.classes.service.ClassService;
import com.ams.student.model.dto.StudentDto;
import com.ams.student.service.StudentService;
import com.ams.teacher.model.dto.TeacherDto;
import com.ams.teacher.service.TeacherSerivce;

@Controller
public class ClassesController {
    @Autowired
    private TeacherSerivce service;
	@Autowired
	private StudentService service2;

	@Autowired
	private ClassService classService;
	/**
	 * 클래스 정보 등록 화면 연결
	 * @return
	 */
	@GetMapping("/class")
	public String register(Model model) {
		List<TeacherDto> list =service.listDao();
		List<StudentDto> list2 = service2.getStudentList();
		for(StudentDto vo : list2){
			int birth = Integer.parseInt(vo.getSt_bth().substring(0, 4));
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
			int nowYear = Integer.parseInt(now.format(formatter));
			String result =String.valueOf(nowYear-birth+1);
			vo.setSt_bth(result);
		}
        model.addAttribute("list", list);
		model.addAttribute("list2", list2);
		return "classes/register";
	}
	/**
	 * 클래스 정보 리스트
	 * @return
	 */
	@RequestMapping("/class/all")
	public String list(Model model) {
		List<ClassDto> list = classService.getAllClasses();
		for(ClassDto dto : list){
			if(dto.getC_wkd()!=null){
				String day = classService.numToDay(Integer.parseInt(dto.getC_wkd()));
				dto.setC_wkd(day);
				int current_student_count = classService.countStClass(dto.getC_idx());
				dto.setCurrent_student_count(current_student_count);
			}

		}
		model.addAttribute("classes", list);
		return "classes/list";
	}
	
	
	@GetMapping("/schedule")
	public String schedule(){
		return "schedule/schedule";
	}
}
