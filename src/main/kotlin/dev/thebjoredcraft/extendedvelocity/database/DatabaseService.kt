package dev.thebjoredcraft.extendedvelocity.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.databaseConfig
import org.jetbrains.exposed.sql.Database
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.notExists

object DatabaseService {
    private lateinit var connection: Database

    lateinit var storageMethod: DatabaseStorageMethod
    lateinit var username: String
    lateinit var password: String
    lateinit var hostname: String
    lateinit var port: String
    lateinit var database: String

    fun load() {
        storageMethod = when(databaseConfig.string("storage-method").lowercase()) {
            "sqlite" -> DatabaseStorageMethod.SQLITE
            "mysql" -> DatabaseStorageMethod.MYSQL
            "mariadb" -> DatabaseStorageMethod.MARIADB
            else -> error("Unknown storage method '${databaseConfig.string("storage-method")}'.")
        }

        username = databaseConfig.string("authentication.username")
        password = databaseConfig.string("authentication.password")
        hostname = databaseConfig.string("authentication.host")
        port = databaseConfig.string("authentication.port")
        database = databaseConfig.string("authentication.database")
    }

    fun reload() {
        disconnect()
        load()
        connect()
    }

    fun connect() {
        if (::connection.isInitialized && !connection.connector().isClosed) {
            disconnect()
        }

        when (storageMethod) {
            DatabaseStorageMethod.SQLITE -> connectSqlite()
            DatabaseStorageMethod.MYSQL -> connectMariaDb()
            DatabaseStorageMethod.MARIADB -> connectMariaDb()
        }
    }

    private fun connectSqlite() {
        Class.forName("org.sqlite.JDBC")
        val dbFile = plugin.dataFolder / "storage.db"

        if (dbFile.notExists()) {
            dbFile.createFile()
        }

        Class.forName("org.sqlite.JDBC")

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = "jdbc:sqlite:${dbFile.toAbsolutePath()}"
            this.username = this@DatabaseService.username
            this.password = this@DatabaseService.password
            this.driverClassName = "org.sqlite.JDBC"
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        connection = Database.connect(HikariDataSource(hikariConfig))
        plugin.logger.info("Connected to SQLite database at ${dbFile.toAbsolutePath()}")
    }

    private fun connectMariaDb() {
        Class.forName("org.mariadb.jdbc.Driver")

        plugin.logger.info("Connecting to MariaDB database at ${hostname}:${port}/${database}")

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = "jdbc:mariadb://${hostname}:${port}/${database}"
            this.username = this@DatabaseService.username
            this.password = this@DatabaseService.password
            this.driverClassName = "org.mariadb.jdbc.Driver"
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        connection = Database.connect(HikariDataSource(hikariConfig))
        plugin.logger.info("Connected to MariaDB database at ${hostname}:${port}/${database}")
    }

    fun disconnect() {
        if (!::connection.isInitialized || connection.connector().isClosed) {
            return
        }

        connection.connector().close()
    }
}