#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
#include "stack.h"
#include <unistd.h>

#define EPSILON 1e-3
#define F(arg)  cosh(arg)*cosh(arg)*cosh(arg)*cosh(arg)
#define A 0.0
#define B 5.0

#define SLEEPTIME 1
#define SEND_TAG 10
#define RECEIVE_TAG 11


int *tasks_per_process;
MPI_Status status;

double farmer(int);
void worker(int);
int get_free_worker(int *, int);



/* Student: s1115104
 *
 * The worker program is designed as follows:
 *   1. Receive message
 *   2. Decide if we need to compute a problem or terminate
 *     a). Compute the problem
 *       i)  if the area is below the required precision, send back to the farmer a triple of
 *           lower_from, lower_to and upper_to. We can send only 3 values rather than two upper
 *           and two lower bounds as the lower_to and upper_from bounds are the same.
 *       ii) if we are withing the required precision, send back a single double with the area computed
 *     b) Terminate - in this case we simply exit the loop and thus terminate the process/thread
 *
 * The farmer program attempts to perform the following actions, each depending on the state of the program:
 *   1) Assign work
 *   2) Receive solutions
 *   3) Notify workers about convergence
 *
 * Assigning work consists of ensuring that there is some work to assign (stored on a stack)
 * and that there are indeed some workers who are free to receive new work. If such condition
 * is satisfied, the program will pop a problem (work) from the stack and send the problem
 * definition to the free worker marking any such worker as not free.
 *
 * Reception of solutions is designed to execute, if all workers are working or there are no problems
 * to assign. In this situation, the number free workers must be lower than the number of total workers
 * available to us. In this scenario, we allocate a buffer of size 3 and block until we receive a
 * solution to the problem. In this situation, we could have received two types of answers - the
 * area solution or two new problems. Checking the actual size of the message with MPI_Get_count
 * we can determine if it is an answer (size 1) or a new problem (size 3). There is a point to be
 * made about the wasteful allocation of a larger buffer than we need in case we receive the full
 * answer. We could instead send a tag indicating the type of answer, however, the overhead of allocating
 * a larger buffer is rather insignificant as well as the logic required to handle such scenario would be
 * slightly more complicated due to the inability to specify sets of tags we can receive.
 *
 * Finally, if there is no work to be done (stack is empty) and there are no workers working currently,
 * we must have converged with the computation. In this case, we send a termination signal to the workers.
 *
 *
 * The program implemented below uses MPI_Recv and MPI_Send APIs as well as MPI_Get_count as their provide
 * sufficient functionality to implement the required Farmer-Worker concept with a fixed number of workers
 * and a non-all work/not-work at the same time.
 *
 * The program does not use MPI_Reduce or similar APIs as it would require the slowest of workers to finish
 * before assigning new work to others.
 *
 * MPI_Scatter does not provide the desired behavior and therefore isn't used. Similarly, MPI_Gather, which is
 * the inverse of MPI_Scatter does not provide the required behavior.
 */

int main(int argc, char **argv ) {
  int i, myid, numprocs;
  double area, a, b;

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
  MPI_Comm_rank(MPI_COMM_WORLD, &myid);

  if(numprocs < 2) {
    fprintf(stderr, "ERROR: Must have at least 2 processes to run\n");
    MPI_Finalize();
    exit(1);
  }

  if (myid == 0) { // Farmer
    // init counters
    tasks_per_process = (int *) malloc(sizeof(int)*(numprocs));
    for (i=0; i<numprocs; i++) {
      tasks_per_process[i]=0;
    }
  }

  if (myid == 0) { // Farmer
    area = farmer(numprocs);
  } else { //Workers
    worker(myid);
  }

  if(myid == 0) {
    fprintf(stdout, "Area=%lf\n", area);
    fprintf(stdout, "\nTasks Per Process\n");
    for (i=0; i<numprocs; i++) {
      fprintf(stdout, "%d\t", i);
    }
    fprintf(stdout, "\n");
    for (i=0; i<numprocs; i++) {
      fprintf(stdout, "%d\t", tasks_per_process[i]);
    }
    fprintf(stdout, "\n");
    free(tasks_per_process);
  }
  MPI_Finalize();
  return 0;
}

