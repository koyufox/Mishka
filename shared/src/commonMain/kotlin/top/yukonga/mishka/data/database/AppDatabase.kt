package top.yukonga.mishka.data.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.TypeConverters
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

@Database(
    entities = [ImportedEntity::class, PendingEntity::class, SelectionEntity::class],
    version = 2,
)
@TypeConverters(ProfileTypeConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun importedDao(): ImportedDao
    abstract fun pendingDao(): PendingDao
    abstract fun selectionDao(): SelectionDao
}

// v2: 为 imported / pending 增加 userAgent 列（per-profile UA 覆写）
val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE imported ADD COLUMN userAgent TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE pending ADD COLUMN userAgent TEXT NOT NULL DEFAULT ''")
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
