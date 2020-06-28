package com.scmitltda.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scmitltda.ppmtool.domain.Backlog;
import com.scmitltda.ppmtool.domain.ProjectTask;
import com.scmitltda.ppmtool.exceptions.ProjectNotFoundException;
import com.scmitltda.ppmtool.repositories.ProjectTaskReposity;

@Service
public class ProjectTaksService {
	final Integer LOW_PRIORITY = 3;
	final String INITIAL_STATUS = "TO_DO";
	
	@Autowired
	private ProjectTaskReposity projectTaskReposity;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, 
			ProjectTask projectTask, String username) {
		
		// PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = projectService.findByProjectIdentifier(projectIdentifier, username).getBacklog();
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
		if (projectTaskPriority == null || projectTaskPriority == 0) {
			projectTask.setPriority(LOW_PRIORITY);
		}
		
		// Initial status when the status = null
		String projectStatus = projectTask.getStatus();
		if (projectStatus == "" || projectStatus == null) {
			projectTask.setStatus(INITIAL_STATUS);
		}
		
		return projectTaskReposity.save(projectTask);		

	}

	public Iterable<ProjectTask> findBacklogById(String id, String username) {
		projectService.findByProjectIdentifier(id, username);
		return projectTaskReposity.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

		projectService.findByProjectIdentifier(backlog_id, username);
		
		// make sure that our task exists
		ProjectTask projectTask = projectTaskReposity.findByProjectSequence(pt_id);
		
		if (projectTask == null) {
			throw new ProjectNotFoundException("ProjectTaks with ID '" + pt_id + "' does not found!");	
		}
		
		// make sure that the backlog/project_id in the path corresponds to the right project
		if (!projectTask.getProjectIdentifier().contentEquals(backlog_id)) {
			throw new ProjectNotFoundException("ProjectTaks with ID '" + pt_id + "' does not exist in Project with ID '" + backlog_id +"'" );				
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskReposity.save(projectTask);
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
				
		projectTaskReposity.delete(projectTask);
	}
}
