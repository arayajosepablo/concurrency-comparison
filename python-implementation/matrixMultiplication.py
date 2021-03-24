import threading
import time
import numpy as np
 
n = 100
matrix_1 = np.random.randint(0, 1000, (n, n))
matrix_2 = np.random.randint(0, 1000, (n, n))
 
def multiplication(matrix_1, matrix_2):
   result = [[0]*n]
   for i in range(len(matrix_2[0])):
       for j in range(len(matrix_1)):
           result[0][i] += matrix_1[0] * matrix_2[j][i]
 
threads = list()
start = time.perf_counter()
for i in range(len(matrix_1[0])):
   x = threading.Thread(target = multiplication, args=(matrix_1[i], matrix_2))
   threads.append(x)
   x.start()

for index, thread in enumerate(threads):
   thread.join()
end = time.perf_counter()
 
print(f"Multiplication of {n}X{n} matrix: {round(end - start, 5)} seconds")