package io.github.konradmalik.cassandra

import scala.collection.JavaConverters._

import com.datastax.driver.core._
import io.github.konradmalik.config.CassandraConfig
import io.github.konradmalik.util.StringUtil
import io.github.konradmalik.util.Using
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
  * Provides cassandra-related methods and values to the scope of the class that mixes-in this trait.
  */
trait CassandraEnv extends CassandraConfig {

  import CassandraEnv._

  /**
    * Gets all keyspaces
    *
    * @return Existing keyspaces with the built-in filtered out ([[KEYSPACES_TO_OMIT]]
    */
  final def getKeyspaces: Seq[String] = {
    withCassandraCluster { cluster =>
      val metadata: Metadata = cluster.getMetadata
      val set = metadata.getKeyspaces.asScala
        .map(_.getName)
        .toSet
        .diff(KEYSPACES_TO_OMIT)
      set.toSeq.sorted
    }
  }

  /**
    * Executes provided function on a cassandra session which is later closed/released.
    *
    * @param f function to execute
    * @tparam T Type returned from the function
    * @return Returns whatever {f} returns
    */
  final def withCassandraSession[T](f: Session => T): T = {
    withCassandraCluster { cluster =>
      Using.withResources(createSession(cluster))(f)
    }
  }

  /**
    * Executes provided function on a cassandra cluster which is later released.
    *
    * @param f function to execute
    * @tparam T Type returned from the function
    * @return Returns whatever {f} returns
    */
  final def withCassandraCluster[T](f: Cluster => T): T =
    Using.withResources(createCluster)(f)

  /**
    * Creates independent [[Session]] instance to be used without "with..." pattern
    *
    * @return
    */
  final def sessionInstance: Session = createSession(createCluster)

  /**
    * Initializes a role with zero permissions
    *
    * @param name    Name of the role
    * @param session Session to be used
    * @return [[ResultSet]]
    */
  final def initializeRole(
      name: String
  )(implicit session: Session): ResultSet = {
    assert(
      StringUtil.isAlphanumeric(name),
      s"only alphanumeric characters allowed for roles! was $name"
    )
    session.execute(initializeRoleQuery(name))
  }

  /**
    * Query to be used as a role initializer
    *
    * @param name Role name
    * @return Query
    */
  private final def initializeRoleQuery(name: String): String =
    "create ROLE if not exists " + name

  /**
    * Assign admin permissions to the specified keyspace
    *
    * @param role     Role name
    * @param keyspace keyspace to assign admin rights to
    * @param session  Session to be used
    * @return [[ResultSet]]
    */
  final def assignAdminRightsTo(role: String, keyspace: String)(
      implicit session: Session
  ): ResultSet = {
    assert(
      StringUtil.isAlphanumeric(keyspace),
      s"only alphanumeric characters allowed for keyspaces! was $keyspace"
    )
    session.execute(assignAdminRightsToQuery(role, keyspace))
  }

  /**
    * Query to be used to assign admin permissions to role on keyspace
    *
    * @param role     Role name
    * @param keyspace keyspace to assign admin rights to
    * @return Query
    */
  private final def assignAdminRightsToQuery(
      role: String,
      keyspace: String
  ): String =
    s"GRANT ALL PERMISSIONS on KEYSPACE $keyspace to $role"

  /**
    * Initializes keyspace in Cassandra
    *
    * @param keyspace    Name of the keyspace
    * @param replication Keyspace's replication factor
    * @param session     Session to be used
    * @return [[ResultSet]]
    */
  final def initializeKeyspace(
      keyspace: String,
      replication: Int = CASSANDRA_REPLICATION
  )(implicit session: Session): ResultSet = {
    // prepare cassandra keyspace
    session.execute(initializeKeyspaceQuery(keyspace, replication))
  }

  /**
    * Query to be used as a keyspace initializer
    *
    * @param keyspace    Name of the keyspace
    * @param replication Keyspace's replication factor
    * @return Query
    */
  private final def initializeKeyspaceQuery(
      keyspace: String,
      replication: Int = CASSANDRA_REPLICATION
  ): String =
    "create KEYSPACE if not exists " + keyspace + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': " + replication + "} AND durable_writes = 'true'"
}

/**
  * Companion object containing static methods for [[CassandraEnv]] trait
  */
private object CassandraEnv extends CassandraConfig {

  protected val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
    * Creates a Cassandra cluster instance based on provided configuration in an 'conf' file.
    *
    * @return
    */
  private def createCluster = {
    logger.debug(s"Cassandra database creating cluster at $CASSANDRA_HOST.")
    Cluster
      .builder()
      .withoutMetrics()
      .addContactPoint(CASSANDRA_HOST)
      .withCredentials(CASSANDRA_USERNAME.trim(), CASSANDRA_PASSWORD.trim())
      .withPort(CASSANDRA_PORT)
      .build()
  }

  /**
    * Creates new (legacy-api) based session based on the existent configuration
    *
    * @param cluster Cassandra Cluster instance
    * @return Session instance
    */
  private def createSession(cluster: Cluster): Session = {
    logger.debug(s"Cassandra database creating session at $CASSANDRA_HOST.")
    cluster.connect()
  }

}
