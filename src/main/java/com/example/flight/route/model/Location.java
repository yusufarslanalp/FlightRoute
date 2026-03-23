package com.example.flight.route.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "location")
public class Location implements Serializable {
	@Serial
    private static final long serialVersionUID = -1876413150295883382L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 2, max = 100, message = "Location name must be between 2 and 100 characters")
	@Column(nullable = false)
	private String name;
	
	@Size(max = 100, message = "Country name must not exceed 100 characters")
	private String country;
	
	@Size(max = 100, message = "City name must not exceed 100 characters")
	private String city;
	
	@Size(min = 3, max = 10, message = "Location code must be between 3 and 10 characters")
	@Column(unique = true)
	private String locationCode;
}
