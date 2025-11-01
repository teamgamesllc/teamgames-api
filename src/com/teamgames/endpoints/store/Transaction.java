package com.teamgames.endpoints.store;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.teamgames.https.JsonPost;
import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;
import com.teamgames.lib.gson.JsonElement;
import com.teamgames.lib.gson.JsonObject;
import com.teamgames.lib.gson.SerializedName;
import com.teamgames.util.Thread;

/**
 * Represents a claim item returned by the store transaction API and provides helpers to
 * execute claim requests.
 */
public class Transaction {

  private static final String ENDPOINT_URL_V3 = "api/v3/store/transaction/update";
  private static final String ENDPOINT_URL_V4 = "api/v4/store/transaction/update";

  private static final Gson GSON = new Gson();

  // Claim item fields (legacy structure retained for v3 compatibility)
  public final String player_name;
  public final int product_id;
  public final String product_id_string;
  @SerializedName(value = "quantity_to_grant", alternate = {"product_amount"})
  public final int quantity_to_grant;
  @SerializedName(value = "quantity_purchased", alternate = {"amount_purchased"})
  public final int quantity_purchased;
  public final String product_name;
  public final double product_price;

  // Request state
  private String endpointUrl = ENDPOINT_URL_V3;
  private String apiKey;
  private String playerName;
  private boolean preview;
  private boolean includeRawTransactions;

  public Transaction() {
    this(null, 0, null, 0, 0, null, 0d);
  }

  public Transaction(String player_name, int product_id, String product_id_string, int quantity_to_grant, int quantity_purchased,
                     String product_name, double product_price) {
    this.player_name = player_name;
    this.product_id = product_id;
    this.product_id_string = product_id_string;
    this.quantity_to_grant = quantity_to_grant;
    this.quantity_purchased = quantity_purchased;
    this.product_name = product_name;
    this.product_price = product_price;
  }

  public Transaction setApiKey(String apiKey) {
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new IllegalArgumentException("API key must not be null or blank.");
    }
    this.apiKey = apiKey;
    return this;
  }

  public Transaction setPlayerName(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      throw new IllegalArgumentException("Player name must not be null or blank.");
    }
    this.playerName = playerName;
    return this;
  }

  public Transaction useV4Endpoint() {
    this.endpointUrl = ENDPOINT_URL_V4;
    return this;
  }

  public Transaction setEndpointUrl(String endpointUrl) {
    if (endpointUrl == null || endpointUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("Endpoint URL must not be null or blank.");
    }
    this.endpointUrl = endpointUrl;
    return this;
  }

  public Transaction setPreview(boolean preview) {
    this.preview = preview;
    return this;
  }

  public Transaction setIncludeRawTransactions(boolean includeRawTransactions) {
    this.includeRawTransactions = includeRawTransactions;
    return this;
  }

  public ClaimResponse execute() throws Exception {
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new IllegalStateException("API key must be set before fetching transactions.");
    }
    if (playerName == null || playerName.trim().isEmpty()) {
      throw new IllegalStateException("Player name must be set before fetching transactions.");
    }

    if (ENDPOINT_URL_V3.equals(endpointUrl)) {
      if (preview) {
        throw new IllegalStateException("Preview mode is only supported on the v4 endpoint.");
      }
      Map<String, Object> params = new LinkedHashMap<>();
      params.put("playerName", playerName);
      String response = Post.sendPostData(params, endpointUrl, apiKey);
      Transaction[] legacyClaims = normalizeClaims(GSON.fromJson(response, Transaction[].class));
      JsonObject[] legacyRaw = toJsonObjects(legacyClaims);
      String status = legacyClaims.length > 0 ? "SUCCESS" : "ERROR";
      String code = legacyClaims.length > 0 ? "SUCCESS" : "NO_ITEMS";
      String message = legacyClaims.length > 0 ? "Legacy claim list returned." : "There are currently no items to claim.";
      return new ClaimResponse(status, code, message, new ClaimData(legacyClaims, legacyRaw));
    }

    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("playerName", playerName);
    payload.put("preview", preview);
    payload.put("includeRawTransactions", includeRawTransactions);

    String response = JsonPost.send(GSON.toJson(payload), endpointUrl, apiKey);
    ClaimResponse envelope = GSON.fromJson(response, ClaimResponse.class);

    if (envelope == null) {
      return new ClaimResponse("ERROR", "INVALID_RESPONSE", "Unable to parse response", new ClaimData(new Transaction[0], new JsonObject[0]));
    }

    ClaimData data = envelope.data != null ? envelope.data : new ClaimData(new Transaction[0], new JsonObject[0]);
    Transaction[] claims = normalizeClaims(data.claims);
    JsonObject[] raw = data.rawTransactions != null ? data.rawTransactions : new JsonObject[0];

    return envelope.withData(new ClaimData(claims, raw));
  }

  public CompletableFuture<ClaimResponse> executeAsync() {
    return executeAsync(Thread.executor);
  }

  public CompletableFuture<ClaimResponse> executeAsync(Executor executor) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return execute();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }, executor);
  }

  public static class ClaimResponse {
    public final String status;
    public final String code;
    public final String message;
    public final ClaimData data;

    public ClaimResponse() {
      this(null, null, null, null);
    }

    public ClaimResponse(String status, String code, String message, ClaimData data) {
      this.status = status;
      this.code = code;
      this.message = message;
      this.data = data;
    }

    public ClaimResponse withData(ClaimData data) {
      return new ClaimResponse(status, code, message, data);
    }
  }

  public static class ClaimData {
    public final Transaction[] claims;
    public final JsonObject[] rawTransactions;

    public ClaimData() {
      this(null, null);
    }

    public ClaimData(Transaction[] claims, JsonObject[] rawTransactions) {
      this.claims = claims;
      this.rawTransactions = rawTransactions;
    }
  }

  private Transaction[] normalizeClaims(Transaction[] claims) {
    if (claims == null || claims.length == 0) {
      return new Transaction[0];
    }

    Transaction[] normalized = new Transaction[claims.length];
    for (int i = 0; i < claims.length; i++) {
      Transaction claim = claims[i];
      if (claim == null) {
        normalized[i] = new Transaction();
        continue;
      }
      String productIdString = claim.product_id_string;
      if ((productIdString == null || productIdString.isEmpty()) && claim.product_id > 0) {
        productIdString = String.valueOf(claim.product_id);
      }
      normalized[i] = new Transaction(
        claim.player_name,
        claim.product_id,
        productIdString,
        claim.quantity_to_grant,
        claim.quantity_purchased,
        claim.product_name,
        claim.product_price
      );
    }

    return normalized;
  }

  private JsonObject[] toJsonObjects(Transaction[] claims) {
    if (claims == null || claims.length == 0) {
      return new JsonObject[0];
    }

    JsonObject[] jsonObjects = new JsonObject[claims.length];
    for (int i = 0; i < claims.length; i++) {
      Transaction claim = claims[i];
      if (claim == null) {
        jsonObjects[i] = new JsonObject();
        continue;
      }
      JsonElement element = GSON.toJsonTree(claim);
      if (element != null && element.isJsonObject()) {
        jsonObjects[i] = element.getAsJsonObject();
      } else {
        jsonObjects[i] = new JsonObject();
      }
    }

    return jsonObjects;
  }
}
