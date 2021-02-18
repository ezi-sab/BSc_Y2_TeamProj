Game Idea

Game outline - 1 player placed in a random corner of a maze with a AI bot placed in the opposite corner. The objective of the game is to destroy the other spaceship to gain a point. Spaceship shoots lasers which can rebound twice off walls.

Graphics
display a maze on top of a background
Display the player and the AI bot in the current position
display score in top right corner
One big health bar split in the middle (red represents player/black represents AI)
Panel bar at top includes:
Pause button
Exit button
Various power ups displayed around the maze every 10 seconds. (represented by symbols)

UI design (main menu)
System will display name of game at the top centre of the screen
System will display 5 buttons along the side
Play (go to player select screen then to maze to play the game)
Help (provide the user with the rules and give power ups definition)
Settings (Give user the option to edit audio settings (volume/sfx/music)/key bindings/resolution)
Credits (displays each member of the team and their roles in the implementation of the game).
Exit (exit the game)


Game Logic (player control)

Each player/AI starts with 300 HP in a random corner of the maze
Initially the user controls their ship with the arrows ( up = forward/ down=backwards/left= rotate left/ right= rotate right these can be used simultaneously and these bindings can be changed in settings)
player/AI cannot go through walls in the maze
Each player can shoot 3 lasers individually before a 5 second time delay before being able to shoot again (reload time)
The laser rebounds off 2 walls before disintegrating when coming into contact with the 3rd wall ( maybe damage decreases at each wall bounce)
When a player touches a laser they lose 100 hp
The players wins a point when the opponent reaches 0 hp
Power Ups/Health packs
Speed boost
One shot kill
Shield (10 seconds cannot take damage)
Homing missile (follows enemy directly for 5 seconds using optimal path finding)
Health pack (restores player to full health despite what health they are currently on)
















Competitive play- 1 player vs 1 AI bot (at least for now)

Artificial Intelligence 

The AI ship should have a list of behaviours it can perform and every update it iterates through these until it finds one for which the conditions are met.
6 unique behaviours in order:
1) IDLE- bot waits a few seconds after game starts to simulate player reaction time
2) Shoot - bot shoots if there is a valid shot in a 200 degree arc in front of him anticipating 2 bounces (maybe 20 raycasts with difference of 10 degree difference)
Doesn’t shoot instantly and waits before being able to fire again (to not make it too difficult)
3) Dodge
4) Path to player
			 

User Interface-

Audio 
play background music
‘Pew’ sound effect when laser is fired 
Explosion sound effect when player dies
Sound effect when player absorbs laser
Sound effect when laser bounces off wall


Team Roles 
- Artificial Intelligence/Character Class (Reuben and Eunji)
- UI design/Physics/Interactions (Alfred and Bharath)
- Audio (Iniyan Kanmani)

Prototype demo plan
