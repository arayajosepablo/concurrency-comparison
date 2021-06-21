package main

import (
	"encoding/csv"
	"io/ioutil"
	"log"
	"strconv"
	"strings"
	"sync"
	"sync/atomic"
	"time"
)

var concurrent_thread_count int32
var _thread_amount int32 = 32

func ParallelQuicksort(arr *[]int, left, right int) {
	var wg sync.WaitGroup
	atomic.AddInt32(&concurrent_thread_count, 1)
	wg.Add(1)
	go parallelQuicksort(arr, left, right, &wg)
	wg.Wait()
	atomic.AddInt32(&concurrent_thread_count, -1)
}

func parallelQuicksort(arr *[]int, left, right int, pwg *sync.WaitGroup) {
	defer pwg.Done()

	var wg sync.WaitGroup
	piv := partition(arr, left, right)
	if left < piv-1 {
		wg.Add(1)
		if atomic.LoadInt32(&concurrent_thread_count) <= _thread_amount {
			atomic.AddInt32(&concurrent_thread_count, 1)
			go parallelQuicksort(arr, left, piv-1, &wg)
			atomic.AddInt32(&concurrent_thread_count, -1)
		} else {
			parallelQuicksort(arr, left, piv-1, &wg)
		}
	}
	if piv < right {
		wg.Add(1)
		if atomic.LoadInt32(&concurrent_thread_count) <= _thread_amount {
			atomic.AddInt32(&concurrent_thread_count, 1)
			go parallelQuicksort(arr, piv, right, &wg)
			atomic.AddInt32(&concurrent_thread_count, -1)
		} else {
			parallelQuicksort(arr, piv, right, &wg)
		}
	}
	wg.Wait()
}

func quickSort(values []int, left int, right int) {
	if left < right {

		temp := values[left]

		i, j := left, right
		for {
			for values[j] >= temp && i < j {
				j--
			}

			for values[i] <= temp && i < j {
				i++
			}

			if i >= j {
				break
			}

			// Exchange the values ​​on the left and right sides
			values[i], values[j] = values[j], values[i]
		}

		values[left] = values[i]
		values[i] = temp

		// recursive, sorted separately on the left and right sides
		quickSort(values, left, i-1)
		quickSort(values, i+1, right)
	}
}

func partition(arr *[]int, left, right int) int {
	piv := (*arr)[(left+right)/2]

	for left <= right {
		for (*arr)[left] < piv {
			left++
		}
		for (*arr)[right] > piv {
			right--
		}
		if left <= right {
			// swap
			t := (*arr)[left]
			(*arr)[left] = (*arr)[right]
			(*arr)[right] = t

			left++
			right--
		}
	}

	return left
}

func loadArrayFromFile(filePath string) []int {
	array := make([]int, 0)

	content, err := ioutil.ReadFile(filePath)

	if err != nil {
		log.Fatal(err)
	}

	file := strings.NewReader(string(content))
	lines, _ := csv.NewReader(file).ReadAll()

	for _, line := range lines {
		for _, val := range line {
			val_int, err := strconv.Atoi(val)
			if err != nil {
				log.Fatal(err)
			}
			array = append(array, val_int)
		}
	}

	return array

}

func isSorted(array []int) bool {
	for i := range array {
		if i == 0 {
			continue
		}
		if array[i-1] > array[i] {
			log.Fatal("Didn't sort correctly.")
			return false
		}
	}
	return true
}

func main() {

	log.Println("Loading array in memory...")
	array := loadArrayFromFile("/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/java-implementation/arrayToSort.txt")
	log.Println("Array was loaded.")

	log.Println("About to do some sequencial sorting...")
	begin := time.Now()
	quickSort(array, 0, len(array)-1)
	end := time.Now()
	isSorted(array)
	log.Println("Sequencial sorting finished in", end.Sub(begin))

	// Cooling dowm
	time.Sleep(3 * time.Second)

	log.Println("Loading array in memory...")
	array = loadArrayFromFile("/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/java-implementation/arrayToSort.txt")
	log.Println("Array was loaded.")

	log.Println("About to do some parallel sorting...")
	begin = time.Now()
	ParallelQuicksort(&array, 0, len(array)-1)
	end = time.Now()
	isSorted(array)
	log.Println("Parallel sorting finished in", end.Sub(begin))
}
