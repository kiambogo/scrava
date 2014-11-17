package scrava.controllers

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by christopher on 14-11-04.
 */
object AutoPagination {
  def getAll[A, B](f: (Option[Int], Option[Int], Option[Int], Option[Int]) => Future[List[B]]): List[B] = {
    println("curry4")
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      Await.result(curry4(f)(None)(None)(Some(200))(Some(counter)), Duration(10, TimeUnit.SECONDS))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Option[Int], Option[Int], Option[Int]) => Future[List[B]], id: Option[Int]): List[B] = {
    println("curry2optId")
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      Await.result(curry2optId(f)(Some(200))(id)(Some(counter)), Duration(10, TimeUnit.SECONDS))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Int, Option[Int], Option[Int]) => Future[List[B]], id: Int): List[B] = {
    println("curry2id")
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      Await.result(curry2id(f)(Some(200))(id)(Some(counter)), Duration(10, TimeUnit.SECONDS))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def getAll[A, B](f: (Option[Int], Option[Int]) => Future[List[B]]): List[B] = {
    println("curry2")
    var counter = 0
    Iterator.continually {
      counter = counter + 1
      Await.result(curry2(f)(Some(200))(Some(counter)), Duration(10, TimeUnit.SECONDS))
    }.takeWhile(_.size != 0).toList.flatten
  }

  def curry4[A, B, C, E, D](f: (A, B, C, D) => E): A => B => D => C => E = {
    { (a: A) => { (b: B) => { (d: D) => { (c: C) => f(a, b, c, d) } } } }
  }

  def curry2id[A, B, C, D](f: (Int, B, C) => D): C => Int => B => D = {
    { (c: C) => { (a: Int) => { (b: B) => f(a, b, c) } } }
  }

  def curry2optId[A, B, C, D](f: (Option[Int], B, C) => D): C => Option[Int] => B => D = {
    { (c: C) => { (a: Option[Int]) => { (b: B) => f(a, b, c) } } }
  }

  def curry2[A, B, C](f: (A, B) => C): B => A => C = {
    { (b: B) => { (a: A) => f(a, b) } }
  }

}
