import com.sun.javafx.image.BytePixelSetter;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.awt.Image;
import java.awt.Toolkit;

public class BattlePaint extends JPanel {
    private Robot robot;
    private BufferedImage BattleBackground;
    private int x,y,xR,yR,turn,turnType;
    Field field = new Field(20,20);
    // ����� �� battle
    final Random random = new Random();
    int i,j,k,a;
    int damageTaker;
    int attackIndicator, moveIndicator;
    int stepsDone;
    Timer timer;
    int[] enemy = new int[7];
    int[] order = new int[14];  // order[ ������ ������: 0 - hero1, 1-6 - army1, 7 - hero2, 8-13 - army2 ]
    Image[] A = new Image[7];
    Image[] B = new Image[7];
    public void GenerateOrder() { // ����������� ������� ����� ������
        int i,k;
        for (i=0;i<14;i++) {
            order[i]=-1;
        }
        for (j=13;j>=0;j--) {   // �� ������
            k = random.nextInt(j+1); // ��������� �� 0 �� j ������������ ����� �����
            for (i=0;k>0||order[i]>=0;i++) {    // �� ������� � �������
                if (order[i]<0) {
                    k--;
                }
            }
            order[i]=j;
        }
    }

    public BattlePaint() {
        super();
        //������ �����������
        try {
            if (paintTest.mode == 2) {
                BattleBackground = ImageIO.read(new File("Bckg2.png"));
            }
            else {
                BattleBackground = ImageIO.read(new File("Bckg3.png"));
            }
        } catch (IOException ex) {
//            System.out.println("������ ��� ����������� ��������. �� �����.");
        }
        //������ �����������
        ActionListener task = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
//                System.out.println("stepsDone="+ stepsDone +"; Timer is called.");
                repaint();
            }
        };
        try {
            A[0] = ImageIO.read(new File("Creatures/A/hero.png"));
            B[0] = ImageIO.read(new File("Creatures/B/Ehero.png"));
            A[1] = ImageIO.read(new File("Creatures/A/weak.png"));
            B[1] = ImageIO.read(new File("Creatures/B/Eweak.png"));
            A[2] = ImageIO.read(new File("Creatures/A/tank.png"));
            B[2] = ImageIO.read(new File("Creatures/B/Etank.png"));
            A[3] = ImageIO.read(new File("Creatures/A/archer.png"));
            B[3] = ImageIO.read(new File("Creatures/B/Earcher.png"));
            A[4] = ImageIO.read(new File("Creatures/A/dd.png"));
            B[4] = ImageIO.read(new File("Creatures/B/Edd.png"));
            A[5] = ImageIO.read(new File("Creatures/A/mag.png"));
            B[5] = ImageIO.read(new File("Creatures/B/Emag.png"));
            A[6] = ImageIO.read(new File("Creatures/A/elite.png"));
            B[6] = ImageIO.read(new File("Creatures/B/Eelite.png"));
        } catch (IOException ex) {
            System.out.println("������ ��� ����������� ��������. �� �����.");
        }
        // = Toolkit.getDefaultToolkit().createImage("��������/�����/�����������/�����.gif");
        timer = new Timer(400 , task);  // 900
        field.makeNeighbours();
        x = xR = 0;
        y = yR = 0;
        setSize(600, 600);
        GenerateOrder();
        turn = 0;
        turnType = 1;
        attackIndicator = 0;
        moveIndicator = 0;
        stepsDone = 0;
        paintTest.Ehero[paintTest.mode - 1].army[0].number = 2 - paintTest.mode;
        paintTest.Ehero[paintTest.mode - 1].army[0].position = 402;
        for (i = 1; i < 7; i++) { // ��������������� ��������� currHealth � currMana ����� �������� ���.
            paintTest.hero.army[i].setDefault(i);
            paintTest.Ehero[paintTest.mode - 1].army[i].setDefault(i);
        }
        for (i=1; i < 7; i++) {
            paintTest.Ehero[paintTest.mode - 1].army[i].position = 399 - paintTest.Ehero[paintTest.mode - 1].army[i].position;
        }
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getModifiers() == evt.BUTTON1_MASK) {
                    x = evt.getPoint().x;
                    y = evt.getPoint().y;
                    //IMPORTANT!
                    repaint();
                }
                if (evt.getModifiers() == evt.BUTTON3_MASK) {
                    xR = evt.getPoint().x;
                    yR = evt.getPoint().y;
                    //IMPORTANT!
                    repaint();
                }
            }
        });
    }

    public void nextTurn() {
        turn++;
        if(turn == 14) {
            GenerateOrder();
            turn=0;
        }
        turnType = 1;
        x = xR = 0;
        y = yR = 0;
        field.refreshContain();
        repaint();
    }

    public void makeMovePlayer(Graphics g, Polygon poly[][], Hero PlayerA, Hero PlayerB){
        // 0 - ������������ ������
        // 1 - ��� ����
        // 10 - ��������� ������ ������
        // (-11) - (-16) - ����� ������, �� ������� ����� ���������
        // (-21) - (-26) - ����� ������, �� ������� ������ ���������
        // (-31) - (-36) - ����� �����, �� ������� ����� ���������
        // (-41) - (-46) - ����� �����, �� ������� ������ ���������
        int i, j, k;
        if (turnType == 1) {
            if(PlayerA.army[order[turn] % 7].number > 0) {
                field.refreshContain();
                for (k = 1; k < 7; k++) {
                    field.contain[PlayerA.army[k].position] = -20 - k;
                    field.contain[PlayerB.army[k].position] = -40 - k;
                }
                field.range(PlayerA, order[turn] % 7);
                field.contain[PlayerA.army[order[turn] % 7].position] = 1;
                for (i = 2; i <= field.available[0]; i++) { //������������ ��������� ����
                    field.contain[field.available[i]] += 10;
                    g.setColor(Color.ORANGE);
                    g.fillPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                    g.setColor(new Color(255, 255, 255));
                    g.drawPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                }
                if ((order[turn] % 7 == 0) || (order[turn] % 7 == 3)) { // ���� ����� ��� ������ - ����� ���� ���� �����������
                    for (k = 1; k < 7; k++) {
                        if (field.contain[PlayerB.army[k].position] < -40) {
                            field.contain[PlayerB.army[k].position] += 10;
                        }
                    }
                }
                field.contain[400] = -1;
                // poly[20][1] � poly[20][2] - ����� ��� ������, ������ - ������ ������������.
                g.setColor(Color.black);
                Font font = new Font("Arial", Font.BOLD , 15);
                g.setFont(font);
                if (((order[turn] % 7) == 0) || ((order[turn] % 7) == 5)) { // ���� ����� ��� ��� - ����� ��������� ��������
                    if (PlayerA.army[order[turn] % 7].currMana >= 10) {  // ���� ���� ���� �� ��������
                        g.setColor(new Color(255, 255, 255));
                    }
                    else {
                        g.setColor(new Color(150, 150, 150));
                    }
                    if (order[turn] / 7 == 0) {
                        g.drawPolygon(poly[3][20]);
                        g.fillPolygon(poly[3][20]);
                        g.setColor(new Color(0, 0, 0));
                        g.drawString("Fireball", 33, 77);
                    }
                    else {
                        g.drawPolygon(poly[4][20]);
                        g.fillPolygon(poly[4][20]);
                        g.setColor(new Color(0, 0, 0));
                        g.drawString("Fireball", 958, 761);
                    }
                }
                for (i = 0; i < 7; i++) {
                    if ((PlayerA.army[i].number > 0) && (poly[PlayerA.army[i].position % field.sizeX][PlayerA.army[i].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                        g.drawString("Number: " + PlayerA.army[i].number + "   Health: " + PlayerA.army[i].health + "   Current Health: " + PlayerA.army[i].currHealth + "   Total Health: " + (PlayerA.army[i].health * (PlayerA.army[i].number - 1) + PlayerA.army[i].currHealth) + "   Damage: " + PlayerA.army[i].damage + "   Total Damage: " + (PlayerA.army[i].damage * PlayerA.army[i].number) + "   Mana: " + PlayerA.army[i].currMana, 150, 840);
                    }
                    if ((PlayerB.army[i].number > 0) && (poly[PlayerB.army[i].position % field.sizeX][PlayerB.army[i].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                        g.drawString("Number: " + PlayerB.army[i].number + "   Health: " + PlayerB.army[i].health + "   Current Heatlth: " + PlayerB.army[i].currHealth + "   Total Health: " + (PlayerB.army[i].health * (PlayerB.army[i].number - 1) + PlayerB.army[i].currHealth) + "   Damage: " + PlayerB.army[i].damage + "   Total Damage: " + (PlayerB.army[i].damage * PlayerB.army[i].number) + "   Mana: " + PlayerB.army[i].currMana, 150, 840);
                        if ((-36 <= field.contain[PlayerB.army[i].position]) && (field.contain[PlayerB.army[i].position] <= -31)) {
                            if ((PlayerB.army[i].number - (((((PlayerB.army[i].health * (PlayerB.army[i].number - 1)) + PlayerB.army[i].currHealth - (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].damage)) - 1) / PlayerB.army[i].health) + 1)) < PlayerB.army[i].number) {
                                g.drawString("Can deal " + (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].damage) + " damage and kill " + (PlayerB.army[i].number - (((((PlayerB.army[i].health * (PlayerB.army[i].number - 1)) + PlayerB.army[i].currHealth - (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].damage)) - 1) / PlayerB.army[i].health) + 1)), 150, 860);
                            }
                            else {
                                g.drawString("Can deal " + (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].damage) + " damage and kill them all", 150, 880);
                            }
                        }
                        if (((order[turn] % 7 == 0) || (order[turn] % 7 == 5)) && (PlayerA.army[order[turn] % 7].currMana >= 10)) {
                            if ((PlayerB.army[i].number - (((((PlayerB.army[i].health * (PlayerB.army[i].number - 1)) + PlayerB.army[i].currHealth - (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].magicPower * 5)) - 1) / PlayerB.army[i].health) + 1)) < PlayerB.army[i].number){
                                g.drawString("Can deal " + (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].magicPower * 5) + " damage and kill " + (PlayerB.army[i].number - (((((PlayerB.army[i].health * (PlayerB.army[i].number - 1)) + PlayerB.army[i].currHealth - (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].magicPower * 5)) - 1) / PlayerB.army[i].health) + 1)) + " with Fireball", 150, 880);
                            }
                            else {
                                g.drawString("Can deal " + (PlayerA.army[order[turn] % 7].number * PlayerA.army[order[turn] % 7].magicPower * 5) + " damage and kill them all with Fireball", 150, 880);
                            }
                        }
                    }
                }
                for (i = 0; i < 400; i++) { // ������ ����
                    if (poly[i % field.sizeX][i / field.sizeX].contains(x, y)) {    // ���� ������ �� ������ ����
                        if (field.contain[i] == 1) {   // ���� ������ �� ����
                            nextTurn();
                            break;
                        }
                        if (field.contain[i] == 10) {   // ���� ������ �� ������ ��������� ����
                            field.pathway(PlayerA.army[order[turn] % 7].position % field.sizeX, PlayerA.army[order[turn] % 7].position / field.sizeX, i % field.sizeX, i / field.sizeX);
                            attackIndicator = 0;
                            moveIndicator = 1;
                            stepsDone = 0;
                            turnType = 4;
                            repaint();
                        }
                        if ((-16 <= field.contain[i]) && (field.contain[i] <= -11)) { // ���� ������ �� ���������� ��������
                            // ������� ���-������ ������� ����� ��������. ��� ���.
                            // nextTurn();
                            // break;
                        }
                        if ((-36 <= field.contain[i]) && (field.contain[i] <= -31)) { // ���� ������ �� ���������� �����
                            if ((order[turn] % 7 != 0) && (order[turn] % 7 != 3)) { // ���� �� ����� � �� ������
                                turnType = 2;
                                damageTaker = -field.contain[i] - 23 - (7 * (order[turn] / 7));
                                x = 0;
                                y = 0;
                                repaint();
                            }
                            else {
                                PlayerA.army[order[turn] % 7].hit(-field.contain[i] - 23 - (7 * (order[turn] / 7)));
                                nextTurn();
                            }
                            break;
                        }
                    }
                }
                if ((order[turn] % 7 == 0) && (poly[PlayerA.army[0].position % field.sizeX][PlayerA.army[0].position / field.sizeX].contains(x, y))) {   // ���� ����� � ������ �� ����
                    nextTurn();
                }
                if (poly[(PlayerA.army[0].position % field.sizeX) + 2][20].contains(x, y)) { // ���� ������ �� ��������
                    if (PlayerA.army[order[turn] % 7].currMana >= 10) {
                        turnType = 3;
                        x = 0;
                        y = 0;
                        repaint();
                    }
                }
            } else {
                nextTurn();
            }
        }
        else {
            if (turnType == 2) {    // ���� ����� ������� ��� �����
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[PlayerB.army[damageTaker % 7].position][k]) && (field.g[PlayerB.army[damageTaker % 7].position][k] < field.sizeX * field.sizeY) && (field.contain[field.g[PlayerB.army[damageTaker % 7].position][k]] > 0)) {
                        field.contain[field.g[PlayerB.army[damageTaker % 7].position][k]] += 10;
                        g.setColor(Color.RED);
                        g.fillPolygon(poly[field.g[PlayerB.army[damageTaker % 7].position][k] % field.sizeX][field.g[PlayerB.army[damageTaker % 7].position][k] / field.sizeX]);
                        g.setColor(new Color(255, 255, 255));
                        g.drawPolygon(poly[field.g[PlayerB.army[damageTaker % 7].position][k] % field.sizeX][field.g[PlayerB.army[damageTaker % 7].position][k] / field.sizeX]);
                    }
                }
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[PlayerB.army[damageTaker % 7].position][k]) && (field.g[PlayerB.army[damageTaker % 7].position][k] < field.sizeX * field.sizeY) && (poly[field.g[PlayerB.army[damageTaker % 7].position][k] % field.sizeX][field.g[PlayerB.army[damageTaker % 7].position][k] / field.sizeX].contains(x, y)) && (field.contain[field.g[PlayerB.army[damageTaker % 7].position][k]] > 10)) {
                        field.pathway(PlayerA.army[order[turn] % 7].position % field.sizeX, PlayerA.army[order[turn] % 7].position / field.sizeX, field.g[PlayerB.army[damageTaker % 7].position][k] % field.sizeX, field.g[PlayerB.army[damageTaker % 7].position][k] / field.sizeX);
                        attackIndicator = 1;
                        moveIndicator = 1;
                        stepsDone = 0;
                        turnType = 4;
                        repaint();
                    }
                }
            } else {
                if (turnType == 3) {   // ���� ����� ��������� ��������
                    for (k = 1; k < 7; k++) {
                        if (PlayerB.army[k].number > 0) {
                            if (poly[PlayerB.army[k].position % field.sizeX][PlayerB.army[k].position / field.sizeX].contains(x, y)) {
                                PlayerA.army[order[turn] % 7].castFireball(k + (7 * (1 - (order[turn] / 7))));
                                nextTurn();
                                break;
                            }
                        }
                    }
                } else {
                    if (turnType == 4) {
                        if (moveIndicator == 1) {
                            //System.out.println("Moving, moveIndicator = 1");
                            if (stepsDone > (PlayerA.army[order[turn] % 7].distance/2) || PlayerA.army[order[turn] % 7].position == field.way[1]/*field.way[0] == stepsDone*/) {
                                moveIndicator = 0;
                                repaint();
                            }
                            else {
                                PlayerA.army[order[turn] % 7].position = field.way[field.way[0] - stepsDone];
                                g.setColor(new Color(0, 150, 0));
                                g.fillPolygon(poly[PlayerA.army[order[turn] % 7].position % field.sizeX][PlayerA.army[order[turn] % 7].position / field.sizeX]);
                                for (i = 0; i < 6; i++) {
                                    if (field.g[field.way[field.way[0] - stepsDone]][i] == field.way[field.way[0] - (stepsDone - 1)]) {
                                        //g.drawImage(movEhero[paintTest.mode - 1][i], hero.x * 42 + 21 * (hero.y % 2) + 100,  hero.y * 36 + 0, this);
                                        // ��������
                                    }
                                }
                                stepsDone++;
                                if (PlayerA.army[order[turn] % 7].position != field.way[1]) {
                                    timer.start();
                                }
                                else {
                                    repaint();
                                }
                            }
                        } else {
                            if (attackIndicator == 1) {
                                PlayerA.army[order[turn] % 7].hit(damageTaker);
                                attackIndicator = 0;
                            }
                            nextTurn();
                        }
                    }
                }
            }
        }
    }

    public void makeMoveMob(Graphics g, Polygon poly[][]){
        // 0 - ������������ ������
        // 1 - ��� ����
        // 10 - ��������� ������ ������
        // (-11) - (-16) - ����� ������, �� ������� ����� ���������
        // (-21) - (-26) - ����� ������, �� ������� ������ ���������
        // (-31) - (-36) - ����� �����, �� ������� ����� ���������
        // (-41) - (-46) - ����� �����, �� ������� ������ ���������
        int i, j, k;
        i = 0; // ����� ��� turnType = 2.
        g.setColor(Color.black);
        Font font = new Font("Arial", Font.BOLD , 15);
        g.setFont(font);
        if(paintTest.Ehero[1].army[order[turn] - 7].number > 0) {
            if (turnType == 1) {
                field.refreshContain();
                for (k = 1; k < 7; k++) {
                    field.contain[paintTest.hero.army[k].position] = -20 - k;
                    field.contain[paintTest.Ehero[1].army[k].position] = -40 - k;
                }
                field.contain[paintTest.Ehero[1].army[order[turn] - 7].position] = 1;
                field.range(paintTest.Ehero[1], order[turn] - 7);
                for (i = 2; i <= field.available[0]; i++) { //������������ ��������� ����
                    field.contain[field.available[i]] += 10;
                }
                if (order[turn] == 10) { // ���� ������ - ����� ���� ���� �����������
                    for (k = 1; k < 7; k++) {
                        if (field.contain[paintTest.hero.army[k].position] < -20) {
                            field.contain[paintTest.hero.army[k].position] += 10;
                        }
                    }
                }
                field.contain[400] = -1;
                // poly[20][1] � poly[20][2] - ����� ��� ������, ������ - ������ ������������.
                enemy[order[turn] - 7] = 0;
                for (i = 1; i < 7; i++) { // �����
                    if (paintTest.hero.army[i].number > 0) {
                        if (enemy[order[turn] - 7] == 0) {
                            if ((-16 <= field.contain[paintTest.hero.army[i].position]) && (field.contain[paintTest.hero.army[i].position] <= -11)) {
                                enemy[order[turn] - 7] = i;
                            } else {
                                enemy[order[turn] - 7] = (i + 10);
                            }
                        } else {
                            if ((-26 <= field.contain[paintTest.hero.army[(enemy[order[turn] - 7] % 10)].position]) && (field.contain[paintTest.hero.army[(enemy[order[turn] - 7] % 10)].position] <= -21)) {
                                if ((-16 <= field.contain[paintTest.hero.army[i].position]) && (field.contain[paintTest.hero.army[i].position] <= -11)) {
                                    enemy[order[turn] - 7] = i;
                                } else {
                                    if (paintTest.Ehero[1].army[order[turn] - 7].damageTaken[i] >= paintTest.Ehero[1].army[order[turn] - 7].damageTaken[(enemy[order[turn] - 7] % 10)]) {
                                        enemy[order[turn] - 7] = (i + 10);
                                    }
                                }
                            } else {
                                if ((-16 <= field.contain[paintTest.hero.army[i].position]) && (field.contain[paintTest.hero.army[i].position] <= -11)) {
                                    if (paintTest.Ehero[1].army[order[turn] - 7].damageTaken[i] >= paintTest.Ehero[1].army[order[turn] - 7].damageTaken[(enemy[order[turn] - 7] % 10)]) {
                                        enemy[order[turn] - 7] = i;
                                    }
                                }
                            }
                        }
                    }
                }
                field.contain[paintTest.hero.army[(enemy[order[turn] - 7] % 10)].position] = 2;
                if (order[turn] == 10) {
                    paintTest.Ehero[1].army[order[turn] - 7].hit(enemy[order[turn] - 7]);
                    field.way[0] = 0;
                    nextTurn();
                }
                else {
                    field.pathway((paintTest.Ehero[1].army[order[turn] - 7].position % field.sizeX), (paintTest.Ehero[1].army[order[turn] - 7].position / field.sizeX), (paintTest.hero.army[(enemy[order[turn] - 7] % 10)].position % field.sizeX), (paintTest.hero.army[(enemy[order[turn] - 7] % 10)].position / field.sizeX));
                    if (enemy[order[turn] - 7] < 10) {
                        attackIndicator = 1;
                    }
                    else {
                        attackIndicator = 0;
                    }
                    moveIndicator = 1;
                    stepsDone = 0;
                    turnType = 4;
                    repaint();
                }
            }
            else {
                if (turnType == 4) {
                    if (moveIndicator == 1) {
                        //System.out.println("Moving, moveIndicator = 1");
                        if (stepsDone == (paintTest.Ehero[1].army[order[turn] - 7].distance/2) || paintTest.Ehero[1].army[order[turn] - 7].position == field.way[1 + attackIndicator]) {
                            moveIndicator = 0;
                            repaint();
                        }
                        else {
                            stepsDone++;
                            paintTest.Ehero[1].army[order[turn] - 7].position = field.way[field.way[0] - stepsDone];
                            g.setColor(new Color(0, 150, 0));
                            g.fillPolygon(poly[paintTest.Ehero[1].army[order[turn] - 7].position % field.sizeX][paintTest.Ehero[1].army[order[turn] - 7].position / field.sizeX]);
                            for(i=0; i<6; i++){
                                if(field.g[field.way[field.way[0] - stepsDone]][i] == field.way[field.way[0] - (stepsDone - 1)]){
                                    //g.drawImage(movEhero[paintTest.mode - 1][i], hero.x * 42 + 21 * (hero.y % 2) + 100,  hero.y * 36 + 0, this);
                                    // ��������
                                }
                            }
                            if (paintTest.Ehero[1].army[order[turn] - 7].position != field.way[2]) {
                                timer.start();
                            }
                            else {
                                repaint();
                            }
                        }
                    }
                    else {
                        if (attackIndicator == 1) {
                            paintTest.Ehero[paintTest.mode - 1].army[order[turn] - 7].hit(enemy[order[turn] - 7]);
                            attackIndicator = 0;
                        }
                        nextTurn();
                    }
                }
            }
        }
        else {
            nextTurn();
        }
    }


    public void drawaxis( Graphics g , Polygon poly[][]) {
        g.setColor(new Color(255, 255, 255));
        int[] arrayX = {70, 70 + 21, 70 + 21, 70, 70 - 21, 70 - 21};
        int[] arrayY = {70 - 24, 70 - 12, 70 + 12, 70 + 24, 70 + 12, 70 - 12};
        int centx, centy, sovp;
        int i, j, k;
        centx = 42;
        centy = 36;
        sovp = 0;
        g.setColor(new Color(255, 255, 255));
        for (j = 0; j <= 19; j++) {
            for (i = 0; i <= 19; i++) {
                for (k = 0; k <= 5; k++) {
                    arrayX[k] += centx;
                }
                poly[i][j] = new Polygon(arrayX, arrayY, 6);
                g.drawPolygon(poly[i][j]);
            }
            for (k = 0; k <= 5; k++) {
                arrayY[k] += centy;
            }
            for (k = 0; k <= 5; k++) {
                arrayX[k] -= 20 * centx;
                if (j % 2 == 1) {
                    arrayX[k] -= 21;
                }
                else {
                    arrayX[k] += 21;
                }
            }
        }
        int[] PlayerAX = {49, 49 + 21, 49 + 21, 49, 49 - 21, 49 - 21};
        int[] PlayerAY = {754 - 24, 754 - 12, 754 + 12, 754 + 24, 754 + 12, 754 - 12};
        poly[1][20] = new Polygon(PlayerAX, PlayerAY, 6);
        g.drawPolygon(poly[1][20]);
        int[] FireballPlayerAX = {59 - 28, 59 - 28, 59 + 28, 59 + 28};
        int[] FireballPlayerAY = {70 - 12, 70 + 12, 70 + 12, 70 - 12};
        poly[3][20] = new Polygon(FireballPlayerAX, FireballPlayerAY, 4);
        //g.drawPolygon(poly[3][20]);
        //g.fillPolygon(poly[3][20]);
        if (paintTest.mode == 1) {
            int[] PlayerBX = {994, 994 + 21, 994 + 21, 994, 994 - 21, 994 - 21};
            int[] PlayerBY = {70 - 24, 70 - 12, 70 + 12, 70 + 24, 70 + 12, 70 - 12};
            poly[2][20] = new Polygon(PlayerBX, PlayerBY, 6);
            g.drawPolygon(poly[2][20]);
            int[] FireballPlayerBX = {984 - 28, 984 - 28, 984 + 28, 984 + 28};
            int[] FireballPlayerBY = {754 - 12, 754 + 12, 754 + 12, 754 - 12};
            poly[4][20] = new Polygon(FireballPlayerBX, FireballPlayerBY, 4);
            //g.drawPolygon(poly[4][20]);
            //g.fillPolygon(poly[4][20]);
        }
    }   //������� ��������� �����

    public void drawTroopers( Graphics g, Polygon poly[][]) {
        Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 11);
        g.setFont(font);
        for (i=0;i<7;i++) { // ���������� ������ � �������
            g.setColor(new Color(200, 100, 100));
            if (paintTest.hero.army[i].number > 0) {
                g.fillPolygon(poly[paintTest.hero.army[i].position % field.sizeX][paintTest.hero.army[i].position / field.sizeX]);
            }
            g.setColor(new Color(255, 255, 255));
            if (paintTest.hero.army[i].number > 0) {
                g.drawPolygon(poly[paintTest.hero.army[i].position % field.sizeX][paintTest.hero.army[i].position / field.sizeX]);
            }
        }
        if (paintTest.mode == 1) {
            for (i = 0; i < 7; i++) { // ���������� ������ � �������
                g.setColor(new Color(100, 100, 150));
                if (paintTest.Ehero[paintTest.mode - 1].army[i].number > 0) {
                    g.fillPolygon(poly[paintTest.Ehero[paintTest.mode - 1].army[i].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[i].position / field.sizeX]);
                }
                g.setColor(new Color(255, 255, 255));
                if (paintTest.Ehero[paintTest.mode - 1].army[i].number > 0) {
                    g.drawPolygon(poly[paintTest.Ehero[paintTest.mode - 1].army[i].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[i].position / field.sizeX]);
                }
            }
        }
        else  {
            for (i = 1; i < 7; i++) { // ���������� ������ � �������
                g.setColor(new Color(100, 100, 150));
                if (paintTest.Ehero[paintTest.mode - 1].army[i].number > 0) {
                    g.fillPolygon(poly[paintTest.Ehero[paintTest.mode - 1].army[i].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[i].position / field.sizeX]);
                }
                g.setColor(new Color(255, 255, 255));
                if (paintTest.Ehero[paintTest.mode - 1].army[i].number > 0) {
                    g.drawPolygon(poly[paintTest.Ehero[paintTest.mode - 1].army[i].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[i].position / field.sizeX]);
                }
            }
        }
        g.setColor(Color.GREEN);
        if (order[turn] < 7) {
            if (paintTest.hero.army[order[turn]].number > 0) {
                g.fillPolygon(poly[paintTest.hero.army[order[turn]].position % field.sizeX][paintTest.hero.army[order[turn]].position / field.sizeX]);
            }
        }
        else {
            if (paintTest.Ehero[paintTest.mode - 1].army[order[turn] - 7].number > 0) {
                g.fillPolygon(poly[paintTest.Ehero[paintTest.mode - 1].army[order[turn] - 7].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[order[turn] - 7].position / field.sizeX]);
            }
        }
        g.setColor(new Color(255, 255, 255));
        g.drawPolygon(poly[field.available[1] % field.sizeX][field.available[1] / field.sizeX]);
        g.setColor(new Color(0, 0, 0));
