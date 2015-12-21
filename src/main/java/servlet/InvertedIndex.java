package servlet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


 
@Entity
@Table(name="INVERTED_INDEX")
public class InvertedIndex implements Serializable{
     
    private static final long serialVersionUID = -8767337896773261247L;
 
    private Long id;
//    private Long frequency;
    private Long wordPosition;
//    private Long indexId;
    private String word;
    private int frequency;
    private int indexId;
    
    @Id
	@GeneratedValue
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
//	@Column(name="FREQUENCY")
//	public Long getFrequency() {
//		return frequency;
//	}
//	public void setFrequency(Long frequency) {
//		this.frequency = frequency;
//	}
	
	@Column(name="WORD_POS")
	public Long getWordPosition() {
		return wordPosition;
	}
	public void setWordPosition(Long wordPosition) {
		this.wordPosition = wordPosition;
	}
	
//	@Column(name="INDEX_ID")
//	public Long getIndexId() {
//		return indexId;
//	}
//	public void setIndexId(Long indexId) {
//		this.indexId = indexId;
//	}
	@Column(name="WORD")
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	@Column(name="FREQUENCY")
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	@Column(name="INDEX_ID")
	public int getIndexId() {
		return indexId;
	}
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}	
    
}
