package application.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="dates_of_forms101")
public class Form101 {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
    @ManyToOne 
    @JoinColumn(name="organisation_id")
    private Organisation organisation;
    
    @Column(name = "form_date")
    @Temporal(TemporalType.DATE)
    private Date formDate;
    
    @Column
    private String pln;
    
    @Column
    private String ap;
    
    @Column
    private String vr;
    
    @Column
    private String vv;
    
    @Column
    private String vitg;
    
    @Column
    private String ora;
    
    @Column
    private String ova;
    
    @Column
    private String oitga;
    
    @Column
    private String orp;
    
    @Column
    private String ovp;
    
    @Column
    private String oitgp;
    
    @Column
    private String ir;
    
    @Column
    private String iv;
    
    @Column
    private String iitg;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFormDate() {
		return formDate;
	}

	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	

	public String getPln() {
		return pln;
	}

	public void setPln(String pln) {
		this.pln = pln;
	}

	public String getAp() {
		return ap;
	}

	public void setAp(String ap) {
		this.ap = ap;
	}

	public String getVr() {
		return vr;
	}

	public void setVr(String vr) {
		this.vr = vr;
	}

	public String getVv() {
		return vv;
	}

	public void setVv(String vv) {
		this.vv = vv;
	}

	public String getVitg() {
		return vitg;
	}

	public void setVitg(String vitg) {
		this.vitg = vitg;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getOva() {
		return ova;
	}

	public void setOva(String ova) {
		this.ova = ova;
	}

	public String getOitga() {
		return oitga;
	}

	public void setOitga(String oitga) {
		this.oitga = oitga;
	}

	public String getOrp() {
		return orp;
	}

	public void setOrp(String orp) {
		this.orp = orp;
	}

	public String getOvp() {
		return ovp;
	}

	public void setOvp(String ovp) {
		this.ovp = ovp;
	}

	public String getOitgp() {
		return oitgp;
	}

	public void setOitgp(String oitgp) {
		this.oitgp = oitgp;
	}

	public String getIr() {
		return ir;
	}

	public void setIr(String ir) {
		this.ir = ir;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getIitg() {
		return iitg;
	}

	public void setIitg(String iitg) {
		this.iitg = iitg;
	}

	@Override
	public String toString() {
		return "Form101 [id=" + id + ", formDate=" + formDate + ", organisation=" + organisation + "]";
	}
	
	

}
