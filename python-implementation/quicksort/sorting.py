'''
Based on this code: http://eg.bucknell.edu/~jvs008/SC13/codes/parallelQuicksort.py
'''

import random, time, sys
from multiprocessing import Process, Pipe
import numpy as np
import csv

def main():

    # We want to sort the same list, so make a backup.
    lystbck = randomlist = random.sample(range(0, 10000000), 10000000)
    # lystbck = load_array_from_file("/Users/pablo/Documents/Ideas_para_paper-Concurrencia/Repositorios/concurrency-comparison/java-implementation/arrayToSort.txt")

    print("List size: ", len(lystbck))

    #Sequential quicksort a copy of the list.
    lyst = list(lystbck)            #copy the list
    start = time.time()             #start time
    lyst = quicksort(lyst)          #quicksort the list
    elapsed = time.time() - start   #stop time
    
    if not is_sorted(lyst):
        print('quicksort did not sort the lyst. oops.')
        
    print('Sequential quicksort: %f sec' % (elapsed))

    #So that cpu usage shows a lull.
    time.sleep(3)
    
    #Parallel quicksort.
    lyst = list(lystbck)        
    
    start = time.time()
    n = 8 

    #Instantiate a Pipe so that we can receive the
    #process's response.
    pconn, cconn = Pipe()
    
    #Instantiate a process that executes quicksortParallel
    #on the entire list.
    p = Process(target=parallel_quicksort, \
                args=(lyst, cconn, n))
    p.start()
    
    lyst = pconn.recv()
    #Blocks until there is something (the sorted list)
    #to receive.
    
    p.join()
    elapsed = time.time() - start

    if not is_sorted(lyst):
        print('quicksortParallel did not sort the lyst. oops.')

    print('Parallel quicksort: %f sec' % (elapsed))


    time.sleep(3)
    
    #Built-in test.
    lyst = list(lystbck)
    start = time.time()
    lyst = sorted(lyst)
    elapsed = time.time() - start
    print('Built-in sorted: %f sec' % (elapsed))

    
def quicksort(lyst):
    if len(lyst) <= 1:
        return lyst
    pivot = lyst.pop(random.randint(0, len(lyst)-1))
    
    return quicksort([x for x in lyst if x < pivot]) \
           + [pivot] \
           + quicksort([x for x in lyst if x >= pivot])

def parallel_quicksort(lyst, conn, procNum):
    """
    Partition the list, then quicksort the left and right
    sides in parallel.
    """

    if procNum <= 0 or len(lyst) <= 1:
        #In the case of len(lyst) <= 1, quicksort will
        #immediately return anyway.
        conn.send(quicksort(lyst))
        conn.close()
        return

    #Create two independent lists (independent in that
    #elements will never need be compared between lists).
    pivot = lyst.pop(random.randint(0, len(lyst)-1))

    leftSide = [x for x in lyst if x < pivot]
    rightSide = [x for x in lyst if x >= pivot]

    #Creat a Pipe to communicate with the left subprocess
    pconnLeft, cconnLeft = Pipe()
    #Create a leftProc that executes quicksortParallel on
    #the left half-list.
    leftProc = Process(target=parallel_quicksort, \
                       args=(leftSide, cconnLeft, procNum - 1))
    
    #Again, for the right.
    pconnRight, cconnRight = Pipe()
    rightProc = Process(target=parallel_quicksort, \
                       args=(rightSide, cconnRight, procNum - 1))

    #Start the two subprocesses.
    leftProc.start()
    rightProc.start()

    #Our answer is the concatenation of the subprocesses' 
    #answers, with the pivot in between. 
    conn.send(pconnLeft.recv() + [pivot] + pconnRight.recv())
    conn.close()

    #Join our subprocesses.
    leftProc.join()
    rightProc.join()

def is_sorted(lyst):
    """
    Return whether the argument lyst is in non-decreasing order.
    """
    for i in range(1, len(lyst)):
        if lyst[i] < lyst[i-1]:
            return False
    return True


def load_array_from_file(array_path) :
    result_list = []
    with open(array_path, 'r') as fd:
        reader = csv.reader(fd)
        for row in reader:
            # string_value = row.split(',')
            int_value = list(map(np.int, row))
            for i in int_value:
                result_list.append(i)
    return result_list


#Call the main method if run from the command line.
if __name__ == '__main__':
    main()
