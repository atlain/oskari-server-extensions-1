package elf.license;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


/**
 * <x:productGroup> element
 * @author Pekka Latvala / National Land Survey of Finland
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProductGroup {
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1", localName = "id", isAttribute = true)	
	private String _idAttribute;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1", localName = "parentId", isAttribute = true)	
	private String _parentIdAttribute;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1", localName = "visible", isAttribute = true)	
	private String _visibleAttribute;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1", localName = "name", isAttribute = true)	
	private String _nameAttribute;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1",	localName = "title")
	private String _title;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1",	localName = "abstract")
	private String _abstract;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1",	localName = "calculation")
	private Calculation _calculation;
	
	@JacksonXmlProperty(namespace = "http://www.conterra.de/xcpf/1.1",	localName = "product")
	private Product _product;

	
	
	
	
	
	
	
	
	
	
	
	

	// Getters and Setters
	public String get_idAttribute() {
		return _idAttribute;
	}

	public void set_idAttribute(String _idAttribute) {
		this._idAttribute = _idAttribute;
	}

	public String get_parentIdAttribute() {
		return _parentIdAttribute;
	}

	public void set_parentIdAttribute(String _parentIdAttribute) {
		this._parentIdAttribute = _parentIdAttribute;
	}

	public String get_visibleAttribute() {
		return _visibleAttribute;
	}

	public void set_visibleAttribute(String _visibleAttribute) {
		this._visibleAttribute = _visibleAttribute;
	}
	
	public String get_nameAttribute() {
		return _nameAttribute;
	}

	public void set_nameAttribute(String _nameAttribute) {
		this._nameAttribute = _nameAttribute;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_abstract() {
		return _abstract;
	}

	public void set_abstract(String _abstract) {
		this._abstract = _abstract;
	}

	public Calculation get_calculation() {
		return _calculation;
	}

	public void set_calculation(Calculation _calculation) {
		this._calculation = _calculation;
	}

	public Product get_product() {
		return _product;
	}

	public void set_product(Product _product) {
		this._product = _product;
	}
	
	
	
	
	
	
	
}
