package in.eshwarnaz.services;

import java.util.List;

import in.eshwarnaz.binding.DashboardResponse;
import in.eshwarnaz.binding.EnquiryForm;
import in.eshwarnaz.binding.EnquirySearchCriteria;
import in.eshwarnaz.entities.StudentEnqEntity;

public interface EnquiryService {

	public DashboardResponse getDashBoardData(Integer id);

	public List<String> getCources();

	public List<String> getEnquiryStatus();

	public boolean saveEnquiry(EnquiryForm form);

	public List<StudentEnqEntity> getEnquiries();

	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria, Integer userId);

	public StudentEnqEntity getEnq(Integer id);

	public String updateEnq(Integer enqid, EnquiryForm formObj);

}
