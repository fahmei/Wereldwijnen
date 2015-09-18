package be.vdab.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "soorten")
public class Soort {
	
	@Id
	@GeneratedValue
	long id;

}
