package com.outofbox.model;

import android.util.Pair;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class UsageEntityView {
    private DateTime dateOfUsage;
    private HourlyUsage []hourlyUsage;
    private UsageEntityView(DateTime dateOfUsage, HourlyUsage []hourlyUsage) {
        this.dateOfUsage = dateOfUsage;
        this.hourlyUsage = hourlyUsage;
    }

    public DateTime getDateOfUsage() {
        return dateOfUsage;
    }

    public HourlyUsage[] getHourlyUsage() {
        return hourlyUsage;
    }

    public static class UsageEntityViewBuilder {
        private DateTime date;
        private List<Pair<DateTime, DateTime>> usageDates;

        public UsageEntityViewBuilder(DateTime date, List<Pair<DateTime, DateTime>> usageDates) {
            this.date = date;
            this.usageDates = usageDates;
        }

        public UsageEntityView build() {
            return new UsageEntityView(usageDates.get(0).first, getHourlyUsage(usageDates));
        }

        private HourlyUsage[] getHourlyUsage(final List<Pair<DateTime, DateTime>> usageDates) {
            HourlyUsage[] hourlyUsages = new HourlyUsage[23];
            for (int i = 0; i < hourlyUsages.length; i++) {
                hourlyUsages[i] = new HourlyUsage(i);
            }

            Pair<DateTime, DateTime> lastUsage = null;
            int duration = 0;
            int count = 0;
            for(Pair<DateTime, DateTime> usage : usageDates) {
                for (Pair<DateTime, DateTime> splitted: getSplittedHours(usage)) {
                    if (lastUsage == null) {
                        lastUsage = splitted;
                        count = 1;
                        duration = (int) (lastUsage.second.getMillis() - lastUsage.first.getMillis());
                    } else {
                        if (lastUsage.first.getHourOfDay() == splitted.first.getHourOfDay()) {
                            count++;
                            duration += (int) (splitted.second.getMillis() - splitted.first.getMillis());
                        } else {
                            HourlyUsage hourlyUsage = new HourlyUsage(lastUsage.first.getHourOfDay(), duration, count);
                            hourlyUsages[hourlyUsage.getHourNumber()] = hourlyUsage;
                            count = 1;
                            duration = (int) (splitted.second.getMillis() - splitted.first.getMillis());
                        }
                        lastUsage = splitted;
                    }
                }
            }

            if (count != 0) {
                HourlyUsage hourlyUsage = new HourlyUsage(lastUsage.first.getHourOfDay(), duration, count);
                hourlyUsages[hourlyUsage.getHourNumber()] = hourlyUsage;
            }
            return hourlyUsages;
        }

        private List<Pair<DateTime, DateTime>> getSplittedHours(Pair<DateTime, DateTime> startEnd) {
            List<Pair<DateTime, DateTime>> splittedHours = new ArrayList<>();
            if (startEnd.first.getHourOfDay() != startEnd.second.getHourOfDay()) {
                DateTime beginOfNextHour = new DateTime()
                        .withYear(startEnd.second.getYear())
                        .withMonthOfYear(startEnd.second.getMonthOfYear())
                        .withSecondOfMinute(0)
                        .withMillisOfSecond(0)
                        .withMinuteOfHour(0)
                        .withHourOfDay(startEnd.second.getHourOfDay())
                        .withDayOfMonth(startEnd.second.getDayOfMonth());
                splittedHours.add(new Pair<DateTime, DateTime>(startEnd.first, beginOfNextHour));
                splittedHours.add(new Pair<DateTime, DateTime>(beginOfNextHour, startEnd.second));
            } else {
                splittedHours.add(new Pair<DateTime, DateTime>(startEnd.first, startEnd.second));
            }
            return splittedHours;
        }
    }
}
