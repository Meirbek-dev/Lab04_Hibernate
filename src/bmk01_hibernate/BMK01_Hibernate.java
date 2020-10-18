package bmk01_hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.Week;
import org.hibernate.criterion.Restrictions;

public class BMK01_Hibernate {

    public static void main(String[] args) {

        Session forecast1 = connection.Controller.getSessionFactory().openSession();
        Transaction trans = forecast1.beginTransaction();

        System.out.println("Данные из таблицы:");
        list(forecast1.createCriteria(pojos.Week.class).list());

        System.out.println("Добавление новых данных в таблицу.");
        //Сохранение данных в таблицу
        forecast1.save(new pojos.Week("22 октября", "Четверг", "Переменная облачность", "+9°C"));
        forecast1.save(new pojos.Week("23 октября", "Пятница", "Переменная облачность, замерзающий дождь", "+5°C"));
        forecast1.save(new pojos.Week("22 октября", "Суббота", "Переменная облачность, небольшой дождь", "+7°C"));
        forecast1.save(new pojos.Week("23 октября", "Воскресенье", "Пасмурно, дождь", "+12°C"));

        System.out.println("Обновлённые данные таблицы:");
        list(forecast1.createCriteria(pojos.Week.class).list());

        System.out.println("Удаление записи, где погода - 'Пасмурно, дождь'.");
        pojos.Week sunny = (pojos.Week) forecast1.createCriteria(pojos.Week.class)
                .add(Restrictions.eq("weather", "Пасмурно, дождь"))
                .uniqueResult();
        forecast1.delete(sunny);
        list(forecast1.createCriteria(pojos.Week.class).list());

        System.out.println("Изменение погоды с 'Переменная облачность, замерзающий дождь' на 'Дождь'");
        pojos.Week murky = (pojos.Week) forecast1.createCriteria(pojos.Week.class)
                .add(Restrictions.eq("weather", "Переменная облачность, замерзающий дождь"))
                .uniqueResult();
        murky.setWeather("Дождь");
        forecast1.update(murky);
        list(forecast1.createCriteria(pojos.Week.class).list());

        //Изменение данных во 2 записи - вторник
        System.out.println("Изменение данных во 2 записи - вторник.");
        pojos.Week weekDay2 = (pojos.Week) forecast1.load(pojos.Week.class, 2);
        weekDay2.setDate("27 октября");
        weekDay2.setTemperature("-7°C");
        weekDay2.setWeather("Облачно, дождь");

        forecast1.update(weekDay2);
        System.out.println("Обновлённые данные таблицы:");
        list(forecast1.createCriteria(pojos.Week.class).list());

        //Удаление 5 записи - пятницы из таблицы
        System.out.println("Удаление 5 записи - пятницы из таблицы.");
        pojos.Week weekday5 = (pojos.Week) forecast1.load(pojos.Week.class, 5);

        forecast1.delete(weekday5);

        System.out.println("Конечное состояние таблицы:");
        list(forecast1.createCriteria(pojos.Week.class).list());

        forecast1.close();
        System.exit(0);
        trans = null;

    }

    private static void list(List<Week> days) {
        days.forEach(System.out::println);
        System.out.println("");
    }
}