//        g.drawString("hero", 49 - 12, 754 - 6);
        g.drawImage(A[0], 36, 719, this);
        if(paintTest.hero.army[1].number > 0) {
            g.drawImage(A[1], paintTest.hero.army[1].position % field.sizeX * 42 + 21 * (paintTest.hero.army[1].position / field.sizeX % 2) + 100, paintTest.hero.army[1].position / field.sizeX * 36 + 35, this);
//            g.drawString("weak", 21 * ((paintTest.hero.army[1].position / field.sizeX) % 2) + (paintTest.hero.army[1].position % field.sizeX) * 42 + 70 + 42 - 12, (paintTest.hero.army[1].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[1].number + "", 21 * ((paintTest.hero.army[1].position / field.sizeX) % 2) + (paintTest.hero.army[1].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[1].position / field.sizeX) * 36 + 70 + 18);
        }
        if(paintTest.hero.army[2].number > 0) {
            g.drawImage(A[2], paintTest.hero.army[2].position % field.sizeX * 42 + 21 * (paintTest.hero.army[2].position / field.sizeX % 2) + 100, paintTest.hero.army[2].position / field.sizeX * 36 + 25, this);
//            g.drawString("tnk", 21 * ((paintTest.hero.army[2].position / field.sizeX) % 2) + (paintTest.hero.army[2].position % field.sizeX) * 42 + 70 + 42 - 7, (paintTest.hero.army[2].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[2].number + "", 21 * ((paintTest.hero.army[2].position / field.sizeX) % 2) + (paintTest.hero.army[2].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[2].position / field.sizeX) * 36 + 70 + 18);
        }
        if(paintTest.hero.army[3].number > 0) {
            g.drawImage(A[3], paintTest.hero.army[3].position % field.sizeX * 42 + 21 * (paintTest.hero.army[3].position / field.sizeX % 2) + 100, paintTest.hero.army[3].position / field.sizeX * 36 + 35, this);
//            g.drawString("arch", 21 * ((paintTest.hero.army[3].position / field.sizeX) % 2) + (paintTest.hero.army[3].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.hero.army[3].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[3].number + "", 21 * ((paintTest.hero.army[3].position / field.sizeX) % 2) + (paintTest.hero.army[3].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[3].position / field.sizeX) * 36 + 70 + 18);
        }
        if(paintTest.hero.army[4].number > 0) {
            g.drawImage(A[4], paintTest.hero.army[4].position % field.sizeX * 42 + 21 * (paintTest.hero.army[4].position / field.sizeX % 2) + 93, paintTest.hero.army[4].position / field.sizeX * 36 + 27, this);
//            g.drawString("dam", 21 * ((paintTest.hero.army[4].position / field.sizeX) % 2) + (paintTest.hero.army[4].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.hero.army[4].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[4].number + "", 21 * ((paintTest.hero.army[4].position / field.sizeX) % 2) + (paintTest.hero.army[4].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[4].position / field.sizeX) * 36 + 70 + 18);
        }
        if(paintTest.hero.army[5].number > 0) {
            g.drawImage(A[5], paintTest.hero.army[5].position % field.sizeX * 42 + 21 * (paintTest.hero.army[5].position / field.sizeX % 2) + 100, paintTest.hero.army[5].position / field.sizeX * 36 + 35, this);
//            g.drawString("mag", 21 * ((paintTest.hero.army[5].position / field.sizeX) % 2) + (paintTest.hero.army[5].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.hero.army[5].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[5].number + "", 21 * ((paintTest.hero.army[5].position / field.sizeX) % 2) + (paintTest.hero.army[5].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[5].position / field.sizeX) * 36 + 70 + 18);
        }
        if(paintTest.hero.army[6].number > 0) {
            g.drawImage(A[6], paintTest.hero.army[6].position % field.sizeX * 42 + 21 * (paintTest.hero.army[6].position / field.sizeX % 2) + 66, paintTest.hero.army[6].position / field.sizeX * 36 + 27, this);
//            g.drawString("elite", 21 * ((paintTest.hero.army[6].position / field.sizeX) % 2) + (paintTest.hero.army[6].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.hero.army[6].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.hero.army[6].number + "", 21 * ((paintTest.hero.army[6].position / field.sizeX) % 2) + (paintTest.hero.army[6].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.hero.army[6].position / field.sizeX) * 36 + 70 + 18);
        }
        g.setColor(new Color(255, 255, 255));
