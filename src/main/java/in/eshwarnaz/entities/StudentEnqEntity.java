package in.eshwarnaz.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "AIT_STUDENTS_ENQUIRES")
public class StudentEnqEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enqId;

	private String StudentName;

	private Long phno;

	private String classMode;

	private String course;

	private String enqStatus;

	@CreationTimestamp
	private LocalDate dateCreated;

	@UpdateTimestamp
	private LocalDate lastUpdated;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserDtlsEntity user;
}
