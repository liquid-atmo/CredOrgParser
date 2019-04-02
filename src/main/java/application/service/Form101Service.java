package application.service;

import java.util.List;

import application.entity.Form101;


public interface Form101Service {
	
public List<Form101> findAll();
	
	public Form101 findById(int theId);
	
	public List<Form101> findByOrganisationId(int theId);
	
	public void save(Form101 theDatesOfForm101);
	
	public void deleteById(int theId);


}
