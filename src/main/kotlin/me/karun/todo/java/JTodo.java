package me.karun.todo.java;

public class JTodo {

  private int id;
  private String title;
  private Boolean completed;
  private Integer order;

  public JTodo() {
  }

  public JTodo(String title) {
    this.title = title;
  }

  public JTodo(int id, String title, Boolean completed, Integer order) {
    this.id = id;
    this.title = title;
    this.completed = completed;
    this.order = order;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JTodo JTodo = (JTodo) o;

    if (id != JTodo.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id;
  }

  public boolean isCompleted() {
    return nonNull(completed, false);
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public int getOrder() {
    return nonNull(order, 0);
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public JTodo merge(JTodo newJTodo) {
    return new JTodo(id,
      nonNull(newJTodo.title, title),
      nonNull(newJTodo.completed, completed),
      nonNull(newJTodo.order, order));
  }

  public long getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  private <T> T nonNull(T value, T defaultValue) {
    return value == null ? defaultValue : value;
  }
}
