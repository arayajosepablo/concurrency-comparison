from concurrent.futures import ThreadPoolExecutor
import numpy as np
import time


def load_matrix_from_file(matrix_path):
    result_list = []
    f = open(matrix_path, 'r')
    for line in f:
        line = line.rstrip('\n') 
        string_value = line.split(',')
        int_value = list(map(np.int, string_value))
        result_list.append(int_value)
    f.close()

    print('Done loading ' + matrix_path)

    return result_list


def copyMatrix(world, new_world, n):
    for i in range(len(world[0])):
        for j in range(len(world)):
            world[i][j] = new_world[i][j]


def get_neighbour(row, col, world, n):
    if (row >= n or row < 0 or col >= n or col < 0):
        return 0

    return world[row][col]


def get_neighbours_count(row, col, world, n):
    count = 0

    count += get_neighbour(row-1, col-1, world, n)
    count += get_neighbour(row-1, col, world, n)
    count += get_neighbour(row-1, col+1, world, n)

    count += get_neighbour(row, col-1, world, n)
    count += get_neighbour(row, col+1, world, n)

    count += get_neighbour(row+1, col-1, world, n)
    count += get_neighbour(row+1, col, world, n)
    count += get_neighbour(row+1, col+1, world, n)

    return count


def rule(world, row, col, n):
    count = get_neighbours_count(row, col, world, n)
    birth = world[row][col] == 0 and count == 3
    survive = world[row][col] == 1 and (count == 2 or count == 3)
    if (birth or survive):
      return 1
    return 0


def parallel_apply_rules(world, new_world, n, row_index):
    for j in range(len(world)):
        new_world[row_index][j] = rule(world, row_index, j, n)


def parallel_game(world, new_world, n, max_workers):
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        for i in range(n):
            executor.submit(parallel_apply_rules, world, new_world, n, i)
        executor.shutdown(wait=True)
      
    
def print_matrix(matrix):
    if True:
        print(matrix)


def main():
    max_workers = 8
    n = 10
    m = 10

    world = load_matrix_from_file('resources/test_life_1.txt')
    new_world = np.zeros((n, n)).astype(int)
    print_matrix(world)

    start = time.perf_counter()
    for i in range(m):
        parallel_game(world, new_world, n, max_workers)
        copyMatrix(world, new_world, n)
    end = time.perf_counter()
    
    print_matrix(world)

    print(f"Sequential game of life took: {round(end - start, 5)} seconds")

main()