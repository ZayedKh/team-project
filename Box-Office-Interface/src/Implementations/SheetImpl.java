package Implementations;
import interfaces.SheetInterfaces;
import models.DailySheet;

import java.sql.Connection;
import java.time.LocalDate;

class SheetImpl implements SheetInterfaces {
    public void generateTodaySheet(Connection connection) {
    }

    @Override
    public DailySheet generateDateSheet(LocalDate date, Connection connection) {
        return null;
    }
}
