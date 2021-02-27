(Draft)AI
============
* Have good idea as the behaviour of the AI and have made various bahaviour trees/machine state diagrams
* The AI ship should have a list of behaviours it can perform and every update it iterates through these until it finds one for which the conditions are met.
### 4 Unique behaviours in order
1) IDLE- bot waits a few seconds after game starts to simulate player reaction time

2) Shoot - bot shoots if there is a valid shot in a 200 degree arc in front of him anticipating 2 bounces (maybe 20 raycasts with difference of 10 degree difference) Doesn’t shoot instantly and waits before being able to fire again (to not make it too difficult)

3) Dodge- if bot detects an incoming laser it will move out of the way

4) Path to player - AI uses optimal A* pathfinding algorithm to reach a point where a direct shot can be established