//        g.drawString("hero", 994 - 12, 70 - 6);
        if (paintTest.Ehero[paintTest.mode - 1].army[0].number > 0) {
            g.drawImage(B[0], 979, 35, this);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[1].number > 0) {
            g.drawImage(B[1], paintTest.Ehero[paintTest.mode - 1].army[1].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX % 2) + 100, paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX * 36 + 35, this);
//            g.drawString("weak", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[1].position % field.sizeX) * 42 + 70 + 42 - 12, (paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[1].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[1].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[1].position / field.sizeX) * 36 + 70 + 18);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[2].number > 0) {
            g.drawImage(B[2], paintTest.Ehero[paintTest.mode - 1].army[2].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX % 2) + 104, paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX * 36 + 35, this);
//            g.drawString("tnk", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[2].position % field.sizeX) * 42 + 70 + 42 - 7, (paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[2].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[2].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[2].position / field.sizeX) * 36 + 70 + 18);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[3].number > 0) {
            g.drawImage(B[3], paintTest.Ehero[paintTest.mode - 1].army[3].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX % 2) + 98, paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX * 36 + 34, this);
//            g.drawString("arch", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[3].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[3].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[3].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[3].position / field.sizeX) * 36 + 70 + 18);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[4].number > 0) {
            g.drawImage(B[4], paintTest.Ehero[paintTest.mode - 1].army[4].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX % 2) + 96, paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX * 36 + 35, this);
