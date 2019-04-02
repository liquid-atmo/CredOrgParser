package application.service;

import java.util.List;

import application.entity.Organisation;


public interface OrganisationService {
	
public List<Organisation> findAll();
	
	public Organisation findById(int theId);
	
	public void save(Organisation theOrganisation);
	
	public void deleteById(int theId);


}
