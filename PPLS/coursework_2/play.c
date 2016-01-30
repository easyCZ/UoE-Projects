#include <stdio.h>
#include <mpi.h>

int numbers_size = 1000000;


main(int argc, char **argv) {
	int ierr, num_procs, my_id;
	int numbers[numbers_size];

	/* initialize */
	int i;
	for (i = 0; i < numbers_size; i++) {
		numbers[i] = i;
	}

	int sum = 0;
	for (i = 0; i < numbers_size; i++) {
		sum += numbers[i];
	}

	printf("The grand total is: %i\n", sum);
}