double farmer(int numprocs) {

  int workers[numprocs];
  int i, converged;
  int free_workers, max_workers;
  double total_area = 0.0;

  // All workers are free - not working
  for (i = 1; i < numprocs; i++) {
    workers[i] = 0;
  }

  free_workers = numprocs - 1;
  max_workers = numprocs - 1;

  stack *stack = new_stack();

  // Setup initial problem
  double problem[2] = {A, B};
  push(problem, stack);

  converged = 0;
  while (converged != 1) {

    // Assign problems
    if (is_empty(stack) == 0 && free_workers > 0) {
      int worker_id = get_free_worker(workers, numprocs);
      double *boundaries = pop(stack);

      // Issue work
      MPI_Send(boundaries, 2, MPI_DOUBLE, worker_id, SEND_TAG, MPI_COMM_WORLD);

      // Housekeep
      workers[worker_id] = 1;
      free_workers -= 1;
      free(boundaries);
    }

    // Receive solutions
    else if (free_workers < max_workers) {
      int max_size = 3;
      int data_size;

      // Regardless of the actual receive size, allocate the full array
      // The overhead is minimal compared to the rest of the program and the logic is simpler
      double data[max_size];

      MPI_Recv(&data, 3, MPI_DOUBLE, MPI_ANY_SOURCE, RECEIVE_TAG, MPI_COMM_WORLD, &status);

      // Get actual receive size
      // Looking at the actual size simplifies the logic rather than probing first
      // and then allocating buffer
      MPI_Get_count(&status, MPI_DOUBLE, &data_size);

      int worker_id = status.MPI_SOURCE;

      // Area computed
      if (data_size == 1) {
        double area = data[0];
        total_area += area;
      }
      // 2 New problems are submitted
      else if (data_size == 3) {
        double first_problem[2] = {data[0], data[1]};
        double second_problem[2] = {data[1], data[2]};

        push(first_problem, stack);
        push(second_problem, stack);
      }


      tasks_per_process[worker_id] += 1;
      free_workers += 1;
      workers[worker_id] = 0;
    }

    // No work? No workers working? We must have converged
    else {
      converged = 1;
    }

  }

  // Send termination signal
  for (i = 0; i < numprocs; i++) {
    double terminate = 1.0;
    MPI_Send(&terminate, 1, MPI_DOUBLE, i, SEND_TAG, MPI_COMM_WORLD);
  }

  return total_area;
}


void quad(double left, double right) {
  double mid, fmid, larea, rarea, fleft, fright, lrarea;

  // the following is recomputed again when the task is split,
  // could be cached, however, messages of larger size would be required
  // this simplifies the parallelism logic
  fleft = F(left);
  fright = F(right);
  lrarea = (F(left)+F(right)) * (right-left) / 2;

  mid = (left + right) / 2;
  fmid = F(mid);
  larea = (fleft + fmid) * (mid - left) / 2;
  rarea = (fmid + fright) * (right - mid) / 2;

   usleep(SLEEPTIME);
  if( fabs((larea + rarea) - lrarea) > EPSILON ) {
    double boundaries[3] = {left, mid, right};
    MPI_Send(&boundaries, 3, MPI_DOUBLE, 0, RECEIVE_TAG, MPI_COMM_WORLD);
  }

  else {
    double area = larea + rarea;
    MPI_Send(&area, 1, MPI_DOUBLE, 0, RECEIVE_TAG, MPI_COMM_WORLD);
  }
}

void worker(int mypid) {

  int compute = 1;

  while (compute) {
    double data[2];
    int data_size;

    MPI_Recv(&data, 2, MPI_DOUBLE, 0, SEND_TAG, MPI_COMM_WORLD, &status);
    MPI_Get_count(&status, MPI_DOUBLE, &data_size);

    // Terminate
    // A new problem always contains lower and upper bounds, therefore size one can be used otherwise
    if (data_size == 1) {
      compute = 0;
    }

    // compute
    else {
      double lower = data[0];
      double upper = data[1];

      // Quad is responsible for the actual sending of data back to farmer
      quad(lower, upper);
    }

  }

}

int get_free_worker(int *workers_arr, int worker_count) {
  int i;
  for (i = 1; i < worker_count; i++) {
    if (workers_arr[i] == 0) return i;
  }
  return -1;
}