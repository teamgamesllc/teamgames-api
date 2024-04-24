package com.teamgames.endpoints.vote;

import java.util.LinkedHashMap;
import java.util.Map;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;
import com.teamgames.lib.gson.TypeToken;
import com.teamgames.endpoints.vote.obj.ClaimReward;

public class VoteEndpoint {
	
	public static ClaimReward getReward(String secret, String playerName, String id, String amount) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("secret", secret);
        params.put("player", playerName);
        params.put("reward", id);
        params.put("amount", amount);
        return new Gson().fromJson(Post.sendPostData(params, "api/vote/validate"), new TypeToken<ClaimReward[]>().getType());
    }

}
