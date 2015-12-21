package servlet;

import java.io.Serializable; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


 
@Entity
@Table(name="WEB_FILE")
public class WebFile implements Serializable{
     
    private static final long serialVersionUID = -8767337896773261247L;
 
    private Long indexId;
    private String fileName;
    
    @Id
  	@GeneratedValue
  	@Column(name="INDEX_ID")
	public Long getIndexId() {
		return indexId;
	}
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}
	
	@Column(name="NAME")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
  
// 
//    @Id
//    @GeneratedValue
//    @Column(name="P_ID")
//    public Long getId() {
//        return id;
//    }
    
}
