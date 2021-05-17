public class Trooper {
    int number, health, currHealth, damage, distance, magicPower, mana, currMana, position, price, level;
    int[] damageTaken = new int[7]; // Полученный от существа противника i-го уровня урон, где i от 0 до 6 включительно

    /*  Создать два поля, в которых будут храниться ссылки на экземпляры класса Hero.
        В начале BattlePaint записать в первое поле ссылку на Master (тот, кому принадлежит юнит).
        В начале BattlePaint записать во второе поле ссылку на Enemy (тот, против кого сейчас сражается Master).
        Не передавать в методы экземпляры класса Hero.
        Использовать в методах Master и Enemy через ссылки.
        Кроме всего прочего, это позволит убрать if в dealDamage.
        Только тогда нужно будет прописать -7 в BattlePaint.    */

    int i;

    public void setDefault(int type) {  // Для обычных юнитов
        switch (type) {
            case 1: // Слабый
                health = 21;
                damage = 5;
                distance = 10;  // 5
                magicPower = 0;
                mana = 0;
                price = 10;
                position = 0;
                break;
            case 2: // Танк
                health = 69;
                damage = 3;
                distance = 6;   // 3
                magicPower = 0;
                mana = 0;
                price = 15;
                position = 60;
                break;
            case 3: // Лучник
                health = 27;
                damage = 4;
                distance = 6;   // 3
                magicPower = 0;
                mana = 0;
                price = 25;
                position = 120;
                break;
            case 4: // Дамагер
                health = 32;
                damage = 25;
                distance = 16;  // 8
                magicPower = 0;
                mana = 0;
                price = 50;
                position = 180;
                break;
            case 5: // Колдун
                health = 25;
                damage = 2;
                distance = 4;   // 2
                magicPower = 3;
                mana = 23;
                price = 70;
                position = 240;
                break;
            case 6: // Элитный
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

    public void setDefault() {   // Для героя
        currHealth = health = 1;
        damage = 20;
        distance = 0;
        magicPower = 10;
        currMana = mana = 17;
        price = 1000;
        number = 1;
        level = 0;  // Уровень существа на поле боя, соответствующего герою
        position = 401;
    }

    public void dealDamage(int n, int totalDamage) {  // Возвращает полученный опыт! Урон полный, а не от каждого из атакующих.
        if (n < 7) {   // Если комп бьёт игрока
            int totalhealth = paintTest.hero.army[n].health * (paintTest.hero.army[n].number - 1) + paintTest.hero.army[n].currHealth;
            int killed = paintTest.hero.army[n].number;
            totalhealth -= totalDamage; // Урон полный, а не от каждого из атакующих, иначе totalhealth -= enemy.number * damage;
            if (totalhealth > 0) {
                paintTest.hero.army[n].number = totalhealth / paintTest.hero.army[n].health;
                if (totalhealth % paintTest.hero.army[n].health > 0) {
                    paintTest.hero.army[n].number++;
                }
                paintTest.hero.army[n].currHealth = totalhealth - paintTest.hero.army[n].health * (paintTest.hero.army[n].number - 1);
                paintTest.hero.army[n].damageTaken[level] += totalDamage; // Здесь level - уровень атакующего
                // Вызов процедуры отрисовки изменения численности из BattlePaint
            }
            else {
                paintTest.hero.army[n].number = 0;
                paintTest.hero.army[n].currHealth = 0;
                paintTest.hero.living--;
                paintTest.hero.army[n].position = 400;
                // Вызов процедуры обработки смерти из BattlePaint
            }
            killed -= paintTest.hero.army[n].number;
            paintTest.Ehero[paintTest.mode - 1].XP += (5 + 3 * paintTest.hero.army[n].level) * killed;
        }
        else { //   Если игрок бьёт комп
            n -= 7;
            int totalhealth = paintTest.Ehero[paintTest.mode - 1].army[n].health * (paintTest.Ehero[paintTest.mode - 1].army[n].number - 1) + paintTest.Ehero[paintTest.mode - 1].army[n].currHealth;
            int killed = paintTest.Ehero[paintTest.mode - 1].army[n].number;
            totalhealth -= totalDamage; // Урон полный, а не от каждого из атакующих, иначе totalhealth -= enemy.number * damage;
            if (totalhealth > 0) {
                paintTest.Ehero[paintTest.mode - 1].army[n].number = totalhealth / paintTest.Ehero[paintTest.mode - 1].army[n].health;
                if (totalhealth % paintTest.Ehero[paintTest.mode - 1].army[n].health > 0) {
                    paintTest.Ehero[paintTest.mode - 1].army[n].number++;
                }
                paintTest.Ehero[paintTest.mode - 1].army[n].currHealth = totalhealth - paintTest.Ehero[paintTest.mode - 1].army[n].health * (paintTest.Ehero[paintTest.mode - 1].army[n].number - 1);
                paintTest.Ehero[paintTest.mode - 1].army[n].damageTaken[level] += totalDamage; // Здесь level - уровень атакующего
                // Вызов процедуры отрисовки изменения численности из BattlePaint
            }
            else {
                paintTest.Ehero[paintTest.mode - 1].army[n].number = 0;
                paintTest.Ehero[paintTest.mode - 1].army[n].currHealth = 0;
                paintTest.Ehero[paintTest.mode - 1].living--;
                paintTest.Ehero[paintTest.mode - 1].army[n].position = 400;
                // Вызов процедуры обработки смерти из BattlePaint
            }
            killed -= paintTest.Ehero[paintTest.mode - 1].army[n].number;
            paintTest.hero.XP += (5 + 3 * paintTest.Ehero[paintTest.mode - 1].army[n].level) * killed;
        }
    }

    public void hit(int taker) {  // Юниты передаются по номеру от 0 до 13 включительно.
        dealDamage(taker, number * damage);
        // Анимация обычной атаки из BattlePaint
    }

    public void castFireball(int taker) {    // Юниты передаются по номеру от 0 до 13 включительно.
        // Анимация каста фаерболла из BattlePaint
        currMana -= 10;
        dealDamage(taker, number * (magicPower * 5));
    }

    public Trooper(int name, int amount) {   // Обычный юнит
        setDefault(name);
        number = amount;
    }

    public Trooper() {   // Герой
        setDefault();
    }
}