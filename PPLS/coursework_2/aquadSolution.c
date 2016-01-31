#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
#include "stack.h"

#define EPSILON 1e-3
#define F(arg)  cosh(arg)*cosh(arg)*cosh(arg)*cosh(arg)
#define A 0.0
#define B 5.0

#define SLEEPTIME 1
#define SEND_TAG 10
#define RECEIVE_TAG 11

int *tasks_per_process;

double farmer(int);
void worker(int);

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

  stack *stack = new_stack();
  int slaves[numprocs];

  // All slaves are free - not working
  int i;
  for (i = 1; i < numprocs; i++) {
    slaves[i] = 0;
  }

  // Setup initial problem
  double problem[2] = {A, B};
  push(problem, stack);


  int isComplete = 1;
  while (is_empty(stack) == 0) {

    double *boundaries = pop(stack);
    double left = boundaries[0];
    double right = boundaries[1];

    int slave = getFreeSlave(&slaves);

    MPI_Send(&left, 1, MPI_DOUBLE, slave, SEND_TAG, MPI_COMM_WORLD);
    MPI_Send(&right, 1, MPI_DOUBLE, slave, SEND_TAG, MPI_COMM_WORLD);

  }


  // Fill up work stack initially
  double chunk_size = fabs(B - A) / numprocs;
  for (i = 1; i < numprocs; i++) {
    double boundaries[2] = {chunk_size * i, chunk_size * (i + 1)};
    push(boundaries, stack);
  }

  printf("Stack size: %i\n", is_empty(stack));

  while (is_empty(stack) == 0) {
    double *boundaries = pop(stack);
    double lower_bound = boundaries[0];
    double upper_bound = boundaries[1];

    int slave_target = getFreeSlave(&slaves);
    printf("slave target: %i\n", slave_target);


    MPI_Send(&lower_bound, 1, MPI_DOUBLE, slave_target, SEND_TAG, MPI_COMM_WORLD);
    MPI_Send(&upper_bound, 1, MPI_DOUBLE, slave_target, SEND_TAG, MPI_COMM_WORLD);
    slaves[slave_target] = 1;
    printf("Stack is empty: %i", is_empty(stack));
  }


  return 1.0;
}

double quad(double left, double right, double fleft, double fright, double lrarea) {
  double mid, fmid, larea, rarea;

  mid = (left + right) / 2;
  fmid = F(mid);
  larea = (fleft + fmid) * (mid - left) / 2;
  rarea = (fmid + fright) * (right - mid) / 2;
  if( fabs((larea + rarea) - lrarea) > EPSILON ) {
    larea = quad(left, mid, fleft, fmid, larea);
    rarea = quad(mid, right, fmid, fright, rarea);
  }
  return (larea + rarea);
}

void worker(int mypid) {

  MPI_Status status;
  double lower_bound;
  double upper_bound;

  MPI_Recv(&lower_bound, 1, MPI_DOUBLE, 0, SEND_TAG, MPI_COMM_WORLD, &status);
  printf("Worker #%i: lower - %f\n", mypid, lower_bound);
  MPI_Recv(&upper_bound, 1, MPI_DOUBLE, 0, SEND_TAG, MPI_COMM_WORLD, &status);

  printf("Worker #%i: upper-  %f\n", mypid, upper_bound);
}

int getFreeSlave(int *slaves_arr, int slave_count) {
  int i;
  for (i = 1; i < slave_count; i++) {
    if (slaves_arr[i] == 0) return i;
  }
  return -1;
}