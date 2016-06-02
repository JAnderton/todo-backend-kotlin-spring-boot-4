package me.karun

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
class TodoController {

  @Value("\${api.root}")
  lateinit var apiRoot: String
  private val todos = HashSet<Todo>()

  @RequestMapping(method = arrayOf(GET))
  fun listAll(): HttpEntity<List<ResourceWithUrl<Todo>>> {
    val resourceWithUrls = todos
      .map { toResource(it) }
      .toList()
    return ResponseEntity(resourceWithUrls, OK)
  }

  @RequestMapping(value = "/{todo-id}", method = arrayOf(GET))
  fun getTodo(@PathVariable("todo-id") id: Long): HttpEntity<ResourceWithUrl<Todo>> {
    val todo = tryToFindById(id)

    return if (todo == null) notFound() else respondWithResource(todo, OK)
  }

  @RequestMapping(method = arrayOf(POST), headers = arrayOf("Content-type=application/json"))
  fun saveTodo(@RequestBody todo: Todo): HttpEntity<ResourceWithUrl<Todo>> {
    todo.setId(todos.size + 1)
    todos.add(todo)

    return respondWithResource(todo, HttpStatus.CREATED)
  }

  @RequestMapping(method = arrayOf(DELETE))
  fun deleteAllTodos() {
    todos.clear()
  }

  @RequestMapping(value = "/{todo-id}", method = arrayOf(DELETE))
  fun deleteOneTodo(@PathVariable("todo-id") id: Long) {
    val todo = tryToFindById(id)

    if (todo != null) {
      todos.remove(todo)
    }
  }

  @RequestMapping(value = "/{todo-id}", method = arrayOf(PATCH), headers = arrayOf("Content-type=application/json"))
  fun updateTodo(@PathVariable("todo-id") id: Long, @RequestBody newTodo: Todo?): HttpEntity<ResourceWithUrl<Todo>> {
    if (newTodo == null) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    val todo = tryToFindById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

    todos.remove(todo)

    val mergedTodo = todo.merge(newTodo)
    todos.add(mergedTodo)

    return respondWithResource(mergedTodo, OK)
  }

  private fun notFound() = ResponseEntity<ResourceWithUrl<Todo>>(HttpStatus.NOT_FOUND)

  private fun tryToFindById(id: Long) = todos.filter { it.id == id }.firstOrNull()

  private fun getHref(todo: Todo): String? {
    // Open CORS Bug: https://github.com/spring-projects/spring-hateoas/issues/222
//    return ControllerLinkBuilder.linkTo(
//      ControllerLinkBuilder.methodOn<TodoController>(this.javaClass)
//        .getTodo(todo.id))
//      .withSelfRel()
//      .href
    // Require better fix than using an environment variable
    return apiRoot + todo.id
  }

  private fun respondWithResource(todo: Todo, statusCode: HttpStatus) = ResponseEntity(toResource(todo), statusCode)

  private fun toResource(todo: Todo) = ResourceWithUrl(todo, getHref(todo))
}
