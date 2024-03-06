package com.techverse.satya.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
 
import com.techverse.satya.Model.PoliticalPost;

public interface PoliticalPostRepository extends JpaRepository<PoliticalPost, Long> {
}