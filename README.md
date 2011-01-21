##MCTris - A Tetris Plugin for Minecraft

WARNING: Blindly building and installing this on your world may result in undesireable consequinces (i.e. it may take a big bite out of your world). So I HIGHLY recommend using an new world to test it out. Also, there's a chance that blindly installing and doing a /play will teleport you into solid rock -- and I'm not sure what happens when you do that, but I imagine it's not pretty.

This is currently not really in a user-friendly state. It is mostly designed to be used on my own world. There's a number of hard-coded attributes specific to my world.

The big world-specific information you should look for involves locations:

####MCTris.java:

* portCurPlayerToViewingArea(), portCurPlayerToEndGame(), and portCurPlayerToLose() contain locations to port the user to in various circumstances. These should be altered.

####Game.java:

* GAME_LOC_X, GAME_LOC_Y, and GAME_LOC_Z specify the block used for the bottom left-hand corner of the game board. Currently, the game board renders along the X-Y plane and should be viewed from a Z location < GAME_LOC_Z (in order to not have the controls be backwards!).
* TICK_LENGTH represents the number of milliseconds between "ticks" (which move pieces down.

####MCTrisPlayerListener.java:

* ALLOW_PREEMPT will likely be set to "true". This allows a player to jump into the game when someone else is playing. This should be set to false for any "production" servers in order to prevent people form having their games ruined. It might also result in undesireable side-effects to have someone pre-empt someone else.
* There is a "//REMOVE THESE" area. These should be removed for any public servers, as they will allow players to use the "/set" command to modify blocks at will. The /loc command is very useful in identifying locations for the placement of the board and teleport spots.

That's all I can think of that should be checked before deploying the plugin. Again, it's important not to deploy this plugin on any world you don't mind screwing up. It doesn't check before it starts re-writing parts of your world. So be carful!

Thanks!
