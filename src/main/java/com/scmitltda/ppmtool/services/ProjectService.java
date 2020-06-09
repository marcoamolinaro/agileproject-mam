package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.Backlog;
import com.scmitltda.ppmtool.domain.Project;
import com.scmitltda.ppmtool.domain.User;
import com.scmitltda.ppmtool.exceptions.ProjectIdException;
import com.scmitltda.ppmtool.repositories.BacklogRepository;
import com.scmitltda.ppmtool.repositories.ProjectRepository;
import com.scmitltda.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		try {
			
			User user = userRepository.findByUsername(username);
			
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			String projectIdentifier = project.getProjectIdentifier().toUpperCase();
			project.setProjectIdentifier(projectIdentifier);
			
			if (project.getId()==null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIndentifier(projectIdentifier);
			}
			
			if (project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIndentifier(projectIdentifier));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '" + 
					project.getProjectIdentifier().toUpperCase() + "' already exists.");
		}
	}
	
	public Project findByProjectIdentifier(String projectId) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId + "' does not exist.");	
		}
		return project;
	}
	
	public Iterable<Project> findAllProjects() {	
		return projectRepository.findAll();
	}
	
	public void deleteProjectByProjectIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase()); 
		
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() 
				+ "' does not exist.");	
		}
		
		projectRepository.delete(project);
	}
}
