package colourless;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Catalin Floca-Maxim
 */

// make sure you rename this class if you are doing a copy/paste
public class MainGame extends JComponent implements KeyListener    {

    // Height and Width of our game
    static final int WIDTH = 1436;
    static final int HEIGHT = 872;
    
    // sets the framerate and delay for our game
    // you just need to select an appropriate framerate
    long desiredFPS = 60;
    long desiredTime = (1000)/desiredFPS;
    
    //Screens
    BufferedImage youDied = ImageHelper.loadImage("youDied.png");
    BufferedImage FlocaDEV = ImageHelper.loadImage("FlocaDEV.png");
    BufferedImage FlocaDEVPresents = ImageHelper.loadImage("FlocaDEVPresents.png");
    BufferedImage startScreenBW = ImageHelper.loadImage("colourlessTitlePageBW.png");
    BufferedImage startScreen = ImageHelper.loadImage("colourlessTitlePage.png");
    BufferedImage victoryAchieved = ImageHelper.loadImage("victoryAchieved.png");
    
    //Counting variables
    int presentsCount = 0;  //counter for time between FlocaDEV and FlocaDEVPresents screen
    int screenBWCount = 0;  //counter for time between FlocaDEVPresents and startScreenBW screen
    int screenCount = 0;    //counter for time between startScreenBW and startScreen screen
    
    //Game level (0 is the starting screen, -1 is the death screen, 3 is the victory screen)
    int level = 0;
    boolean level1Done = false;
    boolean level2Done = false;
    
    //Key variables:
    boolean up = false; boolean right = false; boolean left = false;    //Movement keys
    boolean R_restart = false;      //Restart button (R)
    boolean space = false;          //"Use item" key
    boolean stuck = false;          //If the player is stuck for whatever reason
    boolean released = true;        //Keep track of previous "up" value
    
    //Song
    Clip soundTrack;
    
    //Collision variables
    int moveX; int moveY;
    
    //Background images
    BufferedImage background = ImageHelper.loadImage("background.png");
    BufferedImage backgroundBW = ImageHelper.loadImage("backgroundBW.png");
    
    //PhaseShifts
    int phaseShift = 0;
    int phaseShiftLava = 0;
    
    //Level 1 variables here:
    
    //Player
    BufferedImage playerImage = ImageHelper.loadImage("playerRight.png");
    BufferedImage playerImageAxeBW = ImageHelper.loadImage("playerRightHasAxeBW.png");
    Rectangle player = new Rectangle(200, 120, 40, 80);
    int dx = 0; int dy = 0;     //displacement variables
    int speed = 4;
    boolean grounded = true;
    int air = 5*60; int airValueHolder = 300;   //How long he can stay submerged (under water)
    boolean hasAxe = false;                     //if the player has an axe
    boolean playerSubmerged = false;            //if the playeris under water
    
    //To prevent player from falling off the screen
    Rectangle bottom = new Rectangle(0, 872, 1436, 50);
    
    //Gravity
    int maxFall = 30;
    int gravity = 3;
    
    //Platforms
    Rectangle startingPlatform = new Rectangle(180, 200, 400, 30);
        BufferedImage startingPlatformImg = ImageHelper.loadImage("startingPlatform.png");
        BufferedImage startingPlatformImgBW = ImageHelper.loadImage("startingPlatformBW.png");
        BufferedImage startingPlatformImgBWGreen = ImageHelper.loadImage("startingPlatformBWGreen.png");
        BufferedImage startingPlatformImgBWBrown = ImageHelper.loadImage("startingPlatformBWBrown.png");
    Rectangle leftGround = new Rectangle(0, 760, 220, 30);
        BufferedImage leftGroundImg = ImageHelper.loadImage("leftGround.png");
        BufferedImage leftGroundImgBW = ImageHelper.loadImage("leftGroundBW.png");
        BufferedImage leftGroundImgBWGreen = ImageHelper.loadImage("leftGroundBWGreen.png");
        BufferedImage leftGroundImgBWBrown = ImageHelper.loadImage("leftGroundBWBrown.png");
    Rectangle rightGround = new Rectangle(320, 730, 630, 52);
        BufferedImage rightGroundImg = ImageHelper.loadImage("rightGround.png");
        BufferedImage rightGroundImgBW = ImageHelper.loadImage("rightGroundBW.png");
        BufferedImage rightGroundImgBWGreen = ImageHelper.loadImage("rightGroundBWGreen.png");
        BufferedImage rightGroundImgBWBrown = ImageHelper.loadImage("rightGroundBWBrown.png");
    Rectangle waterPlatform = new Rectangle(0, 832, 1436, 40); boolean drawAir = false;
    Rectangle axePlat = new Rectangle(0, 580, 50, 30);
        BufferedImage axePlatImg = ImageHelper.loadImage("axePlat.png");
        BufferedImage axePlatImgBW = ImageHelper.loadImage("axePlatBW.png");
        BufferedImage axePlatImgBWGreen = ImageHelper.loadImage("axePlatBWGreen.png");
        BufferedImage axePlatImgBWBrown = ImageHelper.loadImage("axePlatBWBrown.png");
    Rectangle airPlat = new Rectangle(1116, 678, 320, 30);
        BufferedImage airPlatImg = ImageHelper.loadImage("airPlat.png");
        BufferedImage airPlatImgBW = ImageHelper.loadImage("airPlatBW.png");
        BufferedImage airPlatImgBWGreen = ImageHelper.loadImage("airPlatBWGreen.png");
        BufferedImage airPlatImgBWBrown = ImageHelper.loadImage("airPlatBWBrown.png");
    Rectangle bluePotPlat = new Rectangle(900, 430, 350, 30);
        BufferedImage bluePotPlatImg = ImageHelper.loadImage("bluePotPlat.png");
        BufferedImage bluePotPlatImgBW = ImageHelper.loadImage("bluePotPlatBW.png");
        BufferedImage bluePotPlatImgBWGreen = ImageHelper.loadImage("bluePotPlatBWGreen.png");
        BufferedImage bluePotPlatImgBWBrown = ImageHelper.loadImage("bluePotPlatBWBrown.png");
    
    //Objects
    Rectangle tree = new Rectangle(320, 466, 80, 316); boolean drawTree = true;
        BufferedImage treeImgBW = ImageHelper.loadImage("treeBW.png");  //black and white tree
        BufferedImage treeImg = ImageHelper.loadImage("tree.png");      //coloured (brown) tree
    Rectangle treeLeaves = new Rectangle (180, 244, 360, 360); boolean drawTreeLeaves = true;
        BufferedImage treeLeavesImgBW = ImageHelper.loadImage("treeLeavesBW.png");  //black and white tree leaves
        BufferedImage treeLeavesImg = ImageHelper.loadImage("treeLeaves.png");      //coloured (green) tree leaves
    Rectangle vine = new Rectangle(180, 0, 15, 200);
        BufferedImage vineImg = ImageHelper.loadImage("vine.png");      //coloured vines
        BufferedImage vineImgBW = ImageHelper.loadImage("vineBW.png");  //black and white vines
    Rectangle rightSpring = new Rectangle(840, 714, 45, 21);
    Rectangle leftSpring = new Rectangle(140, 744, 45, 21);
        BufferedImage springImg = ImageHelper.loadImage("spring.png");      //coloured spring
        BufferedImage springImgBW = ImageHelper.loadImage("springBW.png");  //black adnw hite spring
    Rectangle airBag = new Rectangle(1136, 663, 15, 15); boolean drawAirBag = true;
        BufferedImage airBagImg = ImageHelper.loadImage("air.png");     //airbag doesn't need colouring
    Rectangle axe = new Rectangle(5, 556, 15, 24); boolean drawAxe = true;
        BufferedImage axeImgBW = ImageHelper.loadImage("axeBW.png");    //black and white axe
        BufferedImage axeImg = ImageHelper.loadImage("axe.png");        //coloured (brown) axe
    Rectangle portalEntrance = new Rectangle(5, 680, 55, 80);
    Rectangle portalExit = new Rectangle(330, 376, 55, 80);
        //Coloured portal animation
        BufferedImage portal1 = ImageHelper.loadImage("portal1.png");
        BufferedImage portal2 = ImageHelper.loadImage("portal2.png");
        BufferedImage portal3 = ImageHelper.loadImage("portal3.png");
        BufferedImage portal4 = ImageHelper.loadImage("portal4.png");
        BufferedImage[] portal = {portal1, portal2, portal3, portal4};
        Animation portalAnim = new Animation(6f, portal);
        //Black and White portal animation
        BufferedImage portal1BW = ImageHelper.loadImage("portal1BW.png");
        BufferedImage portal2BW = ImageHelper.loadImage("portal2BW.png");
        BufferedImage portal3BW = ImageHelper.loadImage("portal3BW.png");
        BufferedImage portal4BW = ImageHelper.loadImage("portal4BW.png");
        BufferedImage[] portalBW = {portal1BW, portal2BW, portal3BW, portal4BW};
        Animation portalBWAnim = new Animation(6f, portalBW);
    Rectangle door = new Rectangle(335, 633, 50, 97);
        BufferedImage doorImg = ImageHelper.loadImage("door.png");      //coloured (brown) door
        BufferedImage doorImgBW = ImageHelper.loadImage("doorBW.png");  //black and white door
    
