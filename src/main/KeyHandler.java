package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The class manages all keyboard inputs during gameplay.
 */
public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean enterPressed;
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
                gp.playSoundEffect(9);
                if (gp.ui.commandNum - 1 < 0) {
                    gp.ui.commandNum = 1;
                } else {
                    gp.ui.commandNum--;
                }

            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.playSoundEffect(9);
                if (gp.ui.commandNum + 1 > 1) {
                    gp.ui.commandNum = 0;
                } else {
                    gp.ui.commandNum++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.playSoundEffect(9);
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
                gp.playSoundEffect(9);
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
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.optionsState;
                gp.stopMusic();
                gp.playSoundEffect(9);
            }

            //DEBUG
            if (code == KeyEvent.VK_T) {
                if (!checkDrawTime) {
                    checkDrawTime = true;
                    gp.tileManager.drawPath = true;
                } else if (checkDrawTime) {
                    checkDrawTime = false;
                    gp.tileManager.drawPath = false;
                }
            }
        } else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_SPACE) {
                gp.playSoundEffect(9);
                gp.gameState = gp.playState;
                gp.music.play();
            } 
        } else if (gp.gameState == gp.optionsState) {

            if (code == KeyEvent.VK_W && !(gp.ui.subState == 1)) {
                if (gp.ui.commandNum > 0) {
                    gp.ui.commandNum--;
                    gp.playSoundEffect(9);
                }
            }

            if (code == KeyEvent.VK_S && !(gp.ui.subState == 1)) {
                if (gp.ui.commandNum < 4) {
                    gp.ui.commandNum++;
                    gp.playSoundEffect(9);
                }
            }

            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
                gp.playSoundEffect(9);
            }

            if (code == KeyEvent.VK_ESCAPE) {
                optionsState(code);
                gp.music.play();
                gp.music.loop();
                gp.playSoundEffect(9);
            }

            if (code == KeyEvent.VK_A) {
                if (gp.ui.subState == 0 && gp.ui.commandNum == 0 && gp.music.volumeScale > 0) {
                    gp.playSoundEffect(9);
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                }
                if (gp.ui.subState == 0 && gp.ui.commandNum == 1 
                    && gp.soundEffect.volumeScale > 0) {
                    gp.soundEffect.volumeScale--;
                    gp.playSoundEffect(9);
                }
            } else if (code == KeyEvent.VK_D) {
                if (gp.ui.subState == 0 && gp.ui.commandNum == 0 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSoundEffect(9);
                }
                if (gp.ui.subState == 0 && gp.ui.commandNum == 1 
                    && gp.soundEffect.volumeScale < 5) {
                    gp.soundEffect.volumeScale++;
                    gp.playSoundEffect(9);
                }
            }
        } else if (gp.gameState == gp.dialougeState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            } 
        } else if (gp.gameState == gp.gameOverState) {
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }
    
    /**
     * Manages escape and enter inputs.
     * 
     * @param code the key code received
     */
    public void optionsState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
            System.out.println("workingg");
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
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
