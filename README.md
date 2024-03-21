# teamgames-api

Usage:

Store Command:
```
if (playerCommand.equalsIgnoreCase("claim")) {
    CompletableFuture.supplyAsync(() -> {
        try {
            return com.everythingrs.store.Transaction.transactions("secret_key", c.playerName);
        } catch (Exception e) {
            c.sendMessage("API Services are currently offline. Please check back shortly.");
            e.printStackTrace();
            return null;
        }
    }).thenAccept(transactions -> {
        if (transactions == null) return;

        if (transactions.length == 0) {
            c.sendMessage("You currently don't have any items waiting. You must make a purchase first!");
            return;
        }
        if (transactions[0].message != null) {
            c.sendMessage(transactions[0].message);
            return;
        }
        for (com.everythingrs.store.Transaction transaction : transactions) {
            c.getItems().addItem(transaction.product_id, transaction.product_amount);
        }
        c.sendMessage("Thank you for supporting the server!");
    });
}
```
