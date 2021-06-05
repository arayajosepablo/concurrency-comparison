package main

import (
	"encoding/csv"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"strconv"
	"strings"
	"sync"
	"time"
)

const matrix_size = 500
const processes = 8

var wg sync.WaitGroup

func newMatrix(r, c int) [][]int {
	a := make([]int, c*r)
	m := make([][]int, r)
	lo, hi := 0, c
	for i := range m {
		m[i] = a[lo:hi:hi]
		lo, hi = hi, hi+c
	}
	return m
}

func loadMatrixFromFile(matrix_path string) [][]int {
	matrix := newMatrix(matrix_size, matrix_size)

	content, err := ioutil.ReadFile(matrix_path)

	if err != nil {
		log.Fatal(err)
	}

	file := strings.NewReader(string(content))
	lines, _ := csv.NewReader(file).ReadAll()
	for i, line := range lines {
		for j, val := range line {
			val_int, err := strconv.Atoi(val)
			if err != nil {
				log.Fatal(err)
			}
			matrix[i][j] = val_int
		}
	}

	return matrix

}

func printMatrix(matrix [][]int) {
	if false {
		for i := 0; i < matrix_size; i++ {
			for j := 0; j < matrix_size; j++ {
				fmt.Print(fmt.Sprint(matrix[i][j]) + " ")
			}
			fmt.Println()
		}
		fmt.Println()
	}
}

func sequentialMatrixMultiplication(matrix_1, matrix_2 [][]int) [][]int {
	matrix_result := newMatrix(matrix_size, matrix_size)

	for i := 0; i < matrix_size; i++ {
		for j := 0; j < matrix_size; j++ {
			sum := 0
			for k := 0; k < matrix_size; k++ {
				sum = sum + matrix_1[i][k]*matrix_2[k][j]
			}
			matrix_result[i][j] = sum
		}

	}

	return matrix_result
}

func parallelMultiply(matrix_1, matrix_2, multiplication_result [][]int, index int) {
	for i := 0; i < matrix_size; i++ {
		for j := 0; j < matrix_size; j++ {
			multiplication_result[index][i] += matrix_1[index][j] * matrix_2[j][i]
		}
	}
}

func parallelWorkHandler(matrix_1, matrix_2, multiplication_result [][]int) {
	max_concurrent_goroutines := flag.Int("max_concurrent_goroutines", processes, "the number of goroutines that are allowed to run concurrently")
	number_of_jobs := flag.Int("number_of_jobs", matrix_size, "the number of jobs that we need to do")
	flag.Parse()

	concurrentGoroutines := make(chan struct{}, *max_concurrent_goroutines)

	for i := 0; i < *number_of_jobs; i++ {
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			concurrentGoroutines <- struct{}{}
			parallelMultiply(matrix_1, matrix_2, multiplication_result, i)
			<-concurrentGoroutines

		}(i)
	}
	wg.Wait()

}

func parallelWorkHandler_2(matrix_1, matrix_2, multiplication_result [][]int) {
	max_concurrent_goroutines := flag.Int("max_concurrent_goroutines", processes, "the number of goroutines that are allowed to run concurrently")
	number_of_jobs := flag.Int("number_of_jobs", matrix_size, "the number of jobs that we need to do")
	flag.Parse()

	// Dummy channel to coordinate the number of concurrent goroutines.
	// This channel should be buffered otherwise we will be immediately blocked
	// when trying to fill it.
	concurrentGoroutines := make(chan struct{}, *max_concurrent_goroutines)
	// Fill the dummy channel with max_concurrent_goroutines empty struct.
	for i := 0; i < *max_concurrent_goroutines; i++ {
		concurrentGoroutines <- struct{}{}
	}

	// The done channel indicates when a single goroutine has
	// finished its job.
	done := make(chan bool)
	// The waitForAllJobs channel allows the main program
	// to wait until we have indeed done all the jobs.
	waitForAllJobs := make(chan bool)

	// Collect all the jobs, and since the job is finished, we can
	// release another spot for a goroutine.
	go func() {
		for i := 0; i < *number_of_jobs; i++ {
			<-done
			// Say that another goroutine can now start.
			concurrentGoroutines <- struct{}{}
		}
		// We have collected all the jobs, the program
		// can now terminate
		waitForAllJobs <- true
	}()

	// Try to start number_of_jobs jobs
	for i := 0; i < *number_of_jobs; i++ {
		// Try to receive from the concurrentGoroutines channel. When we have something,
		// it means we can start a new goroutine because another one finished.
		// Otherwise, it will block the execution until an execution
		// spot is available.
		<-concurrentGoroutines
		go func(id int) {
			parallelMultiply(matrix_1, matrix_2, multiplication_result, id)
			done <- true
		}(i)
	}

	// Wait for all jobs to finish
	<-waitForAllJobs
}

func main() {
	matrix_1 := loadMatrixFromFile("/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/java-implementation/matrix_0.txt")
	matrix_2 := loadMatrixFromFile("/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/java-implementation/matrix_1.txt")
	matrix_result := newMatrix(matrix_size, matrix_size)

	printMatrix(matrix_1)
	printMatrix(matrix_2)

	fmt.Println("About to do some math...")
	start := time.Now()
	parallelWorkHandler_2(matrix_1, matrix_2, matrix_result)
	elapsed := time.Since(start)
	fmt.Printf("Multiplication of %dx%d matrix: %d milliseconds", matrix_size, matrix_size, elapsed.Milliseconds())

	printMatrix(matrix_result)

}
