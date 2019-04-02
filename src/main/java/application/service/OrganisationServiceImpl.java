package application.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.OrganisationRepository;
import application.entity.Organisation;

@Service
public class OrganisationServiceImpl implements OrganisationService {
	
	
	private OrganisationRepository organisationRepository;
	
	@Autowired
	public OrganisationServiceImpl(OrganisationRepository organisationRepository) {
		this.organisationRepository = organisationRepository ;
	}

	@Override
	@Transactional
	public List<Organisation> findAll() {
		
		return organisationRepository.findAll();
	}

	@Override
	public Organisation findById(int theId) {
		
		Optional<Organisation> result = organisationRepository.findById(theId);
		Organisation theOrganisation = null;
		if (result.isPresent()) {
			theOrganisation = result.get();
		}
		else {
			throw new RuntimeException("Didn't find the Organisation" + theId);
		}
		return theOrganisation;
	}

	@Override
	@Transactional
	public void save(Organisation theOrganisation) {
		organisationRepository.save(theOrganisation);
	}

	@Override
	@Transactional
	public void deleteById(int theId) {
		organisationRepository.deleteById(theId);
	}

}
