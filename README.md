# Splendor

## Notes on client-server communication protocols

To run the client, make sure you specify the server address (ie localhost):

` javac Client.java && java Client localhost `

The client initiates connection with the server by obtaining a player name (1-4).

Once the client is connected, it can make requests. You can type these into the GUI for now. Client requests should begin with an ACTION keyword: WITHDRAW, BUY, or RESERVE.

**WITHDRAW** requests should somehow represent the coins that the player wants to withdraw; maybe like so:

` WITHDRAW 11010 `

Where the 5 positions correspond to the 5 colors of coins. You guys can decide your preferred implementation for that.

Similarly, **BUY** and **RESERVE** requests will need to encode an ID for the corresponding card to be bought or reserved.


Once the server receives the request, it will verify that it's valid. If the request is valid, the server will insert the name of the client that originated the request and then echo it to all the other clients: 

` WITHDRAW 1 11010 `

If the request is invalid, the server will send **REJECT**. Right now there's no game logic implemented in the server, so it just marks any request that isn't malformed as valid.

I'll leave it here for a while so you guys can work on adding the game logic and integrating the client and server into the rest of the project.

-- Dallin

## Notes on JavaFX

Because we are not using tools like Maven or gradle, any dependencies have to be configured individually.
Follow this link, click on JavaFX and IntelliJ, and start at step 3 to set up your environment to run the client class:

https://openjfx.io/openjfx-docs/