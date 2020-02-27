package com.app.dekonotes.data.note

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Parcelize
data class Note(
	@PrimaryKey(autoGenerate = true)
	val id: Long,
	val title: String?,
	val text: String?,
	val check: Boolean,
	@ColumnInfo(name = "deadline")
	val dayDeadline: Long,
	@ColumnInfo(name = "last_change")
	val lastChange: Long,
	@ColumnInfo(name = "contains_deadline")
	val containsDeadline: Int
) : Parcelable
