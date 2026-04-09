package mazeGame;

public class Player extends Sprite
{
    public Player(String characterName, int x, int y, int direction)
    {
        super(characterName, x, y, 42, 42, direction);

        // Character image size
        drawW = 140;
        drawH = 140;

        // Character image offset
        imageOffsetX = 18;
        imageOffsetY = -13;

        // Left flip image offset
        leftFlipOffsetX = -36;
    }
}