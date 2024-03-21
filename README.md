# teamgames-api

Usage:

Store Command:
```
if (playerCommand.equalsIgnoreCase("claim")) {
    CompletableFuture.supplyAsync(() -> {
        try {
            return com.everythingrs.donate.Donation.donations("secret_key", c.playerName);
        } catch (Exception e) {
            c.sendMessage("API Services are currently offline. Please check back shortly.");
            e.printStackTrace();
            return null;
        }
    }).thenAccept(donations -> {
        if (donations == null) return; // Error case handled in supplyAsync

        if (donations.length == 0) {
            c.sendMessage("You currently don't have any items waiting. You must donate first!");
            return;
        }
        if (donations[0].message != null) {
            c.sendMessage(donations[0].message);
            return;
        }
        for (com.everythingrs.donate.Donation donation : donations) {
            c.getItems().addItem(donation.product_id, donation.product_amount);
        }
        c.sendMessage("Thank you for donating!");
    });
}
```
