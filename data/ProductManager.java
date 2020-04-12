package data;

import java.util.ArrayList;

public class ProductManager {

	private ArrayList<Product> products = new ArrayList<Product>();
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
}
