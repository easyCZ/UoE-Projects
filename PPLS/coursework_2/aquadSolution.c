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
// #define SLEEPTIME 1000000

int *tasks_per_process;
MPI_Status status;

double farmer(int);
void worker(int);
int get_free_worker(int *, int);

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
  int freeWorkers, max_workers;
  double total_area = 0.0;

  // All workers are free - not working
  for (i = 1; i < numprocs; i++) {
    workers[i] = 0;
  }

  freeWorkers = numprocs - 1;
  max_workers = numprocs - 1;

  stack *stack = new_stack();

  // Setup initial problem
  double problem[2] = {A, B};
  push(problem, stack);

  converged = 0;
  while (converged != 1) {

    // Assign problems
    if (is_empty(stack) == 0 && freeWorkers > 0) {
      int slave_id = get_free_worker(workers, numprocs);
      double *boundaries = pop(stack);

      // Issue work
      MPI_Send(boundaries, 2, MPI_DOUBLE, slave_id, SEND_TAG, MPI_COMM_WORLD);
      // printf("[Farmer] Sent work to #%d\n", slave_id);
      // Housekeep
      workers[slave_id] = 1;
      freeWorkers -= 1;
      free(boundaries);
    }

    // Receive solutions
    else if (freeWorkers < max_workers) {
      int max_size = 3;
      int data_size;
      double data[max_size];
      // printf("[Farmer] Waiting to receive data.\n");
      MPI_Recv(&data, 3, MPI_DOUBLE, MPI_ANY_SOURCE, RECEIVE_TAG, MPI_COMM_WORLD, &status);

      // Get actual receive size
      MPI_Get_count(&status, MPI_DOUBLE, &data_size);
      // printf("[Farmer] Data Size: %d\n", data_size);

      int worker_id = status.MPI_SOURCE;

      // Area computed
      if (data_size == 1) {
        double area = data[0];
        // printf("[Farmer] Received area from worker #%d. Area = %f\n", worker_id, area);
        total_area += area;
      }
      // 2 New problems are submitted
      else if (data_size == 3) {
        double first_problem[2] = {data[0], data[1]};
        double second_problem[2] = {data[1], data[2]};

        // printf("[Farmer] Received 2 new problems: %f - %f, %f - %f\n", data[0], data[1], data[1], data[2]);

        push(first_problem, stack);
        push(second_problem, stack);
      }


      tasks_per_process[worker_id] += 1;
      freeWorkers += 1;
      workers[worker_id] = 0;
    }

    // No work? No workers working? We must have converged
    else {
      // printf("[Farmer] Converged. Terminating.\n");
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
    // printf("[Worker] Sending area: %f\n", area);
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
    if (data_size == 1) {
      compute = 0;
    }

    // compute
    else {
      double lower = data[0];
      double upper = data[1];
      // printf("[Worker #%i] %f - %f\n", mypid, data[0], data[1]);

      quad(lower, upper);

      // MPI_Send(&area, 1, MPI_DOUBLE, 0, RECEIVE_TAG, MPI_COMM_WORLD);
      // printf("[Worker #%i] Sent data.\n", mypid);
    }

  }

}

int get_free_worker(int *slaves_arr, int slave_count) {
  int i;
  for (i = 1; i < slave_count; i++) {
    // printf("Slave #%i: %d\n", i, slaves_arr[i]);
    if (slaves_arr[i] == 0) return i;
  }
  return -1;
}