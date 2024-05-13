package main

import (
	"fmt"
	"time"
)

func work() {
	numTasks := 10
	worker := 3
	taskChannel := make(chan int, numTasks)
	resultChannel := make(chan int, numTasks)

	for i := 0; i < worker; i++ {
		idx := i
		go func(idx int, tasks <-chan int, results chan<- int) {
			fmt.Printf("Start worker %d\n", idx)
			for task := range tasks {
				fmt.Printf("In worker %d, process %d\n", idx, task)
				results <- task * 2
			}

		}(idx, taskChannel, resultChannel)
	}
	time.Sleep(2 * time.Second)

	for i := 0; i < numTasks; i++ {
		taskChannel <- i
	}
	close(taskChannel)

	for i := 0; i < numTasks; i++ {
		fmt.Println(<-resultChannel)
	}
}

func main() {
	work()
}
