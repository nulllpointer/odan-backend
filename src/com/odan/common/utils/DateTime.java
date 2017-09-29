package com.odan.common.utils;

import com.odan.common.model.Flags.SpecificDateType;
import com.odan.common.model.Flags.TimePeriod;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public static Timestamp getCurrentTimestamp() {
        java.util.Date date = new java.util.Date();
        Timestamp t = new Timestamp(date.getTime());
        return t;
    }

    /**
     * @param ts1 current date
     * @param ts2 duedate
     * @return 0 for same date, 1 for date passed (overdue) , -1 for date have
     * not come(notdue)
     */
    public static int compare(Timestamp ts1, Timestamp ts2) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date(ts1.getTime()));
        cal2.setTime(new Date(ts2.getTime()));
        int sameDay = 0;

        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
            sameDay = 0;
        else if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)
                || (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)))
            sameDay = -1;
        else if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)
                || (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)))
            sameDay = 1;
        return sameDay;
    }

    public static Timestamp getMonthStartDate(Timestamp ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts.getTime());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Timestamp date = new Timestamp(calendar.getTimeInMillis());
        return date;
    }

    public static Timestamp getMonthEndDate(Timestamp ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts.getTime());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp date = new Timestamp(calendar.getTimeInMillis());
        return date;
    }

    /**
     * returns the next recurring date
     *
     * @param type  Recurr period type
     * @param tsCur Current timestamp
     * @param tsEnd Recurr end date
     * @return
     */
    public static Timestamp getNextRecurDate(TimePeriod type, Timestamp tsCur, Timestamp tsEnd) {
        long time = 0;
        Timestamp tmp = new Timestamp(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tsCur.getTime());

        if (type == TimePeriod.DAILY) {
            time = 24 * 60 * 60 * 1000;

            // System.out.println("Yawartest tsCur" + tsCur +
            // "Yawartest tsCur.gettime" + tsCur.getTime() + "timeee" + time);
            tmp.setTime(tsCur.getTime() + time);

            if (tmp.before(tsEnd)) {
                return tmp;
            }
        } else if (type == TimePeriod.WEEKLY) {
            time = 7 * 24 * 60 * 60 * 1000;

            tmp.setTime(tsCur.getTime() + time);

            if (tmp.before(tsEnd)) {
                return tmp;
            }
        } else if (type == TimePeriod.MONTHLY) {
            // org.joda.time.DateTime jdts = org.joda.time.DateTime.n ts;//new
            // org.joda.time.DateTime();
            // jdts.plusMonths(1);

            calendar.add(Calendar.MONTH, 1);
            tmp = new Timestamp(calendar.getTimeInMillis());
            if (tmp.before(tsEnd)) {
                return tmp;
            }

        } else if (type == TimePeriod.QUARTERLY) {

            calendar.add(Calendar.MONTH, 3);
            tmp = new Timestamp(calendar.getTimeInMillis());
            if (tmp.before(tsEnd)) {
                return tmp;
            }
        } else if (type == TimePeriod.YEARLY) {

            calendar.add(Calendar.YEAR, 1);
            tmp = new Timestamp(calendar.getTimeInMillis());
            if (tmp.before(tsEnd)) {
                return tmp;
            }
        }
        return null;
    }

    public static Timestamp getNextTimestamp(Timestamp current, Byte period) {
        Timestamp next = new Timestamp(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current.getTime());

        if (period.equals(TimePeriod.DAILY)) {
            calendar.add(Calendar.DATE, 1);
        } else if (period.equals(TimePeriod.WEEKLY)) {
            calendar.add(Calendar.DATE, 7);
        } else if (period.equals(TimePeriod.MONTHLY)) {
            calendar.add(Calendar.MONTH, 1);
        } else if (period.equals(TimePeriod.QUARTERLY)) {
            calendar.add(Calendar.MONTH, 3);
        } else if (period.equals(TimePeriod.HALF_YEARLY)) {
            calendar.add(Calendar.MONTH, 6);
        } else if (period.equals(TimePeriod.YEARLY)) {
            calendar.add(Calendar.YEAR, 1);
        }

        next.setTime(calendar.getTimeInMillis());

        return next;
    }

    public static Timestamp getMidnightTimestamp(Timestamp current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current.getTime());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timestamp midnight = new Timestamp(calendar.getTimeInMillis());
        return midnight;
    }

    public static Timestamp getNextTimestamp(Timestamp current, TimePeriod period) {
        return getNextTimestamp(current, period);
    }

    public static Date getNextDayDate(Timestamp endDate) {
        Date tomorrow = new Date(endDate.getTime() + (1000 * 60 * 60 * 24));
        return tomorrow;
    }

    public static Date getEndOfDay(Date day) {
        Calendar cal = Calendar.getInstance();
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static java.sql.Timestamp getStartTimestamp(SpecificDateType type) {
        Calendar calendar = Calendar.getInstance();
        if (type == SpecificDateType.TODAY) {

        } else if (type == SpecificDateType.YESTERDAY) {
            calendar.add(Calendar.DATE, -1);

        } else if (type == SpecificDateType.TOMORROW) {
            calendar.add(Calendar.DATE, 1);

        } else if (type == SpecificDateType.LAST_MONTH) {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 1);

        } else if (type == SpecificDateType.NEXT_MONTH) {
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 1);

        } else if (type == SpecificDateType.THIS_MONTH) {

            calendar.set(Calendar.DATE, 1);

        } else if (type == SpecificDateType.LAST_WEEK) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, -7);
            } else {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, -1);
                }
                calendar.add(Calendar.DATE, -7);

            }

        } else if (type == SpecificDateType.NEXT_WEEK) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, 7);
            } else {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, 1);
                }

            }
        } else if (type == SpecificDateType.THIS_WEEK) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            } else {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, -1);
                }
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp midnight = new java.sql.Timestamp(calendar.getTimeInMillis());
        return midnight;
    }

    public static java.sql.Timestamp getEndTimestamp(SpecificDateType type) {
        Calendar calendar = Calendar.getInstance();
        if (type == SpecificDateType.TODAY) {
            calendar.add(Calendar.DATE, 0);
        } else if (type == SpecificDateType.YESTERDAY) {
            calendar.add(Calendar.DATE, -1);

        } else if (type == SpecificDateType.TOMORROW) {
            calendar.add(Calendar.DATE, 1);

        } else if (type == SpecificDateType.LAST_MONTH) {
            calendar.set(Calendar.DATE, 0);

        } else if (type == SpecificDateType.NEXT_MONTH) {
            calendar.add(Calendar.MONTH, 2);
            calendar.set(Calendar.DATE, 0);

        } else if (type == SpecificDateType.THIS_MONTH) {
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 0);

        } else if (type == SpecificDateType.LAST_WEEK) {
            calendar.setTimeInMillis(getStartTimestamp(type).getTime());
            calendar.add(Calendar.DATE, 6);

        } else if (type == SpecificDateType.NEXT_WEEK) {
            calendar.setTimeInMillis(getStartTimestamp(type).getTime());
            calendar.add(Calendar.DATE, 6);
        } else if (type == SpecificDateType.THIS_WEEK) {
            calendar.setTimeInMillis(getStartTimestamp(type).getTime());
            calendar.add(Calendar.DATE, 6);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        java.sql.Timestamp midnight = new java.sql.Timestamp(calendar.getTimeInMillis());
        return midnight;
    }


}
