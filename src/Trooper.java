public class Trooper {
    int number, health, currHealth, damage, distance, magicPower, mana, currMana, position, price, level;
    int[] damageTaken = new int[7]; // ���������� �� �������� ���������� i-�� ������ ����, ��� i �� 0 �� 6 ������������

    /*  ������� ��� ����, � ������� ����� ��������� ������ �� ���������� ������ Hero.
        � ������ BattlePaint �������� � ������ ���� ������ �� Master (���, ���� ����������� ����).
        � ������ BattlePaint �������� �� ������ ���� ������ �� Enemy (���, ������ ���� ������ ��������� Master).
        �� ���������� � ������ ���������� ������ Hero.
        ������������ � ������� Master � Enemy ����� ������.
        ����� ����� �������, ��� �������� ������ if � dealDamage.
        ������ ����� ����� ����� ��������� -7 � BattlePaint.    */

    int i;

    public void setDefault(int type) {  // ��� ������� ������
        switch (type) {
            case 1: // ������
                health = 21;
                damage = 5;
                distance = 10;  // 5
                magicPower = 0;
                mana = 0;
                price = 10;
                position = 0;
                break;
            case 2: // ����
                health = 69;
                damage = 3;
                distance = 6;   // 3
                magicPower = 0;
                mana = 0;
                price = 15;
                position = 60;
                break;
            case 3: // ������
                health = 27;
                damage = 4;
                distance = 6;   // 3
                magicPower = 0;
                mana = 0;
                price = 25;
                position = 120;
                break;
            case 4: // �������
                health = 32;
                damage = 25;
                distance = 16;  // 8
                magicPower = 0;
                mana = 0;
                price = 50;
                position = 180;
                break;
            case 5: // ������
                health = 25;
                damage = 2;
                distance = 4;   // 2
                magicPower = 3;
                mana = 23;
                price = 70;
                position = 240;
                break;
            case 6: // �������
                health = 60;
                damage = 21;
                distance = 12;  // 6
                magicPower = 1;
                mana = 8;
                price = 100;
                position = 300;
                break;
            default:
                break;
        }
        level = type;
        currHealth = health;
        currMana = mana;
        for (i = 0; i < 7; i++) {
            damageTaken[i] = 0;
        }
    }

    public void setDefault() {   // ��� �����
        currHealth = health = 1;
        damage = 20;
        distance = 0;
        magicPower = 10;
        currMana = mana = 17;
        price = 1000;
        number = 1;
        level = 0;  // ������� �������� �� ���� ���, ���������������� �����
        position = 401;
    }

    public void dealDamage(int n, int totalDamage) {  // ���������� ���������� ����! ���� ������, � �� �� ������� �� ���������.
        if (n < 7) {   // ���� ���� ���� ������
            int totalhealth = paintTest.hero.army[n].health * (paintTest.hero.army[n].number - 1) + paintTest.hero.army[n].currHealth;
            int killed = paintTest.hero.army[n].number;
            totalhealth -= totalDamage; // ���� ������, � �� �� ������� �� ���������, ����� totalhealth -= enemy.number * damage;
            if (totalhealth > 0) {
                paintTest.hero.army[n].number = totalhealth / paintTest.hero.army[n].health;
                if (totalhealth % paintTest.hero.army[n].health > 0) {
                    paintTest.hero.army[n].number++;
                }
                paintTest.hero.army[n].currHealth = totalhealth - paintTest.hero.army[n].health * (paintTest.hero.army[n].number - 1);
                paintTest.hero.army[n].damageTaken[level] += totalDamage; // ����� level - ������� ����������
                // ����� ��������� ��������� ��������� ����������� �� BattlePaint
            }
            else {
                paintTest.hero.army[n].number = 0;
                paintTest.hero.army[n].currHealth = 0;
                paintTest.hero.living--;
                paintTest.hero.army[n].position = 400;
                // ����� ��������� ��������� ������ �� BattlePaint
            }
            killed -= paintTest.hero.army[n].number;
            paintTest.Ehero[paintTest.mode - 1].XP += (5 + 3 * paintTest.hero.army[n].level) * killed;
        }
        else { //   ���� ����� ���� ����
            n -= 7;
            int totalhealth = paintTest.Ehero[paintTest.mode - 1].army[n].health * (paintTest.Ehero[paintTest.mode - 1].army[n].number - 1) + paintTest.Ehero[paintTest.mode - 1].army[n].currHealth;
            int killed = paintTest.Ehero[paintTest.mode - 1].army[n].number;
            totalhealth -= totalDamage; // ���� ������, � �� �� ������� �� ���������, ����� totalhealth -= enemy.number * damage;
            if (totalhealth > 0) {
                paintTest.Ehero[paintTest.mode - 1].army[n].number = totalhealth / paintTest.Ehero[paintTest.mode - 1].army[n].health;
                if (totalhealth % paintTest.Ehero[paintTest.mode - 1].army[n].health > 0) {
                    paintTest.Ehero[paintTest.mode - 1].army[n].number++;
                }
                paintTest.Ehero[paintTest.mode - 1].army[n].currHealth = totalhealth - paintTest.Ehero[paintTest.mode - 1].army[n].health * (paintTest.Ehero[paintTest.mode - 1].army[n].number - 1);
                paintTest.Ehero[paintTest.mode - 1].army[n].damageTaken[level] += totalDamage; // ����� level - ������� ����������
                // ����� ��������� ��������� ��������� ����������� �� BattlePaint
            }
            else {
                paintTest.Ehero[paintTest.mode - 1].army[n].number = 0;
                paintTest.Ehero[paintTest.mode - 1].army[n].currHealth = 0;
                paintTest.Ehero[paintTest.mode - 1].living--;
                paintTest.Ehero[paintTest.mode - 1].army[n].position = 400;
                // ����� ��������� ��������� ������ �� BattlePaint
            }
            killed -= paintTest.Ehero[paintTest.mode - 1].army[n].number;
            paintTest.hero.XP += (5 + 3 * paintTest.Ehero[paintTest.mode - 1].army[n].level) * killed;
        }
    }

    public void hit(int taker) {  // ����� ���������� �� ������ �� 0 �� 13 ������������.
        dealDamage(taker, number * damage);
        // �������� ������� ����� �� BattlePaint
    }

    public void castFireball(int taker) {    // ����� ���������� �� ������ �� 0 �� 13 ������������.
        // �������� ����� ��������� �� BattlePaint
        currMana -= 10;
        dealDamage(taker, number * (magicPower * 5));
    }

    public Trooper(int name, int amount) {   // ������� ����
        setDefault(name);
        number = amount;
    }

    public Trooper() {   // �����
        setDefault();
    }
}