# IS23-AM08

## Group members
* Gabriele Carminati
* Gabriele Carrino
* Matteo Cenzato CP: 10744078
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

```brainfuck
java -jar AM08.jar 
```

By default the application will start in client mode (you can also specify it with **--client** )

In order to launch the server you can launch

```brainfuck
java -jar AM08.jar --server
```

There are some options you can also modify such as the ports used for either the TCP or RMI connections.
The commands are as follows:

```brainfuck
--tcp-port 'port_number'
--rmi-port 'port_number'
```

**NOTE:** if you want to change the ports the clients also need to know them.

You can also specify two more options which are 

```brainfuck
--server-name 'name of the server app'
--game-name   'name of each game'
```

Which are mostly useful for the RMI connection. In fact they are the names of the registries to which
the client will connect to respectively connect to the main server and play a game.

**NOTE:** each game name is transmitted from the server to the client, but if a RMI client does not
know the server name it will not be able to connect to it.

The default settings (which should be kept this way for the best gaming experience) are:

```brainfuck
--tcp-port    42070
--rmi-port    42069
--server-name LobbyServer
--game-name   Game
```

## Project Requirements and Specifics

Following the scoring table present in the pdf file "requirements.pdf"
here are the functionalities that have been added to the project: 

* **Complete Rules**

The game includes all the rules specified in the ITALIAN rulebook.

* **TUI**

When the file AM08.jar is launched in client mode the application will start by default with the TUI.

* **GUI**

At very start the user will be prompted if he wants to play with GUI or TUI (cli/gui).
If the selected mode is GUI, a new window will be opened where the user can start playing with.

**NOTE:** the gui is done in JavaFX.

* **RMI and SOCKET**

The application can communicate both using RMI and TCP connection.
The server will open both the ports 42069 and 42070 for RMI and TCP respectively.
The client will be asked which connection does he want to use to communicate and the ip he wants to 
connect to.

**NOTE:** if you leave blank or type *default* the client will connect to the ip **127.0.0.1** (or localhost).

**NOTE:** the port selection has been removed at the end for a problem presented when the user selects 
RMI connection and tries to connect to the tcp port.

**NOTE:** the connection type is transparent to the final user, so he can join and
play games created both via TCP and RMI, and also no visible interface difference will be
shown if in a connection or another.

* **MULTIPLE GAMES**

The server can accept the creation of multiple games at the same time.
The client, after the successful connection to the server, can choose to either create
a game (with also the number of players) or to join a match.
It can both join a random match or select one of the possible games not already started.

* **PERSISTENCE**

Whenever the server crashes each game which is currently ongoing will be saved
in a folder called *savedMatches* and can be recovered later in a new start.

When the server is launched, it creates the folder *savedMatches* to store the games.
If it has already been created message will be shown in the server logs and the server will
automatically look for all files ".json" in it. The standard format for each file is
"name1_name2_.json". If some file in that format is found the server will load again that
match waiting for a player with that name to join it

For a user to rejoin a match it needs to have the same exact nickname (case sensitive),
then he needs to select the option to join a game, where the server will respond with a
particular message which indicates that the user can join a recovered game.

**NOTE:** when playing each game is saved in the folder, also the ended ones.
But when a game is ended on the next start will be automatically removed by the server.

**NOTE:** when a client crashes it is not counted as a persistence situation, hence
the game cannot be recovered after.

**NOTE:** it is better to not touch the folder savedMatches whenever the server is running
(preferably also when the server is off, since if you corrupt a file or put some random json file it can disrupt
the normal flow of the server application).

**NOTE:** if a player can recover a game, but decides to join a new one instead,
the recovered game will be completely lost and it will not be possible to re-access it.
This is to ensure the policy of having a single nickname playing or potentially playing
at the game every time.

* **CHAT**

When the users are in game they can chat both in the common and private chat.
The TUI provides different commands for each mode (it is explained when you play).
And the GUI provides different tabs for the all chat and the chat with a specific players.

## Some notes for when playing the game

The nickname you can choose has to be at maximum

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

----------------------------------

To demonstrate the persistance our group has prepared some games (stored in */deliverables/savedMatchesTest* )
which can be used to play some already existing games.

For the proper setup one must follow these few simple steps:

* pick one file with the saved match and **COPY** it in the folder */savedMatches* created by the server application (**NOTE:** you can also decide not to copy it and just drop it there, but if some client crashes or the game ends it will not be possible to reuse that template again since it will be lost)
* launch the application in server mode so that it can recover those games correctly
* enter as a client and choose the nickname suitable for the recovered game
* choose the options to recover a game (in TUI you need to select the "join" option and then the "recovery" option)

E.G. if the file name is *alfaStart_betaStart_.json* two clients, one named **alfaStart** and one **betaStart** need to connect and both select the option to **RECOVER** a game. (If one chooses to join another game it will be lost and the process will have to be done again)