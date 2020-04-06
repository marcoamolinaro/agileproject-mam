package com.scmitltda.ppmtool.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scmitltda.ppmtool.domain.ProjectTask;

@Repository
public interface ProjectTaskReposity extends CrudRepository<ProjectTask, Long>{
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
}
