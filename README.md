## Progress Check Week 3
* (STARCRAFT)

## RUN SETUP
* VM ARGUMENTS

```
--module-path $PATH_TO_FX --add-modules javafx.controls,javafx.media,javafx.fxml

```

## Game Idea

Game outline - 1 player placed in a random corner of a maze with a AI bot placed in the opposite corner. The objective of the game is to destroy the other spaceship to gain a point. Spaceship shoots lasers which can rebound twice off walls.

## Requirements

## 1. Competitive and/or co-operative play
- Must (MVP) ::
    Player must compete against at least 1 AI starship
- Additional ::
    may include other modes E.g
    2 Players on same local computer (no AI)
    1 player against multiple AI starships

## 2. Artificial Intelligence
- Must (MVP) ::
    AI starship must try to kill the player starship
    AI must have same capabilities as the player (i.e  no extra advantages)
    AI must not be unbeatable but be competitive
    AI must be convincing as if playing against another human
- Additional ::
    AI may have more than one difficulty to compensate for players at different levels
## 3. User Interface
- Must (MVP) ::
    Game must have a main menu
    Main menu includes name of the game and various icons 
    Main menu must allow player to navigate to other subscenes including:
        Play (play the game)
        Help (game instructions/controls)
        Settings (adjust game settings including key bindings and sound settings)
        Exit (closes application)
    The interface when playing the game must display:
        Maze 
        Two starships within the maze (1 represents player and other the AI)
        2 health bars corresponding to each ships current health
        Nametag for each ship
        Current score
        Buttons to pause/exit the game at anytime
- Additional ::
    Main menu may include further subscenes such as scores/credits
    Play button may lead to options to change levels if decide to implement various difficulties
    Icons/symbols for additional power/health packs if decide to implement them
## 4. Audio
- Must (MVP) ::
    game must play background music track in a loop at all times
    game must play a ‘pew’ sound effect when laser is fired
    game must display breaking like sound when a laser is absorbed by a ship
    game must play explosion sound effect when a ship dies
    must have option in settings to change/ mute all sounds
- Additional ::
    game may provide additional sounds when a player wins/loses if decide to implement an overall winning condition rather than continuing indefinitely


## Current Implementation

## UI
* Have completed code for a main menu and uploaded to gitlab

## Game Logic
Currently working on base classes representing in game entities using OO programming:
* Bullet
* Map
* Tank 
* Wall

These classes shall integrate together within the game class.

## AI
* have good idea as to the behaviour of the AI and have made various behaviour trees/machine state diagrams.

* The AI ship should have a list of behaviours it can perform and every update it iterates through these until it finds one for which the conditions are met.
##  4 unique behaviours in order:
1) IDLE- bot waits a few seconds after game starts to simulate player reaction time
2) Shoot - bot shoots if there is a valid shot in a 200 degree arc in front of him anticipating 2 bounces (maybe 20 raycasts with difference of 10 degree difference)
Doesn’t shoot instantly and waits before being able to fire again (to not make it too difficult)

3) Dodge- if bot detects an incoming laser it will move out of the way

4) Path to player - AI uses optimal A* pathfinding algorithm to reach a point where a direct shot can be established

## Game Logic (player control)

* Each player/AI starts with 300 HP in a random corner of the maze
* Initially the user controls their ship with the arrows ( up = forward/ down=backwards/left= rotate left/ right= rotate right these can be used simultaneously and these bindings can be changed in settings)
* player/AI cannot go through walls in the maze
* Each player can shoot 3 lasers individually before a 5 second time delay before being able to shoot again (reload time)
* The laser rebounds off 2 walls before disintegrating when coming into contact with the 3rd wall ( maybe damage decreases at each wall bounce)
* When a player touches a laser they lose 100 hp
* The players wins a point when the opponent reaches 0 hp
* Power Ups/Health packs
* Speed boost
* One shot kill
* Shield (10 seconds cannot take damage)
* Homing missile (follows enemy directly for 5 seconds using optimal path finding)
* Health pack (restores player to full health despite what health they are currently on)
* A separate package will be coded where the Key Events from clients are handled. We name package as EventHandlers 

## Event Press Handler:

Triggers the user events from Keyboard and determines the Tank position on the Map.
The player has the ability to move top,right,left and bottom.
The player could shoot the bullet that traverses the map.
When each key pressed the locomotions are set to true in the Game environment.

## Event Release Handler:

Triggers the user events from Keyboard and stops the Tank position on the Map.
The player has the ability to move top,right,left and bottom.
When each key pressed the locomotions are set to false in result the Tank stops it’s movement.

## Team Roles 
- Artificial Intelligence/Character Class (Reuben and Eunji)
- UI design/Physics/Interactions (Alfred and Bharath)
- Audio (Iniyan Kanmani)

## Prototype demo plan

* The Game logic is developed as each individual entity and will be integrated and tested regularly as separate java classes.
* Each sub team will work on their individual areas in the coming weeks
* we aim to have meetings bi weekly to discuss and make sure everybody is on the same page and also to integrate code
* every area will be covered by week 7 and we will present however far we have got

## Aim
* UI should be almost complete (main menu/game interface should 	contain all required features by this point) however the map may   
not be fully installed if the AI bot is having difficulty with wall interaction

* Enemy ship should display a good sense of AI and the pathfinding algorithm should be implemented as a minimum.

* Background music should be implemented (sound effects will depend on the rest of the development)

## Resources

* MUSIC

- Link: https://www.google.com

* STOCK ICONS

- Link: https://www.google.com







