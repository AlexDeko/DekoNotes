package com.app.dekonotes.data.note

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// https://medium.com/@arzumanianartur0/parcelize-%D0%B8%D0%BB%D0%B8-%D0%BA%D0%B0%D0%BA-%D0%BE%D0%B4%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-%D1%83%D0%BF%D1%80%D0%BE%D1%89%D0%B0%D0%B5%D1%82-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%83-%D1%81-parcelable-%D0%B2-kotlin-440b6fbe3a7e
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