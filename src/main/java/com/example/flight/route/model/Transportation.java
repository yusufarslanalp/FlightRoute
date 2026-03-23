package com.example.flight.route.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transportation")
public class Transportation implements Serializable {
	@Serial
    private static final long serialVersionUID = -3211280745040246683L;

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
        return this.to.getId().equals(second.getFrom().getId());
    }

	@JsonIgnore
	public boolean isFlight(){
        return type == TransportationType.FLIGHT;
    }
}
