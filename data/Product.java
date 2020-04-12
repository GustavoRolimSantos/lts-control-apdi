package data;

public class Product {

	private String name, id, quantity, brand;

	public Product(String name, String id, String quantity, String marca) {
		super();
		this.name = name;
		this.id = id;
		this.quantity = quantity;
		this.brand = marca;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
