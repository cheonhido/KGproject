package com.example.spring_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOLLOW_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "FOLLOW_SEQUENCE_GENERATOR", sequenceName = "SEQ_FOLLOW", initialValue = 1, allocationSize = 1)
	private int seq;
	private String from_user;
	private String to_user;
	private int followcheck;
}
