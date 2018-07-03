package models.dao

import anorm._
import models.FavouriteStudio
import play.api.db._
import anorm.SqlParser.get

object FavouriteStudioDAO {

  val database = Databases(
    driver = "com.mysql.jdbc.Driver",
    url = "jdbc:mysql://localhost:3306/mysql",
    name = "mysql",
    config = Map(
      "user" -> "root",
      "password" -> "password")
  )

  def create(favouriteStudio: FavouriteStudio) = {
    database.withConnection { implicit c =>
      val id: Option[Long] = SQL("INSERT IGNORE INTO `favouriteStudio` (`userId`, `studioId`)  VALUES({userId},  {studioId})")
        .on('userId -> favouriteStudio.userId, 'studioId -> favouriteStudio.studioId)
        .executeInsert()
    }
  }


  def delete(favouriteStudio: FavouriteStudio) = {
    database.withConnection { implicit c =>
      SQL("DELETE FROM `favouriteStudio` WHERE `userId`={userId} AND `studioId`={studioId}")
        .on('userId -> favouriteStudio.userId, 'studioId -> favouriteStudio.studioId)
        .executeUpdate()
    }
  }

  def exists(favouriteStudio: FavouriteStudio) = {
    database.withConnection { implicit c =>
      val result = SQL("SELECT COUNT(*) AS numMatches FROM `favouriteStudio` WHERE `userId`={userId} AND `studioId`={studioId};")
        .on('userId -> favouriteStudio.userId, 'studioId -> favouriteStudio.studioId)
        .as(SqlParser.int("numMatches").single)
      result != 0
    }
  }

  def index(userId: Int) = {
    database.withConnection { implicit c =>
      val rowParser: RowParser[FavouriteStudio] = {
        get[Int]("userId") ~
          get[Int]("studioId") map {
          case userId~studioId => FavouriteStudio(userId, studioId)
        }
      }
      SQL("SELECT `userId`, `studioId` FROM `favouriteStudio` WHERE `userId`={userId}")
        .on('userId -> userId)
        .as(rowParser *)
    }
  }
}
