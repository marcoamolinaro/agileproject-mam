package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.Backlog;
import com.scmitltda.ppmtool.domain.ProjectTask;
import com.scmitltda.ppmtool.repositories.BacklogRepository;
import com.scmitltda.ppmtool.repositories.ProjectTaskReposity;

@Service
public class ProjectTaksService {
	final Integer LOW_PRIORITY = 3;
	final String INITIAL_STATUS = "TO_DO";
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskReposity projectTaskReposity;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		// Exception: Project not found
		
		// PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = backlogRepository.findByProjectIndentifier(projectIdentifier);

		// set the BL to PT
		projectTask.setBacklog(backlog);
		
		// We want our project sequence like this: IPRO-1, IPRO-2
		Integer BacklogSequence = backlog.getPTSequence();		
		// Update the BL sequence
		BacklogSequence++;
		
		backlog.setPTSequence(BacklogSequence);
		
		// Add sequence to PT
		projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		// Initial priority when priority = null
		Integer projectTaskPriority = projectTask.getPriority();
		if (projectTaskPriority == null) {
			projectTask.setPriority(LOW_PRIORITY);
		}
		
		// Initial status when the status = null
		String projectStatus = projectTask.getStatus();
		if (projectStatus == "" || projectStatus == null) {
			projectTask.setStatus(INITIAL_STATUS);
		}
		
		return projectTaskReposity.save(projectTask);		
	}

	public Iterable<ProjectTask> findBacklogById(String id) {
		return projectTaskReposity.findByProjectIdentifierOrderByPriority(id);
	}
	
}
