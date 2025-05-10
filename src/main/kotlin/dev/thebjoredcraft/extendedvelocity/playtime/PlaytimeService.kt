package dev.thebjoredcraft.extendedvelocity.playtime

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

object PlaytimeService {
    object Playtime: Table() {
        val uuid = varchar("uuid", 36).transform({ UUID.fromString(it) }, { it.toString() })
        val username = text("name")
        val playtime = integer("playtime")
        val lastSeen = long("last_seen")
        val firstSeen = long("first_seen")

        override val primaryKey = PrimaryKey(uuid)
    }

    object UuidMechanics {
        suspend fun updateFirstSeen(uuid: UUID, username: String) = withContext(Dispatchers.IO){
            newSuspendedTransaction {
                if(this@UuidMechanics.isFirstSeen(uuid)) {
                    Playtime.insert {
                        it[Playtime.uuid] = uuid
                        it[Playtime.username] = username
                        it[Playtime.firstSeen] = System.currentTimeMillis()
                        it[Playtime.lastSeen] = System.currentTimeMillis()
                        it[Playtime.playtime] = 0
                    }
                } else {
                    Playtime.update({ Playtime.uuid eq uuid }) {
                        it[firstSeen] = System.currentTimeMillis()
                    }
                }
            }
        }

        suspend fun getFirstSeen(uuid: UUID): Long? = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.uuid eq uuid).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction null
                }

                result[Playtime.firstSeen]
            }
        }

        suspend fun isFirstSeen(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.uuid eq uuid).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction true
                }

                false
            }
        }

        suspend fun updateLastSeen(uuid: UUID) = withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Playtime.update({ Playtime.uuid eq uuid }) {
                    it[lastSeen] = System.currentTimeMillis()
                }
            }
        }

        suspend fun getLastSeen(uuid: UUID): Long? = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.uuid eq uuid).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction null
                }

                result[Playtime.lastSeen]
            }
        }
    }

    object NameMechanics {
        suspend fun updateFirstSeen(username: String, uuid: UUID) = withContext(Dispatchers.IO){
            newSuspendedTransaction {
                if(this@NameMechanics.isFirstSeen(username)) {
                    Playtime.insert {
                        it[Playtime.username] = username
                        it[Playtime.uuid] = uuid
                        it[Playtime.firstSeen] = System.currentTimeMillis()
                        it[Playtime.lastSeen] = System.currentTimeMillis()
                        it[Playtime.playtime] = 0
                    }
                } else {
                    Playtime.update({ Playtime.username eq username }) {
                        it[firstSeen] = System.currentTimeMillis()
                    }
                }
            }
        }

        suspend fun getFirstSeen(username: String): Long? = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.username eq username).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction null
                }

                result[Playtime.firstSeen]
            }
        }

        suspend fun isFirstSeen(username: String): Boolean = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.username eq username).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction true
                }

                false
            }
        }

        suspend fun updateLastSeen(username: String) = withContext(Dispatchers.IO){
            newSuspendedTransaction {
                Playtime.update({ Playtime.username eq username }) {
                    it[lastSeen] = System.currentTimeMillis()
                }
            }
        }

        suspend fun getLastSeen(username: String): Long? = withContext(Dispatchers.IO) {
            return@withContext newSuspendedTransaction {
                val result = Playtime.selectAll().where(Playtime.username eq username).firstOrNull()

                if(result == null) {
                    return@newSuspendedTransaction null
                }

                result[Playtime.lastSeen]
            }
        }
    }
}