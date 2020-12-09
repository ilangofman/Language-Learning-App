package com.example.lyngua.models.ResultLogs
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.lyngua.models.goals.Goal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

//@Parcelize
//data class Results(
//    val catId: Int, val catName: String, val epochTimestamp: Long, val wrongAnsMap: MutableMap<String,String>,
//    val numCorrect: Int, val numQuestions: Int) : Parcelable


@Parcelize
@Entity(tableName = "result_logs_database")
data class ResultLogs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")          val id:Int,
    @ColumnInfo(name="catId")        val catId: Int,
    @ColumnInfo(name="catName")    val catName:String,
    @ColumnInfo(name="epochTimestamp")   val epochTimestamp: Long,
    @ColumnInfo(name="numCorrect") var numCorrect: Int,
    @ColumnInfo(name="numQuestions") var numQuestions: Int,
): Parcelable


class ResultsTypeConverter {
    @TypeConverter
    fun toWrongAnsMap(jsonString: String?): MutableMap<String, String> {
        return Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, String>>() {}.type)
    }

    @TypeConverter
    fun fromWrongAnsMap(map: MutableMap<String, String>): String {
        return Gson().toJson(map)
    }
}