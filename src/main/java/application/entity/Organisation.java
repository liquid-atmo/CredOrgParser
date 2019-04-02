package application.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="organisation")
public class Organisation {
		
	// define fields
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
		
		@Column(name="name")
		private String name;
		
		@Column(name="bic")
		private String bic;
		
		@Column(name="rn")
		private String rn;
		
		@OneToMany (mappedBy = "organisation",
				cascade = CascadeType.ALL,
				fetch = FetchType.EAGER)
	    private List<Form101> form101;


		public Organisation() {
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBic() {
			return bic;
		}

		public void setBic(String bic) {
			this.bic = bic;
		}

		public String getRn() {
			return rn;
		}

		public void setRn(String rn) {
			this.rn = rn;
		}
		

		public List<Form101> getForm101() {
			
			
			return form101;
		}

		public void setForm101(List<Form101> form101) {
			this.form101 = form101;
		}

		@Override
		public String toString() {
			return "Organisation [id=" + id + ", name=" + name + ", bic=" + bic + ", rn=" + rn + "]";
		}

		
		
		
	
}
