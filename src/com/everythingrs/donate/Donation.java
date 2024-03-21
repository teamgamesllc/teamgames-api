package com.everythingrs.donate;

import java.util.concurrent.atomic.AtomicInteger;

import com.everythingrs.service.call.HTTP;
import com.everythingrs.lib.gson.*;

/**
 * @author Genesis
 */

public class Donation {

    /**
     * These variables represent the JSON response that is sent from EverythingRS
     */

    public String player_name;
    public int product_id;
    public int product_amount;
    public int amount_purchased;
    public String product_name;
    public double product_price;
    public String message;

    /**
     * Fetches whether or not the player has already purchased an item through the on EverythingRS.com API
     * After the player it fetches the result, our backend will remove it.
     * @param secret
     * @param playerName
     * @throws Exception
     */

    public static String validate(String secret, String playerName) throws Exception {
        return HTTP.connection("https://ersdev.everythingrs.com/api/donate/process/" + playerName + "/" + secret);
    }

    /**
     * Returns an array which contains all the players donated items. If the player has not donated
     * or has already claimed their items, the array will be empty.
     * @param secret
     * @param playerName
     * @return
     * @throws Exception
     */

    public static Donation[] donations(String secret, String playerName) throws Exception {
        String test = Donation.validate(secret, playerName);
        Gson gson = new Gson();
        Donation[] donations = gson.fromJson(test, Donation[].class);
        return donations;
    }

}
