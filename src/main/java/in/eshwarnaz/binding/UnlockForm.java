package in.eshwarnaz.binding;

import lombok.Data;

@Data
public class UnlockForm {

	public String email;
	public String tempPwd;
	public String newPwd;
	public String confirmPwd;
}
