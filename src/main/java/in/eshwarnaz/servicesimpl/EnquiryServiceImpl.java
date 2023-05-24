package in.eshwarnaz.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.eshwarnaz.binding.DashboardResponse;
import in.eshwarnaz.binding.EnquiryForm;
import in.eshwarnaz.binding.EnquirySearchCriteria;
import in.eshwarnaz.entities.CourseEntity;
import in.eshwarnaz.entities.EnqStatusEntity;
import in.eshwarnaz.entities.StudentEnqEntity;
import in.eshwarnaz.entities.UserDtlsEntity;
import in.eshwarnaz.repositories.CourseRepo;
import in.eshwarnaz.repositories.EnqStatusRepo;
import in.eshwarnaz.repositories.StudentEnqRepo;
import in.eshwarnaz.repositories.UserDtlsRepo;
import in.eshwarnaz.services.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private EnqStatusRepo enqStatusRepo;

	@Autowired
	private StudentEnqRepo studentEnqRepo;

	@Autowired
	private CourseRepo courseRepo;

	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria, Integer userId) {
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();

			if (null != criteria.getCourse() && !"".equals(criteria.getCourse())) {

				enquiries = enquiries.stream().filter(e -> e.getCourse().equals(criteria.getCourse()))
						.collect(Collectors.toList());
			}

			if (null != criteria.getClassMode() && !"".equals(criteria.getClassMode())) {

				enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());
			}

			if (null != criteria.getStatus() && !"".equals(criteria.getStatus())) {

				enquiries = enquiries.stream().filter(e -> e.getEnqStatus().equals(criteria.getStatus()))
						.collect(Collectors.toList());
			}

			return enquiries;
		}
		return null;
	}

	@Override
	public List<String> getCources() {
		List<CourseEntity> findAll = courseRepo.findAll();
		List<String> names = new ArrayList<>();
		for (CourseEntity entity : findAll) {
			names.add(entity.getCourseName());
		}
		return names;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}

		return null;
	}

	@Override
	public List<String> getEnquiryStatus() {
		List<EnqStatusEntity> findAll = enqStatusRepo.findAll();

		ArrayList<String> enqStatus = new ArrayList<>();
		for (EnqStatusEntity entity : findAll) {
			enqStatus.add(entity.getStatusName());
		}

		return enqStatus;
	}

	@Override
	public boolean saveEnquiry(EnquiryForm form) {
		StudentEnqEntity enqEntity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, enqEntity);

		Integer id = (Integer) session.getAttribute("userId");

		UserDtlsEntity userDtlsEntity = userDtlsRepo.findById(id).get();
		enqEntity.setUser(userDtlsEntity);

		studentEnqRepo.save(enqEntity);

		return true;
	}

	@Override
	public DashboardResponse getDashBoardData(Integer id) {
		DashboardResponse responce = new DashboardResponse();

		// based on the id gettinng user entity
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(id);
		if (findById.isPresent()) {
			UserDtlsEntity userEntity = findById.get();

			// based on the user id getting user enquiries
			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();
			int totalCnt = enquiries.size();

			// filtering the enquiries besed on status by using Stream and java 8
			int enrolledCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();

			int lostCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("Lost")).collect(Collectors.toList())
					.size();

			// setting the enqiries data to responce object
			responce.setTotalEnquiresCnt(totalCnt);
			responce.setEnrolledCnt(enrolledCnt);
			responce.setLostCnt(lostCnt);
		}

		return responce;
	}

	@Override
	public StudentEnqEntity getEnq(Integer id) {
		Optional<StudentEnqEntity> enq = studentEnqRepo.findById(id);
		return enq.get();
	}

	@Override
	public String updateEnq(Integer enqid, EnquiryForm formObj) {

		Optional<StudentEnqEntity> enq = studentEnqRepo.findById(enqid);
		if (enq.isPresent()) {
			StudentEnqEntity enqEntity = enq.get();
			enqEntity.setStudentName(formObj.getStudentName());
			enqEntity.setPhno(formObj.getPhno());
			enqEntity.setClassMode(formObj.getClassMode());
			enqEntity.setEnqStatus(formObj.getEnqStatus());
			enqEntity.setCourse(formObj.getCourse());

			studentEnqRepo.save(enqEntity);
			return "Updated";
		}

		return "Failed";
	}

}
