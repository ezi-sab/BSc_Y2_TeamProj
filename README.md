Game Logic
====================

### Player Control
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
