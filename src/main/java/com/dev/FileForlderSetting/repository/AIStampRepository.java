package com.dev.FileForlderSetting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.FileForlderSetting.model.AIStamp;

@Repository
public interface AIStampRepository extends JpaRepository<AIStamp, Long>{

	List<AIStamp> findAllByOrderByIdAsc();
}
