import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.Timer;
import java.awt.event.*;
// для гиф
import java.awt.Image;
import java.awt.Toolkit;
// для гиф
public class paintTest extends JPanel {
    Image heroStand,heroBackStand,EmobStand,EplStand;
//    Image[] movEmob = new Image[6];
    private BufferedImage background;
    private int x,y,xR,yR,prevX,prevY,stepsDone,sovp,LR;
    public int autoEndTurn,moveIndicator;
    static Hero hero = new Hero(0,0); // static для изменения дальности передвижения кнопкой
    static Hero[] Ehero = new Hero[2];
    static int mode;
    Field field = new Field(20,20);
    Timer timer;
    public paintTest() {
        super();
        Ehero[0] = new Hero(2,17);
        Ehero[1] = new Hero(17,18);
        //gif
//        movEmob[0] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Влево.gif");
//        movEmob[5] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Влево_вниз.gif");
//        movEmob[1] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Влево_вверх.gif");
//        movEmob[3] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Вправо.gif");
//        movEmob[4] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Вправо_вниз.gif");
//        movEmob[2] = Toolkit.getDefaultToolkit().createImage("Существа/герой/перемещение/Вправо_вверх.gif");
        //gif
        ActionListener task = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
//                System.out.println("stepsDone="+ stepsDone +"; Timer is called.");
//                System.out.println("(" + hero.x + "; " + hero.y + ");");
//                System.out.println("x=" + x + "; y=" + y + ";");
//                System.out.println("field.way[0] - stepsDone - 1 = " + (field.way[0] - stepsDone - 1));
//                System.out.println("field.way[0] = " + field.way[0]);
//                System.out.println("field.way[field.way[0]] = " + field.way[field.way[0]]);
//                System.out.println("field.way[1] = " + field.way[1]);
//                System.out.println("hero.currDistance = " + hero.currDistance);
                repaint();
            }
        };
        timer = new Timer(400 , task);  // 900
        // Проработка поля
        int i,j;
        for (i = 0; i < field.sizeY; i++) {
            for (j = 0; j < field.sizeX; j++) {
                field.weight[i * field.sizeX + j] = 3;
            }
        }
        for (i = 0; i < 4; i++) {
            for (j = 2; j < 14; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[2] = field.weight[65] = field.weight[66] = 3;
        field.contain[2] = field.contain[65] = field.contain[66] = 0;
        field.weight[143] = field.weight[144] = field.weight[164] = 6;
        field.weight[186] = field.weight[187] = field.weight[207] = field.weight[208] = 6;
        for (i = 8; i < 13; i++) {
            for (j = 9; j < 11; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[188] = 1000;   field.contain[188] = -1;
        field.weight[240] = 4;
        for (i = 13; i < 20; i++) {
            for (j = 0; j < 4; j++) {
                field.weight[i * field.sizeX + j] = 4;
            }
        }
        field.contain[342] = 1; // !!!
        field.weight[283] = field.weight[303] = 4;
        for (i = 16; i < 20; i++) {
            for (j = 3; j < 6; j++) {
                field.weight[i * field.sizeX + j] = 4;
            }
        }
        field.weight[366] = field.weight[386] = 4;
        for (i = 4; i < 14; i++) {
            for (j = 10; j < 13; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[93] = 1000;    field.contain[93] = -1;
        for (i = 13; i < 18; i++) {
            for (j = 11; j < 14; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[149] = field.weight[189] = field.weight[209] = field.weight[229] = field.weight[249] = 1000;
        field.contain[149] = field.contain[189] = field.contain[209] = field.contain[229] = field.contain[249] = -1;
        field.weight[371] = field.weight[372] = field.weight[373] = 2;
        field.weight[390] = field.weight[391] = field.weight[392] = field.weight[393] = 1000;
        field.contain[390] = field.contain[391] = field.contain[392] = field.contain[393] = -1;
        field.weight[350] = 1000;   field.contain[350] = -1;
        for (i = 15; i < 19; i++) {
            for (j = 17; j < 20; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[399] = 1000;   field.contain[399] = -1;
        field.weight[318] = 3;      field.contain[318] = 0;
        field.weight[377] = 2;      field.contain[377] = 2;    // !!!
        field.weight[313] = field.weight[314] = field.weight[334] =  1000;
        field.contain[313] = field.contain[314] = field.contain[334] = -1;
        for (i = 0; i < 3; i++) {
            for (j = 17; j < 20; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[17] = 2;   field.contain[17] = 0;
        field.weight[77] = 2;   field.contain[77] = 10; // !!!
        field.weight[34] = field.weight[54] = field.weight[74] = 2;
        for (i = 3; i < 5; i++) {
            for (j = 15; j < 18; j++) {
                field.weight[i * field.sizeX + j] = 2;
            }
        }
        for (i = 7; i < 13; i++) {
            for (j = 13; j < 15; j++) {
                field.weight[i * field.sizeX + j] = 1000;
                field.contain[i * field.sizeX + j] = -1;
            }
        }
        field.weight[175] = field.weight[195] = field.weight[215] =  1000;
        field.contain[175] = field.contain[195] = field.contain[215] = -1;
        field.weight[396] = field.weight[397] = 2;
        field.weight[374] = field.weight[375] = field.weight[376] = 2;
        field.weight[354] = field.weight[355] = 2;
        field.weight[315] = field.weight[335] = 2;
        field.weight[296] = field.weight[297] = 2;
        field.weight[277] = field.weight[278] = 2;
        field.weight[198] = field.weight[218] = field.weight[238] = field.weight[258] = 2;
        field.weight[99] = field.weight[119] = field.weight[139] = field.weight[159] = field.weight[179] = 2;
        field.weight[98] = field.weight[118] = 2;
        // Проработка поля
        field.makeNeighbours();
        reset();
        autoEndTurn = 0;
        setSize(600, 600);
        //читаем изображение
        try {
            heroStand = ImageIO.read(new File("Creatures/A/hero.png"));
            heroBackStand = ImageIO.read(new File("Creatures/A/heroBack.png"));
            EmobStand = ImageIO.read(new File("Creatures/B/Ehero.png"));
            EplStand = ImageIO.read(new File("Creatures/B/Ehero.png"));
            background = ImageIO.read(new File("Bckg1.png"));
        } catch (IOException ex) {
//            System.out.println("Ошибка при считывпании картинки. Ты лалка.");
        }
        //читаем изображение
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //System.out.println("mouse clicked " + evt.getPoint().x + " " + evt.getPoint().y);
                if(moveIndicator != 1) {
                    if (evt.getModifiers() == evt.BUTTON1_MASK) {
                        prevX = x;
                        prevY = y;
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
            }
        });
    }

    public void reset () {
        LR = 2;
        x = xR = hero.x;
        prevX = hero.x;
        y = yR = hero.y;
        prevY = hero.y;
        sovp = 0;
        stepsDone = 0;
        moveIndicator = 0;
        field.way[0] = field.way[1] = 0;
    }

    public void drawPathway(Graphics g, Polygon poly[][]){
        int i;
        for (i = field.way[0]-1; i > 1; i--){
            g.setColor(new Color(255, 255, 0));
            if (field.contain[field.way[i]] == 0) {
                g.fillPolygon(poly[field.way[i] % field.sizeX][field.way[i] / field.sizeX]);
            }
            g.setColor(new Color(255, 255, 255));
            g.drawPolygon(poly[field.way[i]%field.sizeX][field.way[i]/field.sizeX]);
            g.setColor(new Color(0,0,0));
            Font font = new Font("Arial", Font.BOLD |Font.ITALIC, 11);
            g.setFont(font);
            g.drawString(field.price[field.way[i]] + "", 21 * ((field.way[i] / field.sizeX) % 2) + (field.way[i] % field.sizeX) * 42 + 70 + 42 - 5, (field.way[i] / field.sizeX) * 36 + 70 + 3);
        }
    }

    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        //g.setColor(new Color(255, 255, 255));
        g.setColor(Color.BLACK);
        int[] arrayX = {70, 70 + 21, 70 + 21, 70, 70 - 21, 70 - 21};
        int[] arrayY = {70 - 24, 70 - 12, 70 + 12, 70 + 24, 70 + 12, 70 - 12};
        int centx, centy;
        int i, j, k;
        g.drawImage(background, 91, 46, null);
        centx = 42;
        centy = 36;
        sovp = 0;
        Polygon poly[][] = new Polygon[20][20];
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
                } else {
                    arrayX[k] += 21;
                }
            }
        }
        if(/*stepsDone < field.way[0] && field.way[0] > 0 && */moveIndicator == 1){
            //System.out.println("Moving, moveIndicator = 1");
            if (hero.y * field.sizeX + hero.x == field.way[1]) {
                moveIndicator = 0;
            }
            else {
                if (stepsDone == 0) {
                    stepsDone++;
                    timer.start();
                }
                else {
                    if (hero.currDistance >= (field.weight[field.way[field.way[0] - stepsDone]] + field.weight[field.way[field.way[0] - (stepsDone - 1)]])) {
                        if (stepsDone > 0) {
                            LR = 1;
                        }
                        for (k = 2; k < 5; k++) {
                            if (field.g[hero.y * field.sizeX + hero.x][k] == field.way[field.way[0] - stepsDone]) {
                                LR = 2;
                            }
                        }
                        hero.x = field.way[field.way[0] - stepsDone] % field.sizeX;
                        hero.y = field.way[field.way[0] - stepsDone] / field.sizeX;
                        hero.currDistance -= (field.weight[field.way[field.way[0] - stepsDone]] + field.weight[field.way[field.way[0] - (stepsDone - 1)]]);
                        g.setColor(new Color(0, 150, 0));
                        g.fillPolygon(poly[hero.x][hero.y]);
//                    for(i=0; i<6; i++){
//                        if(field.g[field.way[field.way[0] - stepsDone]][i] == field.way[field.way[0] - stepsDone -1]){
//                            g.drawImage(movEmob[i], hero.x * 42 + 21 * (hero.y % 2) + 100,  hero.y * 36 + 0, this);
//                            try
//                            {
//                                 Thread.sleep(900);
//                            }
//                            catch (InterruptedException e)
//                            {
//                            }
//                        }
//                    }
                        stepsDone++;
                        timer.start();
                    } else {
                        if (autoEndTurn == 1) {
                            // Закончить ход
                            if (hero.army[0].mana - hero.army[0].currMana > 1) {
                                hero.army[0].currMana += 2;
                            }
                            hero.currDistance = hero.maxDistance;
                            repaint();
                        }
                    }
                }
            }
        }
        for (i = 0; i <= 19; i++) {
            for (j = 0; j <= 19; j++) {
                if (field.contain[j * field.sizeX + i] >= 0) {
                    if (field.contain[field.sizeX * j + i] == 1 || field.contain[field.sizeX * j + i] == 2) {
                        g.setColor(new Color(0, 0, 255));
                        g.fillPolygon(poly[i][j]);
                        g.setColor(new Color(255, 255, 255));
                        g.drawPolygon(poly[i][j]);
                    }
                    if (poly[i][j].contains(x, y)) {
                        if (poly[i][j].contains(prevX, prevY)) {
                            sovp = 1;
                            moveIndicator = 1;
                            //System.out.println("x = " + x + ", prevX = " + prevX + ", y = " + y + ", prevY = " + prevY + ", moveIndicator = 1");
                            stepsDone = 0;
                            x = 0;
                            y = 0;
                            repaint();
                            //field.way[0]=0;
                            //hero.x=i;
                            //hero.y=j;
                        }
                        else {
                            sovp = 0;
                            field.way[0] = 0;
                            field.pathway(hero.x, hero.y, i, j);
                            drawPathway(g, poly);
                        }
                    }
                }
            }
        }
//        field.range(hero, 0);
//        for(i=1; i<=field.available[0]; i++){
//            g.setColor(Color.ORANGE);
//            g.fillPolygon(poly[field.available[i]%field.sizeX][field.available[i]/field.sizeX]);
//            g.setColor(new Color(0, 0, 255));
//            g.drawPolygon(poly[field.available[i]%field.sizeX][field.available[i]/field.sizeX]);
//        }
        k = field.way[0];
        if(sovp == 0) {
            g.setColor(new Color(255, 0, 0)); //Отрисовка конечной клетки
            g.fillPolygon(poly[field.way[1] % field.sizeX][field.way[1] / field.sizeX]);
            g.setColor(new Color(0, 0, 0));
            Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 11);
            g.setFont(font);
            g.drawString(field.price[field.way[1]] + "", 21 * ((field.way[1] / field.sizeX) % 2) + (field.way[1] % field.sizeX) * 42 + 70 + 42 - 5, (field.way[1] / field.sizeX) * 36 + 70 + 3);
        }
        g.setColor(new Color(255, 255, 255));
        g.drawPolygon(poly[field.way[1] % field.sizeX][field.way[1] / field.sizeX]);
        //if(moveIndicator!=1) {
            //gif Отрисовка клетки героя
        g.setColor(new Color(0, 150, 0));
        g.fillPolygon(poly[hero.x][hero.y]);
        g.setColor(new Color(255, 255, 255));
        g.drawPolygon(poly[hero.x][hero.y]);
        if (LR == 1) {
            g.drawImage(heroBackStand, hero.x * 42 + 21 * (hero.y % 2) + 100, hero.y * 36 + 33, this);
        }
        else {
            g.drawImage(heroStand, hero.x * 42 + 21 * (hero.y % 2) + 100, hero.y * 36 + 33, this);
        }
        if (field.contain[Ehero[0].y * field.sizeX + Ehero[0].x] == 1) {
            g.drawImage(EplStand, Ehero[0].x * 42 + 21 * (Ehero[0].y % 2) + 97, Ehero[0].y * 36 + 33, this);
        }
        if (field.contain[Ehero[1].y * field.sizeX + Ehero[1].x] == 2) {
            g.drawImage(EmobStand, Ehero[1].x * 42 + 21 * (Ehero[1].y % 2) + 97, Ehero[1].y * 36 + 33, this);
        }
            //gif
        //}
        if(field.contain[hero.y * field.sizeX + hero.x]!=0){ //если встали на особенную клетку
            if(field.contain[hero.y*field.sizeX + hero.x] == 1 || field.contain[hero.y*field.sizeX + hero.x] == 2){
                mode = field.contain[hero.y * field.sizeX + hero.x];
                JFrame battleframe = new JFrame();
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Battleform BF = new Battleform();
                battleframe.setContentPane(BF.getbattleform());
                battleframe.pack();
                battleframe.setSize(600, 600);
                battleframe.setExtendedState(Frame.MAXIMIZED_BOTH);
                battleframe.dispose();
                battleframe.setUndecorated(true);
                battleframe.setVisible(true);
                field.contain[hero.y * field.sizeX + hero.x]=0;
            }
            if (field.contain[hero.y*field.sizeX + hero.x] == 10) {
                // Победа
                System.exit(0);
            }
        }
    }
}