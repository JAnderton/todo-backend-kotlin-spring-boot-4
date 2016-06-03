package me.karun.todo.kotlin

import com.fasterxml.jackson.annotation.JsonUnwrapped
import javax.xml.bind.annotation.XmlAnyElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class KResourceWithUrl<T>(@JsonUnwrapped @XmlAnyElement val content: T, val url: String)


