package com.medcognize.event;


import com.medcognize.domain.Transaction;
import com.medcognize.domain.User;
import com.medcognize.view.DashboardViewType;

import java.util.Collection;

/*
 * Event bus events used in the application are listed here as inner classes.
 */
public abstract class MedcognizeEvent {

    public static final class UserLoginEvent {
        private final User u;

        public UserLoginEvent(final User u) {
            this.u = u;
        }

        public User getUser() {
            return u;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }

        public DashboardViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}