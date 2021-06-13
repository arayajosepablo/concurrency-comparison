package main

import (
	"encoding/csv"
	"fmt"
	"io/ioutil"
	"log"
	"strconv"
	"strings"
	"time"
)

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

func loadMatrixFromFile(matrix_path string, n int) [][]int {
	matrix := newMatrix(n, n)

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

func printMatrix(matrix [][]int, n int) {
	if true {
		for i := 0; i < n; i++ {
			for j := 0; j < n; j++ {
				fmt.Print(fmt.Sprint(matrix[i][j]) + " ")
			}
			fmt.Println()
		}
		fmt.Println()
	}
}

func copyMatrix(world [][]int, newWorld [][]int, n int) {
	for i := 0; i < n; i++ {
		for j := 0; j < n; j++ {
			world[i][j] = newWorld[i][j]
		}
	}
}

func getNeighbour(row int, col int, world [][]int, n int) int {
	// If a neighbour of a cell is outside the limits of the world it is considered dead.
	if row >= n || row < 0 || col >= n || col < 0 {
		return 0
	}

	return world[row][col]
}

func getNeighboursCount(row int, col int, world [][]int, n int) int {
	count := 0

	count += getNeighbour(row-1, col-1, world, n)
	count += getNeighbour(row-1, col, world, n)
	count += getNeighbour(row-1, col+1, world, n)

	count += getNeighbour(row, col-1, world, n)
	count += getNeighbour(row, col+1, world, n)

	count += getNeighbour(row+1, col-1, world, n)
	count += getNeighbour(row+1, col, world, n)
	count += getNeighbour(row+1, col+1, world, n)

	return count
}

func rule(world [][]int, row int, col int, n int) int {
	count := getNeighboursCount(row, col, world, n)
	birth := world[row][col] == 0 && count == 3
	survive := world[row][col] == 1 && (count == 2 || count == 3)
	if birth || survive {
		return 1
	}
	return 0
}

func applyRules(world [][]int, newWorld [][]int, n int) {
	for i := 0; i < n; i++ {
		for j := 0; j < n; j++ {
			newWorld[i][j] = rule(world, i, j, n)
		}
	}
}

func main() {
	n := 7
	m := 800000

	world := loadMatrixFromFile("resources/infinite_growth_1.txt", n)
	newWorld := newMatrix(n, n)

	fmt.Println("World")
	printMatrix(world, n)
	start := time.Now()

	for i := 0; i < m; i++ {
		applyRules(world, newWorld, n)
		copyMatrix(world, newWorld, n)
		// printMatrix(world, n)
	}

	fmt.Println("Last generation")
	printMatrix(world, n)

	elapsed := time.Since(start)
	fmt.Printf("Sequential game of life took: %d milliseconds", elapsed.Milliseconds())
}
