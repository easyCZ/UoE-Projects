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

// #define SLEEPTIME 1
#define SEND_TAG 10
#define RECEIVE_TAG 11
#define SLEEPTIME 1000000

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
  // double problem[2] = {A, B};
  // push(problem, stack);


  // int isComplete = 1;
  // while (is_empty(stack) == 0) {

  //   double *boundaries = pop(stack);
  //   double left = boundaries[0];
  //   double right = boundaries[1];

  //   int slave = getFreeSlave(&slaves);

  //   MPI_Send(&boundaries, 2, MPI_DOUBLE, slave, SEND_TAG, MPI_COMM_WORLD);
  // }


  // Fill up work stack initially
  double chunk_size = fabs(B - A) / numprocs;
  for (i = 1; i < numprocs; i++) {
    double boundaries[2] = {chunk_size * i, chunk_size * (i + 1)};
    push(boundaries, stack);
  }

  printf("Stack size: %i\n", is_empty(stack));

  while (is_empty(stack) == 0) {
    double *boundaries = (double *) malloc(2*(sizeof(double)));
    boundaries = pop(stack);

    int slave_target = getFreeSlave(&slaves);
    if (slave_target != -1) {
      printf("[Master] Sending %f, %f to worker #%i\n", boundaries[0], boundaries[1], slave_target);


      MPI_Send(boundaries, 2, MPI_DOUBLE, slave_target, SEND_TAG, MPI_COMM_WORLD);
      slaves[slave_target] = 1;
      usleep(SLEEPTIME);
      free(boundaries);
    }

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
  double *boundaries = (double *) malloc(2*(sizeof(double)));

  MPI_Recv(boundaries, 2, MPI_DOUBLE, 0, SEND_TAG, MPI_COMM_WORLD, &status);
  printf("Worker #%i: %f - %f\n", mypid, boundaries[0], boundaries[1]);
}

int getFreeSlave(int *slaves_arr, int slave_count) {
  int i;
  for (i = 1; i < slave_count; i++) {
    printf("Slave #%i: %d\n", i, slaves_arr[i]);
    if (slaves_arr[i] == 0) return i;
  }
  return -1;
}