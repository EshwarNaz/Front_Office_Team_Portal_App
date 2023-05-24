package in.eshwarnaz.services;

import org.springframework.stereotype.Service;

import in.eshwarnaz.binding.LoginForm;
import in.eshwarnaz.binding.SignUpForm;
import in.eshwarnaz.binding.UnlockForm;

@Service
public interface UserService {

	public boolean signUp(SignUpForm form);
	
	public boolean unlock(UnlockForm form);

	public String login(LoginForm form);

	public boolean forgot(String email);
}
