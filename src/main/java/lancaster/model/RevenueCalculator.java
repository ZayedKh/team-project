package lancaster.model;

public class RevenueCalculator {
    // VAT rate as a decimal
    private static final double VAT_RATE = 0.20; // Standard UK VAT rate

    // Enum for days of the week
    public enum DayType {
        MONDAY_TO_THURSDAY,
        FRIDAY_TO_SATURDAY,
        SUNDAY
    }

    // Enum for venue spaces
    public enum VenueSpace {
        WHOLE_VENUE,
        MAIN_HALL,
        SMALL_HALL,
        REHEARSAL_SPACE,
        GREEN_ROOM,
        BRONTE_BOARDROOM,
        DICKENS_DEN,
        POE_PARLOR,
        GLOBE_ROOM,
        CHEKHOV_CHAMBER
    }

    // Enum for booking duration types
    public enum BookingType {
        HOURLY,
        EVENING,
        FULL_DAY,
        MORNING_AFTERNOON,
        WEEKLY
    }

    // Method to calculate revenue for whole venue bookings
    public double calculateWholeVenueRevenue(DayType day, BookingType bookingType, boolean includeVAT) {
        double basePrice = 0.0;

        if (bookingType == BookingType.EVENING) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 6750.0;
            } else {
                basePrice = 6250.0;
            }
        } else if (bookingType == BookingType.FULL_DAY) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 9500.0;
            } else {
                basePrice = 8500.0;
            }
        }

        return includeVAT ? basePrice * (1 + VAT_RATE) : basePrice;
    }

    // Method to calculate revenue for Main Hall bookings
    public double calculateMainHallRevenue(DayType day, BookingType bookingType, int hours, boolean includeVAT) {
        double basePrice = 0.0;

        if (bookingType == BookingType.HOURLY) {
            // Minimum of 3 hours
            hours = Math.max(3, hours);
            basePrice = 325.0 * hours;
        } else if (bookingType == BookingType.EVENING) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 2200.0;
            } else {
                basePrice = 1850.0;
            }
        } else if (bookingType == BookingType.FULL_DAY) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 4200.0;
            } else {
                basePrice = 3800.0;
            }
        }

        return includeVAT ? basePrice * (1 + VAT_RATE) : basePrice;
    }

    // Method to calculate revenue for Small Hall bookings
    public double calculateSmallHallRevenue(DayType day, BookingType bookingType, int hours, boolean includeVAT) {
        double basePrice = 0.0;

        if (bookingType == BookingType.HOURLY) {
            // Minimum of 3 hours
            hours = Math.max(3, hours);
            basePrice = 225.0 * hours;
        } else if (bookingType == BookingType.EVENING) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 1300.0;
            } else {
                basePrice = 950.0;
            }
        } else if (bookingType == BookingType.FULL_DAY) {
            if (day == DayType.FRIDAY_TO_SATURDAY) {
                basePrice = 2500.0;
            } else {
                basePrice = 2200.0;
            }
        }

        return includeVAT ? basePrice * (1 + VAT_RATE) : basePrice;
    }

    // Method to calculate revenue for Rehearsal Space bookings
    public double calculateRehearsalSpaceRevenue(DayType day, BookingType bookingType, int hours, boolean includeVAT) {
        double basePrice = 0.0;

        if (bookingType == BookingType.HOURLY) {
            // Minimum of 3 hours
            hours = Math.max(3, hours);
            basePrice = 60.0 * hours;
        } else if (bookingType == BookingType.FULL_DAY) {
            if (day == DayType.MONDAY_TO_THURSDAY || day == DayType.FRIDAY_TO_SATURDAY) {
                if (hours <= 7) { // Assuming 10:00-17:00 is 7 hours
                    basePrice = 240.0;
                } else { // Assuming 10:00-23:00 is full day
                    basePrice = 450.0;
                }
            } else { // Sunday
                if (hours <= 7) { // Assuming 10:00-17:00 is 7 hours
                    basePrice = 340.0;
                } else { // Assuming 10:00-23:00 is full day
                    basePrice = 500.0;
                }
            }
        } else if (bookingType == BookingType.WEEKLY) {
            if (hours <= 8) { // Assuming 10:00-18:00 is 8 hours
                basePrice = 1000.0;
            } else { // Assuming 10:00-23:00 is daily access
                basePrice = 1500.0;
            }
        }

        return includeVAT ? basePrice * (1 + VAT_RATE) : basePrice;
    }

    // Method to calculate revenue for room bookings
    public double calculateRoomRevenue(VenueSpace room, BookingType bookingType, boolean includeVAT) {
        double basePrice = 0.0;

        switch (room) {
            case GREEN_ROOM:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 25.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 75.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 130.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 600.0;
                }
                break;
            case BRONTE_BOARDROOM:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 40.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 120.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 200.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 900.0;
                }
                break;
            case DICKENS_DEN:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 30.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 90.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 150.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 700.0;
                }
                break;
            case POE_PARLOR:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 35.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 100.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 170.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 800.0;
                }
                break;
            case GLOBE_ROOM:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 50.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 150.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 250.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 1100.0;
                }
                break;
            case CHEKHOV_CHAMBER:
                if (bookingType == BookingType.HOURLY) {
                    basePrice = 38.0;
                } else if (bookingType == BookingType.MORNING_AFTERNOON) {
                    basePrice = 110.0;
                } else if (bookingType == BookingType.FULL_DAY) {
                    basePrice = 180.0;
                } else if (bookingType == BookingType.WEEKLY) {
                    basePrice = 850.0;
                }
                break;
        }

        return includeVAT ? basePrice * (1 + VAT_RATE) : basePrice;
    }

    // Method to calculate total revenue for a booking
    public double calculateTotalRevenue(VenueSpace space, DayType day, BookingType bookingType,
                                        int hours, boolean includeVAT) {
        double revenue = 0.0;

        switch (space) {
            case WHOLE_VENUE:
                revenue = calculateWholeVenueRevenue(day, bookingType, includeVAT);
                break;
            case MAIN_HALL:
                revenue = calculateMainHallRevenue(day, bookingType, hours, includeVAT);
                break;
            case SMALL_HALL:
                revenue = calculateSmallHallRevenue(day, bookingType, hours, includeVAT);
                break;
            case REHEARSAL_SPACE:
                revenue = calculateRehearsalSpaceRevenue(day, bookingType, hours, includeVAT);
                break;
            default:
                revenue = calculateRoomRevenue(space, bookingType, includeVAT);
                break;
        }

        return revenue;
    }

    // Example usage method
    public static void main(String[] args) {
        RevenueCalculator calculator = new RevenueCalculator();

        // Example: Calculate revenue for Main Hall booking on a Friday evening
        double mainHallFridayEvening = calculator.calculateTotalRevenue(
                VenueSpace.MAIN_HALL,
                DayType.FRIDAY_TO_SATURDAY,
                BookingType.EVENING,
                0, // Hours not needed for evening bookings
                true // Include VAT
        );

        System.out.println("Main Hall Friday Evening (with VAT): £" + mainHallFridayEvening);

        // Example: Calculate revenue for Small Hall hourly booking (4 hours) on a Monday
        double smallHallHourly = calculator.calculateTotalRevenue(
                VenueSpace.SMALL_HALL,
                DayType.MONDAY_TO_THURSDAY,
                BookingType.HOURLY,
                4, // 4 hours
                false // Exclude VAT
        );

        System.out.println("Small Hall Monday (4 hours, excl. VAT): £" + smallHallHourly);

        // Example: Calculate revenue for Globe Room full day
        double globeRoomFullDay = calculator.calculateTotalRevenue(
                VenueSpace.GLOBE_ROOM,
                DayType.MONDAY_TO_THURSDAY, // Day doesn't matter for rooms
                BookingType.FULL_DAY,
                0, // Hours not needed for full day
                true // Include VAT
        );

        System.out.println("Globe Room Full Day (with VAT): £" + globeRoomFullDay);
    }
}