    //Potions
    
    /*Note:
     * when any of the "draw--" colours are true, the potions exist; that colour is black and white.
     * when the "draw--" colours are false, the potions are gone, but the part of the world that is
     * that colour naturally, becomes that colour (i.e. that section which is black and white is coloured)
     */

    Rectangle redPot = new Rectangle(400, 181, 14, 19); boolean drawRed = true;
        BufferedImage redPotImg1 = ImageHelper.loadImage("potionEdit1Red.png");
        BufferedImage redPotImg2 = ImageHelper.loadImage("potionEdit2Red.png");
        BufferedImage redPotImg3 = ImageHelper.loadImage("potionEdit3Red.png");
        BufferedImage redPotImg4 = ImageHelper.loadImage("potionEdit4Red.png");
        BufferedImage[] redPotImg = {redPotImg1, redPotImg2, redPotImg3, redPotImg4, redPotImg3, redPotImg2};
        Animation redPotAnim = new Animation(8f, redPotImg);
    
    Rectangle bluePot = new Rectangle(1050, 411, 14, 19); boolean drawBlue = true;
        BufferedImage bluePotImg1 = ImageHelper.loadImage("potionEdit1Blue.png");
        BufferedImage bluePotImg2 = ImageHelper.loadImage("potionEdit2Blue.png");
        BufferedImage bluePotImg3 = ImageHelper.loadImage("potionEdit3Blue.png");
        BufferedImage bluePotImg4 = ImageHelper.loadImage("potionEdit4Blue.png");
        BufferedImage[] bluePotImg = {bluePotImg1, bluePotImg2, bluePotImg3, bluePotImg4, bluePotImg3, bluePotImg2};
        Animation bluePotAnim = new Animation(8f, bluePotImg);
        
    Rectangle greenPot = new Rectangle(90, 741, 14, 19); boolean drawGreen = true;
        BufferedImage greenPotImg1 = ImageHelper.loadImage("potionEdit1Green.png");
        BufferedImage greenPotImg2 = ImageHelper.loadImage("potionEdit2Green.png");
        BufferedImage greenPotImg3 = ImageHelper.loadImage("potionEdit3Green.png");
        BufferedImage greenPotImg4 = ImageHelper.loadImage("potionEdit4Green.png");
        BufferedImage[] greenPotImg = {greenPotImg1, greenPotImg2, greenPotImg3, greenPotImg4, greenPotImg3, greenPotImg2};
        Animation greenPotAnim = new Animation(8f, greenPotImg);
        
    Rectangle brownPot = new Rectangle(410, 336, 14, 19); boolean drawBrown = true;
        BufferedImage brownPotImg1 = ImageHelper.loadImage("potionEdit1Brown.png");
        BufferedImage brownPotImg2 = ImageHelper.loadImage("potionEdit2Brown.png");
        BufferedImage brownPotImg3 = ImageHelper.loadImage("potionEdit3Brown.png");
        BufferedImage brownPotImg4 = ImageHelper.loadImage("potionEdit4Brown.png");
        BufferedImage[] brownPotImg = {brownPotImg1, brownPotImg2, brownPotImg3, brownPotImg4, brownPotImg3, brownPotImg2};
        Animation brownPotAnim = new Animation(8f, brownPotImg);
    
    //Level 2 variables here: (most images are reused from level 1, with the exception of platform images)
    
    //Platforms
    Rectangle startingPlatformL2 = new Rectangle(0, 140, 280, 30);
        BufferedImage startingPlatformL2Img = ImageHelper.loadImage("startingPlatformL2.png");
        BufferedImage startingPlatformL2ImgBW = ImageHelper.loadImage("startingPlatformL2BW.png");
        BufferedImage startingPlatformL2ImgBWBrown = ImageHelper.loadImage("startingPlatformL2BWBrown.png");
        BufferedImage startingPlatformL2ImgBWGreen = ImageHelper.loadImage("startingPlatformL2BWGreen.png");
    Rectangle waterPlatformL2 = new Rectangle(0, 778, WIDTH, 97);   //CANNOT be less than 97!
    Rectangle lavaPlatformL2 = new Rectangle(950, 345, 180, 20);
    Rectangle leftGroundL2 = new Rectangle(0, 748, 170, 30);
        BufferedImage leftGroundL2Img = ImageHelper.loadImage("leftGroundL2.png");
        BufferedImage leftGroundL2ImgBW = ImageHelper.loadImage("leftGroundL2BW.png");
        BufferedImage leftGroundL2ImgBWBrown = ImageHelper.loadImage("leftGroundL2BWBrown.png");
        BufferedImage leftGroundL2ImgBWGreen = ImageHelper.loadImage("leftGroundL2BWGreen.png");
    Rectangle redPotPlatL2 = new Rectangle(620, 230, 70, 30);
        BufferedImage redPotPlatL2Img = ImageHelper.loadImage("redPotPlatL2.png");
        BufferedImage redPotPlatL2ImgBW = ImageHelper.loadImage("redPotPlatL2BW.png");
        BufferedImage redPotPlatL2ImgBWBrown = ImageHelper.loadImage("redPotPlatL2BWBrown.png");
        BufferedImage redPotPlatL2ImgBWGreen = ImageHelper.loadImage("redPotPlatL2BWGreen.png");
    Rectangle mainPlatL2 = new Rectangle(380, 350, 570, 30);
        BufferedImage mainPlatL2Img = ImageHelper.loadImage("mainPlatL2.png");
        BufferedImage mainPlatL2ImgBW = ImageHelper.loadImage("mainPlatL2BW.png");
        BufferedImage mainPlatL2ImgBWBrown = ImageHelper.loadImage("mainPlatL2BWBrown.png");
        BufferedImage mainPlatL2ImgBWGreen = ImageHelper.loadImage("mainPlatL2BWGreen.png");
    Rectangle lavaPlatL2 = new Rectangle(950, 375, 180, 5);
        BufferedImage lavaPlatL2Img = ImageHelper.loadImage("lavaPlatL2.png");
        BufferedImage lavaPlatL2ImgBW = ImageHelper.loadImage("lavaPlatL2BW.png");
    Rectangle portPlatL2 = new Rectangle(1130, 350, 210, 30);
        BufferedImage portPlatL2Img = ImageHelper.loadImage("portPlatL2.png");
        BufferedImage portPlatL2ImgBW = ImageHelper.loadImage("portPlatL2BW.png");
        BufferedImage portPlatL2ImgBWBrown = ImageHelper.loadImage("portPlatL2BWBrown.png");
        BufferedImage portPlatL2ImgBWGreen = ImageHelper.loadImage("portPlatL2BWGreen.png");
    Rectangle rightGroundL2 = new Rectangle(1270, 748, 170, 30);
        BufferedImage rightGroundL2Img = ImageHelper.loadImage("rightGroundL2.png");
        BufferedImage rightGroundL2ImgBW = ImageHelper.loadImage("rightGroundL2BW.png");
        BufferedImage rightGroundL2ImgBWBrown = ImageHelper.loadImage("rightGroundL2BWBrown.png");
        BufferedImage rightGroundL2ImgBWGreen = ImageHelper.loadImage("rightGroundL2BWGreen.png");
    Rectangle midGroundL2 = new Rectangle(220, 748, 1000, 30);
        BufferedImage midGroundL2Img = ImageHelper.loadImage("midGroundL2.png");
        BufferedImage midGroundL2ImgBW = ImageHelper.loadImage("midGroundL2BW.png");
        BufferedImage midGroundL2ImgBWBrown = ImageHelper.loadImage("midGroundL2BWBrown.png");
        BufferedImage midGroundL2ImgBWGreen = ImageHelper.loadImage("midGroundL2BWGreen.png");
    Rectangle greenPotPlatL2 = new Rectangle(0, 545, 200, 30);
        BufferedImage greenPotPlatL2Img = ImageHelper.loadImage("greenPotPlatL2.png");
        BufferedImage greenPotPlatL2ImgBW = ImageHelper.loadImage("greenPotPlatL2BW.png");
        BufferedImage greenPotPlatL2ImgBWBrown = ImageHelper.loadImage("greenPotPlatL2BWBrown.png");
        BufferedImage greenPotPlatL2ImgBWGreen = ImageHelper.loadImage("greenPotPlatL2BWGreen.png");
    
