package com.example.FlightRoute.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transportation")
public class Transportation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private TransportationType type;
	@ManyToOne
	@JoinColumn(name = "from_id")
	private Location from;
	@ManyToOne
	@JoinColumn(name = "to_id")
	private Location to;
	private byte operatingDays;

	public boolean isThereChange(Transportation second){
		if(this.to.getId().equals(second.getFrom().getId())){
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean isFlight(){
		if(type == TransportationType.FLIGHT){
			return true;
		}
		return false;
	}
}
