package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.Backlog;
import com.scmitltda.ppmtool.domain.Project;
import com.scmitltda.ppmtool.domain.User;
import com.scmitltda.ppmtool.exceptions.ProjectIdException;
import com.scmitltda.ppmtool.exceptions.ProjectNotFoundException;
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
			throw new ProjectIdException("Project Id '" + 
					project.getProjectIdentifier().toUpperCase() + "' already exists.");
		}
	}
	
	public Project findByProjectIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (project == null) {
			throw new ProjectIdException("Project Id '" + projectId + "' does not exist.");	
		}
		
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project Id '" + projectId + 
					"' not found in your account.");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username) {	
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByProjectIdentifier(String projectId, String username) {
		projectRepository.delete(findByProjectIdentifier(projectId, username));
	}
}
