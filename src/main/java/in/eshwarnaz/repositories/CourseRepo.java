package in.eshwarnaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.eshwarnaz.entities.CourseEntity;

public interface CourseRepo extends JpaRepository<CourseEntity, Integer> {

}
