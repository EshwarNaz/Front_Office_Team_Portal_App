package in.eshwarnaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.eshwarnaz.entities.StudentEnqEntity;

public interface StudentEnqRepo extends  JpaRepository<StudentEnqEntity, Integer>{

}
