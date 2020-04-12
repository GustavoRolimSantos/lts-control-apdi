package ltscontrol;

import data.ProductManager;
import display.Display;
import extractor.Extractor;

public class LTSControl {
	
	/*
	 * 
	 * 		LTS Control - Advanced PDF data integration
	 * 		This is a part module for LTS Control Systems,
	 * 		None of these codes are owned by LTS Control.
	 * 		
	 * 		Copyright (©) MaxterCreations - All Rights Reserved
	 * 		Written by Gustavo Santos <gustavorolimdossantos@gmail.com>, 2020 April
	 * 
	 * 
	 */

	private static Extractor extractor;
	
	private ProductManager productManager = new ProductManager();
	private Display display;
	
	public LTSControl() {
		display = new Display();
		extractor = new Extractor(this);
	}
	
	public ProductManager getProductManager() {
		return productManager;
	}
	
	public static Extractor getExtractor() {
		return extractor;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public static void main(String[] args) {
		new LTSControl();
	}
	
}
