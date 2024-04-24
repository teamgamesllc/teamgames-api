package com.teamgames.vote;

import com.teamgames.https.Get;
import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;
import com.teamgames.lib.gson.TypeToken;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Nelson
 */

public class Vote {

    /**
     * Handles the reward command on a new thread for better performance
     */

    public static final ExecutorService service = Executors.newFixedThreadPool(1);

    /**
     * These variables represent the JSON response that is sent from EverythingRS
     */

    public String total_votes;
    public int vote_points;
    public int votes_month;
    public String username;
    public int give_amount;
    public int reward_id;
    public String reward_name;
    public int reward_amount;
    public String message;

    /**
     * Returns an array which contains a response from the server containing the
     * vote rewards
     *
     * @param secret
     * @param playerName
     * @return
     * @throws Exception
     */

    public static Vote[] reward(String secret, String playerName, String id, String amount) throws Exception {
        final String response = Vote.validate(secret, playerName, id, amount);
        final Gson gson = new Gson();
        return gson.fromJson(response, new TypeToken<Vote[]>().getType());
    }

    /**
     * Sends a request to the EverythingRS server
     *
     * @param secret
     * @param playerName
     * @param id
     * @param amount
     * @return
     */

    public static String validate(String secret, String playerName, String id, String amount) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("secret", secret);
        params.put("player", playerName);
        params.put("reward", id);
        params.put("amount", amount);
        return Post.sendPostData(params, "api/vote/validate");
    }

    /**
     * Fetches whether or not the player has already voted on EverythingRS.com This
     * code will soon be deprecated, we now use $_POST as it's better for
     * scalability. The code below is only supported for members using older
     * versions of our API and should no longer be used
     *
     * @param secret
     * @param playerName
     * @throws Exception
     */

    public static String validate(String secret, String playerName, int id) throws Exception {
        return Get.connection("https://everythingrs.com/api/vote/process/" + playerName + "/" + secret + "/" + id);
    }

}
