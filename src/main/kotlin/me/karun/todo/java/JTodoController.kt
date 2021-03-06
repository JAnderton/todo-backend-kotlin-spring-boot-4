package me.karun.todo.java

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*
import java.util.*

@CrossOrigin(maxAge = 1,
  methods = arrayOf(POST, GET, OPTIONS, DELETE, PATCH),
  allowedHeaders = arrayOf("x-requested-with", "origin", "Content-Type", "accept"))
@RestController
@RequestMapping(value = "/todos")
class JTodoController constructor(@Value("\${api.root}") val apiRoot:String) {

  private val todos = HashSet<JTodo>()

  @RequestMapping(method = arrayOf(GET))
  fun listAll(): HttpEntity<List<JResourceWithUrl<JTodo>>> {
    val resourceWithUrls = todos
      .map { toResource(it) }
      .toList()
    return ResponseEntity(resourceWithUrls, OK)
  }

  @RequestMapping(value = "/{todo-id}", method = arrayOf(GET))
  fun getTodo(@PathVariable("todo-id") id: Long): HttpEntity<JResourceWithUrl<JTodo>> {
    val todo = tryToFindById(id)

    return if (todo == null) notFound() else respondWithResource(todo, OK)
  }

  @RequestMapping(method = arrayOf(POST), headers = arrayOf("Content-type=application/json"))
  fun saveTodo(@RequestBody todo: JTodo?): HttpEntity<JResourceWithUrl<JTodo>> {
    if (todo == null) {
      return notFound()
    }

    todo.setId(todos.size + 1)
    todos.add(todo)

    return respondWithResource(todo, HttpStatus.CREATED)
  }

  @RequestMapping(method = arrayOf(DELETE))
  fun deleteAllTodos() = todos.clear()

  @RequestMapping(value = "/{todo-id}", method = arrayOf(DELETE))
  fun deleteOneTodo(@PathVariable("todo-id") id: Long) {
    val todo = tryToFindById(id)

    if (todo != null) {
      todos.remove(todo)
    }
  }

  @RequestMapping(value = "/{todo-id}", method = arrayOf(PATCH), headers = arrayOf("Content-type=application/json"))
  fun updateTodo(@PathVariable("todo-id") id: Long, @RequestBody newTodo: JTodo?): HttpEntity<JResourceWithUrl<JTodo>> {
    if (newTodo == null) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    val todo = tryToFindById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

    todos.remove(todo)

    val mergedTodo = todo.merge(newTodo)
    todos.add(mergedTodo)

    return respondWithResource(mergedTodo, OK)
  }

  private fun notFound() = ResponseEntity<JResourceWithUrl<JTodo>>(HttpStatus.NOT_FOUND)

  private fun tryToFindById(id: Long) = todos.filter { it.id == id }.firstOrNull()

  private fun getHref(todo: JTodo): String = apiRoot + todo.id

  private fun respondWithResource(todo: JTodo, statusCode: HttpStatus) = ResponseEntity(toResource(todo), statusCode)

  private fun toResource(todo: JTodo) = JResourceWithUrl(todo, getHref(todo))
}
