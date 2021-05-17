public class Field {
    int sizeX, sizeY;
    int[] way = new int[400+20]; //массив, в котором хранится путь
    //final Random random = new Random();
    int[][] g = new int [400+20][7]; //массив глобальных номеров соседей
    int[][] l = new int [400+20][7]; //массив весов пути до соседа
    int[] contain = new int [400+20]; //массив содержания клетки
    int[] price = new int[400+20]; //цена достижения данной вершины
    int[] Rprice = new int[400+20]; //цена достижения данной вершины
    int[] weight = new int[400+20]; // Вес данной вершины
    int[] available = new int[400+20]; //массив, в котором хранятся доступные для прохождения клетки

    public Field(int x, int y){
        sizeX = x;
        sizeY = y;
        //int[][] g = new int[sizeX*sizeY+2][7]; //массив глобальных номеров соседей
        //int[][] l = new int[sizeX*sizeY+2][7]; //массив весов пути до соседа
        //int[] way = new int[sizeX*sizeY+2];
        way[0] = 0;
        available[0] = 0;
        int i, j;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeY; j++) {
                weight[j * sizeX + i] = 1;
                contain[j * sizeX + i] = 0; //генерируем содержание клеток
            }
        }
    }

    public void refreshContain() {
        int i, j;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeY; j++) {
                contain[j * sizeX + i]=0;   // Обнуляем содержание клеток
            }
        }
        contain[sizeX*sizeY]=-1;
    }

    public void makeNeighbours() {
        int i, j, k;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeY; j++) {
                g[j * sizeX + i][6] = -1;
                l[j * sizeX + i][6] = -1;
                g[j * sizeX + i][0] = j * sizeX + i - 1; //левый сосед
//                l[j * sizeX + i][0] = 1;
                g[j * sizeX + i][3] = j * sizeX + i + 1; //правый сосед
//                l[j * sizeX + i][3] = 1;
//                l[j * sizeX + i][1] = 1;
//                l[j * sizeX + i][2] = 1;
//                l[j * sizeX + i][5] = 1;
//                l[j * sizeX + i][4] = 1;
                if (j % 2 == 0) {
                    g[j * sizeX + i][1] = (j - 1) * sizeX + i - 1; //верхний левый сосед
                    g[j * sizeX + i][2] = (j - 1) * sizeX + i; //верхний правый сосед
                    g[j * sizeX + i][4] = (j + 1) * sizeX + i; //нижний правый сосед
                    g[j * sizeX + i][5] = (j + 1) * sizeX + i - 1; //нижний левый сосед
                } else {
                    g[j * sizeX + i][1] = (j - 1) * sizeX + i; //верхний левый сосед
                    g[j * sizeX + i][2] = (j - 1) * sizeX + i + 1; //верхний правый сосед
                    g[j * sizeX + i][4] = (j + 1) * sizeX + i + 1; //нижний правый сосед
                    g[j * sizeX + i][5] = (j + 1) * sizeX + i; //нижний левый сосед
                }
            }
        }
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeY; j++) {
                for (k = 0; k < 6; k++) {
                    if((g[j * sizeX + i][k] >= 0) && (g[j * sizeX + i][k] < sizeX * sizeY)) {
                        l[j * sizeX + i][k] = (weight[j * sizeX + i] + weight[g[j * sizeX + i][k]]); // Можно и не делить на 2
                    }
                }
            }
        }
        for (i = 0; i < sizeX; i++) { //верхние соседи верхних клеток, нижние соседи нижних клеток
            g[i][1] = sizeX * sizeY;
            l[i][1] = -1;
            g[i][2] = sizeX * sizeY;
            l[i][2] = -1;
            g[(sizeY-1) * sizeX + i][4] = sizeX * sizeY;
            l[(sizeY-1) * sizeX + i][4] = -1;
            g[(sizeY-1) * sizeX + i][5] = sizeX * sizeY;
            l[(sizeY-1) * sizeX + i][5] = -1;
        }
        for (j = 0; j < sizeY; j++) { //соседи левых клеток, соседи правых клеток
            g[j*sizeX][0]=sizeX*sizeY;
            l[j*sizeX][0]=-1;
            g[j*sizeX+(sizeX-1)][3]=sizeX*sizeY;
            l[j*sizeX+(sizeX-1)][3] = -1;
            if(j%2==0) {
                g[j*sizeX][1]=sizeX*sizeY;
                l[j*sizeX][1]=-1;
                g[j*sizeX][5]=sizeX*sizeY;
                l[j*sizeX][5]=-1;
            }
            else {
                g[j*sizeX+(sizeX-1)][2]=sizeX*sizeY;
                l[j*sizeX+(sizeX-1)][2]=-1;
                g[j*sizeX+(sizeX-1)][4]=sizeX*sizeY;
                l[j*sizeX+(sizeX-1)][4]=-1;
            }
        }
        for (i = 1; i <= 2; i++) {
            for (k = 0; k < 7; k++) {
                g[sizeX*sizeY+i][k]=sizeX*sizeY;
                l[sizeX*sizeY+i][k]=-1;
            }
        }
    }

    public void pathway(int hX, int hY, int x, int y){
        int beg;
        int en=sizeX*y+x;
        beg=(sizeX*hY)+hX; //взяли глобальный номер координаты
        //System.out.println(x + " " + y + " " + beg);
        int local,i,j,last,lengthway,prevlast; //last - индекс последнего элемента массива рассмотренных
        int[] visited = new int[sizeX*sizeY+1]; //номера рассмотренных
        int[] previous = new int[sizeX*sizeY+2];  //клетка, из которой попали
        price[beg]=0;
        for(i=0; i<sizeX*sizeY+2; i++){
            previous[i]=-1; //изначально для каждой клетки нет предыдущего элемента
            price[i]=0;
        }
        previous[beg]=-2; //у клетки начала пути свой, особый предыдущий элемент
        previous[sizeX*sizeY]=-3;
        visited[1]=beg;
        last=1;
        prevlast=0;
        local=0;
        //System.out.println("Хотим попасть из " + currHero.x + " ; " + currHero.y + " в " + x + " ; " + y);
        while(visited[last]!=en && visited[last]!=-1){ //пока не достигли конечной вершины или минимального соседа нет
            //System.out.println("Продолжаем, потому что можем. last = " + last + " (координаты - " + visited[last]%sizeX + " ; " + visited[last]/sizeX + " )");
            lengthway=-1;
            visited[last+1]=-1;
            for(i=1; i<=last; i++){
                for(j=0; j<6; j++){
                    if(g[visited[i]][j]!=-1 && g[visited[i]][j]!=sizeX*sizeY && contain[g[visited[i]][j]]>=0){ //если сосед есть и это доступное поле
                        //System.out.println("Сосед есть. Его координаты - " + g[visited[i]][j]%sizeX + " ; " + g[visited[i]][j]/sizeX);
                        if(previous[g[visited[i]][j]]==-1){ //если данный сосед не занят
                            //System.out.println("Он даже не занят.");
                            if(lengthway>(price[visited[i]]+l[visited[i]][j]) || lengthway==-1){ //если до него можно добраться меньшей ценой
                                //System.out.println("Новый кандидат - " + g[visited[i]][j]%sizeX + " ; " + g[visited[i]][j]/sizeX);
                                lengthway=price[visited[i]]+l[visited[i]][j]; //обновили минимальное расстояние до кандидата в рассмотренные
                                prevlast=visited[i]; //обновили кандидата в предыдущие
                                local=j;
                                visited[last+1]=g[visited[i]][j]; //обновили кандидата на следующего рассмотренного
                            }
                        }
                    }
                }
            }
            if(visited[last+1]!=-1){
                //System.out.println("Нашли кого-то. Его координаты - " + visited[last+1]%sizeX + " ; " + visited[last+1]/sizeX);
                previous[visited[last+1]]=prevlast; //записали предыдущий к новому рассмотренному
                price[visited[last+1]]=lengthway;
            }
            last++; //увеличили количество рассмотренных
        }
        //System.out.println("Posetili ");
        int presentx,presenty;
        i=visited[last];
        j=1;
        way[0]=0;
        //while(i >= 0){
        while(i!=-2){
            way[j]=i;
            j++;
            i=previous[i];
            way[0]++;
        }
//        for(i=j-1; i>=1; i--){
//            presentx=way[i]%sizeX;
//            presenty=way[i]/sizeX;
//            System.out.println(presentx + "; " + presenty);
//        }
    }

    public void range(Hero currHero, int k){
        int beg;
        //beg=currHero.army.position[k]; //взяли глобальный номер координаты
        beg=currHero.army[k].position;
        int i,j,last,lengthway,prevlast; //last - индекс последнего элемента массива рассмотренных
        int[] d = new int[sizeX*sizeY+20];  // Учли ли юнита, стоящего на этой клетке
        int[] previous = new int[sizeX*sizeY+20];  //клетка, из которой попали
        Rprice[beg] = 0;
        for(i=0; i < sizeX*sizeY+20; i++){
            previous[i] = -1; //изначально для каждой клети нет предыдущего элемента
            Rprice[i] = 0;
            d[i] = 0;
        }
        previous[beg]=-2; //у клетки начала пути свой, особый предыдущий элемент
        previous[sizeX*sizeY]=-3;
        available[1]=beg;
        last=1;
        prevlast=0;
        while(Rprice[available[last]]<=currHero.army[k].distance && last<400){ //пока цена достижения не больше возможной для данного юнита
            lengthway=-1;
            available[last+1]=-1;
            for(i=1; i<=last; i++){
                for(j=0; j<6; j++){
                    if(g[available[i]][j]!=-1 && g[available[i]][j]!=sizeX*sizeY){ //если сосед есть
                        if (contain[g[available[i]][j]] >= 0) {   // Если это доступное поле
                            if (previous[g[available[i]][j]] == -1) { //если данный сосед не занят
                                if (lengthway > (Rprice[available[i]] + l[available[i]][j]) || lengthway == -1) { //если до него можно добраться меньшей ценой
                                    lengthway = Rprice[available[i]] + l[available[i]][j]; //обновили минимальное расстояние до кандидата в рассмотренные
                                    prevlast = available[i];
                                    available[last + 1] = g[available[i]][j]; //обновили кандидата на следующего рассмотренного
                                }
                            }
                        }
                        else {
                            if ((contain[g[available[i]][j]] < -10) && (d[g[available[i]][j]] == 0)) {
                                contain[g[available[i]][j]] += 10;
                                d[g[available[i]][j]]++;
                            }
                        }
                    }
                }
            }
            last++; //увеличили количество рассмотренных
            if(available[last]!=-1){
                previous[available[last]]=prevlast; //записали предыдущий к новому рассмотренному
                Rprice[available[last]]=lengthway;
            }
            else {
                break;
            }
        }
        available[0]=last-1;
    }
}