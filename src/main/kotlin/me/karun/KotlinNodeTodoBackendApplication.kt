package me.karun

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class KotlinNodeTodoBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinNodeTodoBackendApplication::class.java, *args)
}
