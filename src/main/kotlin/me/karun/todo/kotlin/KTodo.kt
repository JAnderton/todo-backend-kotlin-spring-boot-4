package me.karun.todo.kotlin

data class KTodo(var id: Long = 0, val title: String, val completed: Boolean = false, val order: Int = 0) {
  fun merge(updatedTodo: KTodo): KTodo {
    return KTodo(id,
      updatedTodo.title,
      completed || updatedTodo.completed,
      if (updatedTodo.order != 0) updatedTodo.order else order)
  }

  override fun hashCode(): Int = id.toInt()
}