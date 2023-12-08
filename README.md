# Software Engineering project - a.y. 2022-23
![Box image](https://www.craniocreations.it/storage/media/products/54/112/My_Shelfie_box_ITA-ENG.png)  

## Project task
Implementation of [My Shelfie](https://www.craniocreations.it/prodotto/my-shelfie), a board game created by Cranio Creations.

The project task is to implement in Java a distributed system composed of a single server and multiple clients (one per player) that simulates the board game My Shelfie.  
Clients can interact with the server both via CLI and GUI, JavaFX is used for the latter. The network layer is implemented both using sockets and Java RMI.

### Documentation
Inside the [deliverables](https://github.com/gabricarm/IS23-AM08/tree/master/deliverables) folder there are UML diagrams and pdf documents to show the structure of the code and explain each design choice. Classes, interfaces and methods are well described using [javadoc](https://github.com/gabricarm/IS23-AM08/tree/master/deliverables/Javadoc).

## Implemented features
| Feature | Status | Description |
| --- | :---: | --- |
| Complete game | :green_circle: ||
| RMI | :green_circle: | Network layer using RMI |
| Socket | :green_circle: | Network layer using sockets |
| CLI | :green_circle: | Command line interface, on clients |
| GUI | :green_circle: | JavaFX on clients |
| Multiple matches | :green_circle: | The server supports multiple matches at the same time |
| Persistence | :green_circle: | The status of each match is saved on the server and can be restored |
| Chat | :green_circle: | Messages between players during the match |

## How to run

To launch the application you can open the CMD in the folder where the file AM08.jar is located and write

```
java -jar AM08.jar 
```

By default the application will start in client mode (you can also specify it with **--client** )

In order to launch the server you can launch

```
java -jar AM08.jar --server
```

There are some options you can also modify such as the ports used for either the TCP or RMI connections.
The commands are as follows:

```
--tcp-port 'port_number'
--rmi-port 'port_number'
```

**NOTE:** if you want to change the ports the clients also need to know them.

You can also specify two more options: 

```
--server-name 'name of the server app'
--game-name   'name of each game'
```

Which are mostly useful for the RMI connection. They are the names of the registries to which
the client will connect to play a game.

**NOTE:** each game name is transmitted from the server to the client, but if a RMI client does not
know the server name it will not be able to connect to it.

The default settings are:

```json
{
  "serverPortRMI": 42069,
  "serverPortTCP": 42070,
  "serverName": "LobbyServer",
  "startingName": "Game"
}
```
### Some notes for when playing the game

There is a maximum length for the nickname of a player:

```java
public class ViewConstants {
    // ...
    public static final int MAX_NICKNAME_LENGTH = 30;
    // ...
}
```

And it cannot contain the symbols specified in the file */network/server/lobbyServerBanList.json*. 
Which at the moment contains the following words and characters:

```json
[
  "all",
  ".*TestName1.*",
  ".*[ ]+.*",
  ".*[_]+.*",
  ".*[\\[]+.*",
  ".*[\\]]+.*",
  ""
]
```

**NOTE:** the format used is the standard notation for a regular expression in java, so the banned words
can be read as follows:

* the name cannot be the word "all" (keyword used for the all chat)
* the name cannot contain as a substring "TestName1" (used for testing)
* the name cannot contain a space 
* the name cannot contain an underscore
* the name cannot contain an opened square bracket ( [ )
* the name cannot contain a closed square bracket ( ] )
* the name cannot be empty

## Team members
- __Gabriele Carminati__ _@gabricarm_ gabriele.carminati@mail.polimi.it
- __Gabriele Carrino__ _@gabricarr_ gabriele.carrino@mail.polimi.it
- __Matteo Cenzato__ _@mattecenz_ matteo.cenzato@mail.polimi.it
- __Alessandro Capellino__ _@alecappe_ alessandro.capellino@mail.polimi.it

----------------------------------
## Copyright disclaimer
My Shelfie is property of Cranio Creations and all of the copyrighted graphical assets used in this project were supplied by Politecnico di Milano in collaboration with their rights' holders.
