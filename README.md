# DoD
An implementation of a game called Dungeons of Doom.

In this game, there are one game mode
- Player vs Bot

The player and bot get randomly spawned into a map loaded in from a file. A default one is available if the loading fails. 
The player take turns against the bot to move around. This can be done by entering commands into the console. Rules are explained at the start, but "HELP" can be typed in to provide a list of commands and how to use them.
The commands in the game are:
- HELLO: Shows how much gold you need
- GOLD: Shows how much gold you own
- PICKUP: Picks up gold if you're on a gold tile"
- MOVE <DIRECTION>: Moves the character. Directions are N,E,S,W.
- LOOK: Shows a 5x5 grid of the map around the player
- QUIT: Quit the game.

The player has to collect enough gold to escape before player 2. 
There are tiles with gold (symbolised by the 'G' in the map file). Once the player walks onto that tile, they have to wait for the next turn to pickup the gold. The "PICKUP" command uses the go. 
Once the player has enough gold, they make their way to the exit tile and exiting the dungeon. 

The bot randomly spawns in at the start of the game. 
The bot plays like the human player, taking a turn and playing a command. The aim of the bot is to catch the human player before they get enough gold and exit. 
The bot currently moves randomly, and uses a modified version of the LOOK function. If the human player is in it's vicinity, it then tracks the human player down. 
A future feature that the bot could have is to find the gold before the human and exit before they do, or guard the exit. 
