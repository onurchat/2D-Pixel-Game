package main;

import java.util.ArrayList;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) { 


        //Coordinates of the collision box of the entity
        int entityLeftWorldX = entity.worldX + entity.solidArea.x; 
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;


        //TILE coordinates of the collision box
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2, tileNum3;

        switch(entity.direction) {

            case "up":
                //what 2 tiles player is trying to step in
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "upright":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "upleft":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "downright":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "downleft":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision
                    || gp.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }

    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                //get entitys solid areas position (x,y) in world
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get objects solid areas x and y in world
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "upright":
                        entity.solidArea.y -= entity.speed;
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "upleft":
                        entity.solidArea.y -= entity.speed;
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "downright":
                        entity.solidArea.y += entity.speed;
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "downleft":
                        entity.solidArea.y += entity.speed;
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }

                //resetting values
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidaAreaDefaultY; 
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;

            }
        }

        return index;
    }
    //NPC or MONSTER COLLISION
    public int checkEntity(Entity entity, ArrayList<Entity> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                //get entitys solid areas position (x,y) in world
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get NPCs solid areas x and y in world
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "upright":
                        entity.solidArea.y -= entity.speed;
                        entity.solidArea.x += entity.speed;
                        break;
                    case "upleft":
                        entity.solidArea.y -= entity.speed;
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "downright":
                        entity.solidArea.y += entity.speed;
                        entity.solidArea.x += entity.speed;
                        break;
                    case "downleft":
                        entity.solidArea.y += entity.speed;
                        entity.solidArea.x -= entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    if (target.get(i) != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }
                //resetting values
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidaAreaDefaultY; 
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidaAreaDefaultY;
                }
            }
        return index;
    }

    //CHECKING IF NPC's or MONSTERS ARE TRYING TO HIT PLAYER
    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        //get entitys solid areas position (x,y) in world
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        //get PLAYERs solid areas x and y in world
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;    
        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        //resetting values
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidaAreaDefaultY; 
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidaAreaDefaultY;
    
        return contactPlayer;
    }     
}
