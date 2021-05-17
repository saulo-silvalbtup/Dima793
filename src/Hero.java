public class Hero {
    int x,y,lvl,XP,living,maxDistance,currDistance,i;
    Trooper army[] = new Trooper[7];
    public void levelUp() {
        while (XP>=lvl*100) {
            XP-=lvl*100;
            army[0].damage+=lvl*2;
            army[0].mana+=lvl*2;
            army[0].mana+=lvl;
            lvl++;
        }
    }
    void setDefault(int a, int b) {
        x = a;
        y = b;
        currDistance = maxDistance = 30;
        lvl = 1;
        XP = 0;
        army[0] = new Trooper();
        for (i = 1; i < 7; i++) {
            army[i] = new Trooper(i, 10 * (7 - i));
        }
        living = 6;
    }
    public Hero(int l, int m){
        setDefault(l,m);
    }
}