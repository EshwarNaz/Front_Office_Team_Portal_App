package in.eshwarnaz.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class PwdUtils {

	public static String returnPwd() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String pwd = RandomStringUtils.random(5, characters);
		return pwd;

	}
}
