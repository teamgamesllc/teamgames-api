package com.teamgames.endpoints.marketplace;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Nelson
 *
 */

public class Item {

	protected int item;
	protected int amount;
	protected String name;
	protected String date;

	/**
	 * Constructs an Item object with the given parameters.
	 * 
	 * @param item   The unique identifier for the item.
	 * @param amount The quantity of the item.
	 * @param name   The name of the item.
	 */
	public Item(int item, int amount, String name) {
		this.item = item;
		this.amount = amount;
		this.name = name;
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
		date = dateFormat.format(currentDate);
	}

	/**
	 * Returns the name of the item.
	 * 
	 * @return The name of the item.
	 */
	public String getName() {
		return this.name;
	}

}
