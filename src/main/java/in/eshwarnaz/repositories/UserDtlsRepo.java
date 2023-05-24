package in.eshwarnaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.eshwarnaz.entities.UserDtlsEntity;

public interface UserDtlsRepo extends JpaRepository<UserDtlsEntity, Integer> {

	public UserDtlsEntity findByEmail(String email);

	public UserDtlsEntity findByEmailAndPwd(String email, String Password);
}
