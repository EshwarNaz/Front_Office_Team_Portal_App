package in.eshwarnaz.servicesimpl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.eshwarnaz.binding.LoginForm;
import in.eshwarnaz.binding.SignUpForm;
import in.eshwarnaz.binding.UnlockForm;
import in.eshwarnaz.entities.UserDtlsEntity;
import in.eshwarnaz.repositories.UserDtlsRepo;
import in.eshwarnaz.services.UserService;
import in.eshwarnaz.utility.EmailUtils;
import in.eshwarnaz.utility.PwdUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDtlsRepo repo;

	@Autowired
	private EmailUtils emailutils;

	@Autowired
	private HttpSession session;

	@Override
	public boolean forgot(String email) {
		// to-do check email avaliable in db or not
		UserDtlsEntity entity = repo.findByEmail(email);

		// if not available send error msg
		if (entity == null) {
			return false;

		}
		// if available send mail and success
		String subject = "Recovery Password";
		String body = "Your Password : " + entity.getPwd();
		emailutils.sendMail(email, subject, body);

		return true;
	}

	@Override
	public String login(LoginForm form) {
		UserDtlsEntity entity = repo.findByEmailAndPwd(form.getEmail(), form.getPassword());
		if (entity == null) {
			return "Invalid credintials";
		}
		if (entity.getAccStatus().equals("LOCKED")) {
			return "Your Account Is Locked";
		}
		session.setAttribute("userId", entity.getUserId());
		return "Success";
	}

	@Override
	public boolean unlock(UnlockForm form) {
		UserDtlsEntity entity = repo.findByEmail(form.getEmail());
		if (entity.getPwd().equals(form.getTempPwd())) {
			entity.setPwd(form.getNewPwd());
			entity.setAccStatus("UNLOCKED");
			repo.save(entity);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean signUp(SignUpForm form) {

		UserDtlsEntity user = repo.findByEmail(form.getEmail());
		if (user != null) {
			return false;
		}

		// TODO copy properties from binding to entity
		UserDtlsEntity entity = new UserDtlsEntity();
		BeanUtils.copyProperties(form, entity);

		// TODO generate random password
		String pwd = PwdUtils.returnPwd();
		entity.setPwd(pwd);

		// TODO set account as unlocked
		entity.setAccStatus("LOCKED");

		// TODO insert record
		repo.save(entity);

		// TODO send email to unlock the account
		String to = form.getEmail();
		String subject = "Unlock Your Account";
		StringBuffer body = new StringBuffer();
		body.append("<h1>Use Below Temprory Password to Unlock Your Acccount</h1>");
		body.append("<br/>");
		body.append("Temporary Password : " + pwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:9098/unlock?email=" + to + "\">click here to unlock your account</a>");
		emailutils.sendMail(to, subject, body.toString());
		return true;
	}

}
