package com.example.gestor_de_tareas.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {
    //@Query("SELECT * FROM calendar_events WHERE date(startDate / 1000, 'unixepoch') <= date(:date / 1000, 'unixepoch') AND date(endDate / 1000, 'unixepoch') >= date(:date / 1000, 'unixepoch')")
    //fun getEventsForDate(date: Long): Flow<List<CalendarEvent>>
   // @Query("SELECT * FROM calendar_events WHERE date(:date / 1000, 'unixepoch') BETWEEN date(startDate / 1000, 'unixepoch') AND date(endDate / 1000, 'unixepoch')")
    @Query("SELECT * FROM calendar_events WHERE date(startDate / 1000, 'unixepoch', 'localtime') <= date(:date / 1000, 'unixepoch', 'localtime') AND date(endDate / 1000, 'unixepoch', 'localtime') >= date(:date / 1000, 'unixepoch', 'localtime')")
    fun getEventsForDate(date: Long): Flow<List<CalendarEvent>>

    @Insert
    suspend fun insertEvent(event: CalendarEvent): Long

    @Update
    suspend fun updateEvent(event: CalendarEvent)
    @Delete
    suspend fun deleteEvent(event: CalendarEvent)

    @Query("SELECT * FROM calendar_events WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<CalendarEvent?>

    @Query("DELETE FROM calendar_events")
    suspend fun deleteAllEvents()
}