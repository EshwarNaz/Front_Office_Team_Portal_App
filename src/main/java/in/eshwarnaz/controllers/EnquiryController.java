package in.eshwarnaz.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.eshwarnaz.binding.DashboardResponse;
import in.eshwarnaz.binding.EnquiryForm;
import in.eshwarnaz.binding.EnquirySearchCriteria;
import in.eshwarnaz.entities.StudentEnqEntity;
import in.eshwarnaz.repositories.StudentEnqRepo;
import in.eshwarnaz.services.EnquiryService;

@Controller
public class EnquiryController {
	@Autowired
	private HttpSession session;

	@Autowired
	private EnquiryService enqservice;

	@Autowired
	private StudentEnqRepo sturepo;

	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "index";
	}

	@GetMapping("/dashboard")
	public String dashBoardPage(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		DashboardResponse dashBoardData = enqservice.getDashBoardData(userId);
		model.addAttribute("dashBoardData", dashBoardData);
		return "dashboard";
	}

	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {
		List<String> status = enqservice.getEnquiryStatus();
		List<String> cources = enqservice.getCources();
		model.addAttribute("status", status);
		model.addAttribute("course", cources);
		model.addAttribute("formObj", new EnquiryForm());

		return "add-enquiry";
	}

	@PostMapping("/addEnq")
	public String addEnquiry(@ModelAttribute("formObj") EnquiryForm form, Model model) {

		if (session.getAttribute("enqid") != null) {
			enqservice.updateEnq((Integer) session.getAttribute("enqid"), form);
			session.removeAttribute("enqid");
			model.addAttribute("succMsg", "Enquery Updated");

		} else {

			boolean status = enqservice.saveEnquiry(form);
			if (status) {
				model.addAttribute("succMsg", "Enquiry added successfully");
			} else {
				model.addAttribute("errMsg", "Problem Occured");
			}
		}
		return "add-enquiry";

	}

	private void initForm(Model model) {

		List<String> enquiryStatus = enqservice.getEnquiryStatus();
		EnquiryForm formObj = new EnquiryForm();

		model.addAttribute("course", enqservice.getCources());
		model.addAttribute("enquiryStatus", enquiryStatus);
		model.addAttribute("formObj", formObj);
	}

	@GetMapping("/enquires")
	public String viewEnquiryPage(Model model) {
		initForm(model);
		List<StudentEnqEntity> enquiries = enqservice.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "view-enquiries";
	}

	@GetMapping("/filter-enquiries")
	public String getFilterdEnqs(@RequestParam String cname, @RequestParam String mode, @RequestParam String status,
			Model model) {
		Integer userId = (Integer) session.getAttribute("userId");

		EnquirySearchCriteria criteria = new EnquirySearchCriteria();
		criteria.setCourse(cname);
		criteria.setClassMode(mode);
		criteria.setStatus(status);
		List<StudentEnqEntity> filteredEnqs = enqservice.getFilteredEnqs(criteria, userId);
		model.addAttribute("enquiries", filteredEnqs);
		return "filter-enquiries";
	}

	@GetMapping("/enqu")
	public String enquiry(Model model) {
		initForm(model);
		EnquiryForm enqForm = new EnquiryForm();
		if (session.getAttribute("enq") != null) {
			StudentEnqEntity enq = (StudentEnqEntity) session.getAttribute("enq");
			BeanUtils.copyProperties(enq, enqForm);
			session.removeAttribute("enq");
		}
		model.addAttribute("formObj", enqForm);
		return "add-enquiry";
	}

	@GetMapping("/edit/{id}")
	public String editEnquiry(@PathVariable("id") Integer id) {
		StudentEnqEntity enq = enqservice.getEnq(id);
		session.setAttribute("enq", enq);
		session.setAttribute("enqid", id);
		return "redirect:/enqu";
	}

	@GetMapping("/delete/{id}")
	public String deleteData(@PathVariable("id") Integer id) {
		sturepo.deleteById(id);
		return "redirect:/enquires";
	}

}
