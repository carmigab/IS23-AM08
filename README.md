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


## Project Requirements and Specifics

Following the scoring table present in the pdf file "requirements.pdf"
here are the functionalities that have been added to the project: 

* Complete Rules

The game includes all the rules specified in the ITALIAN rulebook.

* TUI

When the file AM08.jar is launched in client mode the application will start with the TUI.

* GUI

At very start the user will be prompted if he wants to play with GUI or TUI (cli/gui).
If the selected mode is GUI, a new window will be opened where the user can start playing with.

Note that the gui is done in JavaFX.

* RMI and SOCKET

The application can communicate both using RMI and TCP connection.
The server will open both the ports 42069 and 42070 for RMI and TCP respectively (by default, but can be changed).
The client, after has inserted the ip address and the port of the server,
will be asked to select which connection type does he want to use.

Note that the connection type is transparent to the final user, so he can join and
play games created both via TCP and RMI, and also no visible interface difference will be
shown if in a connection or another.

* MULTIPLE GAMES

The server can accept the creation of multiple games at the same time.
The client, after the successful connection to the server, can choose to either create
a game (with also the number of players) or to join a match.
It can both join a random match or select one of the possible games not already started.

* PERSISTENCE

Whenever the server crashes each game which is currently ongoing will be saved
in a folder called "/savedMatches" and can be recovered later in a new start.

When the server is launched, it creates the folder savedMatches to store the games.
If it has already been created message will be shown in the server logs and the server will
automatically look for all files ".json" in it. The standard format for each file is
"name1_name2_.json". If some file in that format is found the server will load again that
match waiting for a player with that name to join it

For a user to rejoin a match it needs to have the same exact nickname (case sensitive),
then he needs to select the option to join a game, where the server will respond with a
particular message which indicates that the user can join a recovered game.

Note that when playing each game is saved in the folder, also the ended ones.
But when a game is ended on the next start will be automatically removed by the server.

Note that when a client crashes it is not counted as a persistence situation, hence
the game cannot be recovered after.

Note that it is better to not touch the folder savedMatches whenever the server is running
(preferrably also when the server is off, since if you corrupt a file or put some random json file it can disrupt
the normal flow of the server application).

Note that if a player can recover a game, but decides to join a new one instead,
the recovered game will be completely lost and it will not be possible to re-access it.
This is to ensure the policy of having a single nickname playing or potentially playing
at the game every time.

* CHAT

When the users are in game they can chat both in the common and private chat.
The TUI provides different commands for each mode (it is explained when you play).