//            g.drawString("dam", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[4].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[4].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[4].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[4].position / field.sizeX) * 36 + 70 + 18);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[5].number > 0) {
            g.drawImage(B[5], paintTest.Ehero[paintTest.mode - 1].army[5].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX % 2) + 99, paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX * 36 + 34, this);
//            g.drawString("mag", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[5].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[5].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[5].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[5].position / field.sizeX) * 36 + 70 + 18);
        }
        if (paintTest.Ehero[paintTest.mode - 1].army[6].number > 0) {
            g.drawImage(B[6], paintTest.Ehero[paintTest.mode - 1].army[6].position % field.sizeX * 42 + 21 * (paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX % 2) + 81, paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX * 36 + 25, this);
//            g.drawString("elite", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[6].position % field.sizeX) * 42 + 70 + 42 - 10, (paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX) * 36 + 70 - 6);
            g.drawString(paintTest.Ehero[paintTest.mode - 1].army[6].number + "", 21 * ((paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX) % 2) + (paintTest.Ehero[paintTest.mode - 1].army[6].position % field.sizeX) * 42 + 70 + 42 - 9, (paintTest.Ehero[paintTest.mode - 1].army[6].position / field.sizeX) * 36 + 70 + 18);
        }
    }

    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        //������ �����������
