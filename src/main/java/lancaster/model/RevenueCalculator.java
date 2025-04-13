package lancaster.model;

/**
 * Utility class for calculating revenue based on various booking parameters.
 * <p>
 * This class provides methods to calculate the revenue for different venue spaces based on the day of the week,
 * booking duration type, and whether VAT should be included. It supports revenue calculations for whole venue bookings,
 * individual hall bookings, rehearsal space bookings, and specific room bookings.
 * </p>
 */
public class RevenueCalculator {
    /**
     * The Value Added Tax rate used in revenue calculations.
     */
    private static final double VAT_RATE = 0.20; // Standard UK VAT rate

    /**
     * Enum representing groups of days for pricing variations.
     */
    public enum DayType {
        /**
         * Represents Monday through Thursday.
         */
        MONDAY_TO_THURSDAY,
        /**
         * Represents Friday and Saturday.
         */
        FRIDAY_TO_SATURDAY,
        /**
         * Represents Sunday.
         */
        SUNDAY
    }

    /**
     * Enum representing the available venue spaces.
     */
    public enum VenueSpace {
        /**
         * Represents the whole venue.
         */
        WHOLE_VENUE,
        /**
         * Represents the Main Hall.
         */
        MAIN_HALL,
        /**
         * Represents the Small Hall.
         */
        SMALL_HALL,
        /**
         * Represents the Rehearsal Space.
         */
        REHEARSAL_SPACE,
        /**
         * Represents the Green Room.
         */
        GREEN_ROOM,
        /**
         * Represents the Brontë Boardroom.
         */
        BRONTE_BOARDROOM,
        /**
         * Represents Dickens Den.
         */
        DICKENS_DEN,
        /**
         * Represents Poe Parlor.
         */
        POE_PARLOR,
        /**
         * Represents the Globe Room.
         */
        GLOBE_ROOM,
        /**
         * Represents Chekhov Chamber.
         */
        CHEKHOV_CHAMBER
    }

    /**
     * Enum representing the duration or type of the booking.
     */
    public enum BookingType {
        /**
         * Booking charged on an hourly basis.
         */
        HOURLY,
        /**
         * Booking for an evening period.
         */
        EVENING,
        /**
         * Booking for the entire day.
         */
        FULL_DAY,
        /**
         * Booking for either a morning or afternoon period.
         */
        MORNING_AFTERNOON,
        /**
         * Booking on a weekly basis.
         */
        WEEKLY
    }

    /**
     * Calculates the revenue for whole venue bookings.
     * <p>
     * The pricing is adjusted based on whether the booking is for an evening or a full day and the day type.
     * </p>
     *
     * @param day         the {@link DayType} indicating the day group.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the calculated revenue.
     */
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

    /**
     * Calculates the revenue for Main Hall bookings.
     * <p>
     * For hourly bookings, a minimum of 3 hours is charged. Evening and full day pricing vary based on the day type.
     * </p>
     *
     * @param day         the {@link DayType} indicating the day group.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param hours       the number of hours booked (relevant for hourly bookings).
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the calculated revenue.
     */
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

    /**
     * Calculates the revenue for Small Hall bookings.
     * <p>
     * For hourly bookings, a minimum of 3 hours is charged. The base price is selected according to the day type and booking duration.
     * </p>
     *
     * @param day         the {@link DayType} indicating the day group.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param hours       the number of hours booked (relevant for hourly bookings).
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the calculated revenue.
     */
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

    /**
     * Calculates the revenue for Rehearsal Space bookings.
     * <p>
     * Pricing is determined by whether the booking is hourly, full day, or weekly. For full day bookings,
     * the pricing further depends on the number of hours and day type.
     * </p>
     *
     * @param day         the {@link DayType} indicating the day group.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param hours       the number of hours booked (affects full day and weekly pricing).
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the calculated revenue.
     */
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

    /**
     * Calculates the revenue for specific room bookings.
     * <p>
     * This method computes revenue based on the venue space and booking type. The pricing is predefined for each room.
     * </p>
     *
     * @param room        the {@link VenueSpace} representing the room.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the calculated revenue.
     */
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

    /**
     * Calculates the total revenue for a booking based on the venue space, day type, booking type, and hours.
     * <p>
     * This method delegates the revenue calculation to the appropriate method according to the venue space.
     * </p>
     *
     * @param space       the {@link VenueSpace} representing the booked space.
     * @param day         the {@link DayType} indicating the day group.
     * @param bookingType the {@link BookingType} specifying the duration type for the booking.
     * @param hours       the number of hours booked (if applicable for the booking type).
     * @param includeVAT  {@code true} to include VAT in the calculation, {@code false} otherwise.
     * @return the total calculated revenue.
     */
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

    /**
     * Demonstrates example usages of the revenue calculation methods.
     * <p>
     * This example usage includes revenue calculations for various booking scenarios such as a Main Hall evening booking,
     * a Small Hall hourly booking, and a Globe Room full day booking.
     * </p>
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        RevenueCalculator calculator = new RevenueCalculator();

        // Example: Calculate revenue for Main Hall booking on a Friday evening.
        double mainHallFridayEvening = calculator.calculateTotalRevenue(
                VenueSpace.MAIN_HALL,
                DayType.FRIDAY_TO_SATURDAY,
                BookingType.EVENING,
                0, // Hours not needed for evening bookings
                true // Include VAT
        );
        System.out.println("Main Hall Friday Evening (with VAT): £" + mainHallFridayEvening);

        // Example: Calculate revenue for Small Hall hourly booking (4 hours) on a Monday.
        double smallHallHourly = calculator.calculateTotalRevenue(
                VenueSpace.SMALL_HALL,
                DayType.MONDAY_TO_THURSDAY,
                BookingType.HOURLY,
                4, // 4 hours
                false // Exclude VAT
        );
        System.out.println("Small Hall Monday (4 hours, excl. VAT): £" + smallHallHourly);

        // Example: Calculate revenue for Globe Room full day booking.
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
