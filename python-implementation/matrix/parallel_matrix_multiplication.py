import threading
from concurrent.futures import ThreadPoolExecutor
import time
import numpy as np
 
# Taken from https://jamesmccaffrey.wordpress.com/2017/03/15/loading-a-text-file-into-a-python-matrix/
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


def multiplication(matrix_1, matrix_2, multiplication_result, row_index):
    for i in range(len(matrix_2[0])):
        for j in range(len(matrix_1)):
            multiplication_result[row_index][i] += matrix_1[row_index][j] * matrix_2[j][i]


def parallel_multiplication_with_thread_pool(matrix_1, matrix_2, multiplication_result, n, max_workers):
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        for i in range(n):
            executor.submit(multiplication, matrix_1, matrix_2, multiplication_result, i)
        executor.shutdown(wait=True)
    

def parallel_multiplication_without_thread_pool(matrix_1, matrix_2, multiplication_result):
    threads = list()

    for i in range(len(matrix_1[0])):
        x = threading.Thread(target = multiplication, args=(matrix_1, matrix_2, multiplication_result, i))
        threads.append(x)
        x.start()
    
    for _, thread in enumerate(threads):
        thread.join()


def main():
    n = 500
    max_workers = 8
    matrix_1 = load_matrix_from_file('java-implementation/matrix_0.txt')
    matrix_2 = load_matrix_from_file('java-implementation/matrix_1.txt')
    multiplication_result = np.random.randint(0, 1, (n, n))

    print('About to do some maths...')

    start = time.perf_counter()

    #parallel_multiplication_without_thread_pool(matrix_1, matrix_2, multiplication_result)
    parallel_multiplication_with_thread_pool(matrix_1, matrix_2, multiplication_result, n, max_workers)

    end = time.perf_counter()

    if(False):
        print(multiplication_result)
    
    print(f"Multiplication of {n}x{n} matrix: {round(end - start, 5)} seconds")


main()