//        g.drawImage(BattleBackground, 154, 46, null);
        g.drawImage(BattleBackground, 28, 46, null);
        Polygon poly[][] = new Polygon[20][21];
        drawaxis(g, poly); //��������� ����� �����
        if (paintTest.mode == 1) {
            if ((paintTest.hero.living > 0) && (paintTest.Ehero[0].living > 0)) {
                if (order[turn] < 7) { //���� �� i-�� ����� ����� ��� ����
                    makeMovePlayer(g, poly, paintTest.hero, paintTest.Ehero[0]);
                    paintTest.hero.levelUp();
                } else { //���� �� i-�� ����� ����� ���� �����
                    makeMovePlayer(g, poly, paintTest.Ehero[0], paintTest.hero);
                    paintTest.Ehero[0].levelUp();
                }
            } else {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice screen = env.getDefaultScreenDevice();
                try {
                    robot = new Robot(screen);
                } catch (AWTException ex) {
                }
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_ALT);
            }
            drawTroopers(g, poly);
            g.setColor(Color.black);
            Font font = new Font("Arial", Font.BOLD, 15);
            g.setFont(font);
            g.drawString("PlayerB:   Level: " + paintTest.Ehero[0].lvl + "   XP: " + paintTest.Ehero[0].XP + "   Damage: " + paintTest.Ehero[0].army[0].damage, 600, 820);
        }
        else {
            if ((paintTest.hero.living > 0) && (paintTest.Ehero[1].living > 0)) {
                if (order[turn] < 7) { //���� �� i-�� ����� ����� ��� ����
                    makeMovePlayer(g, poly, paintTest.hero, paintTest.Ehero[1]);
                    paintTest.hero.levelUp();
                } else { //���� �� i-�� ����� ����� ���� �����
                    makeMoveMob(g, poly);
                    paintTest.Ehero[1].levelUp();
                }
            } else {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice screen = env.getDefaultScreenDevice();
                try {
                    robot = new Robot(screen);
                } catch (AWTException ex) {
                }
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_ALT);
            }
            drawTroopers(g, poly);
        }
        g.setColor(Color.black);
        Font font = new Font("Arial", Font.BOLD, 15);
        g.setFont(font);
        g.drawString("PlayerA:   Level: " + paintTest.hero.lvl + "   XP: " + paintTest.hero.XP + "   Damage: " + paintTest.hero.army[0].damage, 150, 820);
    }
}

