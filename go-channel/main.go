package main

import (
	"fmt"
	"sync"
)

// Calculate number 1 - 500 by using channel and waitGroup

func cal(num int) int {
	var wg sync.WaitGroup
	var sum int = 0
	var split = 100

	tempSum := make(chan int)
	steps := num / split
	for i := range steps {
		wg.Add(1)
		go func(idx int) {
			s := 0
			defer wg.Done()
			for j := idx * split; j < (idx+1)*split; j++ {
				s += j
			}
			fmt.Println(idx, idx*split, (idx+1)*split, s)
			tempSum <- s
		}(i)
	}
	go func() {
		wg.Wait()
		close(tempSum)
	}()
	for s := range tempSum {
		sum += s
	}
	sum += num
	return sum
}

func main() {
	fmt.Println(cal(500))
}