    //objects (door will be reused for all levels)
    Rectangle treeL2 = new Rectangle(390, 480, tree.width, tree.height);
    Rectangle springL2 = new Rectangle(250, 724, leftSpring.width, leftSpring.height);
    Rectangle axeL2 = new Rectangle(485, 721, axe.width, axe.height);
    Rectangle portalEntranceL2 = new Rectangle(1065, 665, portalEntrance.width, portalEntrance.height);
    Rectangle portalExitL2 = new Rectangle(1205, 270, portalExit.width, portalExit.height);
        //Coloured (blue) portal animation
        BufferedImage portal1L2 = ImageHelper.loadImage("portal1L2.png");
        BufferedImage portal2L2 = ImageHelper.loadImage("portal2L2.png");
        BufferedImage portal3L2 = ImageHelper.loadImage("portal3L2.png");
        BufferedImage portal4L2 = ImageHelper.loadImage("portal4L2.png");
        BufferedImage[] portalL2 = {portal1L2, portal2L2, portal3L2, portal4L2};
        Animation portalL2Anim = new Animation(6f, portalL2);
        //Black and White portal animation
        BufferedImage portal1L2BW = ImageHelper.loadImage("portal1L2BW.png");
        BufferedImage portal2L2BW = ImageHelper.loadImage("portal2L2BW.png");
        BufferedImage portal3L2BW = ImageHelper.loadImage("portal3L2BW.png");
        BufferedImage portal4L2BW = ImageHelper.loadImage("portal4L2BW.png");
        BufferedImage[] portalL2BW = {portal1L2BW, portal2L2BW, portal3L2BW, portal4L2BW};
        Animation portalL2BWAnim = new Animation(6f, portalL2BW);
    Rectangle lightningL2 = new Rectangle(740, 380, 15, 368);
        //coloured (blue) lightning animation
        BufferedImage lightning1L2Img = ImageHelper.loadImage("lightning1.png");
        BufferedImage lightning2L2Img = ImageHelper.loadImage("lightning2.png");
        BufferedImage lightning3L2Img = ImageHelper.loadImage("lightning3.png");
        BufferedImage lightning4L2Img = ImageHelper.loadImage("lightning4.png");
        BufferedImage[] lightningL2Img = {lightning1L2Img, lightning2L2Img, lightning3L2Img, lightning4L2Img};
        Animation lightningL2Anim = new Animation(24f, lightningL2Img);
        //Black and white lightning animation
        BufferedImage lightning1L2BWImg = ImageHelper.loadImage("lightning1BW.png");
        BufferedImage lightning2L2BWImg = ImageHelper.loadImage("lightning2BW.png");
        BufferedImage lightning3L2BWImg = ImageHelper.loadImage("lightning3BW.png");
        BufferedImage lightning4L2BWImg = ImageHelper.loadImage("lightning4BW.png");
        BufferedImage[] lightningL2BWImg = {lightning1L2BWImg, lightning2L2BWImg, lightning3L2BWImg, lightning4L2BWImg};
        Animation lightningL2BWAnim = new Animation(24f, lightningL2BWImg);
        