/*
�� ������ / ������� /  ����� ���������� / ��������� / ������
���
�. ����������� �������
�. ������ ���� � ����� ���������� ������ ���������� � ������� ���� �� ������� �������
�. For (i=0 ; ���� ���� ��� ���� ; i++)
�. . ���� ��� i-� ��������
�. . . ���� ���
�. . . . ����������, ���� ����� �����
�. . . . ���� �������� �� �����
�. . . . . ����
�. . . . ���� �������� �� ���������� ��� ����� ����
�. . . . . ���� �������
�. . . . . . ���� �������, � ����� ������ ���������
�. . . . . . ����� ����
�. . . . . ���������
�. . . . . ��������� ���� �����
�. . . ���� ���
�. . . . ���� ����� ���������
�. . . . . ������� ����, ��� ���� ��� ������ ����� (�� ���, ���� ����� ���������)
�. . . . . ��������� ���� �����
�. . . . ���� �� ����� ���������
�. . . . . ������� ��� ����� ����� � ����, ��� ���� ��� ������ �����
�. �������� �������, ���� �����.

�. ��������� �� ������.
�. ���� ��� ����� ���� ����, �� ��� ������ ���������� ������ ���������������, ��� � ������ ���, ���� �� ����. ��� ������� �������.
�. �������� �������������� ���� � ���������� ������ � ��������� ������ ��� ����� ������ � ���������� � ������������ ��������� � ��������� ������ ��� ����� ������ ������� ���� �� ��������� ��� ����� ����.
�. ����������. ���� ��������.
� ������.
*/

    /*
    public void makeMovePlayerAM(Graphics g, Polygon poly[][]){
        // 0 - ������������ ������
        // 1 - ��� ����
        // 10 - ��������� ������ ������
        // (-11) - (-16) - ����� ������, �� ������� ����� ���������
        // (-21) - (-26) - ����� ������, �� ������� ������ ���������
        // (-31) - (-36) - ����� �����, �� ������� ����� ���������
        // (-41) - (-46) - ����� �����, �� ������� ������ ���������
        int i, j, k;
        g.setColor(Color.black);
        Font font = new Font("Arial", Font.BOLD , 15);
        g.setFont(font);
        if (turnType == 1) {
            if (poly[paintTest.hero.army[0].position % field.sizeX][paintTest.hero.army[0].position / field.sizeX].contains(xR, yR)) {    // ���� ������ ��
                g.drawString("Number: " + paintTest.hero.army[0].number + "   Health: " + paintTest.hero.army[0].health + "   Current Heatlt: " + paintTest.hero.army[0].currHealth + "   Total Health: " + (paintTest.hero.army[0].health * (paintTest.hero.army[0].number - 1) + paintTest.hero.army[0].currHealth) + "   Damage: " + paintTest.hero.army[0].damage + "   Total Damage: " + (paintTest.hero.army[0].damage * paintTest.hero.army[0].number + "   Mana: " + paintTest.hero.army[0].currMana), 150, 850);
            }
            for (j = 1; j < 7; j++) {
                if ((paintTest.hero.army[j].number > 0) && (poly[paintTest.hero.army[j].position % field.sizeX][paintTest.hero.army[j].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                    g.drawString("Number: " + paintTest.hero.army[j].number + "   Health: " + paintTest.hero.army[j].health + "   Current Heatlt: " + paintTest.hero.army[j].currHealth + "   Total Health: " + (paintTest.hero.army[j].health*(paintTest.hero.army[j].number-1)+paintTest.hero.army[j].currHealth) + "   Damage: " + paintTest.hero.army[j].damage + "   Total Damage: " + (paintTest.hero.army[j].damage*paintTest.hero.army[j].number + "   Mana: " + paintTest.hero.army[j].currMana), 150, 850);
                }
                if ((paintTest.Ehero[paintTest.mode - 1].army[j].number > 0) && (poly[paintTest.Ehero[paintTest.mode - 1].army[j].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[j].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                    g.drawString("Number: " + paintTest.Ehero[paintTest.mode - 1].army[j].number + "  Health: " + paintTest.Ehero[paintTest.mode - 1].army[j].health + "   Current Heatlt: " + paintTest.Ehero[paintTest.mode - 1].army[j].currHealth + "   Total Health: " + (paintTest.Ehero[paintTest.mode - 1].army[j].health*(paintTest.Ehero[paintTest.mode - 1].army[j].number-1)+paintTest.Ehero[paintTest.mode - 1].army[j].currHealth) + "   Damage: " + paintTest.Ehero[paintTest.mode - 1].army[j].damage + "   Total Damage: " + (paintTest.Ehero[paintTest.mode - 1].army[j].damage*paintTest.Ehero[paintTest.mode - 1].army[j].number + "   Mana: " + paintTest.Ehero[paintTest.mode - 1].army[j].currMana), 150, 850);
                }
            }
            if(paintTest.hero.army[order[turn]].number>0) {
                field.refreshContain();
                for (k = 1; k < 7; k++) {
                    field.contain[paintTest.hero.army[k].position] = -20 - k;
                    field.contain[paintTest.Ehero[paintTest.mode - 1].army[k].position] = -40 - k;
                }
                field.contain[paintTest.hero.army[order[turn]].position] = 1;
                field.range(paintTest.hero, order[turn]);
                for (i = 2; i <= field.available[0]; i++) { //������������ ��������� ����
                    field.contain[field.available[i]] += 10;
                    g.setColor(Color.ORANGE);
                    g.fillPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                    g.setColor(new Color(255, 255, 255));
                    g.drawPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                }
                if ((order[turn] == 0) || (order[turn] == 3)) { // ���� ����� ��� ������ - ����� ���� ���� �����������
                    for (k = 1; k < 7; k++) {
                        if (field.contain[paintTest.Ehero[paintTest.mode - 1].army[k].position] < -40) {
                            field.contain[paintTest.Ehero[paintTest.mode - 1].army[k].position] += 10;
                        }
                    }
                }
                field.contain[400] = -1;
                // poly[20][1] � poly[20][2] - ����� ��� ������, ������ - ������ ������������.
                if ((order[turn] == 0) || (order[turn] == 5)) { // ���� ����� ��� ��� - ����� ��������� ��������
                    if (paintTest.hero.army[order[turn]].currMana >= 10) {  // ���� ���� ���� �� ��������
                        g.setColor(new Color(255, 255, 255));
                    }
                    else {
                        g.setColor(new Color(150, 150, 150));
                    }
                    g.drawPolygon(poly[3][20]);
                    g.fillPolygon(poly[3][20]);
                    g.setColor(new Color(0, 0, 0));
                    g.drawString("Fireball", 958, 761);
                }
                for (i = 0; i < 400; i++) { // ������ ����
                    if (poly[i % field.sizeX][i / field.sizeX].contains(x, y)) {    // ���� ������ �� ������ ����
                        if (field.contain[i] == 1) {   // ���� ������ �� ����
                            nextTurn();
                            break;
                        }
                        if (field.contain[i] == 10) {   // ���� ������ �� ������ ��������� ����
                            paintTest.hero.army[order[turn]].position = i;
                            //����� ������ ���� ������������ �� ����, ������� ����� ����� �������� field.pathway, �� ���� ��� ��� ����������
                            nextTurn();
                            break;
                        }
                        if ((-16 <= field.contain[i]) && (field.contain[i] <= -11)) { // ���� ������ �� ���������� ��������
                            // ������� ���-������ ������� ����� ��������. ��� ���.
                            // nextTurn();
                            // break;
                        }
                        if ((-36 <= field.contain[i]) && (field.contain[i] <= -31)) { // ���� ������ �� ���������� �����
                            if ((order[turn] != 0) && (order[turn] != 3)) { // ���� �� ����� � �� ������
                                turnType = 2;
                                damageTaker = -field.contain[i] - 23;
                                x = 0;
                                y = 0;
                                repaint();
                            }
                            else {
                                paintTest.hero.army[order[turn]].hit(paintTest.hero, paintTest.Ehero[paintTest.mode - 1], -field.contain[i] - 23);
                                nextTurn();
                            }
                            break;
                        }
                    }
                }
                if ((order[turn] == 0) && (poly[1][20].contains(x, y))) {   // ���� ����� � ������ �� ����
                    nextTurn();
                }
                if (poly[3][20].contains(x, y)) { // ���� ������ �� ��������
                    if (paintTest.hero.army[order[turn]].currMana >= 10) {
                        turnType = 3;
                        x = 0;
                        y = 0;
                        repaint();
                    }
                }
            } else {
                nextTurn();
            }
        }
        else{
            if (turnType == 2) {    // ���� ����� ������� ��� �����
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k]) && (field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] < field.sizeX*field.sizeY) && (field.contain[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k]] > 0)) {
                        field.contain[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k]] += 10;
                        g.setColor(Color.RED);
                        g.fillPolygon(poly[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] % field.sizeX][field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] / field.sizeX]);
                        g.setColor(new Color(255, 255, 255));
                        g.drawPolygon(poly[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] % field.sizeX][field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] / field.sizeX]);
                    }
                }
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k]) && (field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] < field.sizeX*field.sizeY) && (poly[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] % field.sizeX][field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k] / field.sizeX].contains(x, y)) && (field.contain[field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k]] > 10)) {
                        paintTest.hero.army[order[turn]].position = field.g[paintTest.Ehero[paintTest.mode - 1].army[damageTaker-7].position][k];
                        //����� ������ ���� ������������ �� ����, ������� ����� ����� �������� field.pathway, �� ���� ��� ��� ����������
                        paintTest.hero.army[order[turn]].hit(paintTest.hero, paintTest.Ehero[paintTest.mode - 1], damageTaker);
                        nextTurn();
                        break;
                    }
                }
            }
            else {
                if (turnType == 3) {   // ���� ����� ��������� ��������
                    for (k = 1; k < 7; k++) {
                        if (paintTest.Ehero[paintTest.mode - 1].army[k].number > 0) {
                            if (poly[paintTest.Ehero[paintTest.mode - 1].army[k].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[k].position / field.sizeX].contains(x, y)) {
                                paintTest.hero.army[order[turn]].castFireball(paintTest.hero, paintTest.Ehero[paintTest.mode - 1], k+7);
                                nextTurn();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void makeMovePlayerB(Graphics g, Polygon poly[][]){
        // 0 - ������������ ������
        // 1 - ��� ����
        // 10 - ��������� ������ ������
        // (-11) - (-16) - ����� ������, �� ������� ����� ���������
        // (-21) - (-26) - ����� ������, �� ������� ������ ���������
        // (-31) - (-36) - ����� �����, �� ������� ����� ���������
        // (-41) - (-46) - ����� �����, �� ������� ������ ���������
        int i, j, k;
        g.setColor(Color.black);
        Font font = new Font("Arial", Font.BOLD , 15);
        g.setFont(font);
        if (turnType == 1) {
            for (i = 0; i < 7; i++) {
                if ((paintTest.hero.army[i].number > 0) && (poly[paintTest.hero.army[i].position % field.sizeX][paintTest.hero.army[i].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                    g.drawString("Number: " + paintTest.hero.army[i].number + "   Health: " + paintTest.hero.army[i].health + "   Current Heatlt: " + paintTest.hero.army[i].currHealth + "   Total Health: " + (paintTest.hero.army[i].health*(paintTest.hero.army[i].number-1)+paintTest.hero.army[i].currHealth) + "   Damage: " + paintTest.hero.army[i].damage + "   Total Damage: " + (paintTest.hero.army[i].damage*paintTest.hero.army[i].number + "   Mana: " + paintTest.hero.army[i].currMana), 150, 850);
                }
                if ((paintTest.Ehero[paintTest.mode - 1].army[i].number > 0) && (poly[paintTest.Ehero[paintTest.mode - 1].army[i].position % field.sizeX][paintTest.Ehero[paintTest.mode - 1].army[i].position / field.sizeX].contains(xR, yR))) {    // ���� ������ ��
                    g.drawString("Number: " + paintTest.Ehero[paintTest.mode - 1].army[i].number + "  Health: " + paintTest.Ehero[paintTest.mode - 1].army[i].health + "   Current Heatlt: " + paintTest.Ehero[paintTest.mode - 1].army[i].currHealth + "   Total Health: " + (paintTest.Ehero[paintTest.mode - 1].army[i].health*(paintTest.Ehero[paintTest.mode - 1].army[i].number-1)+paintTest.Ehero[paintTest.mode - 1].army[i].currHealth) + "   Damage: " + paintTest.Ehero[paintTest.mode - 1].army[i].damage + "   Total Damage: " + (paintTest.Ehero[paintTest.mode - 1].army[i].damage*paintTest.Ehero[paintTest.mode - 1].army[i].number + "   Mana: " + paintTest.Ehero[paintTest.mode - 1].army[i].currMana), 150, 850);
                }
            }
            if(paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].number>0) {
                field.refreshContain();
                for (k = 1; k < 7; k++) {
                    field.contain[paintTest.hero.army[k].position] = -20 - k;
                    field.contain[paintTest.Ehero[paintTest.mode - 1].army[k].position] = -40 - k;
                }
                field.contain[paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].position] = 1;
                field.range(paintTest.Ehero[paintTest.mode - 1], order[turn]-7);
                for (i = 2; i <= field.available[0]; i++) { //������������ ��������� ����
                    field.contain[field.available[i]] += 10;
                    g.setColor(Color.CYAN);
                    g.fillPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                    g.setColor(new Color(255, 255, 255));
                    g.drawPolygon(poly[field.available[i] % field.sizeX][field.available[i] / field.sizeX]);
                }
                if ((order[turn] == 7) || (order[turn] == 10)) { // ���� ����� ��� ������ - ����� ���� ���� �����������
                    for (k = 1; k < 7; k++) {
                        if (field.contain[paintTest.hero.army[k].position] < -20) {
                            field.contain[paintTest.hero.army[k].position] += 10;
                        }
                    }
                }
                field.contain[400] = -1;
                // poly[20][1] � poly[20][2] - ����� ��� ������, ������ - ������ ������������.
                if ((order[turn] == 7) || (order[turn] == 12)) {    //���� ����� ��� ��� - ����� ��������� ��������
                    if (paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].currMana >= 10) {  // ���� ���� ���� �� ��������
                        g.setColor(new Color(255, 255, 255));
                    }
                    else {
                        g.setColor(new Color(150, 150, 150));
                    }
                    g.drawPolygon(poly[4][20]);
                    g.fillPolygon(poly[4][20]);
                    g.setColor(new Color(0, 0, 0));
                    g.drawString("Fireball", 33, 77);
                }
                for (i = 0; i < 400; i++) { // ������ ����
                    if (poly[i % field.sizeX][i / field.sizeX].contains(x, y)) {    // ���� ������ �� ������ ����
                        if (field.contain[i] == 1) {   // ���� ������ �� ����
                            nextTurn();
                            break;
                        }
                        if (field.contain[i] == 10) {   // ���� ������ �� ������ ��������� ����
                            paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].position = i;
                            //����� ������ ���� ������������ �� ����, ������� ����� ����� �������� field.pathway, �� ���� ��� ��� ����������
                            nextTurn();
                            break;
                        }
                        if ((-16 <= field.contain[i]) && (field.contain[i] <= -11)) { // ���� ������ �� ���������� �����
                            if ((order[turn] != 7) && (order[turn] != 10)) { // ���� �� ����� � �� ������
                                turnType = 2;
                                damageTaker = -field.contain[i] - 10;
                                x = 0;
                                y = 0;
                                repaint();
                            }
                            else {
                                paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].hit(paintTest.Ehero[paintTest.mode - 1], paintTest.hero, -field.contain[i] - 10);
                                nextTurn();
                            }
                            break;
                        }
                        if ((-36 <= field.contain[i]) && (field.contain[i] <= -31)) { // ���� ������ �� ���������� ��������
                            // ������� ���-������ ������� ����� ��������. ��� ���.
                            // nextTurn();
                            // break;
                        }
                    }
                }
                if (poly[2][20].contains(x, y)) {   // ���� ����� � ������ �� ����
                    nextTurn();
                }
                if (poly[4][20].contains(x, y)) { // ���� ������ �� ��������
                    if (paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].currMana >= 10) {
                        turnType = 3;
                        x = 0;
                        y = 0;
                        repaint();
                    }
                }
            }
            else {
                nextTurn();
            }
        }
        else{
            if (turnType == 2) {    // ���� ����� ������� ��� �����
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[paintTest.hero.army[damageTaker].position][k]) && (field.g[paintTest.hero.army[damageTaker].position][k] < field.sizeX*field.sizeY) && (field.contain[field.g[paintTest.hero.army[damageTaker].position][k]] > 0)) {
                        field.contain[field.g[paintTest.hero.army[damageTaker].position][k]] += 10;
                        g.setColor(Color.RED);
                        g.fillPolygon(poly[field.g[paintTest.hero.army[damageTaker].position][k] % field.sizeX][field.g[paintTest.hero.army[damageTaker].position][k] / field.sizeX]);
                        g.setColor(new Color(255, 255, 255));
                        g.drawPolygon(poly[field.g[paintTest.hero.army[damageTaker].position][k] % field.sizeX][field.g[paintTest.hero.army[damageTaker].position][k] / field.sizeX]);
                    }
                }
                for (k = 0; k < 6; k++) {
                    if ((0 <= field.g[paintTest.hero.army[damageTaker].position][k]) && (field.g[paintTest.hero.army[damageTaker].position][k] < field.sizeX*field.sizeY) && (poly[field.g[paintTest.hero.army[damageTaker].position][k] % field.sizeX][field.g[paintTest.hero.army[damageTaker].position][k] / field.sizeX].contains(x, y)) && (field.contain[field.g[paintTest.hero.army[damageTaker].position][k]] > 10)) {
                        paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].position = field.g[paintTest.hero.army[damageTaker].position][k];
                        //����� ������ ���� ������������ �� ����, ������� ����� ����� �������� field.pathway, �� ���� ��� ��� ����������
                        paintTest.Ehero[paintTest.mode - 1].army[order[turn]-7].hit(paintTest.Ehero[paintTest.mode - 1], paintTest.hero, damageTaker);
                        nextTurn();
                        break;
                    }
                }
            }
            if (turnType == 3) {   // ���� ����� ��������� ��������
                for (k = 1; k < 7; k++) {
                    if (paintTest.hero.army[k].number > 0) {
                        if (poly[paintTest.hero.army[k].position % field.sizeX][paintTest.hero.army[k].position / field.sizeX].contains(x, y)) {
                            paintTest.Ehero[paintTest.mode - 1].army[order[turn] - 7].castFireball(paintTest.Ehero[paintTest.mode - 1], paintTest.hero, k);
                            nextTurn();
                            break;
                        }
                    }
                }
            }
        }
    }
    */