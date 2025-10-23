package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    public boolean ePressed;
    public int ePressTimer = 0;

    //DEBUG
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                if (gp.ui.commandNum - 1 < 0) {
                    gp.ui.commandNum = 1;
                } else {
                    gp.ui.commandNum--;
                }

            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                if (gp.ui.commandNum + 1 > 1) {
                    gp.ui.commandNum = 0;
                } else {
                    gp.ui.commandNum++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }

        //PLAY STATE
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
                ePressed = false;
            }

            if (code == KeyEvent.VK_S) {
                downPressed = true;
                ePressed = false;
            }

            if (code == KeyEvent.VK_A) {
                leftPressed = true;
                ePressed = false;
            }

            if (code == KeyEvent.VK_D) {
                rightPressed = true;
                ePressed = false;
            }

            if (code == KeyEvent.VK_SPACE) {
                gp.gameState = gp.pauseState;
                checkDrawTime = false;
                gp.music.stop();
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            
            if (code == KeyEvent.VK_E && !upPressed 
                && !downPressed && !rightPressed && !leftPressed && ePressTimer >= 30) {
                ePressed = true;
                ePressTimer = 0;
            }

            //DEBUG
            if (code == KeyEvent.VK_T) {
                if (!checkDrawTime) {
                    checkDrawTime = true;
                } else if (checkDrawTime) {
                    checkDrawTime = false;
                }
            }
        }

        //PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_SPACE) {
                gp.gameState = gp.playState;
                gp.music.play();
            } 
        }

        //DIALOGUE STATE
        else if (gp.gameState == gp.dialougeState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            } 
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
    }

}
