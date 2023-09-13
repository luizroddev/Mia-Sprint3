package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.models.Application;
import br.com.fiap.MiaDBD.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Integer id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public Application getApplicationByName(String name) {
        return applicationRepository.findByName(name).orElse(null);
    }

    public void createApplication(Application application) {
        application.setName(application.getName().toLowerCase());
        applicationRepository.save(application);
    }

    public void updateApplication(Integer id, Application application) {
        Application existingApplication = applicationRepository.findById(id).orElse(null);

        if (existingApplication != null) {
            application.setId(id);
            applicationRepository.save(application);
        }
    }

    public void deleteApplication(Integer id) {
        Application existingApplication = applicationRepository.findById(id).orElse(null);

        if (existingApplication != null) {
            applicationRepository.delete(existingApplication);
        }
    }
}
