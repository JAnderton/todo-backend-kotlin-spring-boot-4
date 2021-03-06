package me.karun.todo.java;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JResourceWithUrl<T> {

  private T content;
  private String url;

  public JResourceWithUrl() {
  }

  public JResourceWithUrl(T content, String url) {
    this.content = content;
    this.url = url;
  }

  @JsonUnwrapped
  @XmlAnyElement
  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