    //Fade colors
    double alpha = 0f;
    double alphaPresentsCount = 0f;
    double alphaScreenBWCount = 0f;
    double alphaScreenCount = 0f;
    
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g)  {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        
        // GAME DRAWING GOES HERE 
        
        if (level == 0) {
            //show FlocaDEV screen
            if (presentsCount != 2*60 && alphaPresentsCount < 1f)    {
                g.drawImage(FlocaDEV, 0, 0, null);
            }
            //show FlocaDEVPresents screen
            else if (presentsCount == 2*60 && screenBWCount < 2*60) {
                g.drawImage(FlocaDEV, 0, 0, null);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alphaPresentsCount));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(FlocaDEVPresents, 0, 0, null);
            }
            //fade to black
            else if (screenBWCount == 2*60 && presentsCount == 2*60 && alpha < 1f) {
                g.drawImage(FlocaDEVPresents, 0, 0, null);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
            }
            //show the black and white title screen
            else if (screenBWCount == 2*60 && presentsCount == 2*60 && alpha == 1f) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alphaScreenBWCount));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(startScreenBW, 0, 0, null);
            }
            else if (screenBWCount < 4*60) {
                g.drawImage(startScreenBW, 0, 0, null);                
            }
            //show the coloured title screen
            else if (screenBWCount == 4*60) {
                g.drawImage(startScreenBW, 0, 0, null);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alphaScreenCount));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(startScreen, 0, 0, null);
            }
        }
        
        if (level == 1 || (level == -1 && alpha < 1f && level1Done == false && level2Done == false)) {
            
            //Colours
            Color water = new Color(39, 189, 226);
            Color waterBW = new Color(140, 140, 140);   //Black and white color of water

            //background/sky
            if (drawBlue == true)   {
                g.drawImage(backgroundBW, 0, 0, null);
            }
            else    {
                g.drawImage(background, 0, 0, null);
            }

            //draw the bottom border (under water)
            g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);

            //platforms
            if (drawGreen == true && drawBrown == true) {
                g.drawImage(startingPlatformImgBW, startingPlatform.x, startingPlatform.y, null);
                g.drawImage(leftGroundImgBW, leftGround.x, leftGround.y, null);
                g.drawImage(rightGroundImgBW, rightGround.x, rightGround.y, null);
                g.drawImage(axePlatImgBW, axePlat.x, axePlat.y, null);
                g.drawImage(airPlatImgBW, airPlat.x, airPlat.y, null);
                g.drawImage(bluePotPlatImgBW, bluePotPlat.x, bluePotPlat.y, null);
            }
            else if (drawGreen == false && drawBrown == true)   {
                g.drawImage(startingPlatformImgBWGreen, startingPlatform.x, startingPlatform.y, null);
                g.drawImage(leftGroundImgBWGreen, leftGround.x, leftGround.y, null);
                g.drawImage(rightGroundImgBWGreen, rightGround.x, rightGround.y, null);
                g.drawImage(axePlatImgBWGreen, axePlat.x, axePlat.y, null);
                g.drawImage(airPlatImgBWGreen, airPlat.x, airPlat.y, null);
                g.drawImage(bluePotPlatImgBWGreen, bluePotPlat.x, bluePotPlat.y, null);
            }
            else if (drawGreen == true && drawBrown == false)   {
                g.drawImage(startingPlatformImgBWBrown, startingPlatform.x, startingPlatform.y, null);
                g.drawImage(leftGroundImgBWBrown, leftGround.x, leftGround.y, null);
                g.drawImage(rightGroundImgBWBrown, rightGround.x, rightGround.y, null);
                g.drawImage(axePlatImgBWBrown, axePlat.x, axePlat.y, null);
                g.drawImage(airPlatImgBWBrown, airPlat.x, airPlat.y, null);
                g.drawImage(bluePotPlatImgBWBrown, bluePotPlat.x, bluePotPlat.y, null);
            }
            else if (drawGreen == false && drawBrown == false)  {
                g.drawImage(startingPlatformImg, startingPlatform.x, startingPlatform.y, null);
                g.drawImage(leftGroundImg, leftGround.x, leftGround.y, null);
                g.drawImage(rightGroundImg, rightGround.x, rightGround.y, null);
                g.drawImage(axePlatImg, axePlat.x, axePlat.y, null);
                g.drawImage(airPlatImg, airPlat.x, airPlat.y, null);
                g.drawImage(bluePotPlatImg, bluePotPlat.x, bluePotPlat.y, null);
            }

            //objects
            
                //potions
            if (drawRed == true)    {
                g.drawImage(redPotAnim.getFrame(), redPot.x, redPot.y, null);
            }
            if (drawBlue == true)   {
                g.drawImage(bluePotAnim.getFrame(), bluePot.x, bluePot.y, null);
            }
            if (drawGreen == true)  {
                g.drawImage(greenPotAnim.getFrame(), greenPot.x, greenPot.y, null);
            }
            if (drawBrown == true)  {
                g.drawImage(brownPotAnim.getFrame(), brownPot.x, brownPot.y, null);
            }
            
                //other objects
            if (drawRed == true)    {
                g.drawImage(portalBWAnim.getFrame(), portalEntrance.x, portalEntrance.y, null);
                g.drawImage(portalBWAnim.getFrame(), portalExit.x, portalExit.y, null);
                
                g.drawImage(springImgBW, rightSpring.x, rightSpring.y, null);
                g.drawImage(springImgBW, leftSpring.x, leftSpring.y, null);
            }
            else    {
                g.drawImage(portalAnim.getFrame(), portalEntrance.x, portalEntrance.y, null);
                g.drawImage(portalAnim.getFrame(), portalExit.x, portalExit.y, null);
                
                g.drawImage(springImg, rightSpring.x, rightSpring.y, null);
                g.drawImage(springImg, leftSpring.x, leftSpring.y, null);
            }
            
            if (drawBrown == true)  {
                g.drawImage(doorImgBW, door.x, door.y, null);
                
                if (drawTree == true)   {
                    g.drawImage(treeImgBW, tree.x, tree.y, null);
                }
                
                if (drawAxe == true)    {
                    g.drawImage(axeImgBW, axe.x, axe.y, null);
                }
            }
            else    {
                g.drawImage(doorImg, door.x, door.y, null);
                
                if (drawTree == true)   {
                    g.drawImage(treeImg, tree.x, tree.y, null);
                }
                
                if (drawAxe == true)    {
                    g.drawImage(axeImg, axe.x, axe.y, null);
                }
            }
            
            if (drawGreen == true)  {
                if (drawTreeLeaves == true) {
                    g.drawImage(treeLeavesImgBW, treeLeaves.x, treeLeaves.y, null);
                }
                
                g.drawImage(vineImgBW, vine.x, vine.y, null);
            }
            
            else    {
                if (drawTreeLeaves == true) {
                    g.drawImage(treeLeavesImg, treeLeaves.x, treeLeaves.y, null);
                }
                
                g.drawImage(vineImg, vine.x, vine.y, null);
            }

            if (drawAirBag == true) {
                g.drawImage(airBagImg, airBag.x, airBag.y, null);
            }

            //water
            if (drawBlue == true)   {
                g.setColor(waterBW);
            }
            else    {
                g.setColor(water);
            }
            //Animate the water (using a sinusoidal function)
            int[] xpoints = new int[51];    int[] ypoints = new int[51];
            for(int i = 0; i < 49 ; i++)  {
                int k = i*30;
                xpoints[i] = k;
                ypoints[i] = (int)(772 - 5*Math.sin(Math.toRadians(k + phaseShift)));
            }
            xpoints[49]= 1440; xpoints[50] = 0;
            ypoints[49] = 872; ypoints[50] = 872;
            
            //g.fillRect(waterPlatform.x, waterPlatform.y, waterPlatform.width, waterPlatform.height);
            g.fillPolygon(xpoints, ypoints, 51);

            //player
            if (hasAxe == false)    {
                g.drawImage(playerImage, player.x, player.y, null);
            }
            else    {
                    g.drawImage(playerImageAxeBW, player.x, player.y, null);  
            }

            //Player conditions (show amount of air)
            if (drawAir == true)    {
                g.setColor(Color.BLUE);
                if (airValueHolder == 300)  {
                    g.drawRoundRect(1122, 20, airValueHolder, 20, 10, 10);
                    g.setColor(Color.CYAN);
                    g.fillRoundRect(1121, 21, air - 1, 19, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("AIR", 1132, 35);
                }
                else if (airValueHolder == 600) {
                    g.drawRoundRect(822, 20, airValueHolder, 20, 10, 10);
                    g.setColor(Color.CYAN);
                    g.fillRoundRect(821, 21, air - 1, 19, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("AIR", 832, 35);
                }
            }
        }
        
        if (level == 2 || (level == -1 && alpha < 1f && level1Done == true && level2Done == false))    {
            
            //Colours
            Color water = new Color(39, 189, 226);
            Color waterBW = new Color(140, 140, 140);   //Black and white color of water
            Color lava = new Color(193, 10, 10);
            Color lavaBW = new Color(50, 50, 50);       //Black and white colour of lava

            //background/sky
            if (drawBlue == true)   {
                g.drawImage(backgroundBW, 0, 0, null);
            }
            else    {
                g.drawImage(background, 0, 0, null);
            }

            //draw the bottom border (under water)
            g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);

            //platforms
            if (drawBrown == true && drawGreen == true) {
                g.drawImage(startingPlatformL2ImgBW, startingPlatformL2.x, startingPlatformL2.y, null);
                g.drawImage(mainPlatL2ImgBW, mainPlatL2.x, mainPlatL2.y, null);
                g.drawImage(redPotPlatL2ImgBW, redPotPlatL2.x, redPotPlatL2.y, null);
                g.drawImage(portPlatL2ImgBW, portPlatL2.x, portPlatL2.y, null);
                g.drawImage(rightGroundL2ImgBW, rightGroundL2.x, rightGroundL2.y, null);
                g.drawImage(midGroundL2ImgBW, midGroundL2.x, midGroundL2.y, null);
                g.drawImage(leftGroundL2ImgBW, leftGroundL2.x, leftGroundL2.y, null);
                g.drawImage(greenPotPlatL2ImgBW, greenPotPlatL2.x, greenPotPlatL2.y, null);
                g.drawImage(lavaPlatL2ImgBW, lavaPlatL2.x, lavaPlatL2.y, null);
            }
            else if (drawBrown == false && drawGreen == true)   {
                g.drawImage(startingPlatformL2ImgBWBrown, startingPlatformL2.x, startingPlatformL2.y, null);
                g.drawImage(mainPlatL2ImgBWBrown, mainPlatL2.x, mainPlatL2.y, null);
                g.drawImage(redPotPlatL2ImgBWBrown, redPotPlatL2.x, redPotPlatL2.y, null);
                g.drawImage(portPlatL2ImgBWBrown, portPlatL2.x, portPlatL2.y, null);
                g.drawImage(rightGroundL2ImgBWBrown, rightGroundL2.x, rightGroundL2.y, null);
                g.drawImage(midGroundL2ImgBWBrown, midGroundL2.x, midGroundL2.y, null);
                g.drawImage(leftGroundL2ImgBWBrown, leftGroundL2.x, leftGroundL2.y, null);
                g.drawImage(greenPotPlatL2ImgBWBrown, greenPotPlatL2.x, greenPotPlatL2.y, null);
                g.drawImage(lavaPlatL2Img, lavaPlatL2.x, lavaPlatL2.y, null);
            }
            else if (drawBrown == true && drawGreen == false)   {
                g.drawImage(startingPlatformL2ImgBWGreen, startingPlatformL2.x, startingPlatformL2.y, null);
                g.drawImage(mainPlatL2ImgBWGreen, mainPlatL2.x, mainPlatL2.y, null);
                g.drawImage(redPotPlatL2ImgBWGreen, redPotPlatL2.x, redPotPlatL2.y, null);
                g.drawImage(portPlatL2ImgBWGreen, portPlatL2.x, portPlatL2.y, null);
                g.drawImage(rightGroundL2ImgBWGreen, rightGroundL2.x, rightGroundL2.y, null);
                g.drawImage(midGroundL2ImgBWGreen, midGroundL2.x, midGroundL2.y, null);
                g.drawImage(leftGroundL2ImgBWGreen, leftGroundL2.x, leftGroundL2.y, null);
                g.drawImage(greenPotPlatL2ImgBWGreen, greenPotPlatL2.x, greenPotPlatL2.y, null);
                g.drawImage(lavaPlatL2ImgBW, lavaPlatL2.x, lavaPlatL2.y, null);
            }
            else if (drawBrown == false && drawGreen == false)  {
                g.drawImage(startingPlatformL2Img, startingPlatformL2.x, startingPlatformL2.y, null);
                g.drawImage(mainPlatL2Img, mainPlatL2.x, mainPlatL2.y, null);
                g.drawImage(redPotPlatL2Img, redPotPlatL2.x, redPotPlatL2.y, null);
                g.drawImage(portPlatL2Img, portPlatL2.x, portPlatL2.y, null);
                g.drawImage(rightGroundL2Img, rightGroundL2.x, rightGroundL2.y, null);
                g.drawImage(midGroundL2Img, midGroundL2.x, midGroundL2.y, null);
                g.drawImage(leftGroundL2Img, leftGroundL2.x, leftGroundL2.y, null);
                g.drawImage(greenPotPlatL2Img, greenPotPlatL2.x, greenPotPlatL2.y, null);
                g.drawImage(lavaPlatL2Img, lavaPlatL2.x, lavaPlatL2.y, null);
            }
            
            //water
            if (drawBlue == true)   {
                g.setColor(waterBW);
            }
            else    {
                g.setColor(water);
            }
            //Animate the water (using a sinusoidal function)
            int[] xpoints = new int[51];    int[] ypoints = new int[51];
            for(int i = 0; i < 49 ; i++)  {
                int k = i*30;
                xpoints[i] = k;
                ypoints[i] = (int)(765 - 5*Math.sin(Math.toRadians(k + phaseShift)));
            }
            xpoints[49]= 1440; xpoints[50] = 0;
            ypoints[49] = 872; ypoints[50] = 872;
            
            //g.fillRect(waterPlatform.x, waterPlatform.y, waterPlatform.width, waterPlatform.height);
            g.fillPolygon(xpoints, ypoints, 51);
            
            //lava
            if (drawRed == true)   {
                g.setColor(lavaBW);
            }
            else    {
                g.setColor(lava);
            }
            //Animate the lava (using a sinusoidal function -- this is much slower moving and with a larger cycle than water)
            int[] xpointsLava = new int[21];    int[] ypointsLava = new int[21];
            for(int i = 0; i < 19 ; i++)  {
                int k = i*10 + 950;
                xpointsLava[i] = k;
                ypointsLava[i] = (int)(355 - 2*Math.sin(Math.toRadians(k + phaseShiftLava)));
            }
            xpointsLava[19]= 1130; xpointsLava[20] = 950;
            ypointsLava[19] = 375; ypointsLava[20] = 375;
            
            g.fillPolygon(xpointsLava, ypointsLava, 21);

            //objects
            
                //potions
            if (drawRed == true)    {
                g.drawImage(redPotAnim.getFrame(), redPot.x, redPot.y, null);
            }
            if (drawBlue == true)   {
                g.drawImage(bluePotAnim.getFrame(), bluePot.x, bluePot.y, null);
            }
            if (drawGreen == true)  {
                g.drawImage(greenPotAnim.getFrame(), greenPot.x, greenPot.y, null);
            }
            if (drawBrown == true)  {
                g.drawImage(brownPotAnim.getFrame(), brownPot.x, brownPot.y, null);
            }
            
                //other objects
            if (drawRed == true)    {
                g.drawImage(springImgBW, springL2.x, springL2.y, null);
            }
            else    {
                g.drawImage(springImg, springL2.x, springL2.y, null);
            }
            
            if (drawBrown == true)  {
                g.drawImage(doorImgBW, door.x, door.y, null);
                
                if (drawTree == true)   {
                    g.drawImage(treeImgBW, treeL2.x, treeL2.y, null);
                }
                
                if (drawAxe == true)    {
                    g.drawImage(axeImgBW, axeL2.x, axeL2.y, null);
                }
            }
            else    {
                g.drawImage(doorImg, door.x, door.y, null);
                
                if (drawTree == true)   {
                    g.drawImage(treeImg, treeL2.x, treeL2.y, null);
                }
                
                if (drawAxe == true)    {
                    g.drawImage(axeImg, axeL2.x, axeL2.y, null);
                }
            }
            
            if (drawBlue == true)   {
                g.drawImage(portalL2BWAnim.getFrame(), portalEntranceL2.x, portalEntranceL2.y, null);
                g.drawImage(portalL2BWAnim.getFrame(), portalExitL2.x, portalExitL2.y, null);
                
                g.drawImage(lightningL2BWAnim.getFrame(), lightningL2.x, lightningL2.y, null);
            }
            else    {
                g.drawImage(portalL2Anim.getFrame(), portalEntranceL2.x, portalEntranceL2.y, null);
                g.drawImage(portalL2Anim.getFrame(), portalExitL2.x, portalExitL2.y, null);
                
                g.drawImage(lightningL2Anim.getFrame(), lightningL2.x, lightningL2.y, null);
            }
            
            //player
            if (hasAxe == false)    {
                g.drawImage(playerImage, player.x, player.y, null);
            }
            else    {
                g.drawImage(playerImageAxeBW, player.x, player.y, null);  
            }
            
            //Player conditions (show amount of air)
            if (drawAir == true)    {
                g.setColor(Color.BLUE);
                if (airValueHolder == 300)  {
                    g.drawRoundRect(1122, 20, airValueHolder, 20, 10, 10);
                    g.setColor(Color.CYAN);
                    g.fillRoundRect(1121, 21, air - 1, 19, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("AIR", 1132, 35);
                }
                else if (airValueHolder == 600) {
                    g.drawRoundRect(822, 20, airValueHolder, 20, 10, 10);
                    g.setColor(Color.CYAN);
                    g.fillRoundRect(821, 21, air - 1, 19, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("AIR", 832, 35);
                }
            }
        }
        
        //Victory achieved
        if (level2Done = true && level == 3)  {
                g.drawImage(victoryAchieved, 0, 0, null);
                g.setColor(Color.WHITE);
                g.drawString("Press [R] to Play Again", 1200, 800);
        }
        
        //if you died
        if (level == -1)    {
            if(alpha == 1f) {
                //after the fade effect, draw the youDied screen
                g.drawImage(youDied, 0, 0, null);
                g.setColor(Color.WHITE);
                g.drawString("Press [R] to Restart", 1200, 800);
            }
            else    {
                //fade effect
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
            }
        }
        
        // GAME DRAWING ENDS HERE
    }
    
        //Method for collisions with regular objects/platforms
        public void handleCollision(Rectangle player, Rectangle startingPlatform)    {
            
            if(player.intersects(startingPlatform))  {
                // returns a retangle of the intersection
                Rectangle intersection = player.intersection(startingPlatform);
                moveX = intersection.width;
                moveY = intersection.height;
                
                // deal with Y collision
                if(moveY <= moveX || dx == 0)  {
                    // collide with head
                    if(dy < 0 && player.y >= startingPlatform.y)  {
                        player.y = player.y + moveY;
                        //System.out.println("force down");     //testing purposes
                    }
                    else if (dy > 0 && player.y < startingPlatform.y)   {
                        player.y = player.y - moveY;
                        grounded = true;
                    }
                    dy = 0;
                }
                //deal with x collision
                else    { 
                    // collide right
                    if(dx > 0 )  {
                        player.x = player.x - moveX;
                    }
                    else {
                        player.x = player.x + moveX;
                    }
                    dx = 0;
                }
            }
    }
        
        //Fade effect for YOU DIED screen AND FlocaDEV and black
        public void fadeToBlack()   {
            if (alpha < 1f)  {
                alpha += 0.02f;
                //System.out.println(alpha);    //testing purposes
                if (alpha > 1) {
                    alpha = 1f;
                }
                repaint();
            }
        }
        
        //Fade effect between FlocaDEV and  FlocaDEVPresents
        public void fadeToPresents()  {
            if (alphaPresentsCount < 1)  {
                alphaPresentsCount += 0.02;
                //System.out.println(alphaPresentsCount);     //testing purposes
                if (alphaPresentsCount > 1) {
                    alphaPresentsCount = 1;
                }
                repaint();
            }
        }
        
        //Fade effect between black screen and ScreenBW
        public void fadeToScreenBW()    {
            if (alphaScreenBWCount < 1)  {
                alphaScreenBWCount += 0.02;
                //System.out.println(alphaScreenBWCount);   //testing purposes
                if (alphaScreenBWCount > 1) {
                    alphaScreenBWCount = 1;
                }
                repaint();
            }
        }
        
        //Fade effect between ScreenBW and Screen
        public void fadeToColour()  {
            if (alphaScreenCount < 1)  {
                alphaScreenCount += 0.005;
               // System.out.println(alphaScreenCount);     //testing purposes
                if (alphaScreenCount > 1) {
                    alphaScreenCount = 1;
                }
                repaint();
            }
        }
        
        //When the player jumps on a spring
        public void springJump(Rectangle player, Rectangle leftSpring)  {
            if (player.intersects(leftSpring))  {
                if (drawRed == true)   {
                    //can only jump if the spring is coloured
                    handleCollision(player, leftSpring);
                }
                else    {
                    //collision while on the ground (sideways)
                    if (dy <= 0 || grounded == true) {
                        //System.out.println("hit");    //testing purposes
                        Rectangle intersection = player.intersection(leftSpring);
                        moveX = intersection.width;
                        //collide right
                        if(dx > 0)  {
                            player.x = player.x - moveX;
                        }
                        //collide left
                        else    {
                            player.x = player.x + moveX;
                        }
                    }
                    else    {
                        //jump!
                        dy = -maxFall - 15;
                        grounded = false;
                    }
                }
            }
        }
        
        //When the player is under water
        public void submerged(Rectangle player, Rectangle waterPlatform) {
            playerSubmerged = true;     //so the player can't cut a tree while submerged
            if (drawBlue == true && level != -1)   {
                level = -1;
                alpha = 0;
                fadeToBlack();
            }
            else    {
                speed = 2;
                //Jumping
                if(grounded == true && up == true && stuck == false && released == true)  {
                    dy = -maxFall;
                    grounded = false;
                    released = false;
                }
                //Running out of breath            
                air = air - 1;
                if (air == 0 && level != -1) {
                    //You died!
                    level = - 1;
                    alpha = 0;
                    fadeToBlack();
                }
            }
        }
        
        //When the player uses an axe
        public void useAxe(Rectangle player, Rectangle axe) {
            if (hasAxe == true && player.intersects(treeLeaves) && drawGreen == false) {
                drawTreeLeaves = false;
            }
            if ((hasAxe == true && player.intersects(tree) || 
                    hasAxe == true && (player.x == 400 && (player.y >= 506 && player.y <= 812) ) //Essentially, the player touches the tree
                    ) && drawBrown == false)  {
                drawTree = false;
            }
            if ((hasAxe == true && player.intersects(treeL2) || 
                    (hasAxe == true && (player.x == 470 && (player.y >= 477 && player.y <= 793)) )
                    || (hasAxe == true && (player.x == 390 - player.width && (player.y >=477 && player.y <= 793)))  //Essentially, player touches the tree
                    ) && drawBrown == false && playerSubmerged == false)  {
                drawTree = false;
            }
        }
        
        //Restart (to level 1)
        public void restartLevelOne()   {
            level = 1;
            
            //game variables
            
            player = new Rectangle(200, 120, 40, 80);
            dx = 0; dy = 0;     //displacement variables
            speed = 4;
            grounded = true;
            air = 5*60; airValueHolder = 300;
            hasAxe = false;
            right = false; left = false; up = false; space = false;    //reset movement

            //To prevent player from falling off the screen
            bottom = new Rectangle(0, 872, 1436, 50);

            //Gravity
            maxFall = 30;
            gravity = 3;

           //Platforms
           startingPlatform = new Rectangle(180, 200, 400, 30);
           leftGround = new Rectangle(0, 760, 220, 52);
           rightGround = new Rectangle(320, 730, 630, 52);
           waterPlatform = new Rectangle(0, 832, 1436, 40); phaseShift = 0; drawAir = false;
           axePlat = new Rectangle(0, 580, 50, 46);
           airPlat = new Rectangle(1116, 678, 320, 46);
           bluePotPlat = new Rectangle(900, 430, 350, 46);
           
           //Objects
           tree = new Rectangle(320, 466, 80, 316); drawTree = true;
           treeLeaves = new Rectangle (180, 244, 360, 360); drawTreeLeaves = true;
           vine = new Rectangle(180, 0, 15, 200);
           rightSpring = new Rectangle(840, 714, 45, 21);
           leftSpring = new Rectangle(140, 744, 45, 21);
           airBag = new Rectangle(1136, 663, 15, 15); drawAirBag = true;
           axe = new Rectangle(5, 556, 15, 24); drawAxe = true;
           portalEntrance = new Rectangle(5, 680, 55, 80);
           portalExit = new Rectangle(330, 376, 55, 80);
           door = new Rectangle(335, 633, 50, 97);
           
           //Potions
           redPot = new Rectangle(400, 181, 14, 19); drawRed = true;
           bluePot = new Rectangle(1050, 411, 14, 19); drawBlue = true;        
           greenPot = new Rectangle(90, 741, 14, 19); drawGreen = true;
           brownPot = new Rectangle(410, 336, 14, 19); drawBrown = true;           
        }
        
        //Restart (to level 2)
        public void restartLevelTwo()   {
            level = 2;
            
            //game variables
            
            drawRed = true; drawBlue = true; drawGreen = true; drawBrown = true;
            
            player = new Rectangle(50, 60, 40, 80);
            dx = 0; dy = 0;     //displacement variables
            speed = 4;
            grounded = true;
            air = 5*60; airValueHolder = 300;
            hasAxe = false;
            right = false; left = false; up = false; space = false;    //reset movement
            
            //To prevent player from falling off the screen
            bottom = new Rectangle(0, 872, 1436, 50);

            //Gravity
            maxFall = 30;
            gravity = 3;
            
            //Platforms
            startingPlatformL2 = new Rectangle(0, 140, 280, 30);
            waterPlatformL2 = new Rectangle(0, 778, WIDTH, 97);
            lavaPlatformL2 = new Rectangle(950, 345, 180, 20);
            leftGroundL2 = new Rectangle(0, 748, 170, 30);
            redPotPlatL2 = new Rectangle(620, 230, 70, 30);
            mainPlatL2 = new Rectangle(380, 350, 570, 30);
            lavaPlatL2 = new Rectangle(950, 375, 180, 5);
            portPlatL2 = new Rectangle(1130, 350, 210, 30);
            rightGroundL2 = new Rectangle(1270, 748, 170, 30);
            midGroundL2 = new Rectangle(220, 748, 1000, 30);
            greenPotPlatL2 = new Rectangle(0, 545, 200, 30);

            //objects
            treeL2 = new Rectangle(390, 480, tree.width, tree.height);
            springL2 = new Rectangle(250, 724, leftSpring.width, leftSpring.height);
            axeL2 = new Rectangle(485, 721, axe.width, axe.height);
            portalEntranceL2 = new Rectangle(1065, 665, portalEntrance.width, portalEntrance.height);
            portalExitL2 = new Rectangle(1205, 270, portalExit.width, portalExit.height);
            door = new Rectangle(405, 775, door.width, door.height);
            
            //potions
            redPot = new Rectangle(648, 211, 14, 19);
            bluePot = new Rectangle(1000, 729, 14, 19);
            greenPot = new Rectangle(113, 526, 14, 19);
            brownPot = new Rectangle(150, 729, 14, 19);
            
            drawTree = true; drawAxe = true;
        }
            
    // The main game loop
    // In here is where all the logic for my game will go
    public void run()   {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        
        // the main game loop section
        // game will end if you set done = false;

        try {
            //game soundtrack
            File song = new File("song.wav");
            AudioInputStream AIS_Song = AudioSystem.getAudioInputStream(song);
            soundTrack = AudioSystem.getClip();
            soundTrack.open(AIS_Song);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        boolean done = false; 
        while(!done)    {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();
            
            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE
            
            //start screen
            if (level == 0) {
                //count to 2 seconds                
                if (presentsCount < 2*60) {
                    presentsCount++;
                }
                //fade to next screen
                if (presentsCount == 2*60)  {
                    fadeToPresents();
                }
                //count to 2 seconds
                if (presentsCount == 2*60 && alphaPresentsCount == 1f && screenBWCount < 2*60)  {
                    screenBWCount++;
                }
                //fade to black
                if (screenBWCount == 2*60 && alpha < 1f)  {
                    fadeToBlack();
                }
                //fade to next screen
                if(screenBWCount == 2*60 && alpha == 1f)    {
                    fadeToScreenBW();
                }
                //count an additional 2 seconds
                if(screenBWCount < 4*60 && alphaScreenBWCount == 1f) {
                    screenBWCount++;
                }
                //fade into colour
                if(screenBWCount == 4*60 && alphaScreenCount < 1f)  {
                    fadeToColour();
                }
                //Start song
                if(alphaScreenCount == 1f && !soundTrack.isActive())  {
                    soundTrack.loop(Clip.LOOP_CONTINUOUSLY);
                }
                //start game
                if (space == true && screenBWCount == 4*60 && alphaScreenCount == 1f)  {
                    level = 1;
                }
            }
            
            //level 1
            if (level == 1 && level1Done == false) {
                //phase shift (for water animation)
                phaseShift = (phaseShift + 2) % 360;
                
                //Restart
                if(R_restart == true)   {
                    restartLevelOne();
                }

                //Horizontal Movement
                if(right == true && player.x < 1406 && stuck == false)   {
                    playerImage = ImageHelper.loadImage("playerRight.png");
                    playerImageAxeBW = ImageHelper.loadImage("playerRightHasAxeBW.png");
                    dx = speed;
                }
                if(left == true && player.x > 0 && stuck == false)    {
                    playerImage = ImageHelper.loadImage("playerLeft.png");
                    playerImageAxeBW = ImageHelper.loadImage("playerLeftHasAxeBW.png");
                    dx = -speed;
                }
                if((right == false && left == false) || (right == true && left == true)) {
                    dx = 0;
                }
                player.x = player.x + dx;

                //Jumping
                if(grounded == true && up == true && stuck == false && released == true)  {
                    dy = -maxFall;
                    grounded = false;
                    released = false;
                }

                //Using tools
                if(space == true)   {
                    if (hasAxe == true) {
                        useAxe(player, axe);
                    }
                }

                //Gravity
                if(grounded == false)   {
                    dy = dy + gravity;
                    if(dy > maxFall) // make sure im not going too fast
                    {
                        dy = maxFall;
                    }
                player.y = player.y + dy;
                }
                
                //Animations
                
                    //Potion Animations
                if (drawRed == true)    {   //red potion
                    redPotAnim.play();
                }
                else    {
                    redPotAnim.stop();
                }
                if (drawBlue == true)   {   //blue potion
                    bluePotAnim.play();
                }
                else    {
                    bluePotAnim.stop();
                }
                if (drawGreen == true)  {   //green potion
                    greenPotAnim.play();
                }
                else    {
                    greenPotAnim.stop();
                }
                if (drawBrown == true)  {   //brown potion
                    brownPotAnim.play();
                }
                else    {
                    brownPotAnim.stop();
                }
                
                    //Portal Animations
                if (drawRed == true)    {
                    portalBWAnim.play();
                    portalAnim.stop();
                }
                else    {
                    portalBWAnim.stop();
                    portalAnim.play();
                }

                //Stop player from falling off the screen
                if (player.x >= 1406)   {
                    right = false;
                }
                if (player.x <= 0)  {
                    left = false;
                }
                if (player.intersects(bottom))    {
                    handleCollision(player, bottom);
                    grounded = true;
                }

                //Player submerged
                if(player.intersects(waterPlatform))    {
                    submerged(player, waterPlatform);
                    drawAir = true;
                }
                else    {
                    playerSubmerged = false;
                    maxFall = 30;
                    gravity = 3;
                    speed = 5;
                    drawAir = false;
                    if (air <= 300 || airValueHolder <= 300) {
                        air = 300;
                        airValueHolder = 300;
                    }
                }

                //Player collisions
                if(player.intersects(startingPlatform))  {
                    handleCollision(player,startingPlatform);
                }
                else    {
                    grounded = false;
                }

                if (player.intersects(leftGround))   {
                    handleCollision(player,leftGround);
                }

                if (player.intersects(tree) && drawTree == true)    {
                    handleCollision(player, tree);
                }

                if (player.intersects(rightGround)) {
                    handleCollision(player, rightGround);
                }

                if (player.intersects(axePlat)) {
                    handleCollision(player, axePlat);
                }

                if (player.intersects(airPlat)) {
                    handleCollision(player, airPlat);
                }

                if (player.intersects(vine))    {
                    handleCollision(player, vine);
                }

                if (player.intersects(bluePotPlat))   {
                    handleCollision(player, bluePotPlat);
                }

                if (player.intersects(rightSpring)) {
                    springJump(player, rightSpring);
                }
                
                if (player.intersects(leftSpring))  {
                    springJump(player, leftSpring);
                }
                
                if (player.intersects(airBag) && drawAirBag == true)  {
                    air = 10*60;
                    airValueHolder = 600;
                    drawAirBag = false;
                }
                
                if (player.intersects(axe) && drawAxe == true)  {
                    hasAxe = true;
                    drawAxe = false;
                }
                
                if (player.intersects(portalEntrance))  {
                    if (drawRed == false)   {
                        player.x = portalExit.x;
                        player.y = portalExit.y;
                    }
                }
                
                if (player.intersects(treeLeaves) && drawTreeLeaves == true)  {
                    //the player cannot move!
                    if (grounded == true)   {
                        stuck = true;                        
                    }
                }
                else    {
                    stuck = false;
                }
                
                if (player.intersects(redPot) && drawRed == true)  {
                    drawRed = false;
                }
                
                if (player.intersects(bluePot) && drawBlue == true) {
                    drawBlue = false;
                }
                
                if (player.intersects(greenPot) && drawGreen == true)    {
                    drawGreen = false;
                }
                
                if (player.intersects(brownPot) && drawBrown == true)    {
                    drawBrown = false;
                }
                
                //Next level (all potions must be collected)
                if (player.intersects(door) && drawBrown == false && drawBlue == false && drawRed == false && drawGreen == false)  {
                    if (space == true)  {
                        level1Done = true;
                        R_restart = false;
                        restartLevelTwo();
                    }
                }
            }
            
            //level 2
            if (level == 2 && level1Done == true && level2Done == false) {
                //phase shift (for water and lava animation)
                phaseShift = (phaseShift + 2) % 360;
                phaseShiftLava = (phaseShiftLava + 1) % 360;
                
                //Restart
                if(R_restart == true)   {
                    restartLevelTwo();
                }

                //Horizontal Movement
                if(right == true && player.x < 1406 && stuck == false)   {
                    playerImage = ImageHelper.loadImage("playerRight.png");
                    playerImageAxeBW = ImageHelper.loadImage("playerRightHasAxeBW.png");
                    dx = speed;
                }
                if(left == true && player.x > 0 && stuck == false)    {
                    playerImage = ImageHelper.loadImage("playerLeft.png");
                    playerImageAxeBW = ImageHelper.loadImage("playerLeftHasAxeBW.png");
                    dx = -speed;
                }
                if((right == false && left == false) || (right == true && left == true)) {
                    dx = 0;
                }
                player.x = player.x + dx;

                //Jumping
                if(grounded == true && up == true && stuck == false && released == true)  {
                    dy = -maxFall;
                    grounded = false;
                    released = false;
                }

                //Using tools
                if(space == true)   {
                    if (hasAxe == true) {
                        useAxe(player, axeL2);
                    }
                }

                //Gravity
                if(grounded == false)   {
                    dy = dy + gravity;
                    if(dy > maxFall) // make sure im not going too fast
                    {
                        dy = maxFall;
                    }
                player.y = player.y + dy;
                }
                
                //Animations
                
                    //Potion Animations
                if (drawRed == true)    {   //red potion
                    redPotAnim.play();
                }
                else    {
                    redPotAnim.stop();
                }
                if (drawBlue == true)   {   //blue potion
                    bluePotAnim.play();
                }
                else    {
                    bluePotAnim.stop();
                }
                if (drawGreen == true)  {   //green potion
                    greenPotAnim.play();
                }
                else    {
                    greenPotAnim.stop();
                }
                if (drawBrown == true)  {   //brown potion
                    brownPotAnim.play();
                }
                else    {
                    brownPotAnim.stop();
                }
                
                    //Portal Animations
                if (drawBlue == true)    {
                    portalL2BWAnim.play();
                    portalL2Anim.stop();
                }
                else    {
                    portalL2BWAnim.stop();
                    portalL2Anim.play();
                }
                
                    //Lightning animations
                if (drawBlue == true)   {
                    lightningL2BWAnim.play();
                    lightningL2Anim.stop();
                }
                else    {
                    lightningL2BWAnim.stop();
                    lightningL2Anim.play();
                }
                
                //Stop player from falling off the screen
                if (player.x >= 1406)   {
                    right = false;
                }
                if (player.x <= 0)  {
                    left = false;
                }
                if (player.intersects(bottom))    {
                    handleCollision(player, bottom);
                    grounded = true;
                }

                //Player submerged
                if(player.intersects(waterPlatformL2))    {
                    submerged(player, waterPlatformL2);
                    drawAir = true;
                }
                else    {
                    playerSubmerged = false;
                    maxFall = 30;
                    gravity = 3;
                    speed = 5;
                    drawAir = false;
                    if (air <= 300 || airValueHolder <= 300) {
                        air = 300;
                        airValueHolder = 300;
                    }
                }
                
                //Player touches lava
                if (lavaPlatformL2.contains(player.x, player.y + 80))  {
                    if (drawRed == false && level != -1)   {
                        //You died!
                        level = -1;
                        alpha = 0;
                        fadeToBlack();
                    }
                }
                
                //Player collisions
                if (player.intersects(startingPlatformL2))  {
                    handleCollision(player, startingPlatformL2);
                }
                else    {
                    grounded = false;
                }
                
                if (player.intersects(leftGroundL2))  {
                    handleCollision(player, leftGroundL2);
                }
                
                if (player.intersects(redPotPlatL2))  {
                    handleCollision(player, redPotPlatL2);
                }
                
                if (player.intersects(mainPlatL2))  {
                    handleCollision(player, mainPlatL2);
                }
                
                if (player.intersects(lavaPlatL2))  {
                    handleCollision(player, lavaPlatL2);
                }
                
                if (player.intersects(portPlatL2))  {
                    handleCollision(player, portPlatL2);
                }
                
                if (player.intersects(rightGroundL2))  {
                    handleCollision(player, rightGroundL2);
                }
                
                if (player.intersects(midGroundL2))  {
                    handleCollision(player, midGroundL2);
                }
                
                if (player.intersects(greenPotPlatL2))  {
                    handleCollision(player, greenPotPlatL2);
                }
                
                if (player.intersects(springL2)) {
                    springJump(player, springL2);
                }
                
                if (player.intersects(axeL2) && drawAxe == true)  {
                    hasAxe = true;
                    drawAxe = false;
                }
                
                if (player.intersects(portalEntranceL2))  {
                    if (drawBlue == false)   {
                        player.x = portalExitL2.x;
                        player.y = portalExitL2.y;
                    }
                }
                
                if (player.intersects(redPot) && drawRed == true)  {
                    drawRed = false;
                }
                
                if (player.intersects(bluePot) && drawBlue == true) {
                    drawBlue = false;
                }
                
                if (player.intersects(greenPot) && drawGreen == true)    {
                    drawGreen = false;
                }
                
                if (player.intersects(brownPot) && drawBrown == true)    {
                    drawBrown = false;
                }
                
                if (player.intersects(treeL2) && drawTree == true)      {
                    handleCollision(player, treeL2);
                }
                
                if (player.intersects(lightningL2)) {
                    if (drawBlue == false)  {
                        //You died!
                        level = -1;
                        alpha = 0;
                        fadeToBlack();
                    }
                }
                
                //Game completed
                if (player.intersects(door) && drawBrown == false && drawBlue == false && drawRed == false && drawGreen == false)  {
                    if (space == true)  {
                        level2Done = true;
                        level = 3;
                        //Victory achieved!
                    }
                }
            }
            
            if (level == 3) {
                if (R_restart == true)  {
                    level = 1;
                    level1Done = false;
                    level2Done = false;
                    restartLevelOne();
                }
            }
            
            //You died screen
            else if (level == -1)   {
                if (alpha < 1)  {
                    fadeToBlack();
                }
                if (R_restart == true)  {
                    if (level1Done == false)    {
                        restartLevelOne();  
                    }
                    else if (level1Done == true && level2Done == false) {
                        restartLevelTwo();
                    }
                } 
                else    {
                    level = -1;
                }
            }

            // GAME LOGIC ENDS HERE 
            
            // update the drawing (calls paintComponent)
            repaint();                        
            
            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if(deltaTime > desiredTime) {
                //took too much time, don't wait
            }
            else    {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                }catch(Exception e){};
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("Colourless");
       
        // creates an instance of my game
        MainGame game = new MainGame();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        // adds the game to the window
        frame.add(game);
         
        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        
        //add key listener
        frame.addKeyListener(game);
        
        // starts my game loop
        game.run();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (level == 0) {
            if(key == KeyEvent.VK_SPACE)    {
                space = true;
            }
        }
        if (level == 1 || level == 2) {
            if(key == KeyEvent.VK_RIGHT)    {
                right = true;
            }
            if(key == KeyEvent.VK_LEFT) {
                left = true;
            }
            if(key == KeyEvent.VK_SPACE)    {
                space = true;
            }
            if (key == KeyEvent.VK_UP)  {
                up = true;
            }
            if (key == KeyEvent.VK_R)   {
                R_restart = true;
            }
        }
        if (level == -1 || level == 3)    {
            if(key == KeyEvent.VK_R)    {
                R_restart = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (level == 0) {
            space = false;
        }
        if (level == 1 || level == 2) {
            if(key == KeyEvent.VK_RIGHT)    {
                right = false;
            }
            if(key == KeyEvent.VK_LEFT) {
                left = false;
            }
            if(key == KeyEvent.VK_SPACE)    {
                space = false;
            }
            if (key == KeyEvent.VK_UP)  {
                up = false;
                released = true;
            }
            if (key == KeyEvent.VK_R)   {
                R_restart = false;
            }
        }
    }
}