package com.example.spring_project.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class InstaLike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTALIKE_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "INSTALIKE_SEQUENCE_GENERATOR", sequenceName = "ID", initialValue = 1, allocationSize = 1)
    private int id;

    @Column(nullable = false)
    private String userid;

    @ManyToOne
    @JoinColumn(name = "instaboard_id", nullable = false)
    @JsonBackReference
    private Instaboard instaboard;

    @Column(name = "liked_date")
    private Date likeddate;
    
}
