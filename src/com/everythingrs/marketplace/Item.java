package com.everythingrs.marketplace;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Nelson
 *
 */

public class Item {
	
	private int item;
	private int amount;
	private String name;
	private String date;
	
	public Item(int item, int amount, String name) {
		this.item = item;
		this.amount = amount;
		this.name = name;
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
		date = dateFormat.format(currentDate);
	}
	
	public String getName() {
		return this.name;
	}

}
