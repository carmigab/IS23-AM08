# IS23-AM08

## Group members
* Gabriele Carminati
* Gabriele Carrino
* Matteo Cenzato 
* Alessandro Capellino


| Functionality  | State           | Test Coverage (Methods) |
|----------------|-----------------|-------------------------|
| GameModel      | :green_circle:  | 100%                    |
| GameBoard      | :green_circle:  | 100%                    |  
| PlayerState    | :green_circle:  | 100%                    |
| Shelf          | :green_circle:  | 100%                    |
| CommonGoal     | :green_circle:  | 63%                     |
| PersonalGoal   | :green_circle:  | 100%                    | 
| SingleGoal     | :green_circle:  | 100%                    | 
| GameController | :green_circle:  | 100%                    |
| Tile           | :green_circle:  | 100%                    |
| Position       | :green_circle:  | 100%                    |
| Server         | :green_circle:  |                         |
| Client         | :green_circle:  |                         |
| CLI            | :green_circle:  |                         |
| GUI            | :green_circle:  |                         |
| RMI            | :green_circle:  |                         |
| TCP            | :green_circle:  |                         |


# Launching the application

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

## Project Requirements and Specifics

Following the scoring table present in the pdf file "requirements.pdf"
those are the functionalities that we choose to implement: 

* **Complete Rules**

The game includes all the rules specified in the ITALIAN rulebook.

* **TUI**

When the file AM08.jar is launched in client mode the application will start by default with the TUI.

* **GUI**

At very start the user will be prompted if he wants to play with GUI or TUI (cli/gui).
If the selected mode is GUI, a new window will be opened.

**NOTE:** the gui is done in JavaFX.

* **RMI and SOCKET**

The application can communicate using both RMI and TCP.
The server will open the ports 42069 and 42070 for RMI and TCP respectively.
The client will be asked which connection does he want to use and the ip he wants to 
connect to.

**NOTE:** if you leave blank or type *default* the client will connect to the ip **127.0.0.1** (or localhost).

**NOTE:** the connection type is transparent to the final user, so he can join and
play games created both via TCP and RMI, and also no visible interface difference will be
shown if a connection is chosen.

* **MULTIPLE GAMES**

The server can accept the creation of multiple games at the same time.
The client, after the successful connection to the server, can choose to either create
a game (with also the number of players) or to join a match.
It can both join a random match or select one of the possible games not already started.

* **PERSISTENCE**

Whenever the server crashes each ongoing game will be saved
in a folder called *savedMatches* and will be able to be recovered when the server is restarted.

The first time the server is launched on a machine, it will create the folder *savedMatches*.
If it has already been created a message will be shown in the server logs and the server will
automatically look for all files ".json" in it. The standard format for each file is
"name1_name2_.json". If files matching the format are found the server will load them and 
make them available to be recovered.

For a user to rejoin a match it needs to have the exact same nickname (case-sensitive),
then he needs to select the option to recover a game.

**NOTE:** when a client crashes it does not fall under the persistence case, hence
the game won't be able to be recovered.

**NOTE:** it is better to not touch the folder savedMatches whenever the server is running
(preferably also when the server is off, since if you corrupt a file or put some random json file in it 
the flow of the server application will be disrupted).

**NOTE:** if a player can recover a game, but decides to join a new one instead,
the recovered game will be completely lost, and it will not be possible to re-access it.
This is to ensure that no player waits for someone that has started a new match.

* **CHAT**

While playing the users will be able to use both a private and common chat.
The TUI provides different commands for each mode, 
the GUI instead provides different tabs.

## Some notes for when playing the game

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

