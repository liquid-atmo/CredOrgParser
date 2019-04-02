package application.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.Form101Repository;
import application.dao.OrganisationRepository;
import application.entity.Form101;
import application.entity.Organisation;

@Service
public class Form101ServiceImpl implements Form101Service {
	
	
	private Form101Repository form101Repository;
	private OrganisationRepository organisationRepository;
	
	@Autowired
	public Form101ServiceImpl(Form101Repository datesOfForm101Repository, 
			OrganisationRepository organisationRepository) {
		this.form101Repository = datesOfForm101Repository ;
		this.organisationRepository = organisationRepository;
	}

	@Override
	@Transactional
	public List<Form101> findAll() {
		
		return form101Repository.findAll();
	}

	@Override
	public Form101 findById(int theId) {
		
		Optional<Form101> result = form101Repository.findById(theId);
		Form101 theDatesOfForm101 = null;
		if (result.isPresent()) {
			theDatesOfForm101 = result.get();
		}
		else {
			throw new RuntimeException("Didn't find the Form101" + theId);
		}
		return theDatesOfForm101;
	}
	
	@Override
	public List<Form101> findByOrganisationId(int theId) {
		
		
		List <Form101> list = null;
		Organisation organisation = null;
		
		Optional<Organisation> result = organisationRepository.findById(theId);
		
		if (result.isPresent()) {
			organisation = result.get();
		}
		
		else {
			throw new RuntimeException("Didn't find any Organisation with ID "
					 + theId);
		}
		
		if (!(organisation.getForm101() == null)) {
		    list = organisation.getForm101();
		
		}
		else {
			throw new RuntimeException("Didn't find any Form101 for Organisation with Id" + theId);
		}
		return list;
	}

	@Override
	@Transactional
	public void save(Form101 theDatesOfForm101) {
		form101Repository.save(theDatesOfForm101);
	}

	@Override
	@Transactional
	public void deleteById(int theId) {
		form101Repository.deleteById(theId);
	}

}
