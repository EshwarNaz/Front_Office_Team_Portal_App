package in.eshwarnaz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.eshwarnaz.binding.LoginForm;
import in.eshwarnaz.binding.SignUpForm;
import in.eshwarnaz.binding.UnlockForm;
import in.eshwarnaz.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping("/signup")
	public String handleSignUp(@Validated @ModelAttribute("user") SignUpForm form, Model model) {
		boolean save = service.signUp(form);
		if (save) {
			model.addAttribute("succMessage", "Please Check Your Email");
		} else {
			model.addAttribute("FailMessage", "Please use Unique Email");
		}
		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("login", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("login") LoginForm form, Model model) {
		String status = service.login(form);
		if (status.contains("Success")) {
			return "redirect:/dashboard";
		}
		model.addAttribute("errMsg", status);
		return "login";
	}

	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {
		UnlockForm unlockFormObj = new UnlockForm();
		unlockFormObj.setEmail(email);
		model.addAttribute("unlock", unlockFormObj);
		return "unlock";
	}

	@PostMapping("/unlock")
	public String handleUnlockPage(@ModelAttribute("unlock") UnlockForm unlock, Model model) {
		if (unlock.getNewPwd().equals(unlock.getConfirmPwd())) {
			boolean status = service.unlock(unlock);
			if (status) {
				model.addAttribute("successMsg", "your account Unlocked successfully.");
			} else {
				model.addAttribute("errMsg", "Your temparory password is incorrect, please check your Email");
			}

		} else {
			model.addAttribute("errMsg", "New password and Confirm password should be same");
		}

		return "unlock";
	}

	@GetMapping("/forgot")
	public String forgotpwdPage(Model model) {
		return "forgotPwd";
	}

	@PostMapping("/forgot")
	public String forgotpwd(@RequestParam("email") String email, Model model) {
		boolean status = service.forgot(email);
		if (status) {
			model.addAttribute("succMsg", "Check Your Email");
		} else {
			model.addAttribute("errMsg", "Invalid Email");
		}
		return "forgotPwd";
	}
}
