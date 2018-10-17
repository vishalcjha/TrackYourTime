package com.outofbox.utility;

import com.outofbox.model.UsageEntity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/*
    SampleUsageEntityData -  this is helper data for UsageEntity.
    restWindow - there is no entry in this window. By default it is from 22PM to 6AM.
    peakWindow - there is 2x more event in this window than normal window.
    In each window, event every hour differ by count of 1, with first window hour being 1 more than second.
 */
public class SampleUsageEntityData {
    private final int numberOfDays;
    private final int numberOfEventEveryDay;
    //private final int []restHours;
    private final List<Integer> peakHours;
    private final List<Integer> restHours;
    private SampleUsageEntityData(final int numberOfDays,
                                  final int numberOfEventEveryDay,
                                  final int[] restWindow,
                                  final int[] peakWindow){
        this.numberOfDays = numberOfDays;
        this.numberOfEventEveryDay = numberOfEventEveryDay;
        this.peakHours = toHours(peakWindow);
        this.restHours = toHours(restWindow);
    }
    private List<Integer> toHours(int []window) {
        int begin = window[0];
        int end = window[1];
        List<Integer> hours = new ArrayList<>();
        while (begin < end || begin <=24 ) {
            hours.add(begin);
            begin++;
            if (begin == 24) {
                begin = 0;
            }
        }
        Collections.sort(hours);
        return hours;
    }
    public List<UsageEntity> getSampleEntities() {

        return null;
    }

    public static List<UsageEntity> getPreFilledSampleEntities(int numOfEventDays) {
        List<UsageEntity> usageEntities = new ArrayList<>();
        DateTime endDate = new DateTime(System.currentTimeMillis());
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        DateTime beginDate = new DateTime(endDate.getMillis() - (numOfEventDays * DAY_IN_MS));

        long MIN_IN_MS = 1000 * 60;
        long id = 1;
        while (beginDate.getMillis() < endDate.getMillis()) {
            int hourOfDay = beginDate.getHourOfDay();
            DateTime endDateOfThisWindow = new DateTime(beginDate.getMillis() + getNextTickValue(hourOfDay) * MIN_IN_MS);
            if ( hourOfDay < 22 && hourOfDay > 7) {
                usageEntities.add(new UsageEntity(id++, beginDate, endDateOfThisWindow));
            }
            beginDate = endDateOfThisWindow;
        }
        return usageEntities;
    }

    private static int getNextTickValue(int currentHour) {
        switch (currentHour) {
            case 9: return 15;
            case 10: return 15;
            case 11: return 10;
            case 12: return 5;
            case 13: return 8;
            case 14: return 20;
            case 15: return 20;
            case 16: return 25;
            case 17: return 5;
            default: return 10;
        }
    }

    private boolean isRestAndPeakOverlapped() {
        return Collections.disjoint(peakHours, restHours);
    }

    public static class Builder {
        private static final String NOT_VALID_INPUT = "Not Valid Input";
        private int numberOfDays;
        private int numberOfEventEveryDay;
        private int []restWindow;
        private int []peakWindow;

        public Builder setNumberOfDays(int numberOfDays) {
            this.numberOfDays = numberOfDays;
            return this;
        }

        public Builder setNumberOfEventEveryDay(int numberOfEventEveryDay) {
            this.numberOfEventEveryDay = numberOfEventEveryDay;
            return this;
        }

        public Builder setRestWindow(int[] restWindow) {
            isValidWindow(restWindow);
            this.restWindow = restWindow;
            return this;
        }

        public Builder setPeakWindow(int[] peakWindow) {
            isValidWindow(peakWindow);
            this.peakWindow = peakWindow;
            return this;
        }

        private void isValidWindow(int []window) {
            boolean isValid = true;
            if (window.length != 2) {
                isValid = false;
            }

            for (int element : window) {
                if (element < 0 || element > 24) {
                    isValid = false;
                }
            }
            if (!isValid) {
                throw new RuntimeException(NOT_VALID_INPUT);
            }
        }

        public Builder(int numberOfDays, int numberOfEventEveryDay){
            this.numberOfDays = numberOfDays;
            this.numberOfEventEveryDay = numberOfEventEveryDay;
            restWindow = new int[] {22, 6};
            peakWindow = new int[] {13, 22};
        }

        public SampleUsageEntityData build() {
            return new SampleUsageEntityData(numberOfDays,
                    numberOfEventEveryDay,
                    restWindow,
                    peakWindow);
        }
    }
}
