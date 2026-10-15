package com.epms.dao;
/** Contract for entities that can receive notifications. */
public interface Notifiable { void notifyUser(String message); }
