package com.knoldus

import java.sql.{Connection, DriverManager, SQLIntegrityConstraintViolationException}
import scala.collection.mutable.ListBuffer
import scala.io.StdIn


object ScalaJdbcConnectSelect extends App {
  // connect to the database named "mysql" on port 8889 of localhost
  private val url = "jdbc:mysql://localhost/employee"
  private val driver = "com.mysql.cj.jdbc.Driver"
  private val username = "root"
  private val password = "Knoldus@12345"
  private var connection: Connection = _

  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement


    println("--- EMPLOYEE TABLE ---")
    println("1. For Insertion in table ")
    println("2. For Update in table")
    println("3. For Deletion in table")
    println("4. To view records in table")
    val choice = StdIn.readInt()

    choice match {
      case 1 => insertData()
      case 2 => updation()
      case 3 => deletion()
      case 4 => viewData()
      case _ => println(s"Invalid input")
    }


    def insertData(): Unit = {
      try {
        val previousIdQuery = "SELECT MAX(id) FROM Emp"
        val previousIdResult = statement.executeQuery(previousIdQuery)
        var previousId: Int = 0
        if (previousIdResult.next()) {
          previousId = previousIdResult.getInt(1)
        }
        println(s"Enter Employee Name for Id: ${previousId + 1}")
        val empName = StdIn.readLine()
        println("Enter city ")
        val cityEmp = StdIn.readLine()
        println(s"Enter salary for employee ")
        val salaryEmp = StdIn.readInt()
        val insert = s"INSERT INTO Emp( name, city, salary) VALUES ('$empName', '$cityEmp', $salaryEmp)"
        val rowsAffected = statement.executeUpdate(insert)

        if (rowsAffected > 0) {
          println("Record inserted successfully!")
        } else {
          println("No records inserted...")
        }
      } catch {
        case ex: NumberFormatException => println("Enter valid input..." + ex.getMessage)
        case duplicate: SQLIntegrityConstraintViolationException => println("Trying to insert duplicate ID " + duplicate.getMessage)
      }
    }

    def updation(): Unit = {
      val totalId = statement.executeQuery("SELECT id FROM Emp")
      val employeeIds = ListBuffer[Int]()
      while (totalId.next()) {
        val employeeId = totalId.getInt("id")
        employeeIds += employeeId
      }
      println("Enter Employee id for Updation")
      val empId = StdIn.readInt()
      if (employeeIds.contains(empId)) {
        println("Enter choice to update like name, city , salary")
        val choiceUpdate = StdIn.readLine()
        println(s"Enter new $choiceUpdate to update")
        val updatedValue = StdIn.readLine()
        val update = s"UPDATE Emp SET $choiceUpdate = '$updatedValue' WHERE id = $empId"
        val preparedStatement = connection.prepareStatement(update)
        val rowsAffected = preparedStatement.executeUpdate()

        if (rowsAffected > 0) {
          println("Record updated successfully!")
        } else {
          println("No records updated.")
        }
      }
      else {
        println("Invalid Employee Id")
      }
    }


    def deletion(): Unit = {
      val totalId = statement.executeQuery("SELECT id FROM Emp")
      val employeeIds = ListBuffer[Int]()
      while (totalId.next()) {
        val employeeId = totalId.getInt("id")
        employeeIds += employeeId
      }
      println("Enter Employee Id to delete from database")
      val empId = StdIn.readInt()
      if (employeeIds.contains(empId)) {
        val delete = s"DELETE FROM Emp WHERE id = '$empId'"
        val preparableStatement = connection.prepareStatement(delete)
        val rowsAffected = preparableStatement.executeUpdate()
        if (rowsAffected > 0)
          println("Record Deleted...")
        else
          println("Record Not Deleted...")
      }
      else {
        println("Employee Id not found in DB...")
      }
    }

    def viewData(): Unit = {
      val rs = statement.executeQuery("SELECT * FROM Emp")
      while (rs.next) {
        val id = rs.getInt("id")
        val name = rs.getString("name")
        val address = rs.getString("city")
        val salary = rs.getString("salary")
        println("id: %s :: name: %s, address: %s, salary: %s".format(id, name, address, salary))
      }
    }
  }
  catch {
    case e: Exception => println(e.getMessage)
  }